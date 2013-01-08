package org.smexec.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.smexec.SmartRunnble;

public interface ISmartThreadPool {

    void execute(SmartRunnble command);

    <T> Future<T> submit(Callable<T> task);

}
