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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.smexec.TaskMetadata;

public interface ISmartThreadPool
    extends IGeneralThreadPool, ExecutorService {

    void execute(Runnable command, TaskMetadata taskMetadata);

    <T> Future<T> submit(Callable<T> task, TaskMetadata taskMetadata);

    <T> Future<T> submit(Runnable task, T result, TaskMetadata taskMetadata);

    Future<?> submit(Runnable task, TaskMetadata taskMetadata);

}
