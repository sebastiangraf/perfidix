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
package org.perfidix.output;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.exceptions.PerfidixMethodCheckException;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.CountingMeter;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.MethodResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test case for {@link TabularSummaryOutput}
 *
 * @author Sebastian Graf, University of Konstanz
 */
public class TabularSummaryOutputTest {

    private final static String CLASSSTRING = "Class: Class1#method1\n";
    private final static String METERSTRING = "Meter: Meter1\n";

    private final static int NUMBEROFTICKS = 10;

    private transient BenchmarkResult benchRes;

    private transient PrintStream consoleOut;

    private transient ByteArrayOutputStream bytes;

    private transient AbstractPerfidixMethodException testException;

    /**
     * Simple Constructor.
     *
     * @throws NoSuchMethodException        if declaration fails
     * @throws SecurityException            if declaration fails
     * @throws PerfidixMethodCheckException
     */
    @Before
    public void setUp() throws SecurityException, NoSuchMethodException, PerfidixMethodCheckException {
        benchRes = new BenchmarkResult();

        final Class<?> class1 = Class1.class;

        final Method meth11 = class1.getDeclaredMethod("method1");

        final CountingMeter meter = new CountingMeter("Meter1");

        for (int i = 0; i < NUMBEROFTICKS; i++) {
            meter.tick();
            benchRes.addData(new BenchmarkMethod(meth11), meter, meter.getValue());
        }

        testException = new PerfidixMethodInvocationException(new IOException(), Class1.class.getDeclaredMethod("method1"), Bench.class);

        benchRes.addException(testException);
        consoleOut = System.out;
        bytes = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bytes));
    }

    /**
     * Simple Constructor.
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        System.setOut(consoleOut);
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.TabularSummaryOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)} .
     */
    @Test
    public final void testVisitBenchmark() {
        final TabularSummaryOutput output = new TabularSummaryOutput();
        output.visitBenchmark(benchRes);
        final String result = bytes.toString();
        assertTrue("Complete Output check", result.startsWith("|= Benchmark ======================================================================|\n" + "| -       | unit  | sum   | min   | max   | avg   | stddev | conf95        | runs  |\n" + "|===================================== Meter1 =====================================|\n" + "|. Class1 .........................................................................|\n" + "| method1 | ticks | 55.00 | 01.00 | 10.00 | 05.50 | 03.03  | [01.00-10.00] | 10.00 |\n" + "|_ Summary for Class1 _____________________________________________________________|\n" + "|         | ticks | 55.00 | 01.00 | 10.00 | 05.50 | 03.03  | [01.00-10.00] | 10.00 |\n" + "|----------------------------------------------------------------------------------|\n" + "|======================== Summary for the whole benchmark =========================|\n" + "|         | ticks | 55.00 | 01.00 | 10.00 | 05.50 | 03.03  | [01.00-10.00] | 10.00 |\n" + "|=================================== Exceptions ===================================|\n" + "|  Related exception: IOException                                                  |\n" + "|  Related place: method invocation                                                |\n" + "|  Related method: method1                                                         |\n" + "|  Related annotation: Bench                                                       |\n" + "|----------------------------------------------------------------------------------|\n" + "|==================================================================================|\n"));
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.TabularSummaryOutput#listenToResultSet(org.perfidix.element.BenchmarkMethod, org.perfidix.meter.AbstractMeter, double)}.
     * .
     *
     * @throws IOException
     */
    @Test
    public final void testListenToResultSet() throws IOException {

        final MethodResult methRes = benchRes.getIncludedResults().iterator().next().getIncludedResults().iterator().next();
        final AbstractMeter meter = methRes.getRegisteredMeters().iterator().next();
        final TabularSummaryOutput output = new TabularSummaryOutput();
        for (final double d : methRes.getResultSet(meter)) {
            output.listenToResultSet((BenchmarkMethod) methRes.getRelatedElement(), meter, d);
        }

        assertEquals("Complete listener test", CLASSSTRING + METERSTRING + "Data: 1.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 2.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 3.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 4.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 5.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 6.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 7.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 8.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 9.0\n" + "\n" + CLASSSTRING + METERSTRING + "Data: 10.0\n" + "\n", bytes.toString());
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.TabularSummaryOutput#listenToException(org.perfidix.exceptions.AbstractPerfidixMethodException)}
     * .
     */
    @Test
    public final void testListenToException() {
        final TabularSummaryOutput output = new TabularSummaryOutput();
        output.listenToException(testException);

        assertTrue("Exception listener test", bytes.toString().startsWith("Class: Class1#method1\n" + "Annotation: Bench\n" + "Exception: PerfidixMethodInvocationException/java.io.IOException\n" + "java.io.IOException\n"));
    }

    private class Class1 {
        @Bench
        public void method1() {
            // Simple skeleton
        }

    }

}
