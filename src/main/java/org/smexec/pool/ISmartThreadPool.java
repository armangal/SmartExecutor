/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.smexec.TaskMetadata;

public interface ISmartThreadPool
    extends IGeneralThreadPool, ExecutorService {

    void execute(Runnable command, TaskMetadata taskMetadata);

    <T> Future<T> submit(Callable<T> task, TaskMetadata taskMetadata);

    <T> Future<T> submit(Runnable task, T result, TaskMetadata taskMetadata);

    Future<?> submit(Runnable task, TaskMetadata taskMetadata);

}
