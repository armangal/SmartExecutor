package org.smexec.run;

public class Delayed
    implements Runnable {

    long delay;

    public Delayed(long delay) {
        super();
        this.delay = delay;
    }

    @Override
    public void run() {
        System.out.println("Delayed with:" + delay + "[" + Thread.currentThread().getName() + "]");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
