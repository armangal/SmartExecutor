/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import org.clevermore.pool.ISmartThreadPool;
import org.clevermore.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bit extended version of {@link ExecutorCompletionService} that makes it possible to track stats over the
 * executed tasks
 * 
 * @author armang
 * @param <V>
 */
public class SmartExecutorCompletionService<V>
    extends ExecutorCompletionService<V> {

    private static Logger logger = LoggerFactory.getLogger("SmartExecutorCompletionService");

    static Method newTaskForCallable;
    static Method newTaskForRunnable;
    static Field completionQueueField;

    private ISmartThreadPool threadPool;

    SmartExecutorCompletionService(ISmartThreadPool executor) {
        super(executor);
        this.threadPool = executor;
    }

    SmartExecutorCompletionService(ISmartThreadPool executor, BlockingQueue<Future<V>> completionQueue) {
        super(executor, completionQueue);
        this.threadPool = executor;
    }

    @Override
    public Future<V> submit(Callable<V> task) {
        if (task == null)
            throw new NullPointerException();
        try {
            RunnableFuture<V> f = newTaskFor(task);
            TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
            Utils.fillMetaData(taskMetadata, task);
            threadPool.execute(new QueueingFuture(f), taskMetadata);
            return f;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Future<V> submit(Runnable task, V result) {
        if (task == null)
            throw new NullPointerException();
        RunnableFuture<V> f = newTaskFor(task, result);
        TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
        Utils.fillMetaData(taskMetadata, task);
        threadPool.execute(new QueueingFuture(f), taskMetadata);
        return f;
    }

    @SuppressWarnings("unchecked")
    private RunnableFuture<V> newTaskFor(Callable<V> task) {
        try {
            if (newTaskForCallable == null) {
                synchronized (this.getClass()) {
                    if (newTaskForCallable == null) {
                        newTaskForCallable = this.getClass().getSuperclass().getDeclaredMethod("newTaskFor", Callable.class);
                        newTaskForCallable.setAccessible(true);
                    }
                }
            }
            return (RunnableFuture<V>) newTaskForCallable.invoke(this, task);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("unchecked")
    private RunnableFuture<V> newTaskFor(Runnable task, V result) {
        try {
            if (newTaskForRunnable == null) {
                synchronized (this.getClass()) {
                    if (newTaskForRunnable == null) {
                        newTaskForRunnable = this.getClass().getSuperclass().getDeclaredMethod("newTaskFor", Runnable.class);
                        newTaskForRunnable.setAccessible(true);
                    }
                }
            }
            return (RunnableFuture<V>) newTaskForRunnable.invoke(this, task, result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public BlockingQueue<Future<V>> getCompletionQueue()
        throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
        if (completionQueueField == null) {
            synchronized (this.getClass()) {
                if (completionQueueField == null) {
                    completionQueueField = this.getClass().getSuperclass().getDeclaredField("completionQueue");
                    completionQueueField.setAccessible(true);

                }
            }
        }
        return (BlockingQueue<Future<V>>) completionQueueField.get(this);
    }

    /**
     * FutureTask extension to enqueue upon completion
     */
    class QueueingFuture
        extends FutureTask<Void> {

        QueueingFuture(RunnableFuture<V> task) {
            super(task, null);
            this.task = task;
        }

        protected void done() {
            try {
                getCompletionQueue().add(task);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        private final Future<V> task;
    }

}
