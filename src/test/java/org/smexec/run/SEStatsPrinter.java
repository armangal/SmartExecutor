package org.smexec.run;

import org.smexec.PoolNamesTest;
import org.smexec.SmartExecutor;
import org.smexec.annotation.ThreadPoolName;

@ThreadPoolName(poolName = PoolNamesTest.SCHEDULED_POOL)
public class SEStatsPrinter
    implements Runnable {

    SmartExecutor se;

    public SEStatsPrinter(SmartExecutor se) {
        super();
        this.se = se;
    }

    @Override
    public void run() {
        System.out.println("print some stats");
    }
}
