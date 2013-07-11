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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.jmx.AbstractJmxStatDataHolder;
import org.smexec.jmx.TaskExecutionChunk;
import org.smexec.jmx.TaskExecutionStats;

public class ThreadPoolStats
    extends AbstractJmxStatDataHolder<TaskExecutionStats> {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolStats.class);

    /**
     * the global stats about the pool
     */
    private final PoolStatsData totalData = new PoolStatsData();

    /**
     * holds the current stats until new one is cutt
     */
    private final PoolStatsData currentStats = new PoolStatsData();

    private final String poolName;

    /**
     * how often to log stats
     */
    private final int logStats;
    private int chunksCounter = 0;

    /**
     * statistics about tasks in current chunk
     */
    private ConcurrentMap<String, PoolStatsData> taskStatsMap = new ConcurrentHashMap<String, PoolStatsData>(0);

    private Set<String> knowsTasks = new HashSet<String>();

    /**
     * @param chunks - the amount of chunks to keep in memory
     * @param poolName
     */
    public ThreadPoolStats(final int chunks, final long interval, final int logStats, final String poolName) {
        super(chunks, interval, poolName);
        this.logStats = logStats;
        this.poolName = poolName;
    }

    /**
     * @return the current chunk where stats are aggregated
     */
    private PoolStatsData getCurrentChunk() {
        return currentStats;
    }

    public long incrementSubmitted(final String taskName) {
        getCurrentChunk().getSubmitted().incrementAndGet();
        getTasksStats(taskName).getSubmitted().incrementAndGet();
        return totalData.getSubmitted().incrementAndGet();
    }

    public long incrementSubmitted(long delta) {
        getCurrentChunk().getSubmitted().addAndGet(delta);
        return totalData.getSubmitted().addAndGet(delta);
    }

    public long incrementRejected(final String taskName) {
        getCurrentChunk().getRejected().incrementAndGet();
        getTasksStats(taskName).getRejected().incrementAndGet();
        return totalData.getRejected().incrementAndGet();
    }

    public long incrementRejected(long delta) {
        getCurrentChunk().getRejected().addAndGet(delta);
        return totalData.getRejected().addAndGet(delta);
    }

    public long incrementExecuted(final String taskName) {
        getCurrentChunk().getExecuted().incrementAndGet();
        getTasksStats(taskName).getExecuted().incrementAndGet();
        return totalData.getExecuted().incrementAndGet();
    }

    public long incrementFailed(final String taskName) {
        getCurrentChunk().getFailed().incrementAndGet();
        getTasksStats(taskName).getFailed().incrementAndGet();
        return totalData.getFailed().incrementAndGet();
    }

    public long incrementCompleted(final String taskName) {
        getCurrentChunk().getCompleted().incrementAndGet();
        getTasksStats(taskName).getCompleted().incrementAndGet();
        return totalData.getCompleted().incrementAndGet();
    }

    public void updateTimings(long executionDuration, final String taskName) {
        // update total stats
        updateTimings(totalData, executionDuration);

        // update current chunk
        updateTimings(getCurrentChunk(), executionDuration);

        // update stats per task
        updateTimings(getTasksStats(taskName), executionDuration);

    }

    private void updateTimings(PoolStatsData poolStatsData, long executionDuration) {
        if (executionDuration > poolStatsData.getMaxTime().get()) {
            poolStatsData.getMaxTime().set(executionDuration);
        }
        if (executionDuration < poolStatsData.getMinTime().get()) {
            poolStatsData.getMinTime().set(executionDuration);
        }

        poolStatsData.getTotalTime().addAndGet(executionDuration);
        poolStatsData.getCompleted().incrementAndGet();
    }

    /**
     * @return array of task ID know/executed in this pool till now.
     */
    public String[] getTaskNames() {
        return knowsTasks.toArray(new String[knowsTasks.size()]);
    }

    public Long getSubmitted() {
        return totalData.getSubmitted().get();
    }

    public Long getExecuted() {
        return totalData.getExecuted().get();
    }

    public Long getCompleted() {
        return totalData.getCompleted().get();
    }

    public Long getRejected() {
        return totalData.getRejected().get();
    }

    public Long getFailed() {
        return totalData.getFailed().get();
    }

    public Long getMinTime() {
        return totalData.getMinTime().get();
    }

    public Long getMaxTime() {
        return totalData.getMaxTime().get();
    }

    public Long getTotalTime() {
        return totalData.getTotalTime().get();
    }

    public Long getAvgTime() {
        return totalData.getAvgTime();
    }

    private PoolStatsData getTasksStats(final String taskName) {
        PoolStatsData poolStatsData = taskStatsMap.get(taskName);
        if (poolStatsData == null) {
            synchronized (this.getClass()) {
                if (taskStatsMap.containsKey(taskName)) {
                    poolStatsData = taskStatsMap.get(taskName);
                } else {
                    poolStatsData = new PoolStatsData();
                    taskStatsMap.put(taskName, poolStatsData);
                }
            }
        }
        if (!knowsTasks.contains(taskName)) {
            knowsTasks.add(taskName);
        }
        return poolStatsData;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nPoolStats [submitted=")
               .append(totalData.getSubmitted())
               .append(", executed=")
               .append(totalData.getExecuted())
               .append(", completed=")
               .append(totalData.getCompleted())
               .append(", rejected=")
               .append(totalData.getRejected())
               .append(", failed=")
               .append(totalData.getFailed())
               .append(", minTime=")
               .append(totalData.getMinTime())
               .append(", maxTime=")
               .append(totalData.getMaxTime())
               .append(", totalTime=")
               .append(totalData.getTotalTime())
               .append(", avgTime=")
               .append(totalData.getAvgTime())
               .append("]");
        return builder.toString();
    }

    @Override
    protected TaskExecutionStats[] getStatsAsArray(List<TaskExecutionStats> list) {
        return list.toArray(new TaskExecutionStats[list.size()]);
    }

    @Override
    protected TaskExecutionStats snapshotStats() {
        PoolStatsData currentChunk = getCurrentChunk();
        TaskExecutionChunk taskExecutionChunk = new TaskExecutionChunk(currentChunk.getSubmitted().getAndSet(0L),
                                                                       currentChunk.getExecuted().getAndSet(0L),
                                                                       currentChunk.getCompleted().getAndSet(0L),
                                                                       currentChunk.getRejected().getAndSet(0L),
                                                                       currentChunk.getFailed().getAndSet(0L),
                                                                       currentChunk.getMinTime().getAndSet(0L),
                                                                       currentChunk.getMaxTime().getAndSet(0L),
                                                                       currentChunk.getTotalTime().getAndSet(0L));
        Map<String, TaskExecutionChunk> taskStats = new HashMap<String, TaskExecutionChunk>(taskStatsMap.size());
        Set<String> keys = new HashSet<String>(taskStatsMap.keySet());
        for (String key : keys) {
            PoolStatsData removed = taskStatsMap.remove(key);
            taskStats.put(key, new TaskExecutionChunk(removed.getSubmitted().getAndSet(0L),
                                                      removed.getExecuted().getAndSet(0L),
                                                      removed.getCompleted().getAndSet(0L),
                                                      removed.getRejected().getAndSet(0L),
                                                      removed.getFailed().getAndSet(0L),
                                                      removed.getMinTime().getAndSet(0L),
                                                      removed.getMaxTime().getAndSet(0L),
                                                      removed.getTotalTime().getAndSet(0L)));
        }

        TaskExecutionStats tes = new TaskExecutionStats(lastStartTime, getCurrentTime(), taskExecutionChunk, taskStats);
        chunksCounter++;
        if (logStats > 0 && chunksCounter % logStats == 0) {
            logger.info("ThreadPoolStats for pool:{}, {}", poolName, this);
            chunksCounter = 0;
        }

        return tes;
    }

}
