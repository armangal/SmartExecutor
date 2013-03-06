package org.smexec.jmx;

import java.beans.ConstructorProperties;

public class TaskExecutionStats {

    private long submitted;
    private long executed;
    private long completed;
    private long rejected;
    private long failed;

    @ConstructorProperties({"submitted", "executed", "completed", "rejected", "failed"})
    public TaskExecutionStats(long submitted, long executed, long completed, long rejected, long failed) {
        super();
        this.submitted = submitted;
        this.executed = executed;
        this.completed = completed;
        this.rejected = rejected;
        this.failed = failed;
    }

    public long getSubmitted() {
        return submitted;
    }

    public long getExecuted() {
        return executed;
    }

    public long getCompleted() {
        return completed;
    }

    public long getRejected() {
        return rejected;
    }

    public long getFailed() {
        return failed;
    }

}
