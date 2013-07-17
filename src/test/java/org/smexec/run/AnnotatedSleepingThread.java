/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec.run;

import java.util.Random;

import org.smexec.IPoolName;
import org.smexec.IThreadPoolAware;
import org.smexec.TestPoolNames;
import org.smexec.annotation.ThreadNameSuffix;

@ThreadNameSuffix(threadNameSuffix= "FAST")
public class AnnotatedSleepingThread
    implements Runnable,IThreadPoolAware {

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
    @Override
    public IPoolName getPoolName() {
        return TestPoolNames.CUSTOM1;
    }
}
