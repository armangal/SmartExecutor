package org.smexec.configuration;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(String.class)
public enum PoolType {
    regular("SERE_"), scheduled("SESC_"), cached("SECA");

    private String threadNamePrefix;

    private PoolType(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }
}
