package org.smexec.jmx;

public interface ExecutorStatsMBean {

	String getName();

	String getDescription();
	
	int getActivePools();
}
