package org.perfidix.Perclipse.util;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;

/**
 * A bench run session holds all information about a bench run. 
 * (launch configuratoin, launch, bench tree including results)
 */

public class BenchRunSession {

	private int startedCount;
	private int ignoredCount;
	private int totalCount;
	private int errorCount;
	private boolean isStopped = false;
	private boolean isRunning = false;
	private List benchedClasses;
	private ILaunch launch;
	private IJavaProject project;
	private String benchRunName;

	private static BenchRunSession runSession;
	
	private BenchRunSession(){
		//runSession=this;
	}
	
	private BenchRunSession(ILaunch launch, IJavaProject project, int port){
		Assert.isNotNull(launch);
		
		this.launch=launch;
		this.project=project;
		
		ILaunchConfiguration configuration= launch.getLaunchConfiguration();
		if(configuration!=null){
			benchRunName=configuration.getName();
			
		}
		
	}

	public void setBenchRunSession(int startedCount, int ignoredCount, int totalCount,
			int errorCount) {
		this.startedCount = startedCount;
		this.ignoredCount = ignoredCount;
		this.totalCount = totalCount;
		this.errorCount = errorCount;

	}

	public int getStartedCount() {
		return startedCount;
	}

	public int getIgnoredCount() {
		return ignoredCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getErrorCount() {
		return errorCount;
	}


	public boolean isStopped() {
		return isStopped;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void reset() {
		startedCount = 0;
		totalCount = 0;
		errorCount = 0;

	}

	public Object getLaunch() {
		// TODO Auto-generated method stub
		return null;
	}

	public void swapOut() {
		// TODO Auto-generated method stub
		
	}

	public Object getBenchRunName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBenchedClasses(List benchedClasses) {
		this.benchedClasses = benchedClasses;
	}

	public List getBenchedClasses() {
		return benchedClasses;
	}
	
	public static BenchRunSession getInstance(){
		if(runSession==null){
			runSession=new BenchRunSession();
		}
		return runSession;
	}


	
	


}
