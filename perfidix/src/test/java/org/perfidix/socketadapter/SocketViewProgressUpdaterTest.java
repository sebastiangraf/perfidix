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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.meter.TimeMeter;

/**
 * This class tests the java class
 * {@link org.perfidix.socketadapter.SocketViewProgressUpdater}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class SocketViewProgressUpdaterTest {
    transient private SocketViewProgressUpdater viewUpdater;
    transient private PerclipseViewSkeletonSimulator skeletonSimulator;
    transient private boolean socketFinished;
    transient private Map<BenchmarkMethod, Integer> dataForView;

    /**
     * Simple setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        socketFinished = false;
        final int port = 8989;
        skeletonSimulator = new PerclipseViewSkeletonSimulator(port);
        skeletonSimulator.start();
        Thread.sleep(10);
        viewUpdater = new SocketViewProgressUpdater(null, port);
        dataForView = new HashMap<BenchmarkMethod, Integer>();
        final Method[] methods = BenchWithException.class.getMethods();
        BenchmarkMethod method1 = null;
        for (Method method : methods) {
            if (method.getName().equals("benchMe")) {
                method1 = new BenchmarkMethod(method);
            }
        }
        dataForView.put(method1, 45);

    }

    /**
     * Simple tearDown
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        if (!socketFinished) {
            viewUpdater.finished();
            Thread.sleep(10);
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewProgressUpdater#SocketViewProgressUpdater(java.lang.String, int)}
     * .
     */
    @Test
    public void testSocketViewProgressUpdater() {
        assertNotNull("Test if not null", skeletonSimulator);
        assertNotNull("Is object not null", viewUpdater);
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewProgressUpdater#initProgressView(java.util.Map)}
     * .
     * 
     * @throws InterruptedException
     *             Thread exception.
     */
    @Test
    public void testInitProgressView() throws InterruptedException {
        try {
            viewUpdater.initProgressView(dataForView);
            final Map<String, Integer> sentData =
                    new HashMap<String, Integer>();
            sentData
                    .put(
                            "org.perfidix.socketadapter.BenchWithException.benchMe",
                            45);
            Thread.sleep(10);
            assertEquals(
                    "Checks if the sent data is equal to the received",
                    skeletonSimulator.getTheMap(), sentData);

        } catch (SocketViewException e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewProgressUpdater#updateCurrentElement(org.perfidix.meter.AbstractMeter, String)}
     * .
     * 
     * @throws InterruptedException
     *             Thread exception.
     */
    @Test
    public void testUpdateCurrentElement() throws InterruptedException {
        try {
            viewUpdater.initProgressView(dataForView);
            final String sentItem =
                    "org.perfidix.socketadapter.BenchWithException.benchMe";
            viewUpdater.updateCurrentElement(new TimeMeter(
                    org.perfidix.meter.Time.MilliSeconds), sentItem);
            Thread.sleep(10);
            assertEquals(
                    "Test if sent string is the same that has been received",
                    sentItem, skeletonSimulator.getReceivedStringObject());
        } catch (SocketViewException e) {
            fail(e.toString());
        }

    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewProgressUpdater#updateErrorInElement(java.lang.String, java.lang.Exception)}
     * .
     * 
     * @throws InterruptedException
     *             Thread exception.
     */
    @Test
    public void testUpdateErrorInElement() throws InterruptedException {
        try {
            viewUpdater.initProgressView(dataForView);
            final String errorItem =
                    "org.perfidix.socketadapter.BenchWithException.benchMe";
            viewUpdater.updateErrorInElement(
                    errorItem, new SocketViewException(new IOException()));
            Thread.sleep(10);
            final IOException exce = new IOException();
            viewUpdater.updateErrorInElement(errorItem, exce);
            Thread.sleep(10);
            assertEquals(
                    "Test if sent errorItem string is the same that has been received",
                    errorItem, skeletonSimulator.getReceivedStringObject());
            assertEquals(
                    "Test if sent exceptino string is the same as received",
                    exce.getClass().getSimpleName(), skeletonSimulator
                            .getErrorStringObject());
        } catch (SocketViewException e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewProgressUpdater#finished()}.
     * 
     * @throws InterruptedException
     *             Thread exception.
     */
    @Test
    public void testFinished() throws InterruptedException {
        try {
            viewUpdater.finished();
            socketFinished = true;
            Thread.sleep(10);
            assertTrue(
                    "Test to see if the socket has been closed on server side",
                    skeletonSimulator.isFinsihed());
        } catch (SocketViewException e) {
            fail(e.toString());
        }
    }
}
