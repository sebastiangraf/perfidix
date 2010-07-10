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
package org.perfidix.socketadapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.PerfidixMethodCheckException;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;

/**
 * This class tests the java class
 * {@link org.perfidix.socketadapter.SocketListener}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class SocketListenerTest {
    private transient SocketListener socketListener;
    private transient SocketViewProgressUpdater viewUpdater;
    private transient boolean socketFinished;
    private transient PerclipseViewSkeletonSimulator skeletonSimulator;

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
        socketListener = new SocketListener(viewUpdater);
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
     * {@link org.perfidix.socketadapter.SocketListener#visitBenchmark(org.perfidix.result.BenchmarkResult)}
     * .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testVisitBenchmark() {
        socketListener.visitBenchmark(null);
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketListener#listenToResultSet(java.lang.reflect.Method, org.perfidix.meter.AbstractMeter, double)}
     * .
     * 
     * @throws InterruptedException
     *             Thread sleep exception.
     */
    @Test
    public void testListenToResultSet() throws InterruptedException {
        final Method[] methods = BenchWithException.class.getMethods();
        BenchmarkMethod method1 = null;
        for (Method method : methods) {
            if (method.getName().equals("benchMe")) {
                method1 = new BenchmarkMethod(method);
            }
        }
        socketListener
                .listenToResultSet(method1.getMethodToBench(), new TimeMeter(
                        Time.MilliSeconds), 0);
        Thread.sleep(10);
        assertEquals(
                "Tests if the sent item has been received",
                "org.perfidix.socketadapter.BenchWithException.benchMe",
                skeletonSimulator.getReceivedStringObject());
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketListener#listenToException(org.perfidix.exceptions.AbstractPerfidixMethodException)}
     * .
     * 
     * @throws InterruptedException
     *             Thread exception occurred.
     */
    @Test
    public void testListenToException() throws InterruptedException {
        final Method[] methods = BenchWithException.class.getMethods();
        BenchmarkMethod method1 = null;
        for (Method method : methods) {
            if (method.getName().equals("benchMe")) {
                method1 = new BenchmarkMethod(method);
            }
        }
        socketListener.listenToException(new PerfidixMethodCheckException(
                new IOException(), method1.getMethodToBench(), Bench.class));
        Thread.sleep(10);
        assertNotNull(
                "Checks if sent error method name has been received",
                skeletonSimulator.getReceivedStringObject());
        assertNotNull(
                "Checks if the sent exception occurred has been reveived",
                skeletonSimulator.getErrorStringObject());
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketListener#SocketListener(org.perfidix.socketadapter.SocketViewProgressUpdater)}
     * .
     */
    @Test
    public void testSocketListener() {
        assertNotNull("Test object if it is null", socketListener);
    }
}
