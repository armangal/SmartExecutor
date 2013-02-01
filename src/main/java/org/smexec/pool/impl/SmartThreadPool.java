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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.configuration.PoolType;
import org.smexec.pool.ISmartThreadPool;

public class SmartThreadPool
    extends AbstractSmartPool<ThreadPoolExecutor>
    implements ISmartThreadPool {

    public SmartThreadPool(final PoolConfiguration poolConf) {
        super(poolConf);

        final BlockingQueue<Runnable> workQueue;

        if (poolConf.getQueueSize() == -1 || poolConf.getPoolType().equals(PoolType.cached)) {
            workQueue = new SynchronousQueue<Runnable>();
        } else {
            workQueue = new LinkedBlockingQueue<Runnable>(poolConf.getQueueSize());

        }
        final int corePollSize = poolConf.getPoolType().equals(PoolType.regular) ? poolConf.getCorePollSize() : 0;

        final int maxPoolSize = poolConf.getPoolType().equals(PoolType.regular) ? poolConf.getMaxPoolSize() : Integer.MAX_VALUE;

        pool = new ThreadPoolExecutor(corePollSize, maxPoolSize, poolConf.getKeepAliveTime(), TimeUnit.MILLISECONDS, workQueue, threadFactory);

    }

    @Override
    public String toString() {
        return "SmartThreadPool " + super.toString();
    }
}
