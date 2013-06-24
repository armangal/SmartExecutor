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
