package org.smexec;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.xml.bind.JAXBException;

import org.smexec.mbeans.StatsMBean;
import org.smexec.mbeans.Stats;
import org.smexec.run.Delayed;

public class SmartExecutorTest {

    public static void main(String[] args)
        throws IOException, JAXBException, MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, InterruptedException {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
        
        ObjectName name = new ObjectName("org.smexec.mbeans:type=Stats"); 
   
        Stats mbean = new Stats(); 
   
        mbs.registerMBean(mbean, name); 
        
        SmartExecutor se = new SmartExecutor("SmartExecutor-test.xml");

        Runnable command = new Runnable() {

            @Override
            public void run() {
                System.out.println("Run1:" + Thread.currentThread().getName());
                try {
                    Thread.currentThread().sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        se.execute(command, PoolNamesTest.DEFAULT_POOL);

        se.execute(command, PoolNamesTest.DEFAULT_POOL, "XXX");

        se.execute(command, "Custom1");

        se.execute(command, "Custom1", "CCC");

        se.scheduleAtFixedRate(command, 1000l, 1000l, TimeUnit.MILLISECONDS, PoolNamesTest.SCHEDULED_POOL, "EverySecond");

        se.scheduleAtFixedRate(command, 1000l, 1000l, TimeUnit.MILLISECONDS, "Scheduled2", "EverySecond");

        for (int i = 0; i < 1000; i++) {
            se.execute(command, "Cached1", "Cached");
        }
        System.out.println(se);
        
        do {
            Delayed d = new Delayed(new Random().nextInt(1000));
            se.execute(d, PoolNamesTest.DEFAULT_POOL, "DEL");
            Thread.sleep(1000l);
        } while(true);
    }
}
