package org.smexec.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pool")
public class PoolConfiguration {

    private String poolName;
    private String poolNameShort;
    private Integer corePollSize;
    private Integer maxPoolSize;
    private Integer queueSize;
    private Long keepAliveTime;
    private PoolType poolType;
    private int chunks;
    private long chunkInterval;

    PoolConfiguration() {}

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolNameShort() {
        return poolNameShort;
    }

    public void setPoolNameShort(String poolNameShort) {
        this.poolNameShort = poolNameShort;
    }

    public Integer getCorePollSize() {
        return corePollSize;
    }

    public void setCorePollSize(Integer corePollSize) {
        this.corePollSize = corePollSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(PoolType poolType) {
        this.poolType = poolType;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public long getChunkInterval() {
        return chunkInterval;
    }

    public void setChunkInterval(long chunkInterval) {
        this.chunkInterval = chunkInterval;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PoolConfiguration [poolName=");
        builder.append(poolName);
        builder.append(", poolNameShort=");
        builder.append(poolNameShort);
        builder.append(", corePollSize=");
        builder.append(corePollSize);
        builder.append(", maxPoolSize=");
        builder.append(maxPoolSize);
        builder.append(", queueSize=");
        builder.append(queueSize);
        builder.append(", keepAliveTime=");
        builder.append(keepAliveTime);
        builder.append(", poolType=");
        builder.append(poolType);
        builder.append(", chunks=");
        builder.append(chunks);
        builder.append(", chunkInterval=");
        builder.append(chunkInterval);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (chunkInterval ^ (chunkInterval >>> 32));
        result = prime * result + chunks;
        result = prime * result + ((corePollSize == null) ? 0 : corePollSize.hashCode());
        result = prime * result + ((keepAliveTime == null) ? 0 : keepAliveTime.hashCode());
        result = prime * result + ((maxPoolSize == null) ? 0 : maxPoolSize.hashCode());
        result = prime * result + ((poolName == null) ? 0 : poolName.hashCode());
        result = prime * result + ((poolNameShort == null) ? 0 : poolNameShort.hashCode());
        result = prime * result + ((poolType == null) ? 0 : poolType.hashCode());
        result = prime * result + ((queueSize == null) ? 0 : queueSize.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PoolConfiguration other = (PoolConfiguration) obj;
        if (chunkInterval != other.chunkInterval)
            return false;
        if (chunks != other.chunks)
            return false;
        if (corePollSize == null) {
            if (other.corePollSize != null)
                return false;
        } else if (!corePollSize.equals(other.corePollSize))
            return false;
        if (keepAliveTime == null) {
            if (other.keepAliveTime != null)
                return false;
        } else if (!keepAliveTime.equals(other.keepAliveTime))
            return false;
        if (maxPoolSize == null) {
            if (other.maxPoolSize != null)
                return false;
        } else if (!maxPoolSize.equals(other.maxPoolSize))
            return false;
        if (poolName == null) {
            if (other.poolName != null)
                return false;
        } else if (!poolName.equals(other.poolName))
            return false;
        if (poolNameShort == null) {
            if (other.poolNameShort != null)
                return false;
        } else if (!poolNameShort.equals(other.poolNameShort))
            return false;
        if (poolType != other.poolType)
            return false;
        if (queueSize == null) {
            if (other.queueSize != null)
                return false;
        } else if (!queueSize.equals(other.queueSize))
            return false;
        return true;
    }

}
