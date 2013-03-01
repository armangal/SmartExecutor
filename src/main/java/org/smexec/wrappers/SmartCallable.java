package org.smexec.wrappers;

import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.ITaskIdentification;
import org.smexec.pool.ThreadPoolStats;

/**
 * A wrapper Callable for internal usage. <br>
 * I allows us to better control the thread execution, hooks and thread naming.
 */
public class SmartCallable<V>
    implements Callable<V>, ITaskIdentification {

    private static final Logger logger = LoggerFactory.getLogger(SmartCallable.class);

    private Callable<V> callable;
    private String taskIdentification;
    private String threadNameSuffix;
    private ThreadPoolStats poolStats;

    public SmartCallable(Callable<V> callable, String taskIdentification, String threadNameSuffix, ThreadPoolStats poolStats) {
        this.callable = callable;
        this.threadNameSuffix = threadNameSuffix;
        this.poolStats = poolStats;
        this.taskIdentification = taskIdentification;

    }

    @Override
    @XmlElement
    public V call()
        throws Exception {
        String orgName = null;
        long start = System.currentTimeMillis();

        try {
            poolStats.incrementExecuted(taskIdentification);

            if (threadNameSuffix != null) {
                orgName = Thread.currentThread().getName();
                Thread.currentThread().setName(orgName + "_" + threadNameSuffix);
            }

            V ret = callable.call();

            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            poolStats.incrementFailed(taskIdentification);
            throw e;

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
