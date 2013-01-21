package org.smexec.configuration;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(String.class)
public enum PoolType {
    regular, scheduled, cached;
}
