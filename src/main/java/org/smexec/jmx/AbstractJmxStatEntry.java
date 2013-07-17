/**
 * Written by Arman Gal, and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @author Arman Gal, arman@armangal.com, @armangal
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
