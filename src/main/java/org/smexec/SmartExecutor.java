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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.annotation.ThreadNameSuffix;
import org.smexec.annotation.ThreadPoolName;
import org.smexec.configuration.Config;
import org.smexec.configuration.PoolConfiguration;
import org.smexec.jmx.ExecutorStats;
import org.smexec.jmx.ExecutorStatsMXBean;
import org.smexec.jmx.PoolStats;
import org.smexec.pool.IGeneralThreadPool;
import org.smexec.pool.ISmartScheduledThreadPool;
import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.impl.SmartScheduledThreadPool;
import org.smexec.pool.impl.SmartThreadPool;

/**
 * The main entry point to use SmartExecutor framework.<br>
 * It's possible to have many different instances of SmartExecutor
 */
public class SmartExecutor {

    private static final Logger logger = LoggerFactory.getLogger("SE");

    private static final String defaultXMLConfName = "SmartExecutor-default.xml";
    private static final String defaultPoolName = "Default";
    private static final String defaultScheduledPoolName = "Scheduled";

    private ConcurrentHashMap<String, IGeneralThreadPool> threadPoolMap = new ConcurrentHashMap<String, IGeneralThreadPool>(0);

    private Config config;

    private MBeanServer mbs;

    /**
     * Initializing SmartExecutor with default pools configurations.
     * 
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public SmartExecutor()
        throws JAXBException, FileNotFoundException {
        this(defaultXMLConfName);
    }

    /**
     * Initializing SmartExecutor with custom configuration file
     * 
     * @param configXMLresource, if the file is in classPath, then only name is enough, if the file is located
     *            out of CP, full file path should be provided
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public SmartExecutor(String configXMLresource)
        throws JAXBException, FileNotFoundException {
        InputStream configXML = Thread.currentThread().getContextClassLoader().getResourceAsStream(configXMLresource);
        if (configXML == null) {
            File f = new File(configXMLresource);
            if (f.exists()) {
                configXML = new FileInputStream(f);
            } else {
                throw new RuntimeException("Configuration file wan't found:" + configXMLresource);
            }
        }
        JAXBContext context = JAXBContext.newInstance(Config.class);
        config = (Config) context.createUnmarshaller().unmarshal(configXML);

        logger.info("Initilized SmartExecutor with properties:{}", config);

        config.validate();

        logger.info("Configurations have been validated.");

        this.mbs = ManagementFactory.getPlatformMBeanServer();

        try {
            ExecutorStatsMXBean es = new ExecutorStats(this, config);
            mbs.registerMBean(es, new ObjectName("org.smexec:type=SmartExecutor,name=" + config.getExecutorConfiguration().getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public void execute(Runnable command, String poolName, String threadNameSuffix) {
        ISmartThreadPool smartThreadPool = (ISmartThreadPool) getPool(poolName);
        smartThreadPool.execute(command, threadNameSuffix);
    }

    public void execute(Runnable command, String poolName) {
        execute(command, poolName, getThreadNameSuffix(command));
    }

    public void execute(Runnable command) {
        execute(command, getPoolName(command), getThreadNameSuffix(command));
    }

    public void execute(String threadNameSuffix, Runnable command) {
        execute(command, getPoolName(command), threadNameSuffix);
    }

    /**
     * determine the pool name by reading the annotation or interface
     * 
     * @param some
     * @return
     */
    private String getPoolName(Object some) {
        ThreadPoolName annotation = some.getClass().getAnnotation(ThreadPoolName.class);
        if (annotation != null) {
            if (annotation.poolName() != null) {
                return annotation.poolName();
            } else {
                return defaultPoolName;
            }
        } else {
            if (some instanceof IThreadPoolAware) {
                return ((IThreadPoolAware) some).getPoolName();
            }
        }

        return defaultPoolName;
    }

    private String getThreadNameSuffix(Object some) {
        ThreadNameSuffix tns = some.getClass().getAnnotation(ThreadNameSuffix.class);
        if (tns != null) {
            return tns.threadNameSuffix();
        }
        if (some instanceof IThreadNameSuffixAware) {
            return ((IThreadNameSuffixAware) some).getThreadNameSuffix();
        }
        return null;
    }

    public <T> Future<T> submit(Callable<T> task, String poolName) {
        ISmartThreadPool smartThreadPool = (ISmartThreadPool) getPool(poolName);
        return smartThreadPool.submit(task, getThreadNameSuffix(task));
    }

    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, defaultPoolName);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, String poolName, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(poolName);
        return scheduledPool.schedule(command, delay, unit, threadName);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, String poolName, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(poolName);
        return scheduledPool.schedule(callable, delay, unit, threadName);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, String poolName, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(poolName);
        return scheduledPool.scheduleAtFixedRate(command, initialDelay, period, unit, threadName);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(getPoolName(command));
        return scheduledPool.scheduleAtFixedRate(command, initialDelay, period, unit, threadName);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, String poolName, String threadName) {
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(poolName);
        return scheduledPool.scheduleWithFixedDelay(command, initialDelay, delay, unit, threadName);
    }

    public void shutdown() {
        for (ExecutorService smartThreadPool : threadPoolMap.values()) {
            logger.info("Shoting down pool:{}", smartThreadPool);
            smartThreadPool.shutdown();
        }

    }

    private ISmartScheduledThreadPool getScheduledPool(String poolName) {
        IGeneralThreadPool pool = getPool(poolName);
        if (pool instanceof ISmartScheduledThreadPool) {
            return (ISmartScheduledThreadPool) pool;
        }
        throw new RuntimeException("Pool:" + poolName + " not found.");
    }

    public ScheduledExecutorService getScheduledThreadPool(String poolName) {
        return getScheduledPool(poolName);
    }

    public ExecutorService getThreadPool(String poolName) {
        return getPool(poolName);
    }

    private IGeneralThreadPool getPool(String poolName) {
        if (threadPoolMap.containsKey(poolName)) {
            return threadPoolMap.get(poolName);
        } else {
            synchronized (threadPoolMap) {
                if (threadPoolMap.containsKey(poolName)) {
                    return threadPoolMap.get(poolName);
                } else {
                    IGeneralThreadPool threadPool = initThreadPool(poolName);
                    threadPoolMap.put(poolName, threadPool);

                    try {
                        PoolStats mbean = new PoolStats(threadPool);
                        mbs.registerMBean(mbean, new ObjectName("org.smexec:type=SmartExecutor.Pools,name=" + threadPool.getPoolConfiguration().getPoolName()
                                                                + "(" + threadPool.getPoolConfiguration().getPoolNameShort() + ")"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return threadPool;
                }
            }
        }
    }

    private IGeneralThreadPool initThreadPool(String poolName) {
        PoolConfiguration poolConfiguration = config.getExecutorConfiguration().getPoolConfiguration(poolName);
        if (poolConfiguration != null) {
            switch (poolConfiguration.getPoolType()) {
                case regular:
                case cached:
                    return new SmartThreadPool(poolConfiguration);
                case scheduled:
                    return new SmartScheduledThreadPool(poolConfiguration);
            }
        }
        throw new RuntimeException("Configaration for pool:" + poolName + " were not found.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SmartExecutor:\n");
        for (IGeneralThreadPool smartThreadPool : threadPoolMap.values()) {
            sb.append(smartThreadPool.toString()).append("\n");
        }
        return sb.toString();
    }

    public int getActivePools() {
        return threadPoolMap.size();
    }
}
