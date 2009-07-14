package org.perfidix.Perclipse.model;

import org.eclipse.jdt.core.IJavaElement;

public class JavaElementsWithTotalRuns {
	
	private IJavaElement javaElement;
	private int totalRuns;
	private int currentRun;

	public JavaElementsWithTotalRuns(IJavaElement javaElement, int totalRuns){
		this.javaElement=javaElement;
		this.totalRuns=totalRuns;
		currentRun=0;
	}
	
	
	public int getCurrentRun(){
		return currentRun;
	}

	public int getTotalRuns() {
		return totalRuns;
	}
	
	public IJavaElement getJavaElement(){
		return javaElement;
	}
	
	public void updateCurrentRun(){
		currentRun=currentRun+1;
	}
	
	

}
