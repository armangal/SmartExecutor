package org.smexec;

public enum SmartProperties {
    POOL_NAME("poolName", "Default"),
    POOL_SIZE("poolSize", "2"),
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
