package org.smexec;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

public class SmartExecutorTest {

    public static void main(String[] args)
        throws IOException, JAXBException {

        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SmartExecutor-test.xml");
        
        SmartExecutor se = new SmartExecutor("SmartExecutor-test.xml");

        Runnable command = new Runnable() {

            @Override
            public void run() {
                System.out.println("Run1:" + Thread.currentThread().getName());
            }
        };

        se.execute(command, PoolNamesTest.DEFAULT_POOL);

        se.execute(command, PoolNamesTest.DEFAULT_POOL, "XXX");

        se.execute(command, "Custom1");

        se.execute(command, "Custom1", "CCC");

        se.scheduleAtFixedRate(command, 1000l, 1000l, TimeUnit.MILLISECONDS, PoolNamesTest.SCHEDULED_POOL, "EverySecond");

        se.scheduleAtFixedRate(command, 1000l, 1000l, TimeUnit.MILLISECONDS, "Scheduled2", "EverySecond");
    }
}
