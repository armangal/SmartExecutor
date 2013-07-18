/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.clevermore.configuration.Config;
import org.clevermore.configuration.PoolConfiguration;
import org.clevermore.jmx.ExecutorStats;
import org.clevermore.jmx.ExecutorStatsMXBean;
import org.clevermore.jmx.PoolStats;
import org.clevermore.jmx.PoolStatsMXBean;
import org.clevermore.pool.IGeneralThreadPool;
import org.clevermore.pool.ISmartScheduledThreadPool;
import org.clevermore.pool.ISmartThreadPool;
import org.clevermore.pool.impl.SmartScheduledThreadPool;
import org.clevermore.pool.impl.SmartThreadPool;
import org.clevermore.utils.ConfigLoader;
import org.clevermore.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entry point to use SmartExecutor framework.<br>
 * It's possible to have many different instances of SmartExecutor
 */
public class SmartExecutor {

    private static final Logger logger = LoggerFactory.getLogger("SE");

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
        this(null);
    }

    /**
     * Initializing SmartExecutor with custom configuration file
     * 
     * @param configXMLresource, if the file is in classPath, then only name is enough, if the file is located
     *            out of CP, full file path should be provided or full resource name like:
     *            <b>com/example/work/config.xml</b>
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public SmartExecutor(String configXMLresource)
        throws JAXBException, FileNotFoundException {
        logger.info("Looking from configuration file:{}", configXMLresource);
        InputStream configXML = null;
        try {
            configXML = new ConfigLoader().loadConfig(configXMLresource);
            if (configXML == null) {
                throw new RuntimeException("Config file wasn't found, check previous exceptions");
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
                        PoolStatsMXBean poolStatsMXBean = new PoolStats(config.getExecutorConfiguration().getName(), threadPool);
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

    /**
     * @return currently registered thread pools
     */
    public int getActivePools() {
        return threadPoolMap.size();
    }

    /**
     * @param poolName - thread pool to be used with CompletionService
     * @return - extended {@link ExecutorCompletionService} that allows to track stats
     */
    public <V> CompletionService<V> getCompletionService(IPoolName poolName) {
        return new SmartExecutorCompletionService<V>((ISmartThreadPool) getPool(poolName.getPoolName()));
    }

    /**
     * @param poolName - thread pool to be used with CompletionService
     * @param completionQueue - the queue to use as the completion queue normally one dedicated for use by
     *            this service
     * @return extended {@link ExecutorCompletionService} that allows to track stats
     */
    public <V> CompletionService<V> getCompletionService(IPoolName poolName, BlockingQueue<Future<V>> completionQueue) {
        return new SmartExecutorCompletionService<V>((ISmartThreadPool) getPool(poolName.getPoolName()), completionQueue);
    }
}
