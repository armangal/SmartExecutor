/**
 * Copyright (C) 2013 Arman Gal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smexec.jmx;

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
