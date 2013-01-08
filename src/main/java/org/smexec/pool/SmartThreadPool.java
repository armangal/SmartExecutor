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
package org.smexec.pool;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.smexec.SmartExecutorProperty;
import org.smexec.SmartProperties;
import org.smexec.SmartRunnable;

public class SmartThreadPool
    implements ISmartThreadPool {

    private ThreadPoolExecutor pool;

    public SmartThreadPool(final String poolName, final Properties properties) {
        SmartExecutorProperty<Integer> corePoolSize = SmartExecutorProperty.Factory.asProperty(Integer.valueOf(properties.getProperty(poolName
                                                                                                                                                      + "."
                                                                                                                                                      + SmartProperties.POOL_SIZE.getName(),
                                                                                                                                      SmartProperties.POOL_SIZE.getDefaultValue())));

        SmartExecutorProperty<Integer> maximumPoolSize = SmartExecutorProperty.Factory.asProperty(Integer.valueOf(properties.getProperty(poolName
                                                                                                                                                         + "."
                                                                                                                                                         + SmartProperties.MAX_POOL_SIZE.getName(),
                                                                                                                                         SmartProperties.MAX_POOL_SIZE.getDefaultValue())));

        SmartExecutorProperty<Integer> keepAliveTime = SmartExecutorProperty.Factory.asProperty(Integer.valueOf(properties.getProperty(poolName
                                                                                                                                                       + "."
                                                                                                                                                       + SmartProperties.KEEP_ALIVE_TIME.getName(),
                                                                                                                                       SmartProperties.KEEP_ALIVE_TIME.getDefaultValue())));

        SmartExecutorProperty<Integer> queueSize = SmartExecutorProperty.Factory.asProperty(Integer.valueOf(properties.getProperty(poolName
                                                                                                                                                   + "."
                                                                                                                                                   + SmartProperties.QUEUE_SIZE.getName(),
                                                                                                                                   SmartProperties.QUEUE_SIZE.getDefaultValue())));

        BlockingQueue<Runnable> workQueue;

        if (queueSize.get() == -1) {
            workQueue = new SynchronousQueue<Runnable>();
        } else {
            workQueue = new LinkedBlockingQueue<Runnable>(queueSize.get());

        }

        pool = new ThreadPoolExecutor(corePoolSize.get(), maximumPoolSize.get(), keepAliveTime.get(), TimeUnit.MILLISECONDS, workQueue, new ThreadFactory() {

            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                // SER means SmartExecutorRegular pool
                return new Thread(r, "SER_" + poolName + "-" + threadNumber.incrementAndGet());
            }
        });

    }

    public void execute(SmartRunnable command) {
        pool.execute(command);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }
}
