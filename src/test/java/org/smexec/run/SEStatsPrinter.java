/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.smexec.run;

import org.smexec.IPoolName;
import org.smexec.IThreadPoolAware;
import org.smexec.SmartExecutor;
import org.smexec.TestPoolNames;

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
