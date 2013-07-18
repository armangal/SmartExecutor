/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.configuration;

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

    public Config() {}

    public ExecutorConfiguration getExecutorConfiguration() {
        return executorConfiguration;
    }

    public void validate()
        throws ValidationException {
        executorConfiguration.validate();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Config [executorConfiguration=").append(executorConfiguration).append("]");
        return builder.toString();
    }

}
