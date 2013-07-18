/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore.run;

import org.clevermore.IPoolName;
import org.clevermore.IThreadPoolAware;
import org.clevermore.SmartExecutor;
import org.clevermore.TestPoolNames;

public class SEStatsPrinter
    implements Runnable, IThreadPoolAware {

    SmartExecutor se;

    public SEStatsPrinter(SmartExecutor se) {
        super();
        this.se = se;
    }

    @Override
    public void run() {
        // System.out.println("print some stats");
    }

    @Override
    public IPoolName getPoolName() {
        return TestPoolNames.SCHEDULED_POOL;
    }
}
