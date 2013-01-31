package org.smexec;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class MonitorJMX {

	/**
	 * @param args
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws ReflectionException
	 * @throws MalformedObjectNameException
	 * @throws IntrospectionException
	 * @throws InstanceNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			InstanceNotFoundException, IntrospectionException,
			MalformedObjectNameException, ReflectionException,
			NullPointerException {
		JMXServiceURL u = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://localhost:9010/jmxrmi");
		JMXConnector c = JMXConnectorFactory.connect(u);

		System.out.println(c.getConnectionId());

		MBeanServerConnection mbsc = c.getMBeanServerConnection();
		System.out.println(mbsc.getMBeanCount());

		System.out.println(Arrays.toString(mbsc.getDomains()));

		Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(null,
				null));

		for (ObjectName on : names) {
			System.out.println(on);
		}
	}
}
