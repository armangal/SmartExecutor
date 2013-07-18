/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.pool;

import java.util.concurrent.ExecutorService;

import org.clevermore.configuration.PoolConfiguration;

public interface IGeneralThreadPool
    extends ExecutorService {

    ThreadPoolStats getPoolStats();

    PoolConfiguration getPoolConfiguration();

    /**
     * Returns the core number of threads.
     * 
     * @return the core number of threads
     */
    int getCorePoolSize();

    /**
     * Returns the maximum allowed number of threads.
     * 
     * @return the maximum allowed number of threads
     */
    int getMaximumPoolSize();

    /**
     * Returns the current number of threads in the pool.
     * 
     * @return the number of threads
     */
    int getPoolSize();

    /**
     * Returns the approximate number of threads that are actively executing tasks.
     * 
     * @return the number of threads
     */
    int getActiveCount();

    /**
     * Returns the largest number of threads that have ever simultaneously been in the pool.
     * 
     * @return the number of threads
     */
    int getLargestPoolSize();

    /**
     * Returns the approximate total number of tasks that have ever been scheduled for execution. Because the
     * states of tasks and threads may change dynamically during computation, the returned value is only an
     * approximation.
     * 
     * @return the number of tasks
     */
    long getTaskCount();

}
