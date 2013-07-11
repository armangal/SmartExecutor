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
 * A metadata class that is "following" the task during its' life-cycle and holding extra information about
 * where it should be executed, what it's name and statistics ID.
 * 
 * @author armang
 */
public class TaskMetadata {

    /**
     * destination thread pool name
     */
    private IPoolName poolName;

    /**
     * what should be the thread name suffix while executing the task
     */
    private String threadNameSuffix;

    /**
     * the ID of the task for statistical usage
     */
    private String taskId;

    private StackTraceElement[] stack;

    /**
     * keep it private, static factory methods are available.
     * 
     * @param poolName
     * @param threadNameSuffix
     * @param taskId
     */
    private TaskMetadata(IPoolName poolName, String threadNameSuffix, String taskId) {
        super();
        this.poolName = poolName;
        this.threadNameSuffix = threadNameSuffix;
        this.taskId = taskId;
    }

    /**
     * keep it private, static factory methods are available.
     * 
     * @param poolName
     * @param threadNameSuffix
     */
    private TaskMetadata(IPoolName poolName, String threadNameSuffix) {
        super();
        this.poolName = poolName;
        this.threadNameSuffix = threadNameSuffix;
    }

    /**
     * keep it private, static factory methods are available.
     */
    private TaskMetadata() {
        super();
    }

    /**
     * keep it private, static factory methods are available.
     * 
     * @param poolName
     */
    private TaskMetadata(IPoolName poolName) {
        super();
        this.poolName = poolName;
    }

    /**
     * @return - what should be the thread name suffix while executing the task
     */
    public String getThreadNameSuffix() {
        return threadNameSuffix;
    }

    /**
     * @return true is meta data has defined suffix
     */
    public boolean hasSuffix() {
        return threadNameSuffix != null;
    }

    /**
     * ugly, I know, but in some case suffix have to be set after meta data is created
     * 
     * @param threadNameSuffix
     */
    public void setThreadNameSuffix(String threadNameSuffix) {
        this.threadNameSuffix = threadNameSuffix;
    }

    /**
     * @return destination thread pool name
     */
    public IPoolName getPoolName() {
        return poolName;
    }

    /**
     * ugly, I know, but in some case pool have to be set after meta data is created
     * 
     * @param poolName
     */
    public void setPoolName(IPoolName poolName) {
        this.poolName = poolName;
    }

    /**
     * @return true if meta data has pool specified
     */
    public boolean hasPoolName() {
        return poolName != null;
    }

    /**
     * @return destination thread pool name as string
     */
    public String getPoolNameAsString() {
        return poolName.getPoolName();
    }

    /**
     * @return task ID to be used for statistics
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @return true if taskID was specified
     */
    public boolean hasTaskId() {
        return taskId != null;
    }

    /**
     * ugly, I know, but in some case taskId have to be set after meta data is created
     * 
     * @param taskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @param stack - sets the original stack trace of where the task was executed from
     */
    public void setStack(StackTraceElement[] stack) {
        this.stack = Arrays.copyOf(stack, stack.length);
    }

    /**
     * @return the original execution point stack trace
     */
    public StackTraceElement[] getStack() {
        return Arrays.copyOf(stack, stack.length);
    }

    /**
     * @return the original execution point stack trace as String
     */
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
    /**
     * Factory method
     * 
     * @return Default {@link TaskMetadata} instance
     */
    public static TaskMetadata newDefaultMetadata() {
        return new TaskMetadata();
    }

    /**
     * Factory method
     * 
     * @param poolName - destination thread pool
     * @return new {@link TaskMetadata} with provided pool name
     */
    public static TaskMetadata newMetadata(final IPoolName poolName) {
        return new TaskMetadata(poolName);
    }

    /**
     * Factory method
     * 
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @return new {@link TaskMetadata} with a provided thread name suffix
     */
    public static TaskMetadata newMetadata(String threadNameSuffix) {
        return new TaskMetadata(null, threadNameSuffix);
    }

    /**
     * Factory method
     * 
     * @param poolName- destination thread pool
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @return new {@link TaskMetadata} with a provided pool name and thread name suffix
     */
    public static TaskMetadata newMetadata(IPoolName poolName, String threadNameSuffix) {
        return new TaskMetadata(poolName, threadNameSuffix);
    }

    /**
     * Factory method
     * 
     * @param poolName- destination thread pool
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @param taskId - id of the task to collect statistics for
     * @return new {@link TaskMetadata} with a provided pool name, thread name suffix and task ID.
     */
    public static TaskMetadata newMetadata(IPoolName poolName, String threadNameSuffix, String taskId) {
        return new TaskMetadata(poolName, threadNameSuffix, taskId);
    }

}
