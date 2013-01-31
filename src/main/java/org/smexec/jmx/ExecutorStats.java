package org.smexec.jmx;

import org.smexec.SmartExecutor;

public class ExecutorStats
    implements ExecutorStatsMBean {

    private SmartExecutor smartExecutor;

    public ExecutorStats(SmartExecutor smartExecutor) {
        super();
        this.smartExecutor = smartExecutor;
    }

    @Override
    public String getName() {
        return smartExecutor.getConfig().getExecutorConfiguration().getName();
    }

    @Override
    public String getDescription() {
        return smartExecutor.getConfig().getExecutorConfiguration().getDescription();
    }

    @Override
    public int getActivePools() {
        return smartExecutor.getActivePools();
    }

}
