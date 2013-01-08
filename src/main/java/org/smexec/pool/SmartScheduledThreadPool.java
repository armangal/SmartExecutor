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
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.smexec.SmartCallble;
import org.smexec.SmartExecutorProperty;
import org.smexec.SmartProperties;
import org.smexec.SmartRunnable;

public class SmartScheduledThreadPool
    implements ISmartScheduledThreadPool {

    private ScheduledExecutorService pool;

    public SmartScheduledThreadPool(final String poolName, final Properties properties) {

        SmartExecutorProperty<Integer> corePoolSize = SmartExecutorProperty.Factory.asProperty(Integer.valueOf(properties.getProperty(poolName
                                                                                                                                                      + "."
                                                                                                                                                      + SmartProperties.POOL_SIZE.getName(),
                                                                                                                                      SmartProperties.POOL_SIZE.getDefaultValue())));

        pool = Executors.newScheduledThreadPool(corePoolSize.get(), new ThreadFactory() {

            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                // SESR means SmartExecutorScheduled pool
                return new Thread(r, "SES_" + poolName + "-" + threadNumber.incrementAndGet());
            }
        });

    }

    @Override
    public void execute(SmartRunnable command) {
        pool.execute(command);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

    @Override
    public ScheduledFuture<?> schedule(SmartRunnable command, long delay, TimeUnit unit) {
        return pool.schedule(command, delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(SmartCallble<V> callable, long delay, TimeUnit unit) {
        return pool.schedule(callable, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(SmartRunnable command, long initialDelay, long period, TimeUnit unit) {
        return pool.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(SmartRunnable command, long initialDelay, long delay, TimeUnit unit) {
        return pool.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

}
