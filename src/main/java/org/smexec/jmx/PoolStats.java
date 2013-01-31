package org.smexec.jmx;

import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.PoolStatsData;

public class PoolStats
    implements PoolStatsMBean {

    private final ISmartThreadPool stp;

    public PoolStats(final ISmartThreadPool stp) {
        this.stp = stp;
    }

    @Override
    public void printStats() {
        System.out.println(stp.getPoolStats().toString());
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
    public String getChunks() {
        StringBuilder builder = new StringBuilder();
        for (PoolStatsData h : stp.getPoolStats().getHistory()) {
            builder.append("[").append(h.getMaxTimeLong()).append(",").append(h.getAvgTime()).append(",").append(h.getMinTimeLong()).append("]");
        }

        return builder.toString();
    }
}
