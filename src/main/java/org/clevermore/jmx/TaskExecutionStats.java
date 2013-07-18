/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.jmx;

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
