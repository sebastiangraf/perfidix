/*
 * Copyright 2008 Distributed Systems Group, University of Konstanz
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
package org.perfidix.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.meter.CountingMeter;

/**
 * Test class for the whole package of the results.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class ResultContainerTest {

    private final static int NUMBEROFTICKS = 10;
    private final static int TICKFACTOR = 2;

    private BenchmarkResult benchRes;

    private ClassResult classRes1;
    private ClassResult classRes2;

    private MethodResult methodRes11;
    private MethodResult methodRes12;

    private MethodResult methodRes21;
    private MethodResult methodRes22;

    private CountingMeter meter;

    private Exception testException;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        benchRes = new BenchmarkResult();

        testException = new IOException();

        final Class< ? > class1 = new Class1().getClass();
        final Class< ? > class2 = new Class2().getClass();

        final Method meth11 = class1.getDeclaredMethod("method1");

        meter = new CountingMeter("Meter1");

        for (int i = 0; i < NUMBEROFTICKS; i++) {
            meter.tick();
            benchRes.addData(meth11, meter, meter.getValue());
        }

        final Method meth12 = class1.getDeclaredMethod("method2");

        for (int i = 0; i < NUMBEROFTICKS * TICKFACTOR; i++) {
            meter.tick();
            benchRes.addData(meth12, meter, meter.getValue());
        }

        final Method meth21 = class2.getDeclaredMethod("method1");

        for (int i = 0; i < NUMBEROFTICKS * TICKFACTOR * TICKFACTOR; i++) {
            meter.tick();
            benchRes.addData(meth21, meter, meter.getValue());
        }

        final Method meth22 = class2.getDeclaredMethod("method2");
        for (int i = 0; i < NUMBEROFTICKS
                * TICKFACTOR
                * TICKFACTOR
                * TICKFACTOR; i++) {
            meter.tick();
            benchRes.addData(meth22, meter, meter.getValue());
        }

        classRes1 = benchRes.getResultForObject(class1);
        classRes2 = benchRes.getResultForObject(class2);

        methodRes11 = classRes1.getResultForObject(meth11);
        methodRes12 = classRes1.getResultForObject(meth12);

        methodRes21 = classRes2.getResultForObject(meth21);
        methodRes22 = classRes2.getResultForObject(meth22);

        benchRes.addException(new PerfidixMethodInvocationException(
                testException, meth11, Bench.class));

    }

    /**
     * Test method for
     * {@link org.perfidix.result.BenchmarkResult#addException(org.perfidix.exceptions.AbstractPerfidixMethodException)}
     * and {@link org.perfidix.result.BenchmarkResult#getExceptions()} .
     */
    @Test
    public void testResultWithException() {
        assertTrue(benchRes.getExceptions().contains(
                new PerfidixMethodInvocationException(
                        testException, (Method) methodRes11
                                .getRelatedElement(), Bench.class)));
    }

    /**
     * Test method for {@link org.perfidix.result.MethodResult} .
     */
    @Test
    public void testMethodResults() {

        assertEquals(5.5, methodRes11.mean(meter), 0);
        assertEquals(1.0, methodRes11.min(meter), 0);
        assertEquals(10.0, methodRes11.max(meter), 0);
        assertEquals(1.0, methodRes11.getConf05(meter), 0);
        assertEquals(10.0, methodRes11.getConf95(meter), 0.00001);
        assertEquals(3.0276503540974917, methodRes11
                .getStandardDeviation(meter), 0.000001);
        assertEquals(55.0, methodRes11.sum(meter), 0);
        assertEquals(385.0, methodRes11.squareSum(meter), 0);
        assertEquals(10, methodRes11.getNumberOfResult(meter));

        assertEquals(20.5, methodRes12.mean(meter), 0);
        assertEquals(11.0, methodRes12.min(meter), 0);
        assertEquals(30.0, methodRes12.max(meter), 0);
        assertEquals(11.05, methodRes12.getConf05(meter), 0);
        assertEquals(29.95, methodRes12.getConf95(meter), 0.00001);
        assertEquals(5.916079783099616, methodRes12
                .getStandardDeviation(meter), 0.000001);
        assertEquals(410.0, methodRes12.sum(meter), 0);
        assertEquals(9070.0, methodRes12.squareSum(meter), 0);
        assertEquals(20, methodRes12.getNumberOfResult(meter));

        assertEquals(50.5, methodRes21.mean(meter), 0);
        assertEquals(31.0, methodRes21.min(meter), 0);
        assertEquals(70.0, methodRes21.max(meter), 0);
        assertEquals(32.05, methodRes21.getConf05(meter), 0);
        assertEquals(68.95, methodRes21.getConf95(meter), 0);
        assertEquals(11.69045194450012, methodRes21
                .getStandardDeviation(meter), 0.000001);
        assertEquals(2020.0, methodRes21.sum(meter), 0);
        assertEquals(107340.0, methodRes21.squareSum(meter), 0);
        assertEquals(40, methodRes21.getNumberOfResult(meter));

        assertEquals(110.5, methodRes22.mean(meter), 0);
        assertEquals(71.0, methodRes22.min(meter), 0);
        assertEquals(150.0, methodRes22.max(meter), 0);
        assertEquals(74.05, methodRes22.getConf05(meter), 0);
        assertEquals(146.95, methodRes22.getConf95(meter), 0.00001);
        assertEquals(23.2379000772445, methodRes22
                .getStandardDeviation(meter), 0.000001);
        assertEquals(8840.0, methodRes22.sum(meter), 0);
        assertEquals(1019480.0, methodRes22.squareSum(meter), 0);
        assertEquals(80, methodRes22.getNumberOfResult(meter));

    }

    /**
     * Test method for {@link org.perfidix.result.ClassResult} .
     */
    @Test
    public void testClassResults() {
        assertEquals(15.5, classRes1.mean(meter), 0);
        assertEquals(1.0, classRes1.min(meter), 0);
        assertEquals(30.0, classRes1.max(meter), 0);
        assertEquals(1.55, classRes1.getConf05(meter), 0);
        assertEquals(29.45, classRes1.getConf95(meter), 0);
        assertEquals(8.803408430829505, classRes1
                .getStandardDeviation(meter), 0.000001);
        assertEquals(465.0, classRes1.sum(meter), 0);
        assertEquals(9455.0, classRes1.squareSum(meter), 0);
        assertEquals(30, classRes1.getNumberOfResult(meter), 0);

        assertEquals(90.5, classRes2.mean(meter), 0);
        assertEquals(31.0, classRes2.min(meter), 0);
        assertEquals(150.0, classRes2.max(meter), 0);
        assertEquals(36.05, classRes2.getConf05(meter), 0);
        assertEquals(144.95, classRes2.getConf95(meter), 0);
        assertEquals(34.785054261852174, classRes2
                .getStandardDeviation(meter), 0.000001);
        assertEquals(10860.0, classRes2.sum(meter), 0);
        assertEquals(1126820.0, classRes2.squareSum(meter), 0);
        assertEquals(120, classRes2.getNumberOfResult(meter), 0);
    }

    /**
     * Test method for {@link org.perfidix.result.BenchmarkResult} .
     */
    @Test
    public void testBenchmarkResults() {

        assertEquals(75.5, benchRes.mean(meter), 0);
        assertEquals(1.0, benchRes.min(meter), 0);
        assertEquals(150.0, benchRes.max(meter), 0);
        assertEquals(7.55, benchRes.getConf05(meter), 0);
        assertEquals(143.45, benchRes.getConf95(meter), 0);
        assertEquals(43.445367992456916, benchRes
                .getStandardDeviation(meter), 0.000001);
        assertEquals(11325.0, benchRes.sum(meter), 0);
        assertEquals(1136275.0, benchRes.squareSum(meter), 0);
        assertEquals(150, benchRes.getNumberOfResult(meter));

    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        meter = null;
        benchRes = null;
        classRes1 = null;
        classRes2 = null;
        methodRes11 = null;
        methodRes12 = null;
        methodRes21 = null;
        methodRes22 = null;

    }

    class Class1 {
        public void method1() {
        }

        public void method2() {
        }
    }

    class Class2 {
        public void method1() {
        }

        public void method2() {
        }
    }

}
