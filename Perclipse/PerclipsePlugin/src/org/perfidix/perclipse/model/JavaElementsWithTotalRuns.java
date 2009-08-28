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

/**
 * This class declares an java element that will be benched. It contains of its
 * fully qualified name and bench runs.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class JavaElementsWithTotalRuns {

    final private transient String javaElement;
    final private transient int totalRuns;
    private transient int currentRun;
    private transient int errorCount;

    /**
     * The constructor initializes the java element name and its total runs.
     * Afterwards it resets the current and error count.
     * 
     * @param javaElement
     *            The fully qualified {@link String} name of the java element.
     * @param totalRuns
     *            The total bench runs of the java element.
     */
    public JavaElementsWithTotalRuns(
            final String javaElement, final int totalRuns) {
        this.javaElement = javaElement;
        this.totalRuns = totalRuns;
        currentRun = 0;
        errorCount = 0;
    }

    /**
     * @return Return the currentRun of the java element.
     */
    public int getCurrentRun() {
        return currentRun;
    }

    /**
     * @return Return the total runs of the java element.
     */
    public int getTotalRuns() {
        return totalRuns;
    }

    /**
     * @return Return the fully qualified name of the java element.
     */
    public String getJavaElement() {
        return javaElement;
    }

    /**
     * This method updates the current bench run of this java element.
     */
    public void updateCurrentRun() {
        currentRun = currentRun + 1;
    }

    /**
     * This method updates the current error count of this java element.
     */
    public void updateErrorCount() {
        errorCount = errorCount + 1;
    }

    /**
     * @return Returns the amount of the error count.
     */
    public int getErrorCount() {
        return errorCount;
    }

}
