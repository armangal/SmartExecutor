package org.smexec.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.pool.PoolStats;

/**
 * A wrapper Runnable for internal usage. <br>
 * I allows us to better control the thread execution, hooks and thread naming.
 */
public class SmartRunnable
    implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(SmartRunnable.class);

    private Runnable runnable;
    private String threadNameSuffix;
    private PoolStats poolStats;

    public SmartRunnable(Runnable runnable, String threadNameSuffix, PoolStats poolStats) {
        this.runnable = runnable;
        this.threadNameSuffix = threadNameSuffix;
        this.poolStats = poolStats;
    }

    public SmartRunnable(Runnable runnable, PoolStats poolStats) {
        this.runnable = runnable;
        this.poolStats = poolStats;
    }

    @Override
    public void run() {
        String orgName = null;
        try {
            poolStats.incrementExecuted();

            if (threadNameSuffix != null) {
                orgName = Thread.currentThread().getName();
                Thread.currentThread().setName(orgName + "_" + threadNameSuffix);
            }
            // TODO Pre execution HOOK

            long start = System.currentTimeMillis();
            
            runnable.run();

            long end = System.currentTimeMillis();

            poolStats.updateTimings(end - start);
            // TODO Post execution HOOK

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // nothing we can do here, just aggregate stats
            poolStats.incrementFailed();
            
        } finally {
            if (orgName != null) {
                Thread.currentThread().setName(orgName);
            }
        }
    }

}
