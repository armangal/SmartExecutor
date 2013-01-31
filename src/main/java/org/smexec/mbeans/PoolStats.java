package org.smexec.mbeans;

import org.smexec.pool.ISmartThreadPool;

public class PoolStats implements PoolStatsMBean {

	private final ISmartThreadPool stp;

	public PoolStats(final ISmartThreadPool stp) {
		this.stp = stp;
	}

	@Override
	public String getStats() {
		return stp.getPoolStats().toString();
	}

	@Override
	public void printStats() {
		System.out.println(stp.getPoolStats().toString());
	}

	@Override
	public Long getSubmitted() {
		return stp.getPoolStats().getSubmitted();
	}

	@Override
	public Long getExecuted() {

		return stp.getPoolStats().getExecuted();
	}

	@Override
	public Long getCompleted() {

		return stp.getPoolStats().getCompleted();
	}

	@Override
	public Long getRejected() {

		return stp.getPoolStats().getRejected();
	}

	@Override
	public Long getFailed() {

		return stp.getPoolStats().getFailed();
	}

	@Override
	public Long getMinTime() {

		return stp.getPoolStats().getMinTime();
	}

	@Override
	public Long getMaxTime() {

		return stp.getPoolStats().getMaxTime();
	}

	@Override
	public Long getTotalTime() {

		return stp.getPoolStats().getTotalTime();
	}

	@Override
	public Long getAvgTime() {

		return stp.getPoolStats().getAvgTime();
	}
}
