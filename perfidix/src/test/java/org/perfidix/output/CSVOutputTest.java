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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
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
import org.perfidix.ouput.CSVOutput;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Testcase for CSVOutput.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class CSVOutputTest {

    private final static int NUMBER_OF_TICKS = 10;

    private BenchmarkResult benchRes;

    private PrintStream consoleOut;

    private ByteArrayOutputStream bytes;

    private AbstractPerfidixMethodException testException;

    private final static File TEST_FOLDER = new File("benchTest");

    /**
     * Simple setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benchRes = new BenchmarkResult();

        final Class< ? > class1 = new Class1().getClass();

        final Method meth11 = class1.getDeclaredMethod("method1");
        final Method meth12 = class1.getDeclaredMethod("method2");

        final CountingMeter meter = new CountingMeter("Meter1");

        for (int i = 0; i < NUMBER_OF_TICKS; i++) {
            meter.tick();
            benchRes.addData(meth11, meter, meter.getValue());
            benchRes.addData(meth12, meter, meter.getValue() / 2);
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

        TEST_FOLDER.mkdir();
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
        for (final File file : TEST_FOLDER.listFiles()) {
            file.delete();
        }
        TEST_FOLDER.delete();
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)}
     * .
     */
    @Test
    public final void testVisitSystemOut() {

        final CSVOutput output = new CSVOutput();
        output.visitBenchmark(benchRes);
        final StringBuilder builderData1 = new StringBuilder();
        builderData1.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0");
        assertTrue(bytes.toString().contains(builderData1.toString()));

        final StringBuilder builderData2 = new StringBuilder();
        builderData2.append("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0");
        assertTrue(bytes.toString().contains(builderData2.toString()));

        final StringBuilder builderException = new StringBuilder();
        builderException
                .append("Bench:Class1#method1\njava.io.IOException");
        assertTrue(bytes.toString().contains(builderException.toString()));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#listenToResultSet(java.lang.reflect.Method, org.perfidix.meter.AbstractMeter, double)}
     * .
     */
    @Test
    public final void testListenSystemOut() {
        final ClassResult classRes =
                benchRes.getIncludedResults().iterator().next();
        final CSVOutput output = new CSVOutput();

        final AbstractMeter meter =
                classRes.getRegisteredMeters().iterator().next();
        for (final MethodResult methRes : classRes.getIncludedResults()) {

            for (final double d : methRes.getResultSet(meter)) {
                output.listenToResultSet((Method) methRes
                        .getRelatedElement(), meter, d);
            }
        }
        final StringBuilder builderData1 = new StringBuilder();
        builderData1.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0");
        assertTrue(bytes.toString().contains(builderData1.toString()));

        final StringBuilder builderData2 = new StringBuilder();
        builderData2.append("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0");
        assertTrue(bytes.toString().contains(builderData2.toString()));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#listenToException(org.perfidix.exceptions.AbstractPerfidixMethodException)}
     * .
     * 
     * @throws Exception
     *             because of reflective invocation
     */
    @Test
    public final void testListenExceptionSystemOut() throws Exception {

        final CSVOutput output = new CSVOutput();
        output.listenToException(testException);
        final String beginString =
                "Bench,Class1#method1,java.io.IOException";
        assertTrue(bytes.toString().startsWith(beginString));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)}
     * .
     */
    @Test
    public final void testVisitListenSystemOut() {
        final CSVOutput output = new CSVOutput();

        final ClassResult classRes =
                benchRes.getIncludedResults().iterator().next();
        final AbstractMeter meter =
                classRes.getRegisteredMeters().iterator().next();
        for (final MethodResult methRes : classRes.getIncludedResults()) {
            for (final double d : methRes.getResultSet(meter)) {
                output.listenToResultSet((Method) methRes
                        .getRelatedElement(), meter, d);
            }
        }
        output.visitBenchmark(benchRes);
        final String data =
                "0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0";
        assertTrue(bytes.toString().contains(data));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)}
     * .
     * 
     * @throws Exception
     *             of any kind
     */
    @Test
    public final void testVisitListenerFile() throws Exception {
        final CSVOutput output = new CSVOutput(TEST_FOLDER);

        final ClassResult classRes =
                benchRes.getIncludedResults().iterator().next();
        final AbstractMeter meter =
                classRes.getRegisteredMeters().iterator().next();
        for (final MethodResult methRes : classRes.getIncludedResults()) {
            for (final double d : methRes.getResultSet(meter)) {
                output.listenToResultSet((Method) methRes
                        .getRelatedElement(), meter, d);
            }
        }
        output.visitBenchmark(benchRes);

        final StringBuilder asIsData = new StringBuilder();
        for (final File file : TEST_FOLDER.listFiles()) {
            final BufferedReader reader =
                    new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                asIsData.append(line).append("\n");
            }
        }

        final StringBuilder builderData1 = new StringBuilder();
        builderData1.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0\n");
        assertTrue(asIsData.toString().contains(builderData1.toString()));

        builderData1.append("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0\n");
        assertTrue(asIsData.toString().contains(builderData1.toString()));

        builderData1.append("Bench:Class1#method1\njava.io.IOException");
        assertTrue(asIsData.toString().contains(builderData1.toString()));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#visitBenchmark(org.perfidix.result.BenchmarkResult)}
     * 
     * @throws Exception
     *             of any kind .
     */
    @Test
    public final void testVisitFile() throws Exception {

        final CSVOutput output = new CSVOutput(TEST_FOLDER);
        output.visitBenchmark(benchRes);

        final StringBuilder asIsData = new StringBuilder();

        for (final File file : TEST_FOLDER.listFiles()) {
            final BufferedReader reader =
                    new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                asIsData.append(line).append("\n");
            }
        }

        final StringBuilder builderData1 = new StringBuilder();
        builderData1.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0");
        assertTrue(asIsData.toString().contains(builderData1.toString()));

        final StringBuilder builderData2 = new StringBuilder();
        builderData2.append("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0");
        assertTrue(asIsData.toString().contains(builderData2.toString()));

        final StringBuilder builderException = new StringBuilder();
        builderException
                .append("Bench:Class1#method1\njava.io.IOException");
        assertTrue(asIsData.toString().contains(
                builderException.toString()));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#listenToResultSet(java.lang.reflect.Method, org.perfidix.meter.AbstractMeter, double)}
     * .
     * 
     * @throws Exception
     *             of any kind
     */
    @Test
    public final void testListenFile() throws Exception {
        final ClassResult classRes =
                benchRes.getIncludedResults().iterator().next();
        final CSVOutput output = new CSVOutput(TEST_FOLDER);

        final AbstractMeter meter =
                classRes.getRegisteredMeters().iterator().next();
        for (final MethodResult methRes : classRes.getIncludedResults()) {

            for (final double d : methRes.getResultSet(meter)) {
                output.listenToResultSet((Method) methRes
                        .getRelatedElement(), meter, d);
            }
        }

        final StringBuilder asIsData = new StringBuilder();

        for (final File file : TEST_FOLDER.listFiles()) {
            final BufferedReader reader =
                    new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                asIsData.append(line).append("\n");
            }
        }

        final StringBuilder builderData1 = new StringBuilder();
        builderData1.append("1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0\n");
        assertTrue(asIsData.toString().contains(builderData1.toString()));

        builderData1.append("0.5,1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0\n");
        assertTrue(asIsData.toString().contains(builderData1.toString()));

    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.CSVOutput#listenToException(org.perfidix.exceptions.AbstractPerfidixMethodException)}
     * .
     * 
     * @throws Exception
     *             because of reflective invocation
     */
    @Test
    @Ignore
    public final void testListenExceptionFile() throws Exception {

        final CSVOutput output = new CSVOutput(TEST_FOLDER);
        output.listenToException(testException);
        final String beginString =
                "Bench,Class1#method1,java.io.IOException";

        assertEquals(4, TEST_FOLDER.listFiles().length);

        final StringBuilder asIsData = new StringBuilder();

        final BufferedReader reader =
                new BufferedReader(new FileReader(
                        TEST_FOLDER.listFiles()[0]));
        String line = null;
        while ((line = reader.readLine()) != null) {
            asIsData.append(line).append("\n");
        }

        assertTrue(asIsData.toString().startsWith(beginString));

    }

    class Class1 {
        public void method1() {
        }

        public void method2() {
        }
    }

}
