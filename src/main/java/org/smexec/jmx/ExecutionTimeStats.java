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

public class ExecutionTimeStats {

    private long min;
    private long max;
    private long avg;
    private long chunkTime;

    @ConstructorProperties({"min", "max", "avg", "chunkTime"})
    public ExecutionTimeStats(long min, long max, long avg, long chunkTime) {
        super();
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.chunkTime = chunkTime;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public long getAvg() {
        return avg;
    }

    public long getChunkTime() {
        return chunkTime;
    }

}
