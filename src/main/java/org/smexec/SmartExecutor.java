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
package org.smexec;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.annotation.ThreadPoolName;
import org.smexec.configuration.Config;
import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartScheduledThreadPool;
import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.impl.SmartCachedThreadPool;
import org.smexec.pool.impl.SmartScheduledThreadPool;
import org.smexec.pool.impl.SmartThreadPool;
import org.smexec.wrappers.SmartCallable;
import org.smexec.wrappers.SmartRunnable;

/**
 * The main entry point to utilize SmartExecutor
 */
public class SmartExecutor {

    private static Logger logger = LoggerFactory.getLogger("SE");

    private static final String defaultXMLConfName = "SmartExecutor-default.xml";
    private static final String defaultPoolName = "Default";
    private static final String defaultScheduledPoolName = "Scheduled";

    private ConcurrentHashMap<String, ISmartThreadPool> threadPoolMap = new ConcurrentHashMap<String, ISmartThreadPool>(0);

    private Config config;

    public SmartExecutor()
        throws JAXBException {
        this(defaultXMLConfName);
    }

    public SmartExecutor(String configXMLresource)
        throws JAXBException {
        // TODO improve configuration loading API
        InputStream configXML = Thread.currentThread().getContextClassLoader().getResourceAsStream(configXMLresource);
        if (configXML == null) {
            throw new RuntimeException("Configuration file wan't found:" + configXMLresource);
        }
        JAXBContext context = JAXBContext.newInstance(Config.class);
        config = (Config) context.createUnmarshaller().unmarshal(configXML);

        logger.info("Initilized SmartExecutor with properties:{}", config);
    }

    public void execute(Runnable command, String poolName, String threadNameSuffix) {
        ISmartThreadPool smartThreadPool = getPool(poolName);
        SmartRunnable sr = new SmartRunnable(command, threadNameSuffix, smartThreadPool.getPoolStats());
        smartThreadPool.execute(sr);
    }

    public void execute(Runnable command, String poolName) {
        execute(command, poolName, null);
    }

    public void execute(Runnable command) {
        execute(command, getPoolName(command), null);
    }

    public void execute(String threadNameSuffix, Runnable command) {
        execute(command, getPoolName(command), threadNameSuffix);
    }

    /**
     * determine the pool name by reading the annotation or interface
     * 
     * @param r
     * @return
     */
    private String getPoolName(Runnable r) {
        ThreadPoolName annotation = r.getClass().getAnnotation(ThreadPoolName.class);
        if (annotation != null) {
            if (annotation.poolName() != null) {
                return annotation.poolName();
            } else {
                return defaultPoolName;
            }
        } else {
            if (r instanceof IThreadPoolAware) {
                return ((IThreadPoolAware) r).getPoolName();
            }
        }

        return defaultPoolName;
    }

    public <T> Future<T> submit(Callable<T> task, String poolName) {
        ISmartThreadPool smartThreadPool = getPool(poolName);
        SmartCallable<T> sc = new SmartCallable<T>(task, null);
        return smartThreadPool.submit(sc);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, defaultPoolName);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, String poolName, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(poolName);
        SmartRunnable sr = new SmartRunnable(command, threadName, scheduledPool.getPoolStats());
        return scheduledPool.schedule(sr, delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, String poolName, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(poolName);
        SmartCallable<V> sc = new SmartCallable<V>(callable, threadName, scheduledPool.getPoolStats());
        return scheduledPool.schedule(sc, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, String poolName, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(poolName);
        SmartRunnable sr = new SmartRunnable(command, threadName, scheduledPool.getPoolStats());
        return scheduledPool.scheduleAtFixedRate(sr, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(getPoolName(command));
        SmartRunnable sr = new SmartRunnable(command, threadName, scheduledPool.getPoolStats());
        return scheduledPool.scheduleAtFixedRate(sr, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, String poolName, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(poolName);
        SmartRunnable sr = new SmartRunnable(command, threadName, scheduledPool.getPoolStats());
        return scheduledPool.scheduleWithFixedDelay(sr, initialDelay, delay, unit);
    }

    public void shutdown() {
        for (ISmartThreadPool smartThreadPool : threadPoolMap.values()) {
            logger.info("Shoting down pool:{}", smartThreadPool);
            smartThreadPool.shutdown();
        }

    }

    private ISmartScheduledThreadPool getScheduledPool(String poolName) {
        ISmartThreadPool pool = getPool(poolName);
        if (pool instanceof ISmartScheduledThreadPool) {
            return (ISmartScheduledThreadPool) pool;
        }
        throw new RuntimeException("Pool:" + poolName + " not found.");
    }

    private ISmartThreadPool getPool(String poolName) {
        if (threadPoolMap.containsKey(poolName)) {
            return threadPoolMap.get(poolName);
        } else {
            synchronized (threadPoolMap) {
                if (threadPoolMap.containsKey(poolName)) {
                    return threadPoolMap.get(poolName);
                } else {
                    ISmartThreadPool threadPool = initThreadPool(poolName);
                    threadPoolMap.put(poolName, threadPool);
                    return threadPool;
                }
            }
        }
    }

    private ISmartThreadPool initThreadPool(String poolName) {
        PoolConfiguration poolConfiguration = config.getPoolConfiguration(poolName);
        if (poolConfiguration != null) {
            switch (poolConfiguration.getPoolType()) {
                case regular:
                    return new SmartThreadPool(poolConfiguration);
                case cached:
                    return new SmartCachedThreadPool(poolConfiguration);
                case scheduled:
                    return new SmartScheduledThreadPool(poolConfiguration);
            }
        }
        throw new RuntimeException("Configaration for pool:" + poolName + " were not found.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SmartExecutor:\n");
        for (ISmartThreadPool smartThreadPool : threadPoolMap.values()) {
            sb.append(smartThreadPool.toString()).append("\n");
        }
        return sb.toString();
    }
}
