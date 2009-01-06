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
 * $Revision$
 * $Author$
 * $Date$
 *
 */
package org.perfidix.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.failureHandling.PerfidixMethodException;
import org.perfidix.failureHandling.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.CountingMeter;
import org.perfidix.ouput.CSVOutput;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.MethodResult;

/**
 * Testcase for CSVOutput.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class CSVOutputTest {

    private final static int NUMBEROFTICKS = 10;

    private BenchmarkResult benchRes;

    private PrintStream consoleOut;

    private ByteArrayOutputStream bytes;

    private PerfidixMethodException testException;

    /**
     * Simple setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benchRes = new BenchmarkResult();

        final Class<?> class1 = new Class1().getClass();

        final Method meth11 = class1.getDeclaredMethod("method1");

        final CountingMeter meter = new CountingMeter("Meter1");

        for (int i = 0; i < NUMBEROFTICKS; i++) {
            meter.tick();
            benchRes.addData(meth11, meter, meter.getValue());
        }

        testException =
                new PerfidixMethodInvocationException(
                        new IOException(), new Class1()
                                .getClass().getDeclaredMethod("method1"),
                        Bench.class);

        benchRes.addException(testException);
        consoleOut = System.out;
        bytes = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bytes));
    }

    /**
     * Simple tearDown
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        benchRes = null;
        System.setOut(consoleOut);
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)}
     * .
     */
    @Test
    public final void testVisitBenchmark() {

        final CSVOutput output = new CSVOutput();
        output.visitBenchmark(benchRes);
        final StringBuilder builder = new StringBuilder();
        builder.append("\n1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0\n");
        builder
                .append("org.perfidix.annotation.Bench:org.perfidix.output.CSVOutputTest.Class1$method1\njava.io.IOException\n");
        assertTrue(bytes.toString().startsWith(builder.toString()));
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#listenToResultSet(java.lang.reflect.Method, org.perfidix.meter.AbstractMeter, double)}
     * .
     */
    @Test
    public final void testListenToResultSet() {
        final MethodResult methRes =
                benchRes
                        .getIncludedResults().iterator().next()
                        .getIncludedResults().iterator().next();
        final AbstractMeter meter =
                methRes.getRegisteredMeters().iterator().next();
        final CSVOutput output = new CSVOutput();
        for (final double d : methRes.getResultSet(meter)) {
            output.listenToResultSet(
                    (Method) methRes.getRelatedElement(), meter, d);
        }
        final StringBuilder builder = new StringBuilder();
        builder.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0");
        assertEquals(builder.toString(), bytes.toString());
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#listenToException(org.perfidix.failureHandling.PerfidixMethodException)}
     * .
     * 
     * @throws Exception
     *             because of reflective invocation
     */
    @Test
    public final void testListenToException() throws Exception {

        final CSVOutput output = new CSVOutput();
        output.listenToException(testException);
        final String beginString =
                new String(
                        "org.perfidix.annotation.Bench:org.perfidix.output.CSVOutputTest.Class1$method1\njava.io.IOException\n");
        assertTrue(bytes.toString().startsWith(beginString));

    }

    class Class1 {
        public void method1() {
        }

    }

}
