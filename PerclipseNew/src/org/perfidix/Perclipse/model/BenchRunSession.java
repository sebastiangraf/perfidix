package org.perfidix.Perclipse.model;

import java.util.List;

/**
 * A bench run session holds all information about a bench run.
 */

public class BenchRunSession {

    private int startedCount;
    private int currentCount;
    private int totalCount;
    private int errorCount;
    private boolean isStopped = false;
    private boolean isRunning = false;
    private List<JavaElementsWithTotalRuns> benchElements;

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

    public void setBenchElements(List<JavaElementsWithTotalRuns> elementsList) {
        this.benchElements = elementsList;
    }

    public List<JavaElementsWithTotalRuns> getBenchElements() {
        return benchElements;
    }

    public void updateError(String errorInElement) {
        errorCount = errorCount + 1;

    }

}
