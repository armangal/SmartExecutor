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

import java.util.Random;

import org.smexec.IPoolName;
import org.smexec.IThreadPoolAware;
import org.smexec.TestPoolNames;

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
