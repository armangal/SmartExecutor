package org.smexec.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface ISmartScheduledThreadPool
    extends ScheduledExecutorService {

    ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, String threadNameSuffix);

    <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, String threadNameSuffix);

    ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, String threadNameSuffix);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, String threadNameSuffix);
}
