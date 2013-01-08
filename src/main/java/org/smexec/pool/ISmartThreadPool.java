package org.smexec.pool;

import java.util.concurrent.Future;

import org.smexec.SmartCallable;
import org.smexec.SmartRunnable;

public interface ISmartThreadPool {

    void execute(SmartRunnable command);

    <T> Future<T> submit(SmartCallable<T> task);
    
    void shutdown();

}
