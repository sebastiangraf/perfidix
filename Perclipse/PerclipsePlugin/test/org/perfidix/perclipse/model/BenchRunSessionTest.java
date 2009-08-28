/**
 * The package is responsible for the model skeletons.
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.model.BenchRunSession;
import org.perfidix.perclipse.model.JavaElementsWithTotalRuns;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.BenchRunSession}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */

public class BenchRunSessionTest {
    private BenchRunSession session;
    private JavaElementsWithTotalRuns run;
    private List<JavaElementsWithTotalRuns> theList;

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

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        session = null;
        run = null;
        theList = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#setBenchRunSession(int, java.util.List)}
     * .
     */
    @Test
    public void testSetBenchRunSessoin() {
        session.setBenchRunSession(150, null);
        assertEquals(150, session.getTotalCount());
        assertNotNull(session);
        assertEquals(null, session.getBenchElements());
        assertEquals(0, session.getCurrentCount());
        assertEquals(0, session.getErrorCount());
        assertEquals(0, session.getStartedCount());
        assertEquals(null, session.getCurrentRunElement());
        session.setBenchRunSession(-111, theList);
        assertEquals(0, session.getTotalCount());
        assertEquals(null, session.getCurrentRunElement());
        assertArrayEquals(theList.toArray(), session
                .getBenchElements().toArray());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#setCurrentRun(String)}
     */
    @Test
    public void testSetCurrentRun() {
        session.setBenchRunSession(555, theList);
        session.setCurrentRun("TheElement");
        assertEquals(run, session.getCurrentRunElement());
        assertNotNull(session.getCurrentRunElement());
        assertEquals(1, session.getCurrentCount());
        assertEquals(1, session.getBenchElements().size());
        assertTrue(session.isRunning());
        assertFalse(session.isStopped());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#updateError(String)}.
     */
    @Test
    public void testUpdateError() {
        session.setBenchRunSession(55, theList);
        assertEquals(0, session.getErrorCount());
        session.updateError("TheElement");
        assertEquals(1, session.getErrorCount());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#reset()}.
     */
    @Test
    public void testReset() {
        session.setBenchRunSession(999, theList);
        session.reset();
        assertEquals(0, session.getCurrentCount());
        assertEquals(0, session.getErrorCount());
        assertEquals(0, session.getStartedCount());
        assertEquals(0, session.getTotalCount());
        assertTrue(session.isRunning());
        assertFalse(session.isStopped());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#setBenchElements(List)}
     * .
     */
    @Test
    public void testSetBenchElements() {
        session.setBenchElements(null);
        assertNull(session.getBenchElements());
        session.setBenchElements(theList);
        assertNotNull(session.getBenchElements());
        assertArrayEquals(theList.toArray(), session
                .getBenchElements().toArray());

    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSession#setFinished(boolean)}
     */
    @Test
    public void testSetFinished() {
        session.setFinished(true);
        assertTrue(session.isStopped());
        assertFalse(session.isRunning());
    }
}
