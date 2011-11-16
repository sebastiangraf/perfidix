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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.JavaElementsWithTotalRuns}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class JavaElementsWithTotalRunsTest {

    private transient JavaElementsWithTotalRuns elements;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        elements = new JavaElementsWithTotalRuns("TheObject", 555);
    }

    // /**
    // * Simple tearDown - method.
    // *
    // * @throws java.lang.Exception
    // * The Exception occurred.
    // */
    // @After
    // public void tearDown() throws Exception {
    // elements = null;
    // }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.JavaElementsWithTotalRuns#JavaElementsWithTotalRuns(String, int)}
     * .
     */
    @Test
    public void testJavaElementsWithTotalRuns() {
        assertEquals("Tests if current run is 0.", 0, elements.getCurrentRun());
        assertEquals("Tests if current count is 0 too.", 0, elements
                .getErrorCount());
        assertEquals("Tests if total runs are equal 555", 555, elements
                .getTotalRuns());
        assertEquals(
                "Tests if the elements java element is equal to TheObject.",
                "TheObject", elements.getJavaElement());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.JavaElementsWithTotalRuns#updateCurrentRun()}
     * .
     */
    @Test
    public void testUpdateCurrentRun() {
        assertEquals("Tests if the current run is 0.", 0, elements
                .getCurrentRun());
        elements.updateCurrentRun();
        assertEquals(
                "Tests if the current run element counter is 0.", 1, elements
                        .getCurrentRun());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.JavaElementsWithTotalRuns#updateErrorCount()}
     * .
     */
    @Test
    public void testUpdateErrorCount() {
        assertEquals("Tests if error count is 0.", 0, elements.getErrorCount());
        elements.updateErrorCount();
        assertEquals("Tests if error count is 1.", 1, elements.getErrorCount());
    }
}
