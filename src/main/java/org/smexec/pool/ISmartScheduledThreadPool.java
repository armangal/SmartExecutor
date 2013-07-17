/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.smexec.TaskMetadata;

public interface ISmartScheduledThreadPool
    extends ScheduledExecutorService, IGeneralThreadPool {

    ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, TaskMetadata taskMetadata);

    <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, TaskMetadata taskMetadata);

    ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, TaskMetadata taskMetadata);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, TaskMetadata taskMetadata);
}
