/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
