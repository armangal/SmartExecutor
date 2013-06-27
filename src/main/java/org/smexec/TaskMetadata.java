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
package org.smexec;

import java.util.Arrays;

/**
 * A metadata class that is "following" the task during its' execution and holding extra information about the
 * way it should be executed.
 * 
 * @author armang
 */
public class TaskMetadata {

    private IPoolName poolName;

    private String threadNameSuffix;

    private String taskId;

    private StackTraceElement[] stack;

    private TaskMetadata(IPoolName poolName, String threadNameSuffix, String taskId) {
        super();
        this.poolName = poolName;
        this.threadNameSuffix = threadNameSuffix;
        this.taskId = taskId;
    }

    private TaskMetadata(IPoolName poolName, String threadNameSuffix) {
        super();
        this.poolName = poolName;
        this.threadNameSuffix = threadNameSuffix;
    }

    private TaskMetadata() {
        super();
    }

    private TaskMetadata(IPoolName poolName) {
        super();
        this.poolName = poolName;
    }

    public String getThreadNameSuffix() {
        return threadNameSuffix;
    }

    public boolean hasSuffix() {
        return threadNameSuffix != null;
    }

    public void setThreadNameSuffix(String threadNameSuffix) {
        this.threadNameSuffix = threadNameSuffix;
    }

    public IPoolName getPoolName() {
        return poolName;
    }

    public void setPoolName(IPoolName poolName) {
        this.poolName = poolName;
    }

    public boolean hasPoolName() {
        return poolName != null;
    }

    public String getPoolNameAsString() {
        return poolName.getPoolName();
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean hasTaskId() {
        return taskId != null;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setStack(StackTraceElement[] stack) {
        this.stack = Arrays.copyOf(stack, stack.length);
    }

    public StackTraceElement[] getStack() {
        return Arrays.copyOf(stack, stack.length);
    }

    public String getStackTraceAsString() {
        StringBuilder s = new StringBuilder();
        boolean firstLine = true;
        for (StackTraceElement e : stack) {
            if (e.toString().startsWith("java.lang.Thread.getStackTrace")) {
                // we'll ignore this one
                continue;
            }
            if (!firstLine) {
                s.append("\n\t");
            }
            s.append(e.toString());
            firstLine = false;
        }
        return s.toString();
    }

    /* factory methods */

    public static TaskMetadata newDefaultMetadata() {
        return new TaskMetadata();
    }

    public static TaskMetadata newMetadata(IPoolName poolName) {
        return new TaskMetadata(poolName);
    }

    public static TaskMetadata newMetadata(String threadNameSuffix) {
        return new TaskMetadata(null, threadNameSuffix);
    }

    public static TaskMetadata newMetadata(IPoolName poolName, String threadNameSuffix) {
        return new TaskMetadata(poolName, threadNameSuffix);
    }

    public static TaskMetadata newMetadata(IPoolName poolName, String threadNameSuffix, String taskId) {
        return new TaskMetadata(poolName, threadNameSuffix, taskId);
    }

}
