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
package org.smexec.pool;

import java.util.concurrent.ExecutorService;

import org.smexec.configuration.PoolConfiguration;

public interface IGeneralThreadPool
    extends ExecutorService {

    ThreadPoolStats getPoolStats();

    PoolConfiguration getPoolConfiguration();

    /**
     * Returns the core number of threads.
     * 
     * @return the core number of threads
     * @see #setCorePoolSize
     */
    int getCorePoolSize();

    /**
     * Returns the maximum allowed number of threads.
     * 
     * @return the maximum allowed number of threads
     * @see #setMaximumPoolSize
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
