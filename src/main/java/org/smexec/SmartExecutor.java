/**
 * MIT License 
 * 
 * Copyright (c) 2013 Arman Gal
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.smexec;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.pool.SmartThreadPool;

/**
 * The main entry point to utilize SmartExecutor
 */
public class SmartExecutor {

    private static Logger logger = LoggerFactory.getLogger("SE");

    private ConcurrentHashMap<String, SmartThreadPool> threadPoolMap = new ConcurrentHashMap<String, SmartThreadPool>(0);

    private String defaultPoolName;

    private Properties smartExecutorProperties;

    public SmartExecutor(Properties smartExecutorProperties) {
        this.smartExecutorProperties = smartExecutorProperties;
        logger.info("Initilized SmartExecutor with properties:{}", smartExecutorProperties);
    }

    public void execute(Runnable command, String poolName, String threadNameSuffix) {
        SmartThreadPool smartThreadPool = getPool(poolName);
        SmartRunnable sr = new SmartRunnable(command, threadNameSuffix);
        smartThreadPool.execute(sr);
    }

    public void execute(Runnable command, String poolName) {
        execute(command, poolName, null);
    }

    public void execute(Runnable command) {
        execute(command, defaultPoolName, null);
    }

    public <T> Future<T> submit(Callable<T> task, String poolName) {
        SmartThreadPool smartThreadPool = getPool(poolName);
        return smartThreadPool.submit(task);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, defaultPoolName);
    }

    public void shutdown() {

    }

    private SmartThreadPool getPool(String poolName) {
        if (threadPoolMap.contains(poolName)) {
            return threadPoolMap.get(poolName);
        } else {
            synchronized (threadPoolMap) {
                if (threadPoolMap.contains(poolName)) {
                    return threadPoolMap.get(poolName);
                } else {
                    SmartThreadPool threadPool = initThreadPool(poolName);
                    threadPoolMap.put(poolName, threadPool);
                    return threadPool;
                }
            }
        }
    }

    private SmartThreadPool initThreadPool(String poolName) {
        return new SmartThreadPool(poolName, smartExecutorProperties);
    }
}
