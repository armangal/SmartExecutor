/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.jmx;

public interface PoolStatsMXBean {

    String getSmartExecutorName();

    String getName();

    String getDescription();

    void printStats();

    Long getSubmitted();

    Long getExecuted();

    Long getCompleted();

    Long getRejected();

    Long getFailed();

    Long getMinTime();

    Long getMaxTime();

    Long getTotalTime();

    Long getAvgTime();

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

    String[] getTaskNames();

    TaskExecutionStats[] getTaskExecutionStats();

    TaskExecutionStats[] getLastTaskExecutionStats(long lastUpdate);
}
