package org.smexec.pool.impl;

import java.util.Timer;
import java.util.TimerTask;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.PoolStats;

public abstract class AbstractSmartPool
    implements ISmartThreadPool {

    protected final PoolStats poolStats = new PoolStats(100);

    protected PoolConfiguration poolConfiguration;

    public AbstractSmartPool() {
        Timer t = new Timer("chunker");
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                poolStats.addChunk();
            }
        }, 10000, 10000);
    }

    public PoolStats getPoolStats() {
        return poolStats;
    }

    @Override
    public String toString() {
        return "Stats [" + poolStats + "]";
    }

    @Override
    public String getPoolName() {
        return poolConfiguration.getPoolName();
    }

    public PoolConfiguration getPoolConfiguration() {
        return poolConfiguration;
    }
}
