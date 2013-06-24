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
package org.smexec.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.smexec.TaskMetadata;

public interface ISmartScheduledThreadPool
    extends ScheduledExecutorService, IGeneralThreadPool {

    ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, TaskMetadata taskMetadata);

    <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, TaskMetadata taskMetadata);

    ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, TaskMetadata taskMetadata);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, TaskMetadata taskMetadata);
}
