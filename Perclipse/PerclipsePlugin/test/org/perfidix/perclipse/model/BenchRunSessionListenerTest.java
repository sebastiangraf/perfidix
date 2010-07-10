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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.BenchRunSessionListener}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */

public class BenchRunSessionListenerTest {
    private transient BenchRunSessionListener listener;
    private transient Map<String, Integer> initData;
    private transient TestUtilClass utilClass;
    private static final transient String MY_BOBJECT = "MyBenchObject";

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
        initData.put(MY_BOBJECT, 123);
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
        // listener = null;
        // initData = null;
        utilClass.setViewNull();
        // utilClass = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSessionListener#BenchRunSessionListener()}
     * .
     */
    @Test
    public void testBenchRunSessionListener() {
        assertNotNull("Test if not null", listener);
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSessionListener#initTotalBenchProgress(java.util.HashMap)}
     * .
     */
    @Test
    public void testInitTotalBenchProgress() { // NOPMD by lewandow on 8/31/09
                                               // 4:34 PM
        listener.initTotalBenchProgress(null);
        listener.initTotalBenchProgress(new HashMap<String, Integer>());
        listener.initTotalBenchProgress(initData);
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSessionListener#updateCurrentRun(String)}
     * .
     */
    @Test
    public void testUpdateCurrentRun() { // NOPMD by lewandow on 8/31/09 4:34 PM
        listener.updateCurrentRun(MY_BOBJECT);
        listener.initTotalBenchProgress(initData);
        listener.updateCurrentRun(MY_BOBJECT);
        listener.updateCurrentRun("Nothing");
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSessionListener#updateError(String)}
     * .
     */
    @Test
    public void testUpdateError() { // NOPMD by lewandow on 8/31/09 4:34 PM
        listener.updateError("MyBenchi", "ExceptionTest");
        listener.initTotalBenchProgress(initData);
        listener.updateError(MY_BOBJECT, "ExceptionTest");
        listener.updateError("Sometthin", "ExceptionTest");
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunSessionListener#finishedBenchRuns()}
     * .
     */
    @Test
    public void testFinishedBenchRuns() {

        listener.finishedBenchRuns();
        assertTrue("Test if finished successfully", listener.isFinished());

    }
}
