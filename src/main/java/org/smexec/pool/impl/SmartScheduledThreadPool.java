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

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartScheduledThreadPool;
import org.smexec.wrappers.SmartCallable;
import org.smexec.wrappers.SmartRunnable;

public class SmartScheduledThreadPool
    extends AbstractSmartPool
    implements ISmartScheduledThreadPool {

    private ScheduledExecutorService pool;

    public SmartScheduledThreadPool(final PoolConfiguration poolConfiguration) {
        this.poolConfiguration = poolConfiguration;

        pool = Executors.newScheduledThreadPool(poolConfiguration.getCorePollSize(), new ThreadFactory() {

            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                // SESR means SmartExecutorScheduled pool
                return new Thread(r, "SES_" + poolConfiguration.getPoolNameShort() + "_" + threadNumber.incrementAndGet());
            }
        });

    }

    @Override
    public void execute(SmartRunnable command) {
        try {
            poolStats.incrementSubmitted();
            pool.execute(command);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(SmartCallable<T> task) {
        try {
            poolStats.incrementSubmitted();
            return pool.submit(task);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public ScheduledFuture<?> schedule(SmartRunnable command, long delay, TimeUnit unit) {
        try {
            poolStats.incrementSubmitted();
            return pool.schedule(command, delay, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public <V> ScheduledFuture<V> schedule(SmartCallable<V> callable, long delay, TimeUnit unit) {
        try {
            poolStats.incrementSubmitted();
            return pool.schedule(callable, delay, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(SmartRunnable command, long initialDelay, long period, TimeUnit unit) {
        try {
            poolStats.incrementSubmitted();
            return pool.scheduleAtFixedRate(command, initialDelay, period, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(SmartRunnable command, long initialDelay, long delay, TimeUnit unit) {
        try {
            poolStats.incrementSubmitted();
            return pool.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public void shutdown() {
        pool.shutdown();
    }

    @Override
    public String toString() {
        return "SmartScheduledThreadPool [" + poolConfiguration + "] " + super.toString();
    }

}
