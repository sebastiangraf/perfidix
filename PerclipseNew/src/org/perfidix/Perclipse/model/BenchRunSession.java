package org.perfidix.Perclipse.model;

import java.util.List;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.core.IJavaProject;

/**
 * A bench run session holds all information about a bench run. 
 * (launch configuratoin, launch, bench tree including results)
 */

public class BenchRunSession {

	private int startedCount;
	private int totalCount;
	private int errorCount;
	private boolean isStopped = false;
	private boolean isRunning = false;
	private List benchedClasses;
	private String[] benchElements;
	private ILaunch launch;
	private IJavaProject project;
	private String benchRunName;


	private static BenchRunSession runSession;

	
	private BenchRunSession(){
		//runSession=this;
	}
	

	public void setBenchRunSession(int startedCount, int totalCount,
			int errorCount) {
		this.startedCount = startedCount;
		this.totalCount = totalCount;
		this.errorCount = errorCount;

	}

	public int getStartedCount() {
		return startedCount;
	}


	public int getTotalCount() {
		return totalCount;
	}

	public int getErrorCount() {
		return errorCount;
	}
	
	public void setCurrentRun(int run, String currentElement){
		startedCount=run;
		benchRunName=currentElement;
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

	//Temporarly the arguments for launching perfidix - the java classes containing at least one bench
	public void setBenchedClasses(List benchedClasses) {
		this.benchedClasses = benchedClasses;
	}

	public List getBenchedClasses() {
		return benchedClasses;
	}

	public void setBenchElements(String[] benchElements) {
		this.benchElements = benchElements;
	}

	public String[] getBenchElements() {
		return benchElements;
	}

	public void setError(int error) {
		errorCount=error;
		
	}

	public static BenchRunSession getInstance(){
		if(runSession==null){
			runSession=new BenchRunSession();
		}
		return runSession;
	}



	
	


}
