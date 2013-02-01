package org.smexec.pool.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.PoolStats;
import org.smexec.wrappers.SmartCallable;
import org.smexec.wrappers.SmartRunnable;

public abstract class AbstractSmartPool<E extends ExecutorService>
    implements ISmartThreadPool {

    protected final PoolStats poolStats;

    protected final PoolConfiguration poolConf;

    protected E pool;

    protected final ThreadFactory threadFactory;

    public AbstractSmartPool(final PoolConfiguration poolConf) {
        this.poolConf = poolConf;
        this.poolStats = new PoolStats(poolConf.getChunks());

        threadFactory = new ThreadFactory() {

            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, poolConf.getPoolType().getThreadNamePrefix() + poolConf.getPoolNameShort() + "_" + threadNumber.incrementAndGet());
            }
        };

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

    @Override
    public void execute(Runnable command) {
        execute(command, null);
    }

    @Override
    public void execute(Runnable command, String threadNameSuffix) {
        poolStats.incrementSubmitted();
        try {
            pool.execute(wrapRunnble(command, threadNameSuffix));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, null);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, String threadNameSuffix) {
        try {
            poolStats.incrementSubmitted();
            return pool.submit(wrapCallable(task, threadNameSuffix));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public Future<?> submit(Runnable task) {
        return submit(task, null);
    }

    @Override
    public Future<?> submit(Runnable task, String threadNameSuffix) {
        poolStats.incrementSubmitted();
        try {
            return pool.submit(wrapRunnble(task, threadNameSuffix));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return submit(task, result, null);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result, String threadNameSuffix) {
        poolStats.incrementSubmitted();
        try {
            return pool.submit(wrapRunnble(task, threadNameSuffix), result);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected();
            throw e;
        }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException {
        return pool.awaitTermination(timeout, unit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException {
        poolStats.incrementSubmitted(tasks.size());
        try {
            return pool.invokeAll(wrapCallable(tasks, null));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(tasks.size());
            throw e;
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException {
        poolStats.incrementSubmitted(tasks.size());
        try {
            return pool.invokeAll(wrapCallable(tasks, null), timeout, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(tasks.size());
            throw e;
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException {
        poolStats.incrementSubmitted(tasks.size());
        try {
            return pool.invokeAny(wrapCallable(tasks, null));
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(tasks.size());
            throw e;
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        poolStats.incrementSubmitted(tasks.size());
        try {
            return pool.invokeAny(wrapCallable(tasks, null), timeout, unit);
        } catch (RejectedExecutionException e) {
            poolStats.incrementRejected(tasks.size());
            throw e;
        }
    }

    private <T> Collection<SmartCallable<T>> wrapCallable(Collection<? extends Callable<T>> tasks, String threadNameSuffix) {
        Collection<SmartCallable<T>> smartTasks = new HashSet<SmartCallable<T>>(tasks.size());
        for (Callable<T> c : tasks) {
            smartTasks.add(wrapCallable(c, threadNameSuffix));
        }
        return smartTasks;
    }

    protected SmartRunnable wrapRunnble(Runnable r, String threadNameSuffix) {
        return new SmartRunnable(r, threadNameSuffix, poolStats);
    }

    protected <T> SmartCallable<T> wrapCallable(Callable<T> c, String threadNameSuffix) {
        return new SmartCallable<T>(c, threadNameSuffix, poolStats);
    }

    public PoolStats getPoolStats() {
        return poolStats;
    }

    @Override
    public String toString() {
        return "Pool:[" + pool.toString() + "]\nStats:[" + poolStats + "]\nConf:[" + poolConf + "]";
    }

    @Override
    public String getPoolName() {
        return poolConf.getPoolName();
    }

    public PoolConfiguration getPoolConfiguration() {
        return poolConf;
    }

    @Override
    public void shutdown() {
        pool.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return pool.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return pool.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return pool.isTerminated();
    }

}
