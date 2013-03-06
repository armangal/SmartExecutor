package org.smexec.configuration;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pool")
public class PoolConfiguration {

    /**
     * full pool name, will be used in monitoring software
     */
    private String poolName; 
    
    /**
     * will be used for logs, should be unique
     */
    private String poolNameShort;

    /**
     * core pool size
     */
    private Integer corePollSize = 5;
    
    /**
     * max pool size
     */
    private Integer maxPoolSize = 5;
    
    /**
     * queue size, default -1 means that SynchronousQueue will be used
     */
    private Integer queueSize = -1;
    
    /**
     * how long to keep threads in pool above the core size.
     */
    private Long keepAliveTime = 60000l;
    
    /**
     * the type of the pool
     */
    private PoolType poolType;
    
    /**
     * how many chunks to hold in memory
     */
    private int chunks = 100;
    
    /**
     * how often to "cut" a chunk
     */
    private long chunkInterval = 10000;
    
    /**
     * how often to print chunk stats to log, -1 means never
     * any other positive number means that each X chunk iteration the stats will be printed
     */
    private int logStats = 1; // default each chunk will be printed

    PoolConfiguration() {}

    public void validate()
        throws ValidationException {

        if (poolType == null) {
            throw new ValidationException("Pool Type not specified for:" + toString());
        }
        if (poolName == null || poolName.isEmpty()) {
            throw new ValidationException("Pool name not specified for:" + toString());
        }
        if (poolNameShort == null || poolNameShort.isEmpty()) {
            throw new ValidationException("Pool short anme not specified for:" + toString());
        }
    }

    public String getPoolName() {
        return poolName;
    }

    public String getPoolNameShort() {
        return poolNameShort;
    }

    public Integer getCorePollSize() {
        return corePollSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public PoolType getPoolType() {
        return poolType;
    }

    public int getChunks() {
        return chunks;
    }

    public long getChunkInterval() {
        return chunkInterval;
    }

    public int getLogStats() {
        return logStats;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PoolConfiguration [poolName=")
               .append(poolName)
               .append(", poolNameShort=")
               .append(poolNameShort)
               .append(", corePollSize=")
               .append(corePollSize)
               .append(", maxPoolSize=")
               .append(maxPoolSize)
               .append(", queueSize=")
               .append(queueSize)
               .append(", keepAliveTime=")
               .append(keepAliveTime)
               .append(", poolType=")
               .append(poolType)
               .append(", chunks=")
               .append(chunks)
               .append(", chunkInterval=")
               .append(chunkInterval)
               .append(", logStats=")
               .append(logStats)
               .append("]");
        return builder.toString();
    }

    

}
