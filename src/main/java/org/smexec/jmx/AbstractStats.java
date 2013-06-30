/**
 * Copyright (C) 2013 Arman Gal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smexec.jmx;

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

    /**
     * Registers the MBean or MXBean in MBean server.
     * 
     * @throws RuntimeException
     * @param beanName - the name with MBean is registered in MBeanServer
     */
    public AbstractStats(final String beanName) {
        logger = LoggerFactory.getLogger(getClass().getSimpleName());

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            mbs.registerMBean(this, new ObjectName(beanName));
            logger.info("Registered JMX bean:{}", beanName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(beanName + " can't be initialized", e);
        }
    }

}
