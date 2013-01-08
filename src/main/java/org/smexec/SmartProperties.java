package org.smexec;

import org.smexec.configuration.PoolType;

public enum SmartProperties {
    POOL_TYPE("poolType", PoolType.regular.name()),
    POOL_NAME("poolName", "Default"),
    POOL_NAME_SHORT("poolNameShort", "DTP"),
    POOL_SIZE("corePoolSize", "2"),
    MAX_POOL_SIZE("maxPoolSize", "2"),
    QUEUE_SIZE("queueSize", "-1"),
    KEEP_ALIVE_TIME("keepAliveTime", "1000");
    
    String name;
    String defaultValue;

    private SmartProperties(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
