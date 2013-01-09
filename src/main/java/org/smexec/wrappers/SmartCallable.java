package org.smexec.wrappers;

import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper Callable for internal usage. <br>
 * I allows us to better control the thread execution, hooks and thread naming.
 */
public class SmartCallable<V>
    implements Callable<V> {

    private static Logger logger = LoggerFactory.getLogger(SmartCallable.class);

    private Callable<V> callable;
    private String threadNameSuffix;

    public SmartCallable(Callable<V> callable, String threadNameSuffix) {
        this.callable = callable;
        this.threadNameSuffix = threadNameSuffix;
    }

    public SmartCallable(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    @XmlElement
    public V call()
        throws Exception {
        String orgName = null;
        try {
            if (threadNameSuffix != null) {
                orgName = Thread.currentThread().getName();
                Thread.currentThread().setName(orgName + "_" + threadNameSuffix);
            }
            // TODO Pre execution HOOK

            V ret = callable.call();

            // TODO Post execution HOOK

            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
            
        } finally {
            if (orgName != null) {
                Thread.currentThread().setName(orgName);
            }
        }
    }
}
