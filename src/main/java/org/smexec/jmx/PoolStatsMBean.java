package org.smexec.jmx;

import java.util.List;

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
	
	List<Integer> getStats();
}
