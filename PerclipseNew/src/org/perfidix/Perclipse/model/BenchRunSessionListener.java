package org.perfidix.Perclipse.model;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;



public class BenchRunSessionListener implements IBenchRunSessionListener{
	
	private BenchRunSession runSession;
	private BenchRunViewUpdater viewUpdater;

	public BenchRunSessionListener(){
		 runSession = BenchRunSession.getInstance();
		 viewUpdater = new BenchRunViewUpdater();
	}

	public void initTotalBenchProgress(int totalRun,
			List<JavaElementsWithTotalRuns> benchElementsWithTotalBench) {
		runSession.setBenchRunSession(totalRun, benchElementsWithTotalBench);
		viewUpdater.updateView(runSession);
	}

	public void updateCurrentRun(IJavaElement currentElement) {
		runSession.setCurrentRun(currentElement);
		viewUpdater.updateView(runSession);
	}

	public void updateError(IJavaElement element) {
		runSession.updateError(element);
		viewUpdater.updateView(runSession);
	}

}
