package org.smexec;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.xml.bind.JAXBException;

import org.smexec.run.AnnotatedSleepingThread;
import org.smexec.run.FastCalculationThreadPoolAware;
import org.smexec.run.SEStatsPrinter;
import org.smexec.run.SleepingThreadPoolAware;

public class SmartExecutorTest {

    public static void main(String[] args)
        throws IOException, JAXBException, MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException,
        NotCompliantMBeanException, InterruptedException {

        final SmartExecutor se = new SmartExecutor("SmartExecutor-test.xml");

        Runnable command = new SleepingThreadPoolAware(10011L);
        se.execute(command);

        se.execute(command, PoolNamesTest.DEFAULT_POOL, "XXX");

        se.scheduleAtFixedRate(new SEStatsPrinter(se), 5000l, 5000l, TimeUnit.MILLISECONDS, "Stats");

        se.scheduleAtFixedRate(command, 1000l, 1000l, TimeUnit.MILLISECONDS, "Scheduled2", "EverySecond");

        killing(se);

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

        Random r = new Random();
        double counter = 0;
        do {
            counter++;
            try {
                double sin = (1 + Math.cos(Math.PI + counter / Math.PI)) / 2;
                System.out.println(counter + "|" + sin);
                AnnotatedSleepingThread d = new AnnotatedSleepingThread((long) Math.abs((1000 * sin)));
                se.execute(d);

                AnnotatedSleepingThread d1 = new AnnotatedSleepingThread(r.nextInt(1000));
                ;
                se.execute(d1);
            } catch (Exception e) {
                // e.printStackTrace();
            }
            Thread.sleep(1000l);
        } while (true);

    }

    private static void killing(final SmartExecutor se) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Thread.currentThread().setName("FAST_SUBMITTER");
                do {
                    try {
                        se.execute("FAST", new FastCalculationThreadPoolAware());
                        Thread.sleep(1L);
                    } catch (Exception e) {

                    }
                } while (true);
            };
        }).start();

    }
}
