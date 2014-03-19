/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.socketadapter;


// import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;


/**
 * This class tests the java class {@link org.perfidix.socketadapter.SocketListener}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */

public class SocketListenerTest {

    private transient SocketListener socketListener;

    private IUpdater updater;

    /**
     * Simple setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp () throws Exception {
        updater = mock(IUpdater.class);
        socketListener = new SocketListener(updater);
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketListener#visitBenchmark(org.perfidix.result.BenchmarkResult)} .
     */
    @Test (expected = UnsupportedOperationException.class)
    public void testVisitBenchmark () {
        socketListener = new SocketListener(updater);
        socketListener.visitBenchmark(null);
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketListener#listenToResultSet(java.lang.reflect.Method, org.perfidix.meter.AbstractMeter, double)}
     * .
     * 
     * @throws InterruptedException Thread sleep exception.
     * @throws SocketViewException
     */
    @Test
    public void testListenToResultSet () throws InterruptedException , SocketViewException {
        final Method[] methods = BenchWithException.class.getMethods();
        BenchmarkMethod method1 = null;
        for (Method method : methods) {
            if (method.getName().equals("benchMe")) {
                method1 = new BenchmarkMethod(method, null);
            }
        }
        final AbstractMeter meter = new TimeMeter(Time.MilliSeconds);
        final String methodName = method1.getMethodToBench().getDeclaringClass().getName() + "." + method1.getMethodToBench().getName();

        when(updater.updateCurrentElement(meter, methodName)).thenReturn(true);

        SocketListener myInstance = new SocketListener(updater);
        assertTrue(myInstance.listenToResultSet(method1, meter, 0));
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketListener#listenToException (org.perfidix.exceptions.AbstractPerfidixMethodException)}
     * .
     * 
     * @throws InterruptedException Thread exception occurred.
     */
    @Test
    public void testListenToException () throws InterruptedException , SocketViewException {
        final Method[] methods = BenchWithException.class.getMethods();
        BenchmarkMethod method1 = null;
        for (Method method : methods) {
            if (method.getName().equals("benchMe")) {
                method1 = new BenchmarkMethod(method, null);
            }
        }
        final AbstractMeter meter = new TimeMeter(Time.MilliSeconds);
        final String methodName = method1.getMethodToBench().getDeclaringClass().getName() + "." + method1.getMethodToBench().getName();

        when(updater.updateCurrentElement(meter, methodName)).thenReturn(true);

        SocketListener myInstance = new SocketListener(updater);
        assertTrue(myInstance.listenToResultSet(method1, meter, 0));

    }
}
