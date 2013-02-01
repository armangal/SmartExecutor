package org.smexec.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ExecutorConfiguration {

    @XmlElement(name = "pool")
    @XmlElementWrapper
    private List<PoolConfiguration> pools;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "description")
    private String description;

    public ExecutorConfiguration() {

    }

    public void validate()
        throws ValidationException {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Smart Executor name is empty");
        }
        if (description == null || description.isEmpty()) {
            throw new ValidationException("Smart Executor description is empty");
        }

        Map<String, Boolean> shortName = new HashMap<String, Boolean>(0);
        Map<String, Boolean> name = new HashMap<String, Boolean>(0);
        for (PoolConfiguration p : pools) {
            p.validate();
            Boolean put = name.put(p.getPoolName(), true);
            if (put != null) {
                throw new ValidationException("Pool name:" + p.getPoolName() + " not unique.");
            }
            Boolean put1 = shortName.put(p.getPoolNameShort(), true);
            if (put1 != null) {
                throw new ValidationException("Pool short name:" + p.getPoolName() + " not unique.");
            }
        }
    }

    public PoolConfiguration getPoolConfiguration(String poolName) {
        for (PoolConfiguration pc : pools) {
            if (pc.getPoolName().equals(poolName)) {
                return pc;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
