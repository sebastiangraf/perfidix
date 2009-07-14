package org.perfidix.Perclipse.model;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

public interface IBenchRunSessionListener {
	public void initTotalBenchProgress(int totalRun, List<JavaElementsWithTotalRuns> benchElementsWithTotalBench);
	public void updateCurrentRun(IJavaElement currentElement);
	public void updateError(IJavaElement element);
	
}
