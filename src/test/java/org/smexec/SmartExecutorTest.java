package org.smexec;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.xml.bind.JAXBException;

import org.smexec.mbeans.Stats;
import org.smexec.run.AnnotatedSleepingThread;
import org.smexec.run.SEStatsPrinter;
import org.smexec.run.SleepingThreadPoolAware;

public class SmartExecutorTest {

    public static void main(String[] args)
        throws IOException, JAXBException, MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException,
        NotCompliantMBeanException, InterruptedException {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName("org.smexec.mbeans:type=Stats");

        Stats mbean = new Stats();

        mbs.registerMBean(mbean, name);

        final SmartExecutor se = new SmartExecutor("SmartExecutor-test.xml");

        Runnable command = new SleepingThreadPoolAware(10000L);
        se.execute(command);

        se.execute(command, PoolNamesTest.DEFAULT_POOL, "XXX");

        se.execute(command, "Custom1");

        se.execute(command, "Custom1", "CCC");

        se.scheduleAtFixedRate(new SEStatsPrinter(se), 5000l, 5000l, TimeUnit.MILLISECONDS, "Stats");

        se.scheduleAtFixedRate(command, 1000l, 1000l, TimeUnit.MILLISECONDS, "Scheduled2", "EverySecond");

        // check rejection
        int rejected = 0;
        for (int i = 0; i < 200; i++) {
            try {
                se.execute(command, PoolNamesTest.DEFAULT_POOL, "Reject");
            } catch (RejectedExecutionException e) {
                rejected++;
            }
        }
        System.out.println("Rejected:" + rejected);

        for (int i = 0; i < 10; i++) {
            se.execute(command, "Cached1", "Cached");
        }
        System.out.println(se);

        do {
            Random random = new Random();
            for (int k = 0; k < random.nextInt(10) + 5; k++) {
                try {
                    AnnotatedSleepingThread d = new AnnotatedSleepingThread(random.nextInt(1000));
                    se.execute("SLP", d);
                } catch (Exception e) {
                    //
                }
            }
            Thread.sleep(1000l);
        } while (true);

    }
}
