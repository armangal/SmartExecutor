package org.smexec.pool.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ThreadPoolStats;
import org.smexec.wrappers.SmartCallable;
import org.smexec.wrappers.SmartRunnable;

final class ThreadPoolHelper {

    protected static <T> Collection<SmartCallable<T>> wrapCallable(Collection<? extends Callable<T>> tasks, String threadNameSuffix, ThreadPoolStats poolStats) {
        Collection<SmartCallable<T>> smartTasks = new HashSet<SmartCallable<T>>(tasks.size());
        for (Callable<T> c : tasks) {
            smartTasks.add(wrapCallable(c, threadNameSuffix, poolStats));
        }
        return smartTasks;
    }

    protected static SmartRunnable wrapRunnble(Runnable r, String threadNameSuffix, ThreadPoolStats poolStats) {
        return new SmartRunnable(r, threadNameSuffix, poolStats);
    }

    protected static <V> SmartCallable<V> wrapCallable(Callable<V> c, String threadNameSuffix, ThreadPoolStats poolStats) {
        return new SmartCallable<V>(c, threadNameSuffix, poolStats);
    }

    protected static void scheduleChunker(PoolConfiguration poolConf, final ThreadPoolStats poolStats) {
        // Schedule custom chunker thread
        Timer t = new Timer("SE_CHUNKER_" + poolConf.getPoolNameShort());
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    poolStats.addChunk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, poolConf.getChunkInterval(), poolConf.getChunkInterval());
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
}
