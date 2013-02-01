package org.smexec.pool.impl;

import java.util.Timer;
import java.util.TimerTask;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.PoolStats;

public abstract class AbstractSmartPool
    implements ISmartThreadPool {

    protected final PoolStats poolStats;

    protected PoolConfiguration poolConfiguration;

    public AbstractSmartPool(final PoolConfiguration poolConfiguration) {
        this.poolConfiguration = poolConfiguration;
        this.poolStats = new PoolStats(poolConfiguration.getChunks());
        
        Timer t = new Timer("Chunker_" + poolConfiguration.getPoolNameShort());
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                poolStats.addChunk();
            }
        }, poolConfiguration.getChunkInterval(), poolConfiguration.getChunkInterval());
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
