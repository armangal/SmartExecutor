/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all the MBean, MXBean implementations. Registration of MBean or MXBean to MBeanServer is
 * done in constructor. If any exception occurs during MBean registration, RuntimeException is thrown.
 * 
 * @author erdoan
 * @version $Revision: 1.1 $
 */
public abstract class AbstractStats {

    private Logger logger;
    private ObjectName objectName;

    /**
     * Registers the MBean or MXBean in MBean server.
     * 
     * @throws RuntimeException
     * @param beanName - the objectName with MBean is registered in MBeanServer
     */
    public AbstractStats(final String beanName) {
        logger = LoggerFactory.getLogger(getClass().getSimpleName());

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            objectName = new ObjectName(beanName);
            mbs.registerMBean(this, objectName);
            logger.info("Registered JMX bean:{}", beanName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(beanName + " can't be initialized", e);
        }
    }

    /**
     * in case the pool is closed all smart executor is shot down, we have to unregister JMX beans
     * 
     * @param beanName
     */
    public void unregisterBean() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            mbs.unregisterMBean(objectName);
            logger.info("UNregistered JMX bean:{}", objectName);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }

    }

}
