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

import org.smexec.configuration.PoolConfiguration;
import org.smexec.configuration.PoolType;
import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.ThreadPoolStats;

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
        this.poolStats = new ThreadPoolStats(poolConf.getChunks());

        // Schedule custom chunker thread
        ThreadPoolHelper.scheduleChunker(poolConf, poolStats);

    }

    @Override
    public void execute(Runnable command) {
        execute(command, null);
    }

    @Override
    public void execute(Runnable command, String threadNameSuffix) {
        poolStats.incrementSubmitted();
        try {
            super.execute(ThreadPoolHelper.wrapRunnble(command, threadNameSuffix, poolStats));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, null);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, String threadNameSuffix) {
        try {
            poolStats.incrementSubmitted();
            return super.submit(ThreadPoolHelper.wrapCallable(task, threadNameSuffix, poolStats));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public Future<?> submit(Runnable task) {
        return submit(task, null);
    }

    @Override
    public Future<?> submit(Runnable task, String threadNameSuffix) {
        poolStats.incrementSubmitted();
        try {
            return super.submit(ThreadPoolHelper.wrapRunnble(task, threadNameSuffix, poolStats));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return submit(task, result, null);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result, String threadNameSuffix) {
        poolStats.incrementSubmitted();
        try {
            return super.submit(ThreadPoolHelper.wrapRunnble(task, threadNameSuffix, poolStats), result);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
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
            return super.invokeAll(ThreadPoolHelper.wrapCallable(tasks, null, poolStats));
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
            return super.invokeAll(ThreadPoolHelper.wrapCallable(tasks, null, poolStats), timeout, unit);
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
            return super.invokeAny(ThreadPoolHelper.wrapCallable(tasks, null, poolStats));
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
            return super.invokeAny(ThreadPoolHelper.wrapCallable(tasks, null, poolStats), timeout, unit);
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
