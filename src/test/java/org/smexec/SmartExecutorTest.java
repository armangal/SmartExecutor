package org.smexec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SmartExecutorTest {

    public static void main(String[] args)
        throws IOException {
        InputStream propStrem = Thread.currentThread().getContextClassLoader().getResourceAsStream("smartExecutor.properties");

        Properties properties = new Properties();
        properties.load(propStrem);
        System.out.println(properties);

        SmartExecutor se = new SmartExecutor(properties);
        
        Runnable command = new Runnable() {
            
            @Override
            public void run() {
                System.out.println("Run1:" + Thread.currentThread().getName());
            }
        };
        
        se.execute(command, PoolNamesTest.POOL_TST1);
    }
}
