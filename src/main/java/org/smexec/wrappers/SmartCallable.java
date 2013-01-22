package org.smexec.wrappers;

import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.pool.PoolStats;

/**
 * A wrapper Callable for internal usage. <br>
 * I allows us to better control the thread execution, hooks and thread naming.
 */
public class SmartCallable<V>
    implements Callable<V> {

    private static Logger logger = LoggerFactory.getLogger(SmartCallable.class);

    private Callable<V> callable;
    private String threadNameSuffix;
    private PoolStats poolStats;

    public SmartCallable(Callable<V> callable, String threadNameSuffix, PoolStats poolStats) {
        this.callable = callable;
        this.threadNameSuffix = threadNameSuffix;
        this.poolStats = poolStats;
    }

    public SmartCallable(Callable<V> callable, PoolStats poolStats) {
        this.callable = callable;
        this.poolStats = poolStats;
    }

    @Override
    @XmlElement
    public V call()
        throws Exception {
        String orgName = null;
        try {
            poolStats.incrementExecuted();

            if (threadNameSuffix != null) {
                orgName = Thread.currentThread().getName();
                Thread.currentThread().setName(orgName + "_" + threadNameSuffix);
            }
            // TODO Pre execution HOOK

            long start = System.currentTimeMillis();

            V ret = callable.call();

            // TODO Post execution HOOK

            long end = System.currentTimeMillis();

            poolStats.updateTimings(end - start);
            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            poolStats.incrementFailed();
            throw e;

        } finally {
            if (orgName != null) {
                Thread.currentThread().setName(orgName);
            }
        }
    }
}
