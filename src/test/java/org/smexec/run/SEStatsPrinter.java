/**
 * Copyright (C) 2013 Arman Gal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
