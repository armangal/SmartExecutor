package org.smexec.pool;

import java.util.LinkedList;

public class ThreadPoolStats {

    private final PoolStatsData totalData = new PoolStatsData();
    private final int chunks;
    private LinkedList<PoolStatsData> history = new LinkedList<PoolStatsData>();

    public ThreadPoolStats(final int chunks) {
        this.chunks = chunks + 1;
        history.add(new PoolStatsData());
    }

    private PoolStatsData getCurrentChunk() {
        return history.getLast();
    }

    public void addChunk() {
        history.add(new PoolStatsData());
        if (history.size() > chunks) {
            history.remove();
        }
    }

    public long incrementSubmitted() {
        getCurrentChunk().getSubmitted().incrementAndGet();
        return totalData.getSubmitted().incrementAndGet();
    }

    public long incrementSubmitted(long delta) {
        getCurrentChunk().getSubmitted().addAndGet(delta);
        return totalData.getSubmitted().addAndGet(delta);
    }

    public long incrementRejected() {
        getCurrentChunk().getRejected().incrementAndGet();
        return totalData.getRejected().incrementAndGet();
    }

    public long incrementRejected(long delta) {
        getCurrentChunk().getRejected().addAndGet(delta);
        return totalData.getRejected().addAndGet(delta);
    }

    public long incrementExecuted() {
        getCurrentChunk().getExecuted().incrementAndGet();
        return totalData.getExecuted().incrementAndGet();
    }

    public long incrementFailed() {
        getCurrentChunk().getFailed().incrementAndGet();
        return totalData.getFailed().incrementAndGet();
    }

    public long incrementCompleted() {
        getCurrentChunk().getCompleted().incrementAndGet();
        return totalData.getCompleted().incrementAndGet();
    }

    public void updateTimings(long executionDuration) {
        if (executionDuration > totalData.getMaxTime().get()) {
            totalData.getMaxTime().set(executionDuration);
        }
        if (executionDuration < totalData.getMinTime().get()) {
            totalData.getMinTime().set(executionDuration);
        }

        if (executionDuration > getCurrentChunk().getMaxTime().get()) {
            getCurrentChunk().getMaxTime().set(executionDuration);
        }
        if (executionDuration < getCurrentChunk().getMinTime().get()) {
            getCurrentChunk().getMinTime().set(executionDuration);
        }

        getCurrentChunk().getTotalTime().addAndGet(executionDuration);
        totalData.getTotalTime().addAndGet(executionDuration);

        getCurrentChunk().getCompleted().incrementAndGet();
        totalData.getCompleted().incrementAndGet();
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
        for (PoolStatsData h : history) {
            builder.append("\n[").append(h.getMaxTime().get()).append(",").append(h.getAvgTime()).append(",").append(h.getMinTime().get()).append("]");
        }
        return builder.toString();
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

}
