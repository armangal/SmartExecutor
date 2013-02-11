package org.smexec.run;

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
        if (delay % 10 == 0) {
            throw new RuntimeException("Just fail");
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
