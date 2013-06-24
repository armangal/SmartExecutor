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
package org.smexec;

import static java.lang.management.ManagementFactory.getMemoryMXBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.MemoryMXBean;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.xml.bind.JAXBException;

import org.apache.log4j.xml.DOMConfigurator;
import org.smexec.run.AnnotatedSleepingThread;
import org.smexec.run.FastCalculationThreadPoolAware;
import org.smexec.run.SEStatsPrinter;
import org.smexec.run.SleepingThreadPoolAware;

public class SmartExecutorTest {

    static {
        URL resource = ClassLoader.getSystemResource("log4j-test.xml");
        DOMConfigurator.configure(resource);
    }

    static MemoryMXBean memoryMXBean = getMemoryMXBean();

    public static void main(String[] args)
        throws IOException, JAXBException, MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException,
        NotCompliantMBeanException, InterruptedException {

        final SmartExecutor se = new SmartExecutor("SmartExecutor-test.xml");

        Runnable command = new SleepingThreadPoolAware(10011L);
        se.execute(command);

        se.execute(command, TaskMetadata.newMetadata("XXX"));

        se.scheduleAtFixedRate(new SEStatsPrinter(se), 5000l, 5000l, TimeUnit.MILLISECONDS, TaskMetadata.newMetadata("Stats"));

        se.scheduleAtFixedRate(command, 1000l, 1000l, TimeUnit.MILLISECONDS, TaskMetadata.newMetadata(TestPoolNames.SCHEDULED_POOL, "EverySecond"));

        killing(se);
        control(se);

        // check rejection
        int rejected = 0;
        for (int i = 0; i < 200; i++) {
            try {
                se.execute(command, TaskMetadata.newMetadata(DefaultPoolNames.DEFAULT, "Reject"));
            } catch (RejectedExecutionException e) {
                rejected++;
            }
        }
        System.out.println("Rejected:" + rejected);

        for (int i = 0; i < 10; i++) {
            se.execute(command, TaskMetadata.newMetadata(TestPoolNames.CACHED1, "Cached"));
        }

        Random r = new Random();
        double counter = 0;
        do {
            counter++;
            try {
                double sin = (1 + Math.cos(Math.PI + counter / Math.PI)) / 2;
                // System.out.println(counter + "|" + sin);
                AnnotatedSleepingThread d = new AnnotatedSleepingThread((long) Math.abs((1000 * sin)));
                se.execute(d);

                AnnotatedSleepingThread d1 = new AnnotatedSleepingThread(r.nextInt(1000));

                se.execute(d1);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            Thread.sleep(100l);
        } while (true);

    }

    static List<String> list = new ArrayList<String>();
    static boolean work = true;

    private static void killing(final SmartExecutor se) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Thread.currentThread().setName("FAST_SUBMITTER");
                do {
                    try {
                        se.execute(new FastCalculationThreadPoolAware(), TaskMetadata.newMetadata("FAST"));
                        for (int i = 0; work && i < 5; i++)
                            list.add("" + i + Arrays.toString(Thread.currentThread().getStackTrace()));

                        double usage = memoryMXBean.getHeapMemoryUsage().getUsed() * 100d / memoryMXBean.getHeapMemoryUsage().getMax();
                        if (usage > 95d) {
                            list.clear();
                            // System.out.println("Clearing list, memory usage:" + usage);
                        }
                        Thread.sleep(1L);
                    } catch (Exception e) {

                    }
                } while (true);
            };
        }).start();

    }

    private static void control(final SmartExecutor se) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Thread.currentThread().setName("CONTROL");
                do {
                    try {
                        System.out.print("Enter command: ");

                        // open up standard input
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                        String cmd = null;

                        try {
                            cmd = br.readLine();
                            if ("1".equals(cmd)) {
                                list.clear();
                            }
                            if ("2".equals(cmd)) {
                                work = false;
                            }
                            if ("3".equals(cmd)) {
                                work = true;
                            }
                        } catch (IOException ioe) {
                            System.out.println("IO error trying to read your name!");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (true);
            };
        }).start();

    }
}
