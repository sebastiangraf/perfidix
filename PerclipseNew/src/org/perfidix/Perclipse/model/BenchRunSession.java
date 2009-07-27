package org.perfidix.Perclipse.model;

import java.util.List;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;

/**
 * A bench run session holds all information about a bench run. (launch
 * configuratoin, launch, bench tree including results)
 */

public class BenchRunSession {

    private int startedCount;
    private int currentCount;
    private int totalCount;
    private int errorCount;
    private boolean isStopped = false;
    private boolean isRunning = false;
    private List benchedClasses;
    private List<JavaElementsWithTotalRuns> benchElements;
    private ILaunch launch;
    private IJavaProject project;
    private String benchRunName;

    private static BenchRunSession runSession;

    private BenchRunSession() {
        // runSession=this;
    }

    public void setBenchRunSession(
            int totalCount, List<JavaElementsWithTotalRuns> completeList) {
        this.totalCount = totalCount;
        startedCount = 0;
        errorCount = 0;
        currentCount = 0;
        benchElements = completeList;

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

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentRun(String currentElement) {

        /** Dummy ******/
        currentCount = currentCount + 1;
        /** Dummy end *****/

        if (benchElements != null) {
            for (JavaElementsWithTotalRuns listElement : benchElements) {
                if (listElement.getJavaElement().equals(currentElement)) {
                    listElement.updateCurrentRun();
                    currentCount = currentCount + 1;
                }
            }

        }
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
        currentCount = 0;

    }

    /*********************************/
    // Temporarly the arguments for launching perfidix - the java classes
    // containing at least one bench
    public void setBenchedClasses(List benchedClasses) {
        this.benchedClasses = benchedClasses;
    }

    public List getBenchedClasses() {
        return benchedClasses;
    }

    /*********************************/

    public void setBenchElements(List<JavaElementsWithTotalRuns> elementsList) {
        this.benchElements = elementsList;
    }

    public List<JavaElementsWithTotalRuns> getBenchElements() {
        return benchElements;
    }

    public void updateError(String errorInElement) {
        errorCount = errorCount + 1;

    }

    public static BenchRunSession getInstance() {
        if (runSession == null) {
            runSession = new BenchRunSession();
        }
        return runSession;
    }

}
