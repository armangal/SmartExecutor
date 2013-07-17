/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.ITaskIdentification;
import org.smexec.TaskMetadata;
import org.smexec.pool.ThreadPoolStats;

/**
 * A wrapper Runnable for internal usage. <br>
 * I allows us to better control the thread execution, hooks and thread naming.
 */
public class SmartRunnable
    implements Runnable, ITaskIdentification {

    private static final Logger logger = LoggerFactory.getLogger(SmartRunnable.class);

    private Runnable runnable;
    private TaskMetadata taskMetadata;
    private ThreadPoolStats poolStats;

    public SmartRunnable(Runnable runnable, TaskMetadata taskMetadata, ThreadPoolStats poolStats) {
        this.runnable = runnable;
        this.poolStats = poolStats;
        this.taskMetadata = taskMetadata;
    }

    @Override
    public void run() {
        String orgName = null;
        long start = System.currentTimeMillis();

        try {
            poolStats.incrementExecuted(getTaskId());

            String threadNameSuffix = taskMetadata.getThreadNameSuffix();
            if (threadNameSuffix != null) {
                orgName = Thread.currentThread().getName();
                Thread.currentThread().setName(orgName + "_" + threadNameSuffix);
            }

            runnable.run();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("Invocation Source:{}", taskMetadata.getStackTraceAsString());

            // nothing we can do here, just aggregate stats
            poolStats.incrementFailed(getTaskId());

        } finally {
            long end = System.currentTimeMillis();

            poolStats.updateTimings(end - start, getTaskId());
            if (orgName != null) {
                Thread.currentThread().setName(orgName);
            }
        }
    }

    @Override
    public String getTaskId() {
        return taskMetadata.getTaskId();
    }

}
