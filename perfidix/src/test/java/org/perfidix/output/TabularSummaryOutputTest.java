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
import org.junit.Ignore;
import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.CountingMeter;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.MethodResult;

/**
 * Test case for {@link TabularSummaryOutput}
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class TabularSummaryOutputTest {

    private final static int NUMBEROFTICKS = 10;

    private BenchmarkResult benchRes;

    private PrintStream consoleOut;

    private ByteArrayOutputStream bytes;

    private AbstractPerfidixMethodException testException;

    /**
     * Simple Constructor.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benchRes = new BenchmarkResult();

        final Class< ? > class1 = new Class1().getClass();

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
     * Simple Constructor.
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
     * {@link org.perfidix.ouput.TabularSummaryOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)}
     * .
     */

    @Ignore
    @Test
    public final void testVisitBenchmark() {
        final TabularSummaryOutput output = new TabularSummaryOutput();
        output.visitBenchmark(benchRes);
        final StringBuilder builder = new StringBuilder();
        builder
                .append("|= Benchmark ======================================================================|\n");
        builder
                .append("| -       | unit  | sum   | min   | max   | avg   | stddev | conf95        | runs  |\n");
        builder
                .append("|===================================== Meter1 =====================================|\n");
        builder
                .append("|. Class1 .........................................................................|\n");
        builder
                .append("| method1 | ticks | 55,00 | 01,00 | 10,00 | 05,50 | 03,03  | [01,00-10,00] | 10,00 |\n");
        builder
                .append("|_ Summary for Class1 _____________________________________________________________|\n");
        builder
                .append("|         | ticks | 55,00 | 01,00 | 10,00 | 05,50 | 03,03  | [01,00-10,00] | 10,00 |\n");
        builder
                .append("|----------------------------------------------------------------------------------|\n");
        builder
                .append("|======================== Summary for the whole benchmark =========================|\n");
        builder
                .append("|         | ticks | 55,00 | 01,00 | 10,00 | 05,50 | 03,03  | [01,00-10,00] | 10,00 |\n");
        builder
                .append("|=================================== Exceptions ===================================|\n");
        builder
                .append("|  Related exception: IOException                                                  |\n");
        builder
                .append("|  Related place: method invocation                                                |\n");
        builder
                .append("|  Related method: method1                                                         |\n");
        builder
                .append("|  Related annotation: Bench                                                       |\n");
        builder
                .append("|----------------------------------------------------------------------------------|\n");
        builder
                .append("|==================================================================================|\n");
        assertTrue(bytes.toString().startsWith(builder.toString()));
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.TabularSummaryOutput#listenToResultSet(java.lang.reflect.Method, org.perfidix.meter.AbstractMeter, double)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testListenToResultSet() throws IOException {
        final MethodResult methRes =
                benchRes
                        .getIncludedResults().iterator().next()
                        .getIncludedResults().iterator().next();
        final AbstractMeter meter =
                methRes.getRegisteredMeters().iterator().next();
        final TabularSummaryOutput output = new TabularSummaryOutput();
        for (final double d : methRes.getResultSet(meter)) {
            output.listenToResultSet(
                    (Method) methRes.getRelatedElement(), meter, d);
        }
        final StringBuilder builder = new StringBuilder();

        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 1.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 2.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 3.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 4.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 5.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 6.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 7.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 8.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 9.0\n");
        builder.append("\n");
        builder.append("Class: Class1#method1\n");
        builder.append("Meter: Meter1\n");
        builder.append("Data: 10.0\n");
        builder.append("\n");

        assertEquals(builder.toString(), bytes.toString());
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

        final StringBuilder builder = new StringBuilder();
        builder.append("Class: Class1#method1\n");
        builder.append("Annotation: Bench\n");
        builder
                .append("Exception: PerfidixMethodInvocationException/java.io.IOException\n");
        builder.append("java.io.IOException\n");
        assertTrue(bytes.toString().startsWith(builder.toString()));
    }

    class Class1 {
        public void method1() {
        }

    }

}
