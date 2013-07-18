/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.jmx;

import java.util.concurrent.ThreadPoolExecutor;

import org.clevermore.pool.IGeneralThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoolStats
    extends AbstractStats
    implements PoolStatsMXBean {

    private static final String BEAN_NAME = "org.clevermore:type=SmartExecutor.Pools,name=";

    private Logger logger;

    private final IGeneralThreadPool stp;

    final String smartExecutorName;

    public PoolStats(final String smartExecutorName, final IGeneralThreadPool stp) {
        super(BEAN_NAME + smartExecutorName + "." + stp.getPoolConfiguration().getPoolName() + "(" + stp.getPoolConfiguration().getPoolNameShort() + ")");

        this.smartExecutorName = smartExecutorName;
        logger = LoggerFactory.getLogger("PoolStats_" + stp.getPoolConfiguration().getPoolNameShort());
        this.stp = stp;
    }

    @Override
    public String getSmartExecutorName() {
        return smartExecutorName;
    }

    @Override
    public String getName() {
        return stp.getPoolConfiguration().getPoolName() + "(" + stp.getPoolConfiguration().getPoolNameShort() + ")";
    }

    @Override
    public String getDescription() {
        return stp.getPoolConfiguration().getDescription();
    }

    @Override
    public void printStats() {
        logger.info(stp.getPoolStats().toString());
    }

    @Override
    public Long getSubmitted() {
        return stp.getPoolStats().getSubmitted();
    }

    @Override
    public Long getExecuted() {

        return stp.getPoolStats().getExecuted();
    }

    @Override
    public Long getCompleted() {

        return stp.getPoolStats().getCompleted();
    }

    @Override
    public Long getRejected() {

        return stp.getPoolStats().getRejected();
    }

    @Override
    public Long getFailed() {

        return stp.getPoolStats().getFailed();
    }

    @Override
    public Long getMinTime() {
        return stp.getPoolStats().getMinTime().longValue() == Long.MAX_VALUE ? Long.valueOf(0L) : stp.getPoolStats().getMinTime();
    }

    @Override
    public Long getMaxTime() {
        return stp.getPoolStats().getMaxTime().longValue() == Long.MIN_VALUE ? Long.valueOf(0L) : stp.getPoolStats().getMaxTime();
    }

    @Override
    public Long getTotalTime() {

        return stp.getPoolStats().getTotalTime();
    }

    @Override
    public Long getAvgTime() {

        return stp.getPoolStats().getAvgTime();
    }

    @Override
    public int getPoolSize() {
        return ((ThreadPoolExecutor) stp).getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return ((ThreadPoolExecutor) stp).getActiveCount();
    }

    @Override
    public int getLargestPoolSize() {
        return ((ThreadPoolExecutor) stp).getLargestPoolSize();
    }

    @Override
    public String[] getTaskNames() {
        return stp.getPoolStats().getTaskNames();
    }

    @Override
    public TaskExecutionStats[] getTaskExecutionStats() {

        return stp.getPoolStats().getAllStats();
    }

    @Override
    public TaskExecutionStats[] getLastTaskExecutionStats(long lastUpdate) {
        return stp.getPoolStats().getLastStats(lastUpdate);
    }
}
