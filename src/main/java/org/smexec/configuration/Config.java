package org.smexec.configuration;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SmartExecutor", namespace = "")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

	@XmlElement(name = "executor")
	private ExecutorConfiguration executorConfiguration;

	public Config() {
	}

	public ExecutorConfiguration getExecutorConfiguration() {
		return executorConfiguration;
	}
	
	public void validate() throws ValidationException {
	    executorConfiguration.validate();
	}

}
