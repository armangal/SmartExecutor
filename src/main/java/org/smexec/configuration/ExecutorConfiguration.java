package org.smexec.configuration;

import java.util.List;

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

	public void setPools(List<PoolConfiguration> pools) {
		this.pools = pools;
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
