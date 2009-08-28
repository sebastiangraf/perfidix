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
    private transient TreeDataProvider childElements[]=null;

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
            final String parentElement,final int numberOfBenchs,final int currentBench) {
        if (numberOfBenchs >= 0 && currentBench >= 0 && parentElement != null) {
            this.parentElementName = parentElement;
            this.benchsForElement = numberOfBenchs;
            this.currentBench = currentBench;
            parent = this;
            errorInBench = 0;
            childElements= new TreeDataProvider[0];
        }
    }

    /**
     * @return This returns the child elements of the java elements that will be
     *         benched. Currently it is not used. But it could.
     */
    public TreeDataProvider[] getChildElements() {
        return childElements; // NOPMD by IceMan on 27.08.09 23:01
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
     * @param count The counter.
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
     * @param count The counter.
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
