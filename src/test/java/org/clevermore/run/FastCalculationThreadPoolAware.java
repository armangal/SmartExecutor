/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.run;

import java.util.Random;

import org.clevermore.IPoolName;
import org.clevermore.IThreadPoolAware;
import org.clevermore.TestPoolNames;

public class FastCalculationThreadPoolAware
    implements Runnable, IThreadPoolAware {

    @Override
    public void run() {
        Random r = new Random();
        int i;
        for (i = 0; i < r.nextInt(1000) * 100000; i++) {
            double cos = Math.cos(r.nextDouble());
            double cos1 = Math.cos(r.nextDouble());
            double log = Math.log(r.nextDouble());
            if (cos1 == cos) {
                System.err.println("WOW:" + cos + log);
            }
        }
        // System.out.println(i);
    }

    @Override
    public IPoolName getPoolName() {
        return TestPoolNames.FAST;
    }
}
