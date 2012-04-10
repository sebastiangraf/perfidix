/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
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
