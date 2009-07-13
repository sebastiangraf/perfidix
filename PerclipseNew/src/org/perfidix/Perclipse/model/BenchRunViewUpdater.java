package org.perfidix.Perclipse.model;



import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.views.BenchView;
import org.perfidix.Perclipse.model.BenchRunSession;

public class BenchRunViewUpdater {

	private final BenchView BENCH_VIEW=PerclipseActivator.getDefault().getBenchView();
	
	public void updateView(BenchRunSession benchSession){
		if(BENCH_VIEW!=null)
		BENCH_VIEW.startUpdateJobs(benchSession);
	}
	
}
