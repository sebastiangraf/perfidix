package org.perfidix.Perclipse.model;

import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.util.BenchRunSession;
import org.perfidix.Perclipse.views.BenchView;

public class SimulatedWorkClass {
	
	
	private final int DURATION=10;
	private BenchView benchView;
	private BenchRunSession benchRunSession;

	public SimulatedWorkClass(){
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.benchView=PerclipseActivator.getDefault().getBenchView();
		if(benchView!=null){
			benchRunSession=BenchRunSession.getInstance();
			benchRunSession.setBenchRunSession(0, 0, DURATION, 0);
			simulateLongOperation();
		}
	}

	public void simulateLongOperation(){
		synchronized(this){
		for (int i = 0; i < DURATION; i++) {
			try {
				Thread.sleep(1000);
				benchRunSession.setBenchRunSession(i+1, 0, DURATION, 0);
				benchView.setBenchRunSession(benchRunSession);
				benchView.startUpdateJobs();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		}
	}

	
}
