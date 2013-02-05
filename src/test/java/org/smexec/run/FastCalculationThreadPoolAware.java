package org.smexec.run;

import java.util.Random;

import org.smexec.IThreadPoolAware;

public class FastCalculationThreadPoolAware
    implements Runnable, IThreadPoolAware {

    @Override
    public void run() {
        Random r = new Random();
        double cos = Math.cos(r.nextDouble());
        double cos1 = Math.cos(r.nextDouble());
        if (cos1 == cos) {
            System.err.println("WOW:" + cos);
        }
    }

    @Override
    public String getPoolName() {
        return "Fast";
    }
}
