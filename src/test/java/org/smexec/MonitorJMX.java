package org.smexec;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.smexec.jmx.PoolStatsMBean;

public class MonitorJMX {

    /**
     * @param args
     * @throws IOException
     * @throws NullPointerException
     * @throws ReflectionException
     * @throws MalformedObjectNameException
     * @throws IntrospectionException
     * @throws InstanceNotFoundException
     * @throws MBeanException 
     * @throws AttributeNotFoundException 
     */
    public static void main(String[] args)
        throws IOException, InstanceNotFoundException, IntrospectionException, MalformedObjectNameException, ReflectionException, NullPointerException, MBeanException, AttributeNotFoundException {
        JMXServiceURL u = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9010/jmxrmi");
        JMXConnector c = JMXConnectorFactory.connect(u);

        System.out.println(c);

        MBeanServerConnection mbsc = c.getMBeanServerConnection();
        System.out.println(mbsc.getMBeanCount());

        System.out.println(Arrays.toString(mbsc.getDomains()));

        Set<ObjectInstance> names = new HashSet<ObjectInstance>(mbsc.queryMBeans(null, null));

        for (ObjectInstance on : names) {
            if ("SmartExecutor.Pools".equals(on.getObjectName().getKeyProperty("type"))) {
                System.out.println(on.getObjectName().getKeyProperty("name"));
                System.out.println(mbsc.getAttribute(on.getObjectName(), "ActiveCount"));
                PoolStatsMBean proxy = JMX.newMBeanProxy(mbsc, on.getObjectName(), PoolStatsMBean.class);

                System.out.println(proxy.getChunks());
            }
        }
        
        c.close();
    }
}
