package org.smexec.jmx;

public interface PoolStatsMBean {

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

    String getChunks();

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

}
