/**
 * Copyright (C) 2013 Arman Gal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smexec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.configuration.Config;
import org.smexec.configuration.PoolConfiguration;
import org.smexec.jmx.ExecutorStats;
import org.smexec.jmx.ExecutorStatsMXBean;
import org.smexec.jmx.PoolStats;
import org.smexec.jmx.PoolStatsMXBean;
import org.smexec.pool.IGeneralThreadPool;
import org.smexec.pool.ISmartScheduledThreadPool;
import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.impl.SmartScheduledThreadPool;
import org.smexec.pool.impl.SmartThreadPool;
import org.smexec.utils.Utils;

/**
 * The main entry point to use SmartExecutor framework.<br>
 * It's possible to have many different instances of SmartExecutor
 */
public class SmartExecutor {

    private static final Logger logger = LoggerFactory.getLogger("SE");

    private static final String defaultXMLConfName = "SmartExecutor-default.xml";
    private static final String defaultScheduledPoolName = "Scheduled";

    private ConcurrentHashMap<String, IGeneralThreadPool> threadPoolMap = new ConcurrentHashMap<String, IGeneralThreadPool>(0);

    private Config config;

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
        try {
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

            try {
                ExecutorStatsMXBean esBean = new ExecutorStats(this, config.getExecutorConfiguration().getName(), config.getExecutorConfiguration()
                                                                                                                        .getDescription());
                logger.info("Registered JMX bean for smart executor:{}", esBean.getName());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } finally {
            if (configXML != null) {
                try {
                    configXML.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * will use default metadata to execute the task.
     * 
     * @param command
     */
    public void execute(Runnable command) {
        execute(command, TaskMetadata.newDefaultMetadata());
    }

    /**
     * will use the provided metadata to execute the task
     * 
     * @param command
     * @param taskMetadata
     */
    public void execute(Runnable command, TaskMetadata taskMetadata) {
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, command);
        ISmartThreadPool smartThreadPool = (ISmartThreadPool) getPool(taskMetadata.getPoolNameAsString());
        smartThreadPool.execute(command, taskMetadata);
    }

    // /////////////////////

    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, TaskMetadata.newDefaultMetadata());
    }

    public <T> Future<T> submit(Callable<T> task, TaskMetadata taskMetadata) {
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, task);
        ISmartThreadPool smartThreadPool = (ISmartThreadPool) getPool(taskMetadata.getPoolNameAsString());
        return smartThreadPool.submit(task, taskMetadata);
    }

    // //////////////////////

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return schedule(command, delay, unit, TaskMetadata.newDefaultMetadata());
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, TaskMetadata taskMetadata) {
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, command);
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(taskMetadata.getPoolNameAsString());
        return scheduledPool.schedule(command, delay, unit, taskMetadata);
    }

    // ///////////////////////

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return schedule(callable, delay, unit, TaskMetadata.newDefaultMetadata());
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, TaskMetadata taskMetadata) {
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, callable);
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(taskMetadata.getPoolNameAsString());
        return scheduledPool.schedule(callable, delay, unit, taskMetadata);
    }

    // //////////////////////////

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return scheduleAtFixedRate(command, initialDelay, period, unit, TaskMetadata.newDefaultMetadata());
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, TaskMetadata taskMetadata) {
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, command);
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(taskMetadata.getPoolNameAsString());
        return scheduledPool.scheduleAtFixedRate(command, initialDelay, period, unit, taskMetadata);
    }

    // ///////////////////////////

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return scheduleWithFixedDelay(command, initialDelay, delay, unit, TaskMetadata.newDefaultMetadata());
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, TaskMetadata taskMetadata) {
        taskMetadata.setStack(new Throwable().getStackTrace());
        Utils.fillMetaData(taskMetadata, command);
        ISmartScheduledThreadPool scheduledPool = getScheduledPool(taskMetadata.getPoolNameAsString());
        return scheduledPool.scheduleWithFixedDelay(command, initialDelay, delay, unit, taskMetadata);
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

    public ExecutorService getThreadPool(IPoolName poolName) {
        return getPool(poolName.getPoolName());
    }

    private IGeneralThreadPool getPool(String poolName) {
        if (threadPoolMap.containsKey(poolName)) {
            return threadPoolMap.get(poolName);
        } else {
            synchronized (this.getClass()) {
                if (threadPoolMap.containsKey(poolName)) {
                    return threadPoolMap.get(poolName);
                } else {
                    IGeneralThreadPool threadPool = initThreadPool(poolName);
                    threadPoolMap.put(poolName, threadPool);

                    try {
                        PoolStatsMXBean poolStatsMXBean = new PoolStats(threadPool);
                        logger.info("Registered JMX bean for smart pool:{}", poolStatsMXBean.getName());
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
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
