package org.smexec.run;

import org.smexec.SmartExecutor;
import org.smexec.TestPoolNamesConstants;
import org.smexec.annotation.ThreadPoolName;

@ThreadPoolName(poolName = TestPoolNamesConstants.SCHEDULED)
public class SEStatsPrinter
    implements Runnable {

    SmartExecutor se;

    public SEStatsPrinter(SmartExecutor se) {
        super();
        this.se = se;
    }

    @Override
    public void run() {
//        System.out.println("print some stats");
    }
}
