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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Locale;

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


/**
 * Test case for {@link TabularSummaryOutput}
 *
 * @author Sebastian Graf, University of Konstanz
 */
public class TabularSummaryOutputTest {
	private final String LINE_SEPARATOR = System.lineSeparator();

    private final String CLASSSTRING = MessageFormat.format("Class: Class1#method1{0}", LINE_SEPARATOR);
    private final String METERSTRING =  MessageFormat.format("Meter: Meter1{0}", LINE_SEPARATOR);

    private final static int NUMBEROFTICKS = 10;

    private transient BenchmarkResult benchRes;

    private transient PrintStream consoleOut;

    private transient ByteArrayOutputStream bytes;

    private transient AbstractPerfidixMethodException testException;

	private String TEMPLATE_EXPECTED_LISTEN_TO_RESULT_SET;
	private String TEMPLATE_EXPECTED_LISTEN_TO_EXCEPTION;
    

    /**
     * Simple Constructor.
     *
     * @throws NoSuchMethodException        if declaration fails
     * @throws SecurityException            if declaration fails
     * @throws PerfidixMethodCheckException
     */
    @Before
    public void setUp() throws SecurityException, NoSuchMethodException, PerfidixMethodCheckException {
    	Locale.setDefault(Locale.ENGLISH);
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
        
        buildTemplatesListen();
    }
    
    private void buildTemplatesListen() {
    	StringBuilder builderPatterExpected =  new StringBuilder();
        
        for(double data = 1; data < 11 ;data++) {
        	builderPatterExpected.append(MessageFormat.format("{1}{2}Data: {3}{0}{0}", LINE_SEPARATOR, CLASSSTRING, METERSTRING, String.valueOf(data)));
        }
        
        TEMPLATE_EXPECTED_LISTEN_TO_RESULT_SET = builderPatterExpected.toString();
        TEMPLATE_EXPECTED_LISTEN_TO_EXCEPTION = MessageFormat.format("Class: Class1#method1{0}Annotation: Bench{0}Exception: PerfidixMethodInvocationException/java.io.IOException{0}java.io.IOException{0}", LINE_SEPARATOR);
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
    	final String expected = MessageFormat.format("|= Benchmark ======================================================================|{0}| -       | unit  | sum   | min   | max   | avg   | stddev | conf95        | runs  |{0}|===================================== Meter1 =====================================|{0}|. Class1 .........................................................................|{0}| method1 | ticks | 55.00 | 01.00 | 10.00 | 05.50 | 03.03  | [01.00-10.00] | 10.00 |{0}|_ Summary for Class1 _____________________________________________________________|{0}|         | ticks | 55.00 | 01.00 | 10.00 | 05.50 | 03.03  | [01.00-10.00] | 10.00 |{0}|----------------------------------------------------------------------------------|{0}|======================== Summary for the whole benchmark =========================|{0}|         | ticks | 55.00 | 01.00 | 10.00 | 05.50 | 03.03  | [01.00-10.00] | 10.00 |{0}|=================================== Exceptions ===================================|{0}|  Related exception: IOException                                                  |{0}|  Related place: method invocation                                                |{0}|  Related method: method1                                                         |{0}|  Related annotation: Bench                                                       |{0}|----------------------------------------------------------------------------------|{0}|==================================================================================|{0}{0}", LINE_SEPARATOR);
        final TabularSummaryOutput output = new TabularSummaryOutput();
        output.visitBenchmark(benchRes);
        final String result = bytes.toString();
        
        assertTrue("Complete Output check", result.startsWith(expected));
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
        final String result = bytes.toString();
        
        assertEquals("Complete listener test", TEMPLATE_EXPECTED_LISTEN_TO_RESULT_SET, result);
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
        final String result = bytes.toString();
        
        assertTrue("Exception listener test", result.startsWith(TEMPLATE_EXPECTED_LISTEN_TO_EXCEPTION));
    }

    private class Class1 {
        @Bench
        public void method1() {
            // Simple skeleton
        }

    }
}
