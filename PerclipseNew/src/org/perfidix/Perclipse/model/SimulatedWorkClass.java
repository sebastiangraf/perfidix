package org.perfidix.Perclipse.model;


public class SimulatedWorkClass{
	
	
	private final int DURATION=10;
	private BenchRunSessionListener benchRunSession;

	public SimulatedWorkClass(){
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			simulateLongOperation();

	}

	public void simulateLongOperation(){
		
		benchRunSession = new BenchRunSessionListener();
		benchRunSession.initTotalBenchProgress(0, DURATION, 0, null);
	
		for (int i = 0; i < DURATION; i++) {
			try {
				Thread.sleep(1000);
				benchRunSession.updateCurrentRun(i+1, "null");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	
}
