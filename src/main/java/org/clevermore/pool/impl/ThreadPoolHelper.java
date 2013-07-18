/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.pool.impl;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.clevermore.TaskMetadata;
import org.clevermore.configuration.PoolConfiguration;
import org.clevermore.pool.ThreadPoolStats;
import org.clevermore.utils.Utils;
import org.clevermore.wrappers.SmartCallable;
import org.clevermore.wrappers.SmartRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class with useful methods
 * 
 * @author armang
 */
final class ThreadPoolHelper {

    private static Logger logger = LoggerFactory.getLogger("ThreadPoolHelper");

    protected static <T> Collection<SmartCallable<T>> wrapCallable(Collection<? extends Callable<T>> tasks, TaskMetadata taskMetadata, ThreadPoolStats poolStats) {
        Collection<SmartCallable<T>> smartTasks = new HashSet<SmartCallable<T>>(tasks.size());
        for (Callable<T> c : tasks) {
            smartTasks.add(wrapCallable(c, taskMetadata, poolStats));
        }
        return smartTasks;
    }

    protected static <T> Collection<SmartCallable<T>> wrapCallable(Collection<? extends Callable<T>> tasks, ThreadPoolStats poolStats) {
        Collection<SmartCallable<T>> smartTasks = new HashSet<SmartCallable<T>>(tasks.size());
        for (Callable<T> c : tasks) {
            TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
            taskMetadata.setStack(new Throwable().getStackTrace());
            Utils.fillMetaData(taskMetadata, c);

            smartTasks.add(wrapCallable(c, taskMetadata, poolStats));
        }
        return smartTasks;
    }

    protected static SmartRunnable wrapRunnble(Runnable r, TaskMetadata taskMetadata, ThreadPoolStats poolStats) {
        return new SmartRunnable(r, taskMetadata, poolStats);
    }

    protected static <V> SmartCallable<V> wrapCallable(Callable<V> c, TaskMetadata taskMetadata, ThreadPoolStats poolStats) {
        return new SmartCallable<V>(c, taskMetadata, poolStats);
    }

    /**
     * UncaughtExceptionHandler to be used for each thread created by the framework
     */
    private static UncaughtExceptionHandler eh = new UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("Thread with name:" + t.getName() + " had uncaught execption:" + e.getMessage(), e);
        }
    };

    /**
     * helper method for creating thread factory
     * 
     * @param poolConf - configurations for a destination thread pool
     * @return - factory implementation
     */
    protected static ThreadFactory getThreadFactory(final PoolConfiguration poolConf) {
        return new ThreadFactory() {

            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, poolConf.getPoolType().getThreadNamePrefix() + poolConf.getPoolNameShort() + "_" + threadNumber.incrementAndGet());
                thread.setUncaughtExceptionHandler(eh);
                return thread;
            }
        };

    }

}
