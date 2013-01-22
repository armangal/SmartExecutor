package org.smexec.pool;

import java.util.concurrent.Future;

import org.smexec.wrappers.SmartCallable;
import org.smexec.wrappers.SmartRunnable;

public interface ISmartThreadPool {

    void execute(SmartRunnable command);

    <T> Future<T> submit(SmartCallable<T> task);
    
    void shutdown();
    
    PoolStats getPoolStats();

}
