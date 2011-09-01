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
package org.perfidix.socketadapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.exceptions.SocketViewException;

/**
 * This class tests the java class
 * {@link org.perfidix.socketadapter.SocketViewStub}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class SocketViewStubTest {

    /** Convenient variable for testcase */
    private final static String NAMEFORDISPATCH = "some.Element";

    /**
     * The SocketViewStub instance to test its methods.
     */
    private transient SocketViewStub viewStub;
    /**
     * The PerclipseViewSkeletonSimulater instance is our dummy skeleton to test
     * the connection between the stub and the skeleton.
     */
    private transient PerclipseViewSkeletonSimulator skeletonSimulator;

    /**
     * The finished value shows if a connection finished and the socket is
     * closed.
     */
    private transient boolean finished = false;

    /**
     * Simple setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        skeletonSimulator = new PerclipseViewSkeletonSimulator(6363);
        skeletonSimulator.start();
        Thread.sleep(100);
        viewStub = new SocketViewStub(null, 6363);
    }

    /**
     * Simple tearDown
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        if (!finished) {
            viewStub.finishedBenchRuns();
            Thread.sleep(10);
        }
        finished = false;
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#SocketViewStub(java.lang.String, int)}
     * .
     */
    @Test
    public void testPerclipseViewStub() {
        assertNotNull("Test if instance is not null", viewStub);
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#initTotalBenchProgress(java.util.Map)}
     * .
     * 
     * @throws InterruptedException
     * @throws SocketViewException
     */
    @Test
    public void testInitTotalBenchProgress()
            throws InterruptedException, SocketViewException {
        final Map<String, Integer> dataForView = new HashMap<String, Integer>();
        dataForView.put("org.some.method", 55);
        dataForView.put("blue.some.other", 88);
        viewStub.initTotalBenchProgress(dataForView);
        Thread.sleep(10);
        assertEquals(
                "Checks if both objects, the input and the dispatched, are equals.",
                dataForView, skeletonSimulator.getTheMap());
        viewStub.initTotalBenchProgress(null);
        Thread.sleep(10);
        assertEquals(
                "Checks if both objects, the input and the dispatched, are equals.",
                null, skeletonSimulator.getTheMap());
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#updateCurrentRun(java.lang.String)}
     * .
     * 
     * @throws InterruptedException
     *             exception occurrred.
     * @throws SocketViewException
     *             exception occurred.
     */
    @Test
    public void testUpdateCurrentRun()
            throws InterruptedException, SocketViewException {

        viewStub.updateCurrentRun(NAMEFORDISPATCH);
        Thread.sleep(10);
        assertEquals(
                "Tests if the sent and the reveived object are equal",
                NAMEFORDISPATCH, skeletonSimulator.getReceivedStringObject());
        viewStub.updateCurrentRun(null);
        Thread.sleep(10);
        assertEquals(
                "Tests if the sent one and the received one are equal", null,
                skeletonSimulator.getReceivedStringObject());
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#updateError(java.lang.String, java.lang.String)}
     * .
     * 
     * @throws InterruptedException
     * @throws SocketViewException
     */
    @Test
    public void testUpdateError()
            throws InterruptedException, SocketViewException {
        viewStub.updateError(NAMEFORDISPATCH, "aException");
        Thread.sleep(10);
        assertEquals(
                "Test if the sent name and the received are equal.",
                NAMEFORDISPATCH, skeletonSimulator.getReceivedStringObject());
        assertEquals(
                "Test if the sent exception name and the received one are equal.",
                "aException", skeletonSimulator.getErrorStringObject());
        viewStub.updateError(null, null);
        Thread.sleep(10);
        assertEquals(
                "Test if the null objects are on both sides.", null,
                skeletonSimulator.getReceivedStringObject());
        assertEquals(
                "Test if sent null is equal to the received one", null,
                skeletonSimulator.getErrorStringObject());
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#finishedBenchRuns()} .
     * 
     * @throws InterruptedException
     * @throws SocketViewException
     */
    @Test
    public void testFinishedBenchRuns()
            throws InterruptedException, SocketViewException {
        viewStub.finishedBenchRuns();
        Thread.sleep(10);
        assertTrue("Checks if connection has been closed", skeletonSimulator
                .isFinsihed());
        finished = true;
    }

    /**
     * This method tests
     * {@link org.perfidix.socketadapter.SocketViewStub#SocketViewStub(java.lang.String, int)}
     * for exceptions.
     * 
     * @throws SocketViewException
     *             the expected exception.
     */

    @Test(expected = SocketViewException.class)
    public void testExceptionConstructor() throws SocketViewException {
        viewStub = new SocketViewStub("Ahost", 6363);
        fail();
    }

}
