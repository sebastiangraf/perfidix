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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.BenchRunSession}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */

public class BenchRunSessionTest {
    private transient BenchRunSession session;
    private transient JavaElementsWithTotalRuns run;
    private transient List<JavaElementsWithTotalRuns> theList;
    private static final transient String NOT_NULL = "Test if not null";

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        session = new BenchRunSession();
        run = new JavaElementsWithTotalRuns("TheElement", 123);
        theList = new ArrayList<JavaElementsWithTotalRuns>();
        theList.add(run);
    }

    // /**
    // * Simple tearDown - method.
    // *
    // * @throws java.lang.Exception
    // * The Exception occurred.
    // */
    // @After
    // public void tearDown() throws Exception {
    // session = null;
    // run = null;
    // theList = null;
    // }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#setBenchRunSession(int, java.util.List)}
     * .
     */
    @Test
    public void testSetBenchRunSessoin() {
        session.setBenchRunSession(150, null);
        assertEquals("Tests if the total count is equal to 150", 150, session
                .getTotalCount());
        assertNotNull(NOT_NULL, session);
        assertEquals("Tests if getBenchElements are null", null, session
                .getBenchElements());
        assertEquals("Tests if currentCount is 0", 0, session.getCurrentCount());
        assertEquals("Tests if errorCount is 0", 0, session.getErrorCount());
        assertEquals("Tests if started count is 0", 0, session
                .getStartedCount());
        assertEquals("Tests if current element is null", null, session
                .getCurrentRunElement());
        session.setBenchRunSession(-111, theList);
        assertEquals("Tests if total count is 0", 0, session.getTotalCount());
        assertEquals("Tests if current element is null", null, session
                .getCurrentRunElement());
        assertArrayEquals(
                "Tests if the both arrays are equal (the sent and the received)",
                theList.toArray(), session.getBenchElements().toArray());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#setCurrentRun(String)}
     */
    @Test
    public void testSetCurrentRun() {
        session.setBenchRunSession(555, theList);
        session.setCurrentRun("TheElement");
        assertEquals("Tests if the elements are equal", run, session
                .getCurrentRunElement());
        assertNotNull(NOT_NULL, session.getCurrentRunElement());
        assertEquals("Tests the current counter", 1, session.getCurrentCount());
        assertEquals("Tests the elements size", 1, session
                .getBenchElements().size());
        assertTrue("Tests if the running value is set", session.isRunning());
        assertFalse("Tests if the session is stopped", session.isStopped());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#updateError(String)}.
     */
    @Test
    public void testUpdateError() {
        session.setBenchRunSession(55, theList);
        assertEquals("Tests if the error count is 0", 0, session
                .getErrorCount());
        session.updateError("TheElement");
        assertEquals("Tests if the error count is 1", 1, session
                .getErrorCount());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#reset()}.
     */
    @Test
    public void testReset() {
        session.setBenchRunSession(999, theList);
        session.reset();
        assertEquals("Test if the current count is 0.", 0, session
                .getCurrentCount());
        assertEquals("Test if error count is still 0.", 0, session
                .getErrorCount());
        assertEquals("Test if started count is 0.", 0, session
                .getStartedCount());
        assertEquals("Tests if total count is 0.", 0, session.getTotalCount());
        assertTrue("Tests if the session is still running.", session
                .isRunning());
        assertFalse("Tests if the session stopped.", session.isStopped());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#setBenchElements(List)}
     * .
     */
    @Test
    public void testSetBenchElements() {
        session.setBenchElements(null);
        assertNull("Tests if object is null", session.getBenchElements());
        session.setBenchElements(theList);
        assertNotNull(NOT_NULL, session.getBenchElements());
        assertArrayEquals(
                "test if the sent array is equal to the received one.", theList
                        .toArray(), session.getBenchElements().toArray());

    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#setFinished(boolean)}
     */
    @Test
    public void testSetFinished() {
        session.setFinished(true);
        assertTrue("Test if session stopped.", session.isStopped());
        assertFalse("Tests if session is still running.", session.isRunning());
    }
}
