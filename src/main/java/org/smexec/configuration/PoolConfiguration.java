/**
 * Copyright (C) 2013 Arman Gal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
     * how often to print chunk stats to log, -1 means never any other positive number means that each X chunk
     * iteration the stats will be printed
     */
    private int logStats = 1; // default each chunk will be printed

    PoolConfiguration() {}

    PoolConfiguration(String poolName,
                      String poolNameShort,
                      Integer corePollSize,
                      Integer maxPoolSize,
                      Integer queueSize,
                      Long keepAliveTime,
                      PoolType poolType,
                      int chunks,
                      long chunkInterval,
                      int logStats) {
        super();
        this.poolName = poolName;
        this.poolNameShort = poolNameShort;
        this.corePollSize = corePollSize;
        this.maxPoolSize = maxPoolSize;
        this.queueSize = queueSize;
        this.keepAliveTime = keepAliveTime;
        this.poolType = poolType;
        this.chunks = chunks;
        this.chunkInterval = chunkInterval;
        this.logStats = logStats;
    }

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
