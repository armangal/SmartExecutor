package org.smexec.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.smexec.SmartRunnable;

public interface ISmartThreadPool {

    void execute(SmartRunnable command);

    <T> Future<T> submit(Callable<T> task);

}
