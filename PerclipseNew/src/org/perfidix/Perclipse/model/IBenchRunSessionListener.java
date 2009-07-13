package org.perfidix.Perclipse.model;

public interface IBenchRunSessionListener {
	public void initTotalBenchProgress(int startRun, int totalRun, int error, String[] benchElementsWithTotalBench);
	public void updateCurrentRun(int currentRun, String currentElement);
	public void updateError(int error);
	
}
