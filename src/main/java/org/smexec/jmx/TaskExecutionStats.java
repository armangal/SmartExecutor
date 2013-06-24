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
