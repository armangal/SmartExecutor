package org.smexec.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.ITaskIdentification;
import org.smexec.pool.ThreadPoolStats;

/**
 * A wrapper Runnable for internal usage. <br>
 * I allows us to better control the thread execution, hooks and thread naming.
 */
public class SmartRunnable
    implements Runnable, ITaskIdentification {

    private static final Logger logger = LoggerFactory.getLogger(SmartRunnable.class);

    private Runnable runnable;
    private String taskIdentification;
    private String threadNameSuffix;
    private ThreadPoolStats poolStats;

    public SmartRunnable(Runnable runnable, String taskIdentification, String threadNameSuffix, ThreadPoolStats poolStats) {
        this.runnable = runnable;
        this.threadNameSuffix = threadNameSuffix;
        this.poolStats = poolStats;
        this.taskIdentification = taskIdentification;
    }

    @Override
    public void run() {
        String orgName = null;
        long start = System.currentTimeMillis();

        try {
            poolStats.incrementExecuted(taskIdentification);

            if (threadNameSuffix != null) {
                orgName = Thread.currentThread().getName();
                Thread.currentThread().setName(orgName + "_" + threadNameSuffix);
            }

            runnable.run();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // nothing we can do here, just aggregate stats
            poolStats.incrementFailed(taskIdentification);

        } finally {
            long end = System.currentTimeMillis();

            poolStats.updateTimings(end - start, taskIdentification);
            if (orgName != null) {
                Thread.currentThread().setName(orgName);
            }
        }
    }

    @Override
    public String getTaskId() {
        return taskIdentification;
    }

}
