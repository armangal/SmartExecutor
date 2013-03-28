package org.smexec.jmx;

import java.beans.ConstructorProperties;

public class TaskExecutionStats {

    private long submitted;
    private long executed;
    private long completed;
    private long rejected;
    private long failed;
    private long chunkTime;

    @ConstructorProperties({"submitted", "executed", "completed", "rejected", "failed", "chunkTime"})
    public TaskExecutionStats(long submitted, long executed, long completed, long rejected, long failed, long chunkTime) {
        super();
        this.submitted = submitted;
        this.executed = executed;
        this.completed = completed;
        this.rejected = rejected;
        this.failed = failed;
        this.chunkTime = chunkTime;
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

    public long getChunkTime() {
        return chunkTime;
    }
}
