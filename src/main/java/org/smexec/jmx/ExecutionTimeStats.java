package org.smexec.jmx;

import java.beans.ConstructorProperties;

public class ExecutionTimeStats {

    private long min;
    private long max;
    private long avg;
    private long chunkTime;

    @ConstructorProperties({"min", "max", "avg", "chunkTime"})
    public ExecutionTimeStats(long min, long max, long avg, long chunkTime) {
        super();
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.chunkTime = chunkTime;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public long getAvg() {
        return avg;
    }

    public long getChunkTime() {
        return chunkTime;
    }

}
