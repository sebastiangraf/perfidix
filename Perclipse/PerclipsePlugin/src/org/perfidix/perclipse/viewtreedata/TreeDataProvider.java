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
package org.perfidix.perclipse.viewtreedata;

/**
 * The TreeDataProvider class is responsible for the data that will be displayed
 * in the progress view of the eclipse plug-in
 * 
 * @author Lukas Lewandowski, DiSy, University of Konstanz
 */
public class TreeDataProvider {

    private transient String parentElementName;
    private transient int benchsForElement;
    private transient int currentBench;
    private transient int errorInBench;
    private transient TreeDataProvider parent = null;
    private transient TreeDataProvider childElements[] = null;

    /**
     * The constructor initializes the data which will be displayed in the view.
     * 
     * @param parentElement
     *            This param represents the java element that will be benched by
     *            perfidix.
     * @param numberOfBenchs
     *            This param represents the amount of runs for the responsible
     *            java element.
     * @param currentBench
     *            This param represents the current count of the bench run. Here
     *            it is initialized.
     */
    public TreeDataProvider(
            final String parentElement, final int numberOfBenchs,
            final int currentBench) {
        if (numberOfBenchs >= 0 && currentBench >= 0 && parentElement != null) {
            this.parentElementName = parentElement;
            this.benchsForElement = numberOfBenchs;
            this.currentBench = currentBench;
            parent = this;
            errorInBench = 0;
            childElements = new TreeDataProvider[0];
        }
    }

    /**
     * @return This returns the child elements of the java elements that will be
     *         benched. Currently it is not used. But it could.
     */
    public TreeDataProvider[] getChildElements() {
        return childElements.clone();
    }

    /**
     * @return This method returns the java element that will be benched.
     */
    public String getParentElementName() {
        return parentElementName;
    }

    /**
     * @return This method returns the total runs for an appropriate java
     *         element.
     */
    public int getNumberOfBenchsForElement() {
        return benchsForElement;
    }

    /**
     * Counts the currentBench errors.
     * 
     * @param count
     *            The counter.
     */
    public void updateCurrentBenchError(final int count) {
        errorInBench = count;
    }

    /**
     * @return This method returns the current bench error run of the java
     *         element.
     */
    public int getCurrentBenchError() {
        return errorInBench;
    }

    /**
     * Counts the currentBench;
     * 
     * @param count
     *            The counter.
     */
    public void updateCurrentBench(final int count) {
        currentBench = count;
    }

    /**
     * @return This method returns the current bench run of the java element.
     */
    public int getCurrentBench() {
        return currentBench;
    }

    /**
     * @return This method returns this object.
     */
    public TreeDataProvider getParent() {
        return parent;
    }

    /**
     * Override toString()
     * 
     * @return see the super method description.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return parentElementName;
    }

}
