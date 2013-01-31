package org.smexec.mbeans;

public interface PoolStatsMBean {
	String getStats();

	void printStats();

	Long getSubmitted();

	Long getExecuted();

	Long getCompleted();

	Long getRejected();

	Long getFailed();

	Long getMinTime();

	Long getMaxTime();

	Long getTotalTime();

	Long getAvgTime();
}
