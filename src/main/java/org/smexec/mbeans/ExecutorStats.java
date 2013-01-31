package org.smexec.mbeans;

import org.smexec.configuration.ExecutorConfiguration;

public class ExecutorStats
    implements ExecutorStatsMBean {

    private ExecutorConfiguration executorConfiguration;

    public ExecutorStats(ExecutorConfiguration executorConfiguration) {
        super();
        this.executorConfiguration = executorConfiguration;
    }

    @Override
    public String getName() {
        return executorConfiguration.getName();
    }

    @Override
    public String getDescription() {
        return executorConfiguration.getDescription();
    }

}
