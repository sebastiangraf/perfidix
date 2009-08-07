package org.perfidix.Perclipse.model;

import java.util.List;

/**
 * A bench run session contains all information about a running bench session.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */

public class BenchRunSession {

    private int startedCount;
    private int currentCount;
    private int totalCount;
    private int errorCount;
    private boolean isStopped = false;
    private boolean isRunning = false;
    private List<JavaElementsWithTotalRuns> benchElements;
    private JavaElementsWithTotalRuns currentRunElement;

    /**
     * This method initializes the bench run session.
     * 
     * @param totalCount
     *            This param is the total amount of all bench runs which will be
     *            executed.
     * @param completeList
     *            completeList contains every java element that will be benched.
     */
    public void setBenchRunSession(
            int totalCount, List<JavaElementsWithTotalRuns> completeList) {
        if (totalCount < 0) {
            this.totalCount = 0;
        } else {
            this.totalCount = totalCount;
        }
        startedCount = 0;
        errorCount = 0;
        currentCount = 0;
        benchElements = completeList;
        isRunning = true;
        isStopped = false;

    }

    /**
     * @return The started count value of all bench runs.
     */
    public int getStartedCount() {
        return startedCount;
    }

    /**
     * @return Returns the total amount of all benches.
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @return Returns the amount of the error count.
     */
    public int getErrorCount() {
        return errorCount;
    }

    /**
     * @return Returns the amount of the current count of the bench runs.
     */
    public int getCurrentCount() {
        return currentCount;
    }

    /**
     * This method sets the current count for all benches and each bench runs.
     * 
     * @param currentElement
     *            This param is the name of the current java element that will
     *            be benched.
     */
    public void setCurrentRun(String currentElement) {

        if (benchElements != null && benchElements.size()>0) {
            for (JavaElementsWithTotalRuns listElement : benchElements) {
                if (listElement.getJavaElement().equals(currentElement)) {
                    listElement.updateCurrentRun();
                    currentRunElement = listElement;
                    currentCount = currentCount + 1;
                }
            }

        }
    }

    /**
     * @return The current element.
     */
    public JavaElementsWithTotalRuns getCurrentRunElement() {
        return currentRunElement;
    }

    /**
     * @return Returns if the BenchRunsSession has stopped or not.
     */
    public boolean isStopped() {
        return isStopped;
    }

    /**
     * @return Returns the value if the bench run session is still running.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * This method resets each counter to 0.
     */
    public void reset() {
        startedCount = 0;
        totalCount = 0;
        errorCount = 0;
        currentCount = 0;
        currentRunElement = null;
        benchElements = null;
        isRunning = true;
        isStopped = false;

    }

    /**
     * This method sets the list of java elements that will be benched to
     * display it in the view.
     * 
     * @param elementsList
     *            A List of java elements to be benched of type
     *            {@link JavaElementsWithTotalRunsTest}.
     */
    public void setBenchElements(List<JavaElementsWithTotalRuns> elementsList) {
        this.benchElements = elementsList;
    }

    /**
     * @return Returns a list of java elements that will be benched.
     */
    public List<JavaElementsWithTotalRuns> getBenchElements() {
        return benchElements;
    }

    /**
     * This method updates the error count.
     * 
     * @param errorInElement
     *            The name of the java element where the error occurred.
     */
    public void updateError(String errorInElement) {
        if (benchElements != null && benchElements.size()>0) {
            for (JavaElementsWithTotalRuns listElement : benchElements) {
                if (listElement.getJavaElement().equals(errorInElement)) {
                    listElement.updateErrorCount();
                    errorCount = errorCount + 1;                    
                }
            }

        }

    }

    /**
     * This method sets finished when a bench run session ended.
     * 
     * @param finished
     *            The boolean param that is true when a bench process finished.
     */
    public void setFinished(boolean finished) {
        isRunning = !finished;
        isStopped = finished;

    }

}
