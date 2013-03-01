package org.smexec.pool.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.ITaskIdentification;
import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ThreadPoolStats;
import org.smexec.wrappers.SmartCallable;
import org.smexec.wrappers.SmartRunnable;

final class ThreadPoolHelper {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolHelper.class);

    /**
     * local pool to be used for chunkers
     */
    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(2, new ThreadFactory() {

        int trn = 0;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "SE_CHUNKER_" + trn++);
        }
    });

    protected static <T> Collection<SmartCallable<T>> wrapCallable(Collection<? extends Callable<T>> tasks, String threadNameSuffix, ThreadPoolStats poolStats) {
        Collection<SmartCallable<T>> smartTasks = new HashSet<SmartCallable<T>>(tasks.size());
        for (Callable<T> c : tasks) {
            smartTasks.add(wrapCallable(c, getTaskIdentification(c), threadNameSuffix, poolStats));
        }
        return smartTasks;
    }

    protected static SmartRunnable wrapRunnble(Runnable r, String taskIdentification, String threadNameSuffix, ThreadPoolStats poolStats) {
        return new SmartRunnable(r, taskIdentification, threadNameSuffix, poolStats);
    }

    protected static <V> SmartCallable<V> wrapCallable(Callable<V> c, String taskIdentification, String threadNameSuffix, ThreadPoolStats poolStats) {
        return new SmartCallable<V>(c, taskIdentification, threadNameSuffix, poolStats);
    }

    /**
     * schedules a repeated task that will "cut" chunks of statistics in order to preserve periodical data
     * 
     * @param poolConf
     * @param poolStats
     */
    protected static void scheduleChunker(final PoolConfiguration poolConf, final ThreadPoolStats poolStats) {

        // Schedule custom chunker thread
        pool.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                try {
                    Thread.currentThread().setName("SE_CHUNKER_" + poolConf.getPoolNameShort());
                    poolStats.cutChunk();

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);

                } finally {
                    Thread.currentThread().setName(name);
                }
            }

        }, poolConf.getChunkInterval(), poolConf.getChunkInterval(), TimeUnit.MILLISECONDS);

    }

    protected static ThreadFactory getThreadFactory(final PoolConfiguration poolConf) {
        return new ThreadFactory() {

            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, poolConf.getPoolType().getThreadNamePrefix() + poolConf.getPoolNameShort() + "_" + threadNumber.incrementAndGet());
            }
        };

    }

    protected static String getTaskIdentification(Object task) {
        if (task instanceof ITaskIdentification) {
            return ((ITaskIdentification) task).getTaskId();
        } else {
            return task.getClass().getName();
        }
    }
}
