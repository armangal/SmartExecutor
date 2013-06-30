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

/**
 * Base class for statistic entries, holding statistics for specified period.
 * 
 * @author erdoan
 * @version $Revision: 1.1 $
 */
public abstract class AbstractJmxStatEntry {

    protected long startTime;

    protected long endTime;

    protected double cpuUsage;

    public AbstractJmxStatEntry(long startTime, long endTime) {
        super();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    @Override
    public String toString() {
        return "AbstractJmxStatEntry{" + "startTime=" + startTime + ", endTime=" + endTime + ", cpuUsage=" + cpuUsage + "}";
    }
}
