package org.smexec.run;

import java.util.Random;

import org.smexec.annotation.ThreadNameSuffix;
import org.smexec.annotation.ThreadPoolName;

@ThreadPoolName(poolName = "Custom1")
@ThreadNameSuffix(threadNameSuffix= "AST")
public class AnnotatedSleepingThread
    implements Runnable {

    long delay;

    public AnnotatedSleepingThread(long delay) {
        super();
        this.delay = delay;
    }

    @Override
    public void run() {
//        System.out.println("AnnotatedSleepingThread, Sleeping with:" + delay + "[" + Thread.currentThread().getName() + "]");
        if (new Random().nextInt(5) == 0) {
            throw new RuntimeException("Just fail");
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
