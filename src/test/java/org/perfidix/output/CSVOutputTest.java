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
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.CountingMeter;
import org.perfidix.ouput.CSVOutput;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;


/**
 * Testcase for CSVOutput.
 *
 * @author Sebastian Graf, University of Konstanz
 */
public final class CSVOutputTest {

    private final static int NUMBER_OF_TICKS = 10;

    private transient BenchmarkResult benchRes;

    private transient PrintStream consoleOut;

    private transient ByteArrayOutputStream bytes;

    private transient AbstractPerfidixMethodException testException;

    // private final static File TEST_FOLDER = new File("benchTest");

    /**
     * Simple setUp
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benchRes = new BenchmarkResult();

        final Class<?> class1 = Class1.class;

        final Method meth11 = class1.getDeclaredMethod("method1");
        final Method meth12 = class1.getDeclaredMethod("method2");

        final CountingMeter meter = new CountingMeter("Meter1");

        for (int i = 0; i < NUMBER_OF_TICKS; i++) {
            meter.tick();
            benchRes.addData(new BenchmarkMethod(meth11), meter, meter.getValue());
            benchRes.addData(new BenchmarkMethod(meth12), meter, meter.getValue() / 2);
        }

        testException = new PerfidixMethodInvocationException(new IOException(), Class1.class.getDeclaredMethod("method1"), Bench.class);

        benchRes.addException(testException);
        consoleOut = System.out;
        bytes = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bytes));

        // TEST_FOLDER.mkdir();
    }

    /**
     * Simple tearDown
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        System.setOut(consoleOut);
        // for (final File file : TEST_FOLDER.listFiles()) {
        // file.delete();
        // }
        // TEST_FOLDER.delete();
    }

    /**
     * Test method for {@link org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)} .
     */
    @Test
    public void testVisitSystemOut() {

        final CSVOutput output = new CSVOutput();
        output.visitBenchmark(benchRes);
        assertTrue("first bunch of must be the same", bytes.toString().contains("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0"));

        assertTrue("second bunch of must be the same", bytes.toString().contains("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0"));

        assertTrue("third bunch of must be the same", bytes.toString().contains("Bench:Class1#method1\njava.io.IOException"));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#listenToResultSet(org.perfidix.element.BenchmarkMethod, org.perfidix.meter.AbstractMeter, double)}.
     * .
     */
    @Test
    public void testListenSystemOut() {
        final ClassResult classRes = benchRes.getIncludedResults().iterator().next();
        final CSVOutput output = new CSVOutput();

        final AbstractMeter meter = classRes.getRegisteredMeters().iterator().next();
        for (final MethodResult methRes : classRes.getIncludedResults()) {

            for (final double d : methRes.getResultSet(meter)) {
                output.listenToResultSet((BenchmarkMethod) methRes.getRelatedElement(), meter, d);
            }
        }
        assertTrue("first bunch of in the test string", bytes.toString().contains("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0"));

        assertTrue("second bunch of in the test string", bytes.toString().contains("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0"));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#listenToException(org.perfidix.exceptions.AbstractPerfidixMethodException)} .
     */
    @Test
    public void testListenExceptionSystemOut() {

        final CSVOutput output = new CSVOutput();
        output.listenToException(testException);
        assertTrue("Testcase for exceptions", bytes.toString().startsWith("Bench,Class1#method1,java.io.IOException"));

    }

    /**
     * Test method for {@link org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)} .
     */
    @Test
    public void testVisitListenSystemOut() {
        final CSVOutput output = new CSVOutput();

        final ClassResult classRes = benchRes.getIncludedResults().iterator().next();
        final AbstractMeter meter = classRes.getRegisteredMeters().iterator().next();
        for (final MethodResult methRes : classRes.getIncludedResults()) {
            for (final double d : methRes.getResultSet(meter)) {
                output.listenToResultSet((BenchmarkMethod) methRes.getRelatedElement(), meter, d);
            }
        }
        output.visitBenchmark(benchRes);
        assertTrue("first bunch of numbers must be in the test string", bytes.toString().contains("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0"));

    }

    // /**
    // * Test method for
    // * {@link
    // org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)}
    // * .
    // *
    // * @throws IOException
    // * if file access was not valid
    // */
    // @Test
    // public void testVisitListenerFile() throws IOException {
    // final CSVOutput output = new CSVOutput(TEST_FOLDER);
    //
    // final ClassResult classRes =
    // benchRes.getIncludedResults().iterator().next();
    // final AbstractMeter meter =
    // classRes.getRegisteredMeters().iterator().next();
    // for (final MethodResult methRes : classRes.getIncludedResults()) {
    // for (final double d : methRes.getResultSet(meter)) {
    // output.listenToResultSet(
    // (Method) methRes.getRelatedElement(), meter, d);
    // }
    // }
    // output.visitBenchmark(benchRes);
    //
    // final StringBuilder asIsData = new StringBuilder();
    // for (final File file : TEST_FOLDER.listFiles()) {
    // final BufferedReader reader =
    // new BufferedReader(new FileReader(file));
    // String line = reader.readLine();
    // while (line != null) {
    // asIsData.append(line).append("\n");
    // line = reader.readLine();
    // }
    // }
    //
    // final StringBuilder builderData1 = new StringBuilder();
    // builderData1.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0\n");
    // assertTrue("Test for normal numbers as listener", asIsData
    // .toString().contains(builderData1.toString()));
    //
    // builderData1.append("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0\n");
    // assertTrue("Test for decimal numbers as listener", asIsData
    // .toString().contains(builderData1.toString()));
    //
    // builderData1.append("Bench:Class1#method1\njava.io.IOException");
    // assertTrue("Test for exceptions as listener", asIsData
    // .toString().contains(builderData1.toString()));
    //
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)}
    // *
    // * @throws IOException
    // */
    // @Test
    // public void testVisitFile() throws IOException {
    //
    // final CSVOutput output = new CSVOutput(TEST_FOLDER);
    // output.visitBenchmark(benchRes);
    //
    // final StringBuilder asIsData = new StringBuilder();
    //
    // for (final File file : TEST_FOLDER.listFiles()) {
    // final BufferedReader reader =
    // new BufferedReader(new FileReader(file));
    // String line = reader.readLine();
    // while (line != null) {
    // asIsData.append(line).append("\n");
    // line = reader.readLine();
    // }
    // }
    //
    // final StringBuilder builderData1 = new StringBuilder();
    // builderData1.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0");
    // assertTrue("Test for number as decimals", asIsData.toString().contains(
    // builderData1.toString()));
    //
    // final StringBuilder builderData2 = new StringBuilder();
    // builderData2.append("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0");
    // assertTrue("Test for decimals as visitor", asIsData
    // .toString().contains(builderData2.toString()));
    //
    // final StringBuilder builderException = new StringBuilder();
    // builderException.append("Bench:Class1#method1\njava.io.IOException");
    // assertTrue("Test for exception as visitor", asIsData
    // .toString().contains(builderException.toString()));
    //
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.perfidix.ouput.CSVOutput#listenToResultSet(java.lang.reflect.Method,
    // org.perfidix.meter.AbstractMeter, double)}
    // * .
    // *
    // * @throws IOException
    // * if read fails
    // */
    // @Test
    // public void testListenFile() throws IOException {
    // final ClassResult classRes =
    // benchRes.getIncludedResults().iterator().next();
    // final CSVOutput output = new CSVOutput(TEST_FOLDER);
    //
    // final AbstractMeter meter =
    // classRes.getRegisteredMeters().iterator().next();
    // for (final MethodResult methRes : classRes.getIncludedResults()) {
    //
    // for (final double d : methRes.getResultSet(meter)) {
    // output.listenToResultSet(
    // (Method) methRes.getRelatedElement(), meter, d);
    // }
    // }
    //
    // final StringBuilder asIsData = new StringBuilder();
    //
    // for (final File file : TEST_FOLDER.listFiles()) {
    // final BufferedReader reader =
    // new BufferedReader(new FileReader(file));
    // String line = reader.readLine();
    // while (line != null) {
    // asIsData.append(line).append("\n");
    // line = reader.readLine();
    // }
    // }
    //
    // final StringBuilder builderData1 = new StringBuilder();
    // builderData1.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0\n");
    // assertTrue("Test for numbers as pure listener", asIsData
    // .toString().contains(builderData1.toString()));
    //
    // builderData1.append("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0\n");
    // assertTrue("Test for decimals as pure listener", asIsData
    // .toString().contains(builderData1.toString()));
    //
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.perfidix.ouput.CSVOutput#listenToException(org.perfidix.exceptions.AbstractPerfidixMethodException)}
    // * .
    // *
    // * @throws IOException
    // * if read fails;
    // */
    // @Test
    // public void testListenExceptionFile() throws IOException {
    //
    // final CSVOutput output = new CSVOutput(TEST_FOLDER);
    // output.listenToException(testException);
    //
    // // Disabled
    // // assertEquals(
    // // "Test for number of files", 1, TEST_FOLDER.listFiles().length);
    //
    // final StringBuilder asIsData = new StringBuilder();
    //
    // final BufferedReader reader =
    // new BufferedReader(new FileReader(TEST_FOLDER.listFiles()[0]));
    // String line = reader.readLine();
    // while (line != null) {
    // asIsData.append(line).append("\n");
    // line = reader.readLine();
    // }
    //
    // assertTrue("Test for exception as pure listener", asIsData
    // .toString().startsWith(
    // "Bench,Class1#method1,java.io.IOException"));
    //
    // }

    private class Class1 {
        @Bench
        public void method1() {
            // empty skeleton
        }

        @Bench
        public void method2() {
            // empty skeleton
        }
    }

}
