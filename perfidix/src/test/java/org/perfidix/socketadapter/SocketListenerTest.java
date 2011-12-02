/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.PerfidixMethodCheckException;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;

import static org.mockito.Mockito.mock;

/**
 * This class tests the java class
 * {@link org.perfidix.socketadapter.SocketListener}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class SocketListenerTest
{
    private transient SocketListener socketListener;
    // private transient SocketViewProgressUpdater viewUpdater;
    // private transient boolean socketFinished;
    // private transient PerclipseViewSkeletonSimulator skeletonSimulator;
    private transient IUpdater iUpdaterMock = mock(IUpdater.class);

    /**
     * Simple setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        /*
         * socketFinished = false; final int port = 8989; skeletonSimulator =
         * new PerclipseViewSkeletonSimulator(port); 
         * skeletonSimulator.start();
         * Thread.sleep(10); viewUpdater = new SocketViewProgressUpdater(null,
         *     port); 
         * socketListener = new SocketListener(viewUpdater); 
         */}

    /**
     * Simple tearDown
     * 
     * @throws SocketViewException
     * @throws InterruptedException
     * 
     */
    @After
    public void tearDown() throws SocketViewException, InterruptedException
    {
        /*
         * if (!socketFinished) { viewUpdater.finished(); Thread.sleep(10);
         * viewUpdater.finished(); }
         */}

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketListener#visitBenchmark(org.perfidix.result.BenchmarkResult)}
     * .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testVisitBenchmark()
    {
        socketListener = new SocketListener(iUpdaterMock);
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
    public void testListenToResultSet() throws InterruptedException
    {
        SocketListener myInstance = new SocketListener(iUpdaterMock);
        final Method[] methods = BenchWithException.class.getMethods();
        BenchmarkMethod method1 = null;
        for (Method method : methods)
        {
            if (method.getName().equals("benchMe"))
            {
                method1 = new BenchmarkMethod(method);
            }
        }
        assertTrue(myInstance.listenToResultSet(method1.getMethodToBench(),
                new TimeMeter(Time.MilliSeconds), 0));
        /*
         * Thread.sleep(10);
         * assertEquals("Tests if the sent item has been received",
         * "org.perfidix.socketadapter.BenchWithException.benchMe",
         * skeletonSimulator.getReceivedStringObject());
         */
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
    public void testListenToException() throws InterruptedException
    {
        SocketListener myInstance = new SocketListener(iUpdaterMock);
        ;
        final Method[] methods = BenchWithException.class.getMethods();
        BenchmarkMethod method1 = null;
        for (Method method : methods)
        {
            if (method.getName().equals("benchMe"))
            {
                method1 = new BenchmarkMethod(method);
            }
        }
        assertTrue(myInstance.listenToException(new PerfidixMethodCheckException(
                        new IOException(), method1.getMethodToBench(),
                        Bench.class)));
        /*
         * Thread.sleep(10);
         * assertNotNull("Checks if sent error method name has been received",
         * skeletonSimulator.getReceivedStringObject()); assertNotNull(
         * "Checks if the sent exception occurred has been reveived",
         * skeletonSimulator.getErrorStringObject());
         */
    }

}