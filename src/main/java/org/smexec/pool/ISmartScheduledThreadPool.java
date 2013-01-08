package org.smexec.pool;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.smexec.SmartCallble;

public interface ISmartScheduledThreadPool
    extends ISmartThreadPool {

    ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

    <V> ScheduledFuture<V> schedule(SmartCallble<V> callable, long delay, TimeUnit unit);

    ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);

}
