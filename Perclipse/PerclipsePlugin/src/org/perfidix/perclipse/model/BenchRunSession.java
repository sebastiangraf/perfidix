/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.perclipse.model;

import java.util.List;

/**
 * A bench run session contains all information about a running bench session.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */

public class BenchRunSession {

    private transient int startedCount;
    private transient int currentCount;
    private transient int totalCount;
    private transient int errorCount;
    private transient boolean stopped = false;
    private transient boolean running = false;
    private transient List<JavaElementsWithTotalRuns> benchElements;
    private transient JavaElementsWithTotalRuns currentRunElement;

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
            final int totalCount,
            final List<JavaElementsWithTotalRuns> completeList) {
        if (totalCount < 0) {
            this.totalCount = 0;
        } else {
            this.totalCount = totalCount;
        }
        startedCount = 0;
        errorCount = 0;
        currentCount = 0;
        benchElements = completeList;
        running = true;
        stopped = false;

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
    public void setCurrentRun(final String currentElement) {

        if (benchElements != null && !benchElements.isEmpty()) {
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
        return stopped;
    }

    /**
     * @return Returns the value if the bench run session is still running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * This method resets each counter to 0.
     */
    public void reset() {
        startedCount = 0;
        totalCount = 0;
        errorCount = 0;
        currentCount = 0;
        running = true;
        stopped = false;

    }

    /**
     * This method sets the list of java elements that will be benched to
     * display it in the view.
     * 
     * @param elementsList
     *            A List of java elements to be benched of type
     *            {@link JavaElementsWithTotalRunsTest}.
     */
    public void setBenchElements(
            final List<JavaElementsWithTotalRuns> elementsList) {
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
    public void updateError(final String errorInElement) {
        if (benchElements != null && !benchElements.isEmpty()) {
            for (JavaElementsWithTotalRuns listElement : benchElements) {
                if (listElement.getJavaElement().equals(errorInElement)) {
                    listElement.updateErrorCount();
                    listElement.updateCurrentRun();
                    currentRunElement = listElement;
                    currentCount = currentCount + 1;
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
    public void setFinished(final boolean finished) {
        running = false;
        stopped = finished;

    }

}
