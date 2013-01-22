package org.smexec.pool;

import java.util.concurrent.atomic.AtomicLong;

public class PoolStats {

    private AtomicLong submitted = new AtomicLong(0);
    private AtomicLong executed = new AtomicLong(0);
    private AtomicLong rejected = new AtomicLong(0);
    private AtomicLong failed = new AtomicLong(0);
    private AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
    private AtomicLong maxTime = new AtomicLong(Long.MIN_VALUE);
    private AtomicLong totalTime = new AtomicLong(0);

    public long incrementSubmitted() {
        return submitted.incrementAndGet();
    }

    public long incrementRejected() {
        return rejected.incrementAndGet();
    }

    public long incrementExecuted() {
        return executed.incrementAndGet();
    }

    public long incrementFailed() {
        return failed.incrementAndGet();
    }

    public void updateTimings(long executionDuration) {
        if (executionDuration > maxTime.get()) {
            maxTime.set(executionDuration);
        }
        if (executionDuration < minTime.get()) {
            minTime.set(executionDuration);
        }
        totalTime.addAndGet(executionDuration);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nPoolStats [submitted=")
               .append(submitted)
               .append(", executed=")
               .append(executed)
               .append(", rejected=")
               .append(rejected)
               .append(", failed=")
               .append(failed)
               .append(", minTime=")
               .append(minTime)
               .append(", maxTime=")
               .append(maxTime)
               .append(", totalTime=")
               .append(totalTime)
               .append(", avgTime=")
               .append(executed.get() == 0 ? "none" : (totalTime.get() / executed.get()))
               .append("]");
        return builder.toString();
    }

}
