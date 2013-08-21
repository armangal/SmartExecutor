/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore;

import static java.lang.management.ManagementFactory.getMemoryMXBean;

import java.io.IOException;
import java.lang.management.MemoryMXBean;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.xml.bind.JAXBException;

import org.apache.log4j.xml.DOMConfigurator;

public class SmartExecutorSpeedCompareTest {

    private static final int EXECUTIONS = 10000000;

    static {
        URL resource = ClassLoader.getSystemResource("log4j-test.xml");
        DOMConfigurator.configure(resource);
    }

    static MemoryMXBean memoryMXBean = getMemoryMXBean();
    static Condition condition;
    static Lock lock = new ReentrantLock();

    public static void main(String[] args)
        throws IOException, JAXBException, MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException,
        NotCompliantMBeanException, InterruptedException {

        final SmartExecutor se = new SmartExecutor("SmartExecutor-test.xml");

        long start = System.nanoTime();
        for (int i = 0; i < EXECUTIONS; i++) {
            CalcSome cs = new CalcSome(i, start);
            se.execute(cs);
        }

        condition = lock.newCondition();
        lock.lock();
        condition.await();
        lock.unlock();

        System.out.println("done");

        se.shutdown();

        ExecutorService pool = Executors.newFixedThreadPool(4);
        start = System.nanoTime();
        for (int i = 0; i < EXECUTIONS; i++) {
            CalcSome cs = new CalcSome(i, start);
            pool.execute(cs);
        }

        lock.lock();
        condition.await();
        lock.unlock();

        Runtime.getRuntime().exit(0);

    }

    static class CalcSome
        implements Runnable, IThreadPoolAware, ITaskIdentification {

        int i;
        long start;

        protected CalcSome(int i, long start) {
            super();
            this.i = i;
            this.start = start;
        }

        public void run() {
            int gen = i;
            if (new Random().nextDouble() % i == 0) {
                gen += 4;
            }
            if (i == EXECUTIONS - 1) {
                long end = System.nanoTime() - start;

                System.out.println("SE:" + end);
                lock.lock();
                condition.signal();
                lock.unlock();
            }
            if (gen % 4 == 2) {
                return;
            }
        };

        @Override
        public IPoolName getPoolName() {
            return TestPoolNames.FAST;
        }

        @Override
        public String getTaskId() {
            return "speedTest";
        }
    }

}
