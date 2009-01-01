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

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.meter.CountingMeter;

/**
 * Test class for the whole package of the results.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class ResultContainerTest {

    private final static int NUMBEROFTICKS = 10;
    private final static int TICKFACTOR = 2;

    private MethodResult methodRes11;
    private MethodResult methodRes21;
    private MethodResult methodRes12;
    private MethodResult methodRes22;

    private CountingMeter meter1;
    private CountingMeter meter2;

    private Class1 class1;
    private Class2 class2;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        final Class<?>[] args = {};
        class1 = new Class1();
        class2 = new Class2();

        meter1 = new CountingMeter("Meter1");
        meter2 = new CountingMeter("Meter2");

        methodRes11 =
                new MethodResult(class1.getClass().getDeclaredMethod(
                        "method", args));
        for (int i = 0; i < NUMBEROFTICKS; i++) {
            meter1.tick();
            methodRes11.addData(meter1, meter1.getValue());
        }

        methodRes21 =
                new MethodResult(class2.getClass().getDeclaredMethod(
                        "method", args));

        for (int i = 0; i < NUMBEROFTICKS * TICKFACTOR; i++) {
            meter2.tick();
            methodRes21.addData(meter2, meter2.getValue());
        }

        methodRes12 =
                new MethodResult(class1.getClass().getDeclaredMethod(
                        "method", args));
        for (int i = 0; i < NUMBEROFTICKS * TICKFACTOR

        * TICKFACTOR; i++) {
            meter1.tick();
            methodRes12.addData(meter1, meter1.getValue());
        }

        methodRes22 =
                new MethodResult(class2.getClass().getDeclaredMethod(
                        "method", args));

        for (int i = 0; i < NUMBEROFTICKS * TICKFACTOR

        * TICKFACTOR * TICKFACTOR; i++) {
            meter2.tick();
            methodRes22.addData(meter2, meter2.getValue());
        }

    }

    /**
     * Test method for
     * {@link org.perfidix.result.MethodResult#MethodResult(java.lang.reflect.Method)}
     * .
     */
    @Test
    public void testStatisticalMethods() {
        assertEquals(5.5, methodRes11.mean(meter1), 0);
        assertEquals(1.0, methodRes11.min(meter1), 0);
        assertEquals(10.0, methodRes11.max(meter1), 0);
        assertEquals(1.0, methodRes11.getConf05(meter1), 0);
        assertEquals(10.0, methodRes11.getConf95(meter1), 0);

        assertEquals(3.0276503540974917, methodRes11
                .getStandardDeviation(meter1), 0.000001);
        assertEquals(55.0, methodRes11.sum(meter1), 0);
        assertEquals(385.0, methodRes11.squareSum(meter1), 0);

        assertEquals(30.5, methodRes12.mean(meter1), 0);
        assertEquals(11.0, methodRes12.min(meter1), 0);
        assertEquals(50.0, methodRes12.max(meter1), 0);
        assertEquals(12.05, methodRes12.getConf05(meter1), 0);
        assertEquals(48.95, methodRes12.getConf95(meter1), 0);
        assertEquals(11.69045194450012, methodRes12
                .getStandardDeviation(meter1), 0.000001);
        assertEquals(1220.0, methodRes12.sum(meter1), 0);
        assertEquals(42540.0, methodRes12.squareSum(meter1), 0);

        assertEquals(10.5, methodRes21.mean(meter2), 0);
        assertEquals(1.0, methodRes21.min(meter2), 0);
        assertEquals(20.0, methodRes21.max(meter2), 0);
        assertEquals(1.05, methodRes21.getConf05(meter2), 0);
        assertEquals(19.95, methodRes21.getConf95(meter2), 0);
        assertEquals(5.916079783099616, methodRes21
                .getStandardDeviation(meter2), 0.000001);
        assertEquals(210.0, methodRes21.sum(meter2), 0);
        assertEquals(2870.0, methodRes21.squareSum(meter2), 0);

        assertEquals(60.5, methodRes22.mean(meter2), 0);
        assertEquals(21.0, methodRes22.min(meter2), 0);
        assertEquals(100.0, methodRes22.max(meter2), 0);
        assertEquals(24.05, methodRes22.getConf05(meter2), 0);
        assertEquals(96.95, methodRes22.getConf95(meter2), 0);
        assertEquals(
                23.2379000772445, methodRes22.getStandardDeviation(meter2),
                0.000001);
        assertEquals(4840.0, methodRes22.sum(meter2), 0);
        assertEquals(335480.0, methodRes22.squareSum(meter2), 0);

    }

    /**
     * Test method for
     * {@link org.perfidix.result.ClassResult#ClassResult(Class)}
     */
    @Test
    public void testResultContainer2() {
        final Set<MethodResult> methRes1 = new HashSet<MethodResult>();
        final Set<MethodResult> methRes2 = new HashSet<MethodResult>();
        methRes1.add(methodRes11);
        methRes1.add(methodRes12);
        methRes2.add(methodRes21);
        methRes2.add(methodRes22);

        final ClassResult classRes1 =
                new ClassResult(class1.getClass(), methRes1);

        final ClassResult classRes2 =
                new ClassResult(class2.getClass(), methRes2);

        assertEquals(25.5, classRes1.mean(meter1), 0);
        assertEquals(1.0, classRes1.min(meter1), 0);
        assertEquals(50.0, classRes1.max(meter1), 0);
        assertEquals(2.55, classRes1.getConf05(meter1), 0);
        assertEquals(48.45, classRes1.getConf95(meter1), 0);
        assertEquals(
                14.577379737113251, classRes1.getStandardDeviation(meter1),
                0.000001);
        assertEquals(1275.0, classRes1.sum(meter1), 0);
        assertEquals(42925.0, classRes1.squareSum(meter1), 0);

        assertEquals(50.5, classRes2.mean(meter2), 0);
        assertEquals(1.0, classRes2.min(meter2), 0);
        assertEquals(100.0, classRes2.max(meter2), 0);
        assertEquals(5.05, classRes2.getConf05(meter2), 0);
        assertEquals(95.95, classRes2.getConf95(meter2), 0);
        assertEquals(
                29.011491975882016, classRes2.getStandardDeviation(meter2),
                0.000001);
        assertEquals(5050.0, classRes2.sum(meter2), 0);
        assertEquals(338350.0, classRes2.squareSum(meter2), 0);

        final Set<ClassResult> classRes = new HashSet<ClassResult>();
        classRes.add(classRes1);
        classRes.add(classRes2);
        final BenchmarkResult benchRes = new BenchmarkResult(classRes);

        assertEquals(25.5, benchRes.mean(meter1), 0);
        assertEquals(1.0, benchRes.min(meter1), 0);
        assertEquals(50.0, benchRes.max(meter1), 0);
        assertEquals(2.55, benchRes.getConf05(meter1), 0);
        assertEquals(48.45, benchRes.getConf95(meter1), 0);
        assertEquals(
                14.577379737113251, benchRes.getStandardDeviation(meter1),
                0.000001);
        assertEquals(1275.0, benchRes.sum(meter1), 0);
        assertEquals(42925.0, benchRes.squareSum(meter1), 0);

        assertEquals(50.5, benchRes.mean(meter2), 0);
        assertEquals(1.0, benchRes.min(meter2), 0);
        assertEquals(100.0, benchRes.max(meter2), 0);
        assertEquals(2.55, benchRes.getConf05(meter1), 0);
        assertEquals(95.95, benchRes.getConf95(meter2), 0);
        assertEquals(
                29.011491975882016, benchRes.getStandardDeviation(meter2),
                0.000001);
        assertEquals(5050.0, benchRes.sum(meter2), 0);
        assertEquals(338350.0, benchRes.squareSum(meter2), 0);
    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        methodRes11 = null;
        methodRes12 = null;
        methodRes21 = null;
        methodRes22 = null;
        meter1 = null;
        meter2 = null;
        class1 = null;
        class2 = null;
    }

    class Class1 {
        public void method() {
        }
    }

    class Class2 {
        public void method() {

        }
    }

}
