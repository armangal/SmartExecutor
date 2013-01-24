package org.smexec.mbeans;

import org.smexec.pool.ISmartThreadPool;

public class Stats
    implements StatsMBean {

    private ISmartThreadPool stp;

    public Stats(ISmartThreadPool stp) {
        this.stp = stp;
    }

    @Override
    public String getStats() {
        return stp.getPoolStats().toString();
    }

    @Override
    public void printStats() {
        System.out.println(stp.getPoolStats().toString());
    }
}
