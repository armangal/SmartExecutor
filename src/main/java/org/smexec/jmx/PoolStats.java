package org.smexec.jmx;

import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.pool.IGeneralThreadPool;
import org.smexec.pool.PoolStatsData;

public class PoolStats
    implements PoolStatsMBean {

    private static Logger logger = LoggerFactory.getLogger(PoolStats.class);

    private final IGeneralThreadPool stp;

    public PoolStats(final IGeneralThreadPool stp) {
        this.stp = stp;
    }

    @Override
    public void printStats() {
        logger.debug(stp.getPoolStats().toString());
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
    public String getTimeChunks() {
        StringBuilder builder = new StringBuilder();
        LinkedList<PoolStatsData> history = stp.getPoolStats().getHistory();
        for (int i = 0; i < (history.size() - 1); i++) {
            PoolStatsData h = history.get(i);
            builder.append("[").append(h.getMaxTimeLong()).append(",").append(h.getAvgTime()).append(",").append(h.getMinTimeLong()).append("]");
        }

        return builder.toString();
    }

    @Override
    public String getTasksChunks() {
        StringBuilder builder = new StringBuilder();
        LinkedList<PoolStatsData> history = stp.getPoolStats().getHistory();
        for (int i = 0; i < (history.size() - 1); i++) {
            PoolStatsData h = history.get(i);
            builder.append("[")
                   .append(h.getSubmitted())
                   .append(",")
                   .append(h.getExecuted())
                   .append(",")
                   .append(h.getFailed())
                   .append(",")
                   .append(h.getRejected())
                   .append(",")
                   .append(h.getCompleted())
                   .append("]");
        }

        return builder.toString();
    }

    @Override
    public int getPoolSize() {
        return ((ThreadPoolExecutor) stp).getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return ((ThreadPoolExecutor) stp).getActiveCount();
    }

    @Override
    public int getLargestPoolSize() {
        return ((ThreadPoolExecutor) stp).getLargestPoolSize();
    }
    
    @Override
    public String[] getTaskNames() {
        return stp.getPoolStats().getTaskNames();
    }

}
