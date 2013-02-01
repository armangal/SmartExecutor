package org.smexec.run;

import org.smexec.IThreadPoolAware;

public class SleepingThreadPoolAware
    implements Runnable, IThreadPoolAware {

    long delay;

    public SleepingThreadPoolAware(long delay) {
        super();
        this.delay = delay;
    }

    @Override
    public void run() {
        System.out.println("SleepingThreadPoolAware, Sleeping with:" + delay + "[" + Thread.currentThread().getName() + "]");
        if (delay % 10 == 0) {
            throw new RuntimeException("Just fail");
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPoolName() {
        return "Default";
    }
}
