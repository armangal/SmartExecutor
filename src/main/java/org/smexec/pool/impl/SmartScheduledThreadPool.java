/**
 * Copyright (C) 2013 Arman Gal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smexec.pool.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.smexec.TaskMetadata;
import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartScheduledThreadPool;
import org.smexec.pool.ThreadPoolStats;
import org.smexec.utils.Utils;

public class SmartScheduledThreadPool
    extends ScheduledThreadPoolExecutor
    implements ISmartScheduledThreadPool {

    protected final ThreadPoolStats poolStats;

    protected final PoolConfiguration poolConf;

    public SmartScheduledThreadPool(final PoolConfiguration poolConf) {
        super(poolConf.getCorePollSize(), ThreadPoolHelper.getThreadFactory(poolConf));

        this.poolConf = poolConf;
        this.poolStats = new ThreadPoolStats(poolConf.getChunks(), poolConf.getLogStats(), poolConf.getPoolName());

        // Schedule custom chunker thread
        ThreadPoolHelper.scheduleChunker(poolConf, poolStats);

    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {}

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, command);

        return schedule(command, delay, unit, taskMetadata);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, TaskMetadata taskMetadata) {
        try {
            poolStats.incrementSubmitted(taskMetadata.getTaskId());
            return super.schedule(ThreadPoolHelper.wrapRunnble(command, taskMetadata, poolStats), delay, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskMetadata.getTaskId());
            throw e;
        }
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, callable);

        return schedule(callable, delay, unit, taskMetadata);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, TaskMetadata taskMetadata) {
        try {
            poolStats.incrementSubmitted(taskMetadata.getTaskId());
            return super.schedule(ThreadPoolHelper.wrapCallable(callable, taskMetadata, poolStats), delay, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskMetadata.getTaskId());
            throw e;
        }
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, command);

        return scheduleAtFixedRate(command, initialDelay, period, unit, taskMetadata);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, TaskMetadata taskMetadata) {

        try {
            poolStats.incrementSubmitted(taskMetadata.getTaskId());
            return super.scheduleAtFixedRate(ThreadPoolHelper.wrapRunnble(command, taskMetadata, poolStats), initialDelay, period, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskMetadata.getTaskId());
            throw e;
        }
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, command);

        return scheduleWithFixedDelay(command, initialDelay, delay, unit, taskMetadata);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, TaskMetadata taskMetadata) {

        try {
            poolStats.incrementSubmitted(taskMetadata.getTaskId());
            return super.scheduleWithFixedDelay(ThreadPoolHelper.wrapRunnble(command, taskMetadata, poolStats), initialDelay, delay, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskMetadata.getTaskId());
            throw e;
        }
    }

    @Override
    public String toString() {
        return "SmartScheduledThreadPool Pool:[" + super.toString() + "]\nStats:[" + poolStats + "]\nConf:[" + poolConf + "]";
    }

    @Override
    public ThreadPoolStats getPoolStats() {
        return poolStats;
    }

    @Override
    public PoolConfiguration getPoolConfiguration() {
        return poolConf;
    }

}
