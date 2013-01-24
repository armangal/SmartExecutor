package org.smexec.pool.impl;

import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.PoolStats;

public abstract class AbstractSmartPool
    implements ISmartThreadPool {

    PoolStats poolStats = new PoolStats();
    
    PoolConfiguration poolConfiguration;

    
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
}
