package org.smexec.jmx;

public interface ExecutorStatsMXBean {

	String getName();

	String getDescription();
	
	int getActivePools();
}
