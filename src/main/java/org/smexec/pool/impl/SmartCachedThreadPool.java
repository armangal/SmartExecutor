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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartCachedThreadPool;
import org.smexec.wrappers.SmartCallable;
import org.smexec.wrappers.SmartRunnable;

public class SmartCachedThreadPool
    extends AbstractSmartPool
    implements ISmartCachedThreadPool {

    private ExecutorService pool;

    private PoolConfiguration poolConfiguration;

    public SmartCachedThreadPool(final PoolConfiguration poolConfiguration) {
        this.poolConfiguration = poolConfiguration;

        this.pool = Executors.newCachedThreadPool(new ThreadFactory() {

            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                // SER means SmartExecutorCached pool
                return new Thread(r, "SEC_" + poolConfiguration.getPoolNameShort() + "_" + threadNumber.incrementAndGet());
            }
        });

    }

    public void execute(SmartRunnable command) {
        poolStats.incrementSubmitted();
        try {
            pool.execute(command);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

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
    public void shutdown() {
        pool.shutdown();
    }

    @Override
    public String toString() {
        return "SmartCachedThreadPool [" + poolConfiguration + "] " + super.toString();
    }
}
