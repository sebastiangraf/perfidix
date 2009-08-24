/**
 * The package is responsible for the model skeletons.
 */
package org.perfidix.Perclipse.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.Perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.model.BenchRunSessionListener}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */

public class BenchRunSessionListenerTest {
    private BenchRunSessionListener listener;
    private HashMap<String, Integer> initData;
    private TestUtilClass utilClass;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        // TestUtilClass.setViewForTesting();
        utilClass = new TestUtilClass();
        utilClass.setViewForTesting();
        listener = new BenchRunSessionListener();
        initData = new HashMap<String, Integer>();
        initData.put("MyBenchObject", 123);
        initData.put("AnotherObject", 654);
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        listener = null;
        initData = null;
        utilClass.setViewNull();
        utilClass = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.BenchRunSessionListener#BenchRunSessionListener()}
     * .
     */
    @Test
    public void testBenchRunSessionListener() {
        assertNotNull(listener);
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.BenchRunSessionListener#initTotalBenchProgress(java.util.HashMap)}
     * .
     */
    @Test
    public void testInitTotalBenchProgress() {
        listener.initTotalBenchProgress(null);
        listener.initTotalBenchProgress(new HashMap<String, Integer>());
        listener.initTotalBenchProgress(initData);
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.BenchRunSessionListener#updateCurrentRun(String)}
     * .
     */
    @Test
    public void testUpdateCurrentRun() {
        listener.updateCurrentRun("MyBenchObject");
        listener.initTotalBenchProgress(initData);
        listener.updateCurrentRun("MyBenchObject");
        listener.updateCurrentRun("Nothing");
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.BenchRunSessionListener#updateError(String)}
     * .
     */
    @Test
    public void testUpdateError() {
        listener.updateError("MyBenchi");
        listener.initTotalBenchProgress(initData);
        listener.updateError("MyBenchObject");
        listener.updateError("Sometthin");
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.BenchRunSessionListener#finishedBenchRuns()}
     * .
     */
    @Test
    public void testFinishedBenchRuns() {

        listener.finishedBenchRuns();
        assertTrue(listener.getFinished());

    }
}
