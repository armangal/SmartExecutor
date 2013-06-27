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

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.pool.IGeneralThreadPool;

public class PoolStats
    extends AbstractStats
    implements PoolStatsMXBean {

    private static final String BEAN_NAME = "org.smexec:type=SmartExecutor.Pools,name=";

    private Logger logger;

    private final IGeneralThreadPool stp;

    public PoolStats(final IGeneralThreadPool stp) {
        super(BEAN_NAME + stp.getPoolConfiguration().getPoolName() + "(" + stp.getPoolConfiguration().getPoolNameShort() + ")");

        logger = LoggerFactory.getLogger("PoolStats_" + stp.getPoolConfiguration().getPoolNameShort());
        this.stp = stp;
    }

    @Override
    public String getName() {
        return stp.getPoolConfiguration().getPoolName() + "(" + stp.getPoolConfiguration().getPoolNameShort() + ")";
    }

    @Override
    public void printStats() {
        logger.info(stp.getPoolStats().toString());
    }

    @Override
    public Long getSubmitted() {
        return stp.getPoolStats().getSubmitted();
    }

    @Override
    public Long getExecuted() {

        return stp.getPoolStats().getExecuted();
    }

    @Override
    public Long getCompleted() {

        return stp.getPoolStats().getCompleted();
    }

    @Override
    public Long getRejected() {

        return stp.getPoolStats().getRejected();
    }

    @Override
    public Long getFailed() {

        return stp.getPoolStats().getFailed();
    }

    @Override
    public Long getMinTime() {
        return stp.getPoolStats().getMinTime().longValue() == Long.MAX_VALUE ? Long.valueOf(0L) : stp.getPoolStats().getMinTime();
    }

    @Override
    public Long getMaxTime() {
        return stp.getPoolStats().getMaxTime().longValue() == Long.MIN_VALUE ? Long.valueOf(0L) : stp.getPoolStats().getMaxTime();
    }

    @Override
    public Long getTotalTime() {

        return stp.getPoolStats().getTotalTime();
    }

    @Override
    public Long getAvgTime() {

        return stp.getPoolStats().getAvgTime();
    }

    @Override
    public int getPoolSize() {
        return ((ThreadPoolExecutor) stp).getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return ((ThreadPoolExecutor) stp).getActiveCount();
    }

    @Override
    public int getLargestPoolSize() {
        return ((ThreadPoolExecutor) stp).getLargestPoolSize();
    }

    @Override
    public String[] getTaskNames() {
        return stp.getPoolStats().getTaskNames();
    }

    @Override
    public TaskExecutionStats[] getTaskExecutionStats() {

        return stp.getPoolStats().getAllStats();
    }

    @Override
    public TaskExecutionStats[] getLastTaskExecutionStats(long lastUpdate) {
        return stp.getPoolStats().getLastStats(lastUpdate);
    }
}
