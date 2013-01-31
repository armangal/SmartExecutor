package org.smexec.jmx;


public interface PoolStatsMBean {
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
	
	String getChunks();
}
