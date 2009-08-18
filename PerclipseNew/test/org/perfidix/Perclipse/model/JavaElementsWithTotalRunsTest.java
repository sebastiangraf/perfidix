/**
 * The package is responsible for the model skeletons.
 */
package org.perfidix.Perclipse.model;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.model.JavaElementsWithTotalRuns}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class JavaElementsWithTotalRunsTest {

    private JavaElementsWithTotalRuns elements;

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

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        elements = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.JavaElementsWithTotalRuns#JavaElementsWithTotalRuns(String, int)}
     * .
     */
    @Test
    public void testJavaElementsWithTotalRuns() {
        assertEquals(0, elements.getCurrentRun());
        assertEquals(0, elements.getErrorCount());
        assertEquals(555, elements.getTotalRuns());
        assertEquals("TheObject", elements.getJavaElement());
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.JavaElementsWithTotalRuns#updateCurrentRun()}
     * .
     */
    @Test
    public void testUpdateCurrentRun() {
        assertEquals(0, elements.getCurrentRun());
        elements.updateCurrentRun();
        assertEquals(1, elements.getCurrentRun());
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.JavaElementsWithTotalRuns#updateErrorCount()}
     * .
     */
    @Test
    public void testUpdateErrorCount() {
        assertEquals(0, elements.getErrorCount());
        elements.updateErrorCount();
        assertEquals(1, elements.getErrorCount());
    }
}
