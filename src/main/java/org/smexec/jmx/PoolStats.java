package org.smexec.jmx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.smexec.pool.ISmartThreadPool;

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
    public List<Integer> getStats() {
        int x = 3;
        List<Integer> is = new ArrayList<Integer>();
        for (int i = 0; i < x; i++)
            is.add(new Random().nextInt(100));

        return is;
    }
}
