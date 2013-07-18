/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.wrappers;

import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlElement;

import org.clevermore.ITaskIdentification;
import org.clevermore.TaskMetadata;
import org.clevermore.pool.ThreadPoolStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper Callable for internal usage. <br>
 * I allows us to better control the thread execution, hooks and thread naming.
 */
public class SmartCallable<V>
    implements Callable<V>, ITaskIdentification {

    private static final Logger logger = LoggerFactory.getLogger(SmartCallable.class);

    private Callable<V> callable;
    private ThreadPoolStats poolStats;
    private TaskMetadata taskMetadata;

    public SmartCallable(Callable<V> callable, TaskMetadata taskMetadata, ThreadPoolStats poolStats) {
        this.callable = callable;
        this.poolStats = poolStats;
        this.taskMetadata = taskMetadata;

    }

    @Override
    @XmlElement
    public V call()
        throws Exception {
        String orgName = null;
        long start = System.currentTimeMillis();

        try {
            poolStats.incrementExecuted(getTaskId());

            String threadNameSuffix = taskMetadata.getThreadNameSuffix();
            if (threadNameSuffix != null) {
                orgName = Thread.currentThread().getName();
                Thread.currentThread().setName(orgName + "_" + threadNameSuffix);
            }

            V ret = callable.call();

            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("Invocation Source:{}", taskMetadata.getStackTraceAsString());
            poolStats.incrementFailed(getTaskId());
            throw e;

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
