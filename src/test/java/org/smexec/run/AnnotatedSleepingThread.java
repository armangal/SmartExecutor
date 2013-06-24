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
