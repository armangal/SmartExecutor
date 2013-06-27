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

import java.util.concurrent.atomic.AtomicLong;

public class PoolStatsData {

    private final AtomicLong submitted = new AtomicLong(0);
    private final AtomicLong executed = new AtomicLong(0);
    private final AtomicLong completed = new AtomicLong(0);
    private final AtomicLong rejected = new AtomicLong(0);
    private final AtomicLong failed = new AtomicLong(0);
    private final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxTime = new AtomicLong(Long.MIN_VALUE);
    private final AtomicLong totalTime = new AtomicLong(0);

    public PoolStatsData() {}

    public AtomicLong getSubmitted() {
        return submitted;
    }

    public AtomicLong getExecuted() {
        return executed;
    }

    public AtomicLong getCompleted() {
        return completed;
    }

    public AtomicLong getRejected() {
        return rejected;
    }

    public AtomicLong getFailed() {
        return failed;
    }

    public AtomicLong getMinTime() {
        return minTime;
    }

    public AtomicLong getMaxTime() {
        return maxTime;
    }

    public Long getMaxTimeLong() {
        return maxTime.get() == Long.MIN_VALUE ? 0 : maxTime.get();
    }

    public Long getMinTimeLong() {
        return minTime.get() == Long.MAX_VALUE ? 0 : minTime.get();
    }

    public AtomicLong getTotalTime() {
        return totalTime;
    }

    public Long getAvgTime() {
        return (getCompleted().get() == 0 ? 0 : (getTotalTime().get() / getCompleted().get()));
    }

    public void reset() {
        submitted.set(0L);
        executed.set(0L);
        completed.set(0L);
        rejected.set(0L);
        failed.set(0L);
        minTime.set(Long.MAX_VALUE);
        maxTime.set(Long.MIN_VALUE);
        totalTime.set(0L);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PoolStatsData [submitted=");
        builder.append(submitted);
        builder.append(", executed=");
        builder.append(executed);
        builder.append(", completed=");
        builder.append(completed);
        builder.append(", rejected=");
        builder.append(rejected);
        builder.append(", failed=");
        builder.append(failed);
        builder.append(", minTime=");
        builder.append(minTime);
        builder.append(", maxTime=");
        builder.append(maxTime);
        builder.append(", totalTime=");
        builder.append(totalTime);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((completed == null) ? 0 : completed.hashCode());
        result = prime * result + ((executed == null) ? 0 : executed.hashCode());
        result = prime * result + ((failed == null) ? 0 : failed.hashCode());
        result = prime * result + ((maxTime == null) ? 0 : maxTime.hashCode());
        result = prime * result + ((minTime == null) ? 0 : minTime.hashCode());
        result = prime * result + ((rejected == null) ? 0 : rejected.hashCode());
        result = prime * result + ((submitted == null) ? 0 : submitted.hashCode());
        result = prime * result + ((totalTime == null) ? 0 : totalTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PoolStatsData other = (PoolStatsData) obj;
        if (completed == null) {
            if (other.completed != null)
                return false;
        } else if (!completed.equals(other.completed))
            return false;
        if (executed == null) {
            if (other.executed != null)
                return false;
        } else if (!executed.equals(other.executed))
            return false;
        if (failed == null) {
            if (other.failed != null)
                return false;
        } else if (!failed.equals(other.failed))
            return false;
        if (maxTime == null) {
            if (other.maxTime != null)
                return false;
        } else if (!maxTime.equals(other.maxTime))
            return false;
        if (minTime == null) {
            if (other.minTime != null)
                return false;
        } else if (!minTime.equals(other.minTime))
            return false;
        if (rejected == null) {
            if (other.rejected != null)
                return false;
        } else if (!rejected.equals(other.rejected))
            return false;
        if (submitted == null) {
            if (other.submitted != null)
                return false;
        } else if (!submitted.equals(other.submitted))
            return false;
        if (totalTime == null) {
            if (other.totalTime != null)
                return false;
        } else if (!totalTime.equals(other.totalTime))
            return false;
        return true;
    }

}
