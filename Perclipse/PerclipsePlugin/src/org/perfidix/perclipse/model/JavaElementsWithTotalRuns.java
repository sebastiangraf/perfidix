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
