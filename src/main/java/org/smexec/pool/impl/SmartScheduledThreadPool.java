/**
 * MIT License 
 * 
 * Copyright (c) 2013 Arman Gal
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.smexec.pool.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartScheduledThreadPool;
import org.smexec.pool.ThreadPoolStats;

public class SmartScheduledThreadPool
    extends ScheduledThreadPoolExecutor
    implements ISmartScheduledThreadPool {

    protected final ThreadPoolStats poolStats;

    protected final PoolConfiguration poolConf;

    public SmartScheduledThreadPool(final PoolConfiguration poolConf) {
        super(poolConf.getCorePollSize(), ThreadPoolHelper.getThreadFactory(poolConf));

        this.poolConf = poolConf;
        this.poolStats = new ThreadPoolStats(poolConf.getChunks());

        // Schedule custom chunker thread
        ThreadPoolHelper.scheduleChunker(poolConf, poolStats);

    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {}

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return schedule(command, delay, unit, null);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, String threadNameSuffix) {
        String taskIdentification = ThreadPoolHelper.getTaskIdentification(command);
        try {
            poolStats.incrementSubmitted(taskIdentification);
            return super.schedule(ThreadPoolHelper.wrapRunnble(command, taskIdentification, threadNameSuffix, poolStats), delay, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskIdentification);
            throw e;
        }
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return schedule(callable, delay, unit, null);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, String threadNameSuffix) {
        String taskIdentification = ThreadPoolHelper.getTaskIdentification(callable);
        try {
            poolStats.incrementSubmitted(taskIdentification);
            return super.schedule(ThreadPoolHelper.wrapCallable(callable, taskIdentification, threadNameSuffix, poolStats), delay, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskIdentification);
            throw e;
        }
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return scheduleAtFixedRate(command, initialDelay, period, unit, null);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, String threadNameSuffix) {
        String taskIdentification = ThreadPoolHelper.getTaskIdentification(command);

        try {
            poolStats.incrementSubmitted(taskIdentification);
            return super.scheduleAtFixedRate(ThreadPoolHelper.wrapRunnble(command, taskIdentification, threadNameSuffix, poolStats), initialDelay, period, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskIdentification);
            throw e;
        }
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return scheduleWithFixedDelay(command, initialDelay, delay, unit, null);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, String threadNameSuffix) {
        String taskIdentification = ThreadPoolHelper.getTaskIdentification(command);

        try {
            poolStats.incrementSubmitted(taskIdentification);
            return super.scheduleWithFixedDelay(ThreadPoolHelper.wrapRunnble(command, taskIdentification, threadNameSuffix, poolStats),
                                                initialDelay,
                                                delay,
                                                unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskIdentification);
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
