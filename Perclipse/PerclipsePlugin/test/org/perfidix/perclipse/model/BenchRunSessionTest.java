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
