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
package org.smexec.pool.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smexec.TaskMetadata;
import org.smexec.configuration.PoolConfiguration;
import org.smexec.pool.ThreadPoolStats;
import org.smexec.utils.Utils;
import org.smexec.wrappers.SmartCallable;
import org.smexec.wrappers.SmartRunnable;

final class ThreadPoolHelper {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolHelper.class);

    /**
     * local pool to be used for chunkers
     */
    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(2, new ThreadFactory() {

        int trn = 0;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "SE_CHUNKER_" + trn++);
        }
    });

    protected static <T> Collection<SmartCallable<T>> wrapCallable(Collection<? extends Callable<T>> tasks, TaskMetadata taskMetadata, ThreadPoolStats poolStats) {
        Collection<SmartCallable<T>> smartTasks = new HashSet<SmartCallable<T>>(tasks.size());
        for (Callable<T> c : tasks) {
            smartTasks.add(wrapCallable(c, taskMetadata, poolStats));
        }
        return smartTasks;
    }

    protected static <T> Collection<SmartCallable<T>> wrapCallable(Collection<? extends Callable<T>> tasks, ThreadPoolStats poolStats) {
        Collection<SmartCallable<T>> smartTasks = new HashSet<SmartCallable<T>>(tasks.size());
        for (Callable<T> c : tasks) {
            TaskMetadata taskMetadata = TaskMetadata.newDefaultMetadata();
            taskMetadata.setStack(new Throwable().getStackTrace());
            Utils.fillMetaData(taskMetadata, c);

            smartTasks.add(wrapCallable(c, taskMetadata, poolStats));
        }
        return smartTasks;
    }

    protected static SmartRunnable wrapRunnble(Runnable r, TaskMetadata taskMetadata, ThreadPoolStats poolStats) {
        return new SmartRunnable(r, taskMetadata, poolStats);
    }

    protected static <V> SmartCallable<V> wrapCallable(Callable<V> c, TaskMetadata taskMetadata, ThreadPoolStats poolStats) {
        return new SmartCallable<V>(c, taskMetadata, poolStats);
    }

    protected static ThreadFactory getThreadFactory(final PoolConfiguration poolConf) {
        return new ThreadFactory() {

            protected final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, poolConf.getPoolType().getThreadNamePrefix() + poolConf.getPoolNameShort() + "_" + threadNumber.incrementAndGet());
            }
        };

    }

}
