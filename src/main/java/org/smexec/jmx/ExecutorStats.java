package org.smexec.jmx;

import org.smexec.SmartExecutor;
import org.smexec.configuration.Config;

public class ExecutorStats
    implements ExecutorStatsMBean {

    private final SmartExecutor smartExecutor;
    private final Config config;

    public ExecutorStats(SmartExecutor smartExecutor, Config config) {
        super();
        this.smartExecutor = smartExecutor;
        this.config = config;
    }

    @Override
    public String getName() {
        return config.getExecutorConfiguration().getName();
    }

    @Override
    public String getDescription() {
        return config.getExecutorConfiguration().getDescription();
    }

    @Override
    public int getActivePools() {
        return smartExecutor.getActivePools();
    }

}
