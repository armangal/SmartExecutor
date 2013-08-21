/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
 */
package org.clevermore;

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

    /**
     * if to record invoking thread stack trace, it has performance affect
     */
    private boolean recordStackTrace = false;

    /**
     * the recorded stack trace of the invoking thread.
     */
    private StackTraceElement[] stack;

    /**
     * keep it private, static factory methods are available.
     * 
     * @param poolName
     * @param threadNameSuffix
     * @param taskId
     */
    private TaskMetadata(IPoolName poolName, String threadNameSuffix, String taskId, boolean recordStackTrace) {
        super();
        this.poolName = poolName;
        this.threadNameSuffix = threadNameSuffix;
        this.taskId = taskId;
        this.recordStackTrace = recordStackTrace;
    }

    /**
     * keep it private, static factory methods are available.
     * 
     * @param poolName
     * @param threadNameSuffix
     */
    private TaskMetadata(IPoolName poolName, String threadNameSuffix, boolean recordStackTrace) {
        super();
        this.poolName = poolName;
        this.threadNameSuffix = threadNameSuffix;
        this.recordStackTrace = recordStackTrace;
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
     * keep it private, static factory methods are available.
     * 
     * @param poolName
     */
    private TaskMetadata(IPoolName poolName, final boolean recordStackTrace) {
        super();
        this.poolName = poolName;
        this.recordStackTrace = recordStackTrace;
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
    public void setStack() {
        if (!recordStackTrace) {
            return;
        }
        if (this.stack != null) {
            System.err.println(new Throwable());
        }
        StackTraceElement[] stack = new Throwable().getStackTrace();
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
     * @param poolName - destination thread pool
     * @return new {@link TaskMetadata} with provided pool name
     */
    public static TaskMetadata newMetadata(final IPoolName poolName, final boolean recordStackTrace) {
        return new TaskMetadata(poolName);
    }

    /**
     * Factory method
     * 
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @return new {@link TaskMetadata} with a provided thread name suffix
     */
    public static TaskMetadata newMetadata(final String threadNameSuffix) {
        return new TaskMetadata(null, threadNameSuffix, false);
    }

    /**
     * Factory method
     * 
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @param recordStackTrace - if to record the caller stacktrace
     * @return new {@link TaskMetadata} with a provided thread name suffix
     */
    public static TaskMetadata newMetadata(final String threadNameSuffix, final boolean recordStackTrace) {
        return new TaskMetadata(null, threadNameSuffix, recordStackTrace);
    }

    /**
     * Factory method
     * 
     * @param poolName- destination thread pool
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @return new {@link TaskMetadata} with a provided pool name and thread name suffix
     */
    public static TaskMetadata newMetadata(final IPoolName poolName, final String threadNameSuffix) {
        return new TaskMetadata(poolName, threadNameSuffix, false);
    }

    /**
     * Factory method
     * 
     * @param poolName- destination thread pool
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @param recordStackTrace - if to record the caller stacktrace
     * @return new {@link TaskMetadata} with a provided pool name and thread name suffix
     */
    public static TaskMetadata newMetadata(final IPoolName poolName, final String threadNameSuffix, final boolean recordStackTrace) {
        return new TaskMetadata(poolName, threadNameSuffix, recordStackTrace);
    }

    /**
     * Factory method
     * 
     * @param poolName- destination thread pool
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @param taskId - id of the task to collect statistics for
     * @param recordStackTrace - if to record the caller stacktrace
     * @return new {@link TaskMetadata} with a provided pool name, thread name suffix and task ID.
     */
    public static TaskMetadata newMetadata(final IPoolName poolName, final String threadNameSuffix, final String taskId, final boolean recordStackTrace) {
        return new TaskMetadata(poolName, threadNameSuffix, taskId, recordStackTrace);
    }

    /**
     * Factory method
     * 
     * @param poolName- destination thread pool
     * @param threadNameSuffix - thread name suffix to be used during task execution
     * @param taskId - id of the task to collect statistics for
     * @return new {@link TaskMetadata} with a provided pool name, thread name suffix and task ID.
     */
    public static TaskMetadata newMetadata(final IPoolName poolName, final String threadNameSuffix, final String taskId) {
        return new TaskMetadata(poolName, threadNameSuffix, taskId, false);
    }
}
