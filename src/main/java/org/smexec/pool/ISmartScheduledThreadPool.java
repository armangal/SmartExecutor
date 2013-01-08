package org.smexec.pool;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.smexec.SmartCallble;
import org.smexec.SmartRunnable;

public interface ISmartScheduledThreadPool
    extends ISmartThreadPool {

    ScheduledFuture<?> schedule(SmartRunnable command, long delay, TimeUnit unit);

    <V> ScheduledFuture<V> schedule(SmartCallble<V> callable, long delay, TimeUnit unit);

    ScheduledFuture<?> scheduleAtFixedRate(SmartRunnable command, long initialDelay, long period, TimeUnit unit);

    ScheduledFuture<?> scheduleWithFixedDelay(SmartRunnable command, long initialDelay, long delay, TimeUnit unit);

}
