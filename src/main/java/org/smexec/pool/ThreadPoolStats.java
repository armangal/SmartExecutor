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

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolStats {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolStats.class);

    /**
     * the total stats about the pool
     */
    private final PoolStatsData totalData = new PoolStatsData();

    private final String poolName;
    /**
     * the amount of chunks to keep in memory
     */
    private final int chunks;

    /**
     * how often to log stats
     */
    private final int logStats;

    private int chunksCounter = 0;

    /**
     * the actual chunks
     */
    private LinkedList<PoolStatsData> history = new LinkedList<PoolStatsData>();

    /**
     * statistics about tasks
     */
    private ConcurrentMap<String, PoolStatsData> taskStatsMap = new ConcurrentHashMap<String, PoolStatsData>(0);

    /**
     * @param chunks - the amount of chunks to keep in memory
     * @param poolName
     */
    public ThreadPoolStats(final int chunks, final int logStats, final String poolName) {
        // keeping one more for current stats
        this.chunks = chunks + 1;
        this.logStats = logStats;
        this.poolName = poolName;
        // adding the current one
        history.add(new PoolStatsData());
    }

    /**
     * @return the current chunk where stats are aggregated
     */
    private PoolStatsData getCurrentChunk() {
        return history.getLast();
    }

    /**
     * creates new chunk and cleans the history if needed
     */
    public void cutChunk() {
        PoolStatsData currentChunk = getCurrentChunk();
        history.add(new PoolStatsData());

        Calendar calendar = Calendar.getInstance();
        currentChunk.setChunkTime((calendar.get(Calendar.HOUR_OF_DAY) * 10000) + (calendar.get(Calendar.MINUTE) * 100) + calendar.get(Calendar.SECOND));
        if (history.size() > chunks) {
            history.remove();
        }

        chunksCounter++;
        if (logStats > 0 && chunksCounter % logStats == 0) {
            logger.info("ThreadPoolStats for pool:{}, {}", poolName, this);
            chunksCounter = 0;
        }
    }

    public long incrementSubmitted(String taskName) {
        getCurrentChunk().getSubmitted().incrementAndGet();
        getTasksStats(taskName).getSubmitted().incrementAndGet();
        return totalData.getSubmitted().incrementAndGet();
    }

    public long incrementSubmitted(long delta) {
        getCurrentChunk().getSubmitted().addAndGet(delta);
        return totalData.getSubmitted().addAndGet(delta);
    }

    public long incrementRejected(String taskName) {
        getCurrentChunk().getRejected().incrementAndGet();
        getTasksStats(taskName).getRejected().incrementAndGet();
        return totalData.getRejected().incrementAndGet();
    }

    public long incrementRejected(long delta) {
        getCurrentChunk().getRejected().addAndGet(delta);
        return totalData.getRejected().addAndGet(delta);
    }

    public long incrementExecuted(String taskName) {
        getCurrentChunk().getExecuted().incrementAndGet();
        getTasksStats(taskName).getExecuted().incrementAndGet();
        return totalData.getExecuted().incrementAndGet();
    }

    public long incrementFailed(String taskName) {
        getCurrentChunk().getFailed().incrementAndGet();
        getTasksStats(taskName).getFailed().incrementAndGet();
        return totalData.getFailed().incrementAndGet();
    }

    public long incrementCompleted(String taskName) {
        getCurrentChunk().getCompleted().incrementAndGet();
        getTasksStats(taskName).getCompleted().incrementAndGet();
        return totalData.getCompleted().incrementAndGet();
    }

    public void updateTimings(long executionDuration, String taskName) {
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

    public String[] getTaskNames() {
        Set<String> keySet = taskStatsMap.keySet();
        return keySet.toArray(new String[keySet.size()]);
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

    public LinkedList<PoolStatsData> getHistory() {
        return history;
    }

    private PoolStatsData getTasksStats(String taskName) {
        PoolStatsData poolStatsData = taskStatsMap.get(taskName);
        if (poolStatsData == null) {
            synchronized (taskStatsMap) {
                if (taskStatsMap.containsKey(taskName)) {
                    poolStatsData = taskStatsMap.get(taskName);
                } else {
                    poolStatsData = new PoolStatsData();
                    taskStatsMap.put(taskName, poolStatsData);
                }
            }
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

        PoolStatsData h = null;
        if (history.size() > 1) {
            Iterator<PoolStatsData> iterator = history.descendingIterator();
            iterator.next();
            h = iterator.next(); // take the one before last element, beacuse the last one will be empty
        } else {
            h = getCurrentChunk();
        }
        builder.append("\nChunk: [").append(h.toString()).append("]");
        return builder.toString();
    }

}
