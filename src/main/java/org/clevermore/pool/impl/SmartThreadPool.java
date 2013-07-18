/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.pool.impl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.clevermore.TaskMetadata;
import org.clevermore.configuration.PoolConfiguration;
import org.clevermore.configuration.PoolType;
import org.clevermore.pool.ISmartThreadPool;
import org.clevermore.pool.ThreadPoolStats;
import org.clevermore.utils.Utils;

public class SmartThreadPool
    extends ThreadPoolExecutor
    implements ISmartThreadPool {

    protected final ThreadPoolStats poolStats;

    protected final PoolConfiguration poolConf;

    public SmartThreadPool(final PoolConfiguration poolConf) {
        super((poolConf.getPoolType().equals(PoolType.regular) ? poolConf.getCorePollSize() : 0),
              (poolConf.getPoolType().equals(PoolType.regular) ? poolConf.getMaxPoolSize() : Integer.MAX_VALUE),
              poolConf.getKeepAliveTime(),
              TimeUnit.MILLISECONDS,
              (poolConf.getQueueSize() == -1 || poolConf.getPoolType().equals(PoolType.cached)) ? new SynchronousQueue<Runnable>()
                              : new LinkedBlockingQueue<Runnable>(poolConf.getQueueSize()), ThreadPoolHelper.getThreadFactory(poolConf));

        this.poolConf = poolConf;
        this.poolStats = new ThreadPoolStats(poolConf.getChunks(), poolConf.getChunkInterval(), poolConf.getLogStats(), poolConf.getPoolName());

    }

    @Override
    public void execute(Runnable command) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, command);

        execute(command, taskMetadata);
    }

    @Override
    public void execute(Runnable command, TaskMetadata taskMetadata) {
        poolStats.incrementSubmitted(taskMetadata.getTaskId());
        try {
            super.execute(ThreadPoolHelper.wrapRunnble(command, taskMetadata, poolStats));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskMetadata.getTaskId());
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, task);
        return submit(task, taskMetadata);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, TaskMetadata taskMetadata) {
        try {
            poolStats.incrementSubmitted(taskMetadata.getTaskId());
            return super.submit(ThreadPoolHelper.wrapCallable(task, taskMetadata, poolStats));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskMetadata.getTaskId());
            throw e;
        }
    }

    @Override
    public Future<?> submit(Runnable task) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, task);

        return submit(task, taskMetadata);
    }

    @Override
    public Future<?> submit(Runnable task, TaskMetadata taskMetadata) {
        poolStats.incrementSubmitted(taskMetadata.getTaskId());
        try {
            return super.submit(ThreadPoolHelper.wrapRunnble(task, taskMetadata, poolStats));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskMetadata.getTaskId());
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, task);

        return submit(task, result, taskMetadata);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result, TaskMetadata taskMetadata) {
        poolStats.incrementSubmitted(taskMetadata.getTaskId());
        try {
            return super.submit(ThreadPoolHelper.wrapRunnble(task, taskMetadata, poolStats), result);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(taskMetadata.getTaskId());
            throw e;
        }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException {
        return super.awaitTermination(timeout, unit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException {
        poolStats.incrementSubmitted(tasks.size());
        try {
            return super.invokeAll(ThreadPoolHelper.wrapCallable(tasks, poolStats));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(tasks.size());
            throw e;
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException {
        poolStats.incrementSubmitted(tasks.size());
        try {
            return super.invokeAll(ThreadPoolHelper.wrapCallable(tasks, poolStats), timeout, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(tasks.size());
            throw e;
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException {
        poolStats.incrementSubmitted(tasks.size());
        try {
            return super.invokeAny(ThreadPoolHelper.wrapCallable(tasks, poolStats));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(tasks.size());
            throw e;
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        poolStats.incrementSubmitted(tasks.size());
        try {
            return super.invokeAny(ThreadPoolHelper.wrapCallable(tasks, poolStats), timeout, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(tasks.size());
            throw e;
        }
    }

    public ThreadPoolStats getPoolStats() {
        return poolStats;
    }

    public PoolConfiguration getPoolConfiguration() {
        return poolConf;
    }

    @Override
    public String toString() {
        return "SmartThreadPool Pool:[" + super.toString() + "]\nStats:[" + poolStats + "]\nConf:[" + poolConf + "]";
    }
}
