/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.run;

import org.clevermore.DefaultPoolNames;
import org.clevermore.IPoolName;
import org.clevermore.IThreadPoolAware;

public class SleepingThreadPoolAware
    implements Runnable, IThreadPoolAware {

    long delay;

    public SleepingThreadPoolAware(long delay) {
        super();
        this.delay = delay;
    }

    @Override
    public void run() {
//        System.out.println("SleepingThreadPoolAware, Sleeping with:" + delay + "[" + Thread.currentThread().getName() + "]");
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
    public IPoolName getPoolName() {
        return DefaultPoolNames.DEFAULT;
    }
}
