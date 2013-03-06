package org.smexec.jmx;

import java.beans.ConstructorProperties;

public class ExecutionTimeStats {

    private long min;
    private long max;
    private long avg;

    @ConstructorProperties({"min", "max", "avg"})
    public ExecutionTimeStats(long min, long max, long avg) {
        super();
        this.min = min;
        this.max = max;
        this.avg = avg;
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

}
