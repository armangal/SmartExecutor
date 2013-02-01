package org.smexec.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.smexec.configuration.PoolConfiguration;

public interface ISmartThreadPool
    extends ExecutorService {

    PoolStats getPoolStats();

    String getPoolName();

    PoolConfiguration getPoolConfiguration();

    void execute(Runnable command, String threadNameSuffix);

    <T> Future<T> submit(Callable<T> task, String threadNameSuffix);

    <T> Future<T> submit(Runnable task, T result, String threadNameSuffix);

    Future<?> submit(Runnable task, String threadNameSuffix);

}
