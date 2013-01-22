package org.smexec.run;

public class SleepingThread
    implements Runnable {

    long delay;

    public SleepingThread(long delay) {
        super();
        this.delay = delay;
    }

    @Override
    public void run() {
        System.out.println("Sleeping with:" + delay + "[" + Thread.currentThread().getName() + "]");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
