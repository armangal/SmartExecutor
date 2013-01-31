package org.smexec.pool;

import java.util.concurrent.atomic.AtomicLong;

public class PoolStatsData {

    private final AtomicLong submitted = new AtomicLong(0);
    private final AtomicLong executed = new AtomicLong(0);
    private final AtomicLong completed = new AtomicLong(0);
    private final AtomicLong rejected = new AtomicLong(0);
    private final AtomicLong failed = new AtomicLong(0);
    private final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxTime = new AtomicLong(Long.MIN_VALUE);
    private final AtomicLong totalTime = new AtomicLong(0);

    public PoolStatsData() {}

    public AtomicLong getSubmitted() {
        return submitted;
    }

    public AtomicLong getExecuted() {
        return executed;
    }

    public AtomicLong getCompleted() {
        return completed;
    }

    public AtomicLong getRejected() {
        return rejected;
    }

    public AtomicLong getFailed() {
        return failed;
    }

    public AtomicLong getMinTime() {
        return minTime;
    }

    public AtomicLong getMaxTime() {
        return maxTime;
    }

    public Long getMaxTimeLong() {
        return maxTime.get() == Long.MIN_VALUE ? 0 : maxTime.get();
    }

    public Long getMinTimeLong() {
        return minTime.get() == Long.MAX_VALUE ? 0 : minTime.get();
    }

    public AtomicLong getTotalTime() {
        return totalTime;
    }

    public Long getAvgTime() {
        return (getExecuted().get() == 0 ? 0 : (getTotalTime().get() / getExecuted().get()));
    }

}
