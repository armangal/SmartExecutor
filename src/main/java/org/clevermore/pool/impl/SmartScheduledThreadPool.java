/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.pool.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.clevermore.TaskMetadata;
import org.clevermore.configuration.PoolConfiguration;
import org.clevermore.jmx.PoolStats;
import org.clevermore.pool.ISmartScheduledThreadPool;
import org.clevermore.pool.ThreadPoolStats;
import org.clevermore.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartScheduledThreadPool
    extends ScheduledThreadPoolExecutor
    implements ISmartScheduledThreadPool {

    private static Logger logger = LoggerFactory.getLogger("SmartScheduledThreadPool");

    protected final ThreadPoolStats poolStats;

    protected final PoolConfiguration poolConf;

    private PoolStats poolStatsJmx;

    public SmartScheduledThreadPool(final PoolConfiguration poolConf, final String smartExecutorName) {
        super(poolConf.getCorePollSize(), ThreadPoolHelper.getThreadFactory(poolConf));

        this.poolConf = poolConf;
        this.poolStats = new ThreadPoolStats(poolConf.getChunks(), poolConf.getChunkInterval(), poolConf.getLogStats(), poolConf.getPoolName());

        try {
            poolStatsJmx = new PoolStats(smartExecutorName, this);
            logger.info("Registered JMX bean for smart pool:{}", poolStatsJmx.getName());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {}

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack();
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
        taskMetadata.setStack();
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
        taskMetadata.setStack();
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
        taskMetadata.setStack();
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

    @Override
    public void shutdown() {
        super.shutdown();
        poolStats.shutdown();
        poolStatsJmx.unregisterBean();
    }
}
