package org.perfidix.Perclipse.tempPerfidixClientClasses;

import java.util.ArrayList;


public interface IBenchRunSessionListener {
	public void initTotalBenchProgress(int totalRun, Object[] benchElementsWithTotalBench);
	public void updateCurrentRun(String currentElement);
	public void updateError(String element);
	public void finishedBenchRuns();
	
}
