package org.perfidix.Perclipse.model;



public class BenchRunSessionListener implements IBenchRunSessionListener{
	
	private BenchRunSession runSession;
	private BenchRunViewUpdater viewUpdater;

	public BenchRunSessionListener(){
		 runSession = BenchRunSession.getInstance();
		 viewUpdater = new BenchRunViewUpdater();
	}

	public void initTotalBenchProgress(int startRun, int totalRun, int error,
			String[] benchElementsWithTotalBench) {
		runSession.setBenchRunSession(startRun, totalRun, error);
		runSession.setBenchElements(benchElementsWithTotalBench);
		viewUpdater.updateView(runSession);
	}

	public void updateCurrentRun(int currentRun, String currentElement) {
		runSession.setCurrentRun(currentRun, currentElement);
		viewUpdater.updateView(runSession);
	}

	public void updateError(int error) {
		runSession.setError(error);
		viewUpdater.updateView(runSession);
	}

}
