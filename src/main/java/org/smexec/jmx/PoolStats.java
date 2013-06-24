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

import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.pool.IGeneralThreadPool;
import org.smexec.pool.PoolStatsData;

public class PoolStats
    implements PoolStatsMXBean {

    private static Logger logger = LoggerFactory.getLogger(PoolStats.class);

    private final IGeneralThreadPool stp;

    public PoolStats(final IGeneralThreadPool stp) {
        this.stp = stp;
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

        return stp.getPoolStats().getMinTime() == Long.MAX_VALUE ? 0 : stp.getPoolStats().getMinTime();
    }

    @Override
    public Long getMaxTime() {

        return stp.getPoolStats().getMaxTime() == Long.MIN_VALUE ? 0 : stp.getPoolStats().getMaxTime();
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
    public ExecutionTimeStats[] getExecutionTimeStats() {
        LinkedList<PoolStatsData> history = stp.getPoolStats().getHistory();
        ExecutionTimeStats[] arr = new ExecutionTimeStats[history.size() - 1];
        for (int i = 0; i < (history.size() - 1); i++) {
            PoolStatsData h = history.get(i);
            ExecutionTimeStats e = new ExecutionTimeStats(h.getMinTimeLong(), h.getMaxTimeLong(), h.getAvgTime(), h.getChunkTime());
            arr[i] = e;
        }
        return arr;
    }

    @Override
    public TaskExecutionStats[] getTaskExecutionStats() {
        LinkedList<PoolStatsData> history = stp.getPoolStats().getHistory();
        TaskExecutionStats[] arr = new TaskExecutionStats[history.size() - 1];
        for (int i = 0; i < (history.size() - 1); i++) {
            PoolStatsData h = history.get(i);
            arr[i] = new TaskExecutionStats(h.getSubmitted().get(),
                                            h.getExecuted().get(),
                                            h.getCompleted().get(),
                                            h.getRejected().get(),
                                            h.getFailed().get(),
                                            h.getChunkTime());
        }
        return arr;
    }
}
