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
package org.perfidix.result;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.element.BenchmarkMethod;
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

    private transient BenchmarkResult benchRes;

    private transient ClassResult classRes1;
    private transient ClassResult classRes2;

    private transient MethodResult methodRes11;
    private transient MethodResult methodRes12;

    private transient MethodResult methodRes21;
    private transient MethodResult methodRes22;

    private transient CountingMeter meter;

    private transient Exception testException;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp () throws Exception {

        benchRes = new BenchmarkResult();

        testException = new IOException();

        final Class<?> class1 = Class1.class;
        final Class<?> class2 = Class2.class;

        final BenchmarkMethod meth11 = new BenchmarkMethod(class1.getDeclaredMethod("method1"), null);

        meter = new CountingMeter("Meter1");

        for (int i = 0; i < NUMBEROFTICKS; i++) {
            meter.tick();
            benchRes.addData(meth11, meter, meter.getValue());
        }

        final BenchmarkMethod meth12 = new BenchmarkMethod(class1.getDeclaredMethod("method2"), null);

        for (int i = 0; i < NUMBEROFTICKS * TICKFACTOR; i++) {
            meter.tick();
            benchRes.addData(meth12, meter, meter.getValue());
        }

        final BenchmarkMethod meth21 = new BenchmarkMethod(class2.getDeclaredMethod("method1"), null);

        for (int i = 0; i < NUMBEROFTICKS * TICKFACTOR * TICKFACTOR; i++) {
            meter.tick();
            benchRes.addData(meth21, meter, meter.getValue());
        }

        final BenchmarkMethod meth22 = new BenchmarkMethod(class2.getDeclaredMethod("method2"), null);
        for (int i = 0; i < NUMBEROFTICKS * TICKFACTOR * TICKFACTOR * TICKFACTOR; i++) {
            meter.tick();
            benchRes.addData(meth22, meter, meter.getValue());
        }

        classRes1 = benchRes.getResultForObject(class1);
        classRes2 = benchRes.getResultForObject(class2);

        methodRes11 = classRes1.getResultForObject(meth11);
        methodRes12 = classRes1.getResultForObject(meth12);

        methodRes21 = classRes2.getResultForObject(meth21);
        methodRes22 = classRes2.getResultForObject(meth22);

        benchRes.addException(new PerfidixMethodInvocationException(testException, meth11.getMethodToBench(), Bench.class));

    }

    /**
     * Test method for
     * {@link org.perfidix.result.BenchmarkResult#addException(org.perfidix.exceptions.AbstractPerfidixMethodException)}
     * and {@link org.perfidix.result.BenchmarkResult#getExceptions()} .
     */
    @Test
    public void testResultWithException () {
        assertTrue("Check if benchRes.exceptions contains the desired exception", benchRes.getExceptions().contains(new PerfidixMethodInvocationException(testException, (Method) ((BenchmarkMethod) methodRes11.getRelatedElement()).getMethodToBench(), Bench.class)));
    }

    /**
     * Test method1 for {@link org.perfidix.result.MethodResult} .
     */
    @Test
    public void testMethodRes11 () {

        assertEquals("Mean should be the same as given by method11", 5.5, methodRes11.mean(meter), 0);
        assertEquals("Min should be the same as given by method11", 1.0, methodRes11.min(meter), 0);
        assertEquals("Max should be the same as given by method11", 10.0, methodRes11.max(meter), 0);
        assertEquals("Conf05 should be the same as given by method11", 1.0, methodRes11.getConf05(meter), 0);
        assertEquals("Conf95 should be the same as given by method11", 10.0, methodRes11.getConf95(meter), 0.00001);
        assertEquals("Stdev should be the same as given by method11", 3.0276503540974917, methodRes11.getStandardDeviation(meter), 0.000001);
        assertEquals("Sum should be the same as given by method11", 55.0, methodRes11.sum(meter), 0);
        assertEquals("SquareSum should be the same as given by method11", 385.0, methodRes11.squareSum(meter), 0);
        assertEquals("Number of result should be the same as given by method11", 10, methodRes11.getNumberOfResult(meter));

    }

    /**
     * Test method2 for {@link org.perfidix.result.MethodResult} .
     */
    @Test
    public void testMethodRes12 () {
        assertEquals("Mean should be the same as given by method12", 20.5, methodRes12.mean(meter), 0);
        assertEquals("Min should be the same as given by method12", 11.0, methodRes12.min(meter), 0);
        assertEquals("Max should be the same as given by method12", 30.0, methodRes12.max(meter), 0);
        assertEquals("Conf05 should be the same as given by method12", 11.05, methodRes12.getConf05(meter), 0);
        assertEquals("Con95 should be the same as given by method12", 29.95, methodRes12.getConf95(meter), 0.00001);
        assertEquals("Stdev should be the same as given by method12", 5.916079783099616, methodRes12.getStandardDeviation(meter), 0.000001);
        assertEquals("Sum should be the same as given by method12", 410.0, methodRes12.sum(meter), 0);
        assertEquals("SquareSum should be the same as given by method12", 9070.0, methodRes12.squareSum(meter), 0);
        assertEquals("Number of results should be the same as given by method12", 20, methodRes12.getNumberOfResult(meter));

    }

    /**
     * Test method3 for {@link org.perfidix.result.MethodResult} .
     */
    @Test
    public void testMethodRes21 () {
        assertEquals("Mean should be the same as given by method21", 50.5, methodRes21.mean(meter), 0);
        assertEquals("Min should be the same as given by method21", 31.0, methodRes21.min(meter), 0);
        assertEquals("Max should be the same as given by method21", 70.0, methodRes21.max(meter), 0);
        assertEquals("Conf05 should be the same as given by method21", 32.05, methodRes21.getConf05(meter), 0);
        assertEquals("Conf95 should be the same as given by method21", 68.95, methodRes21.getConf95(meter), 0);
        assertEquals("Stdev should be the same as given by method21", 11.69045194450012, methodRes21.getStandardDeviation(meter), 0.000001);
        assertEquals("Sum should be the same as given by method21", 2020.0, methodRes21.sum(meter), 0);
        assertEquals("Squaresum should be the same as given by method21", 107340.0, methodRes21.squareSum(meter), 0);
        assertEquals("Number of results should be the same as given by method21", 40, methodRes21.getNumberOfResult(meter));

    }

    /**
     * Test method3 for {@link org.perfidix.result.MethodResult} .
     */
    @Test
    public void testMethodRes22 () {
        assertEquals("Mean should be the same as given by method22", 110.5, methodRes22.mean(meter), 0);
        assertEquals("Min should be the same as given by method22", 71.0, methodRes22.min(meter), 0);
        assertEquals("Max should be the same as given by method22", 150.0, methodRes22.max(meter), 0);
        assertEquals("Conf05 should be the same as given by method22", 74.05, methodRes22.getConf05(meter), 0);
        assertEquals("Conf95 should be the same as given by method22", 146.95, methodRes22.getConf95(meter), 0.00001);
        assertEquals("Stdev should be the same as given by method22", 23.2379000772445, methodRes22.getStandardDeviation(meter), 0.000001);
        assertEquals("Sum should be the same as given by method22", 8840.0, methodRes22.sum(meter), 0);
        assertEquals("SquareSum should be the same as given by method22", 1019480.0, methodRes22.squareSum(meter), 0);
        assertEquals("Number of runs should be the same as given by method22", 80, methodRes22.getNumberOfResult(meter));
    }

    /**
     * Test method for {@link org.perfidix.result.ClassResult} .
     */
    @Test
    public void testClassResults () {
        assertEquals("Mean should be the same as given by class1", 15.5, classRes1.mean(meter), 0);
        assertEquals("Min should be the same as given by class1", 1.0, classRes1.min(meter), 0);
        assertEquals("Max should be the same as given by class1", 30.0, classRes1.max(meter), 0);
        assertEquals("Conf05 should be the same as given by class1", 1.55, classRes1.getConf05(meter), 0);
        assertEquals("Conf95 should be the same as given by class1", 29.45, classRes1.getConf95(meter), 0);
        assertEquals("Stdev should be the same as given by class1", 8.803408430829505, classRes1.getStandardDeviation(meter), 0.000001);
        assertEquals("Sum should be the same as given by class1", 465.0, classRes1.sum(meter), 0);
        assertEquals("SquareSum should be the same as given by class1", 9455.0, classRes1.squareSum(meter), 0);
        assertEquals("Number of runs should be the same as given by class1", 30, classRes1.getNumberOfResult(meter), 0);

        assertEquals("Mean should be the same as given by class2", 90.5, classRes2.mean(meter), 0);
        assertEquals("Min should be the same as given by class2", 31.0, classRes2.min(meter), 0);
        assertEquals("Max should be the same as given by class2", 150.0, classRes2.max(meter), 0);
        assertEquals("Conf05 should be the same as given by class2", 36.05, classRes2.getConf05(meter), 0);
        assertEquals("Conf95 should be the same as given by class2", 144.95, classRes2.getConf95(meter), 0);
        assertEquals("Stdev should be the same as given by class2", 34.785054261852174, classRes2.getStandardDeviation(meter), 0.000001);
        assertEquals("Sum should be the same as given by class2", 10860.0, classRes2.sum(meter), 0);
        assertEquals("SquareSum should be the same as given by class2", 1126820.0, classRes2.squareSum(meter), 0);
        assertEquals("Number of runs should be the same as given by class2", 120, classRes2.getNumberOfResult(meter), 0);
    }

    /**
     * Test method for {@link org.perfidix.result.BenchmarkResult} .
     */
    @Test
    public void testBenchmarkResults () {

        assertEquals("Mean should be the same as given by benchmark", 75.5, benchRes.mean(meter), 0);
        assertEquals("Min should be the same as given by benchmark", 1.0, benchRes.min(meter), 0);
        assertEquals("Max should be the same as given by benchmark", 150.0, benchRes.max(meter), 0);
        assertEquals("Conf05 should be the same as given by benchmark", 7.55, benchRes.getConf05(meter), 0);
        assertEquals("Conf95 should be the same as given by benchmark", 143.45, benchRes.getConf95(meter), 0);
        assertEquals("Stdev should be the same as given by benchmark", 43.445367992456916, benchRes.getStandardDeviation(meter), 0.000001);
        assertEquals("Sum should be the same as given by benchmark", 11325.0, benchRes.sum(meter), 0);
        assertEquals("SquareSum should be the same as given by benchmark", 1136275.0, benchRes.squareSum(meter), 0);
        assertEquals("Number of runs should be the same as given by benchmark", 150, benchRes.getNumberOfResult(meter));

    }

    class Class1 {
        @Bench
        public void method1 () {
            // empty method for class1#method1 invocation
        }

        @Bench
        public void method2 () {
            // empty method for class1#method2 invocation
        }
    }

    class Class2 {
        @Bench
        public void method1 () {
            // empty method for class2#method1 invocation
        }

        @Bench
        public void method2 () {
            // empty method for class2#method2 invocation
        }
    }

}
