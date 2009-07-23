package org.perfidix.Perclipse.model;


public class JavaElementsWithTotalRuns {
	
	private String javaElement;
	private int totalRuns;
	private int currentRun;

	public JavaElementsWithTotalRuns(String javaElement, int totalRuns){
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
	
	public String getJavaElement(){
		return javaElement;
	}
	
	public void updateCurrentRun(){
		currentRun=currentRun+1;
	}
	
	

}
