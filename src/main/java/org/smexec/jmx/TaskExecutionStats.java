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

import java.beans.ConstructorProperties;
import java.util.Map;

public class TaskExecutionStats
    extends AbstractJmxStatEntry {

    private TaskExecutionChunk globalStats;
    private Map<String, TaskExecutionChunk> taskStatsMap;

    @ConstructorProperties({"startTime", "endTime", "globalStats", "taskStatsMap"})
    public TaskExecutionStats(long startTime, long endTime, TaskExecutionChunk globalStats, Map<String, TaskExecutionChunk> taskStatsMap) {
        super(startTime, endTime);

        this.globalStats = globalStats;
        this.taskStatsMap = taskStatsMap;
    }

    public TaskExecutionChunk getGlobalStats() {
        return globalStats;
    }

    public Map<String, TaskExecutionChunk> getTaskStatsMap() {
        return taskStatsMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskExecutionStats [globalStats=")
               .append(globalStats)
               .append(", taskStatsMap=")
               .append(taskStatsMap)
               .append(", endTime=")
               .append(endTime)
               .append(", cpuUsage=")
               .append(cpuUsage)
               .append("]");
        return builder.toString();
    }

}
