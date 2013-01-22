package org.smexec.pool.impl;

import org.smexec.pool.ISmartThreadPool;
import org.smexec.pool.PoolStats;

public abstract class AbstractSmartPool
    implements ISmartThreadPool {

    PoolStats poolStats = new PoolStats();
    
    
    public PoolStats getPoolStats() {
        return poolStats;
    }
    
    @Override
    public String toString() {
        return "Stats [" + poolStats + "]";
    }
}
