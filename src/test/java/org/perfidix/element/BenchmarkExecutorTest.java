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
package org.perfidix.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.failureHandling.PerfidixMethodCheckException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.CountingMeter;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.result.BenchmarkResult;

/**
 * Test case for the BenchmarkExecutor. Note that all classes used in this
 * testcase are not allowed to be internal classes because of the reflective
 * invocation. This is not working with encapsulated classes.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class BenchmarkExecutorTest {

    private LinkedHashSet<AbstractMeter> meter;
    /** static int to check the beforefirstcounter */
    public static int once;
    /** static int to check the beforeeachcounter */
    public static int each;

    /**
     * Simple SetUp.
     */
    @Before
    public void setUp() {
        meter = new LinkedHashSet<AbstractMeter>();
        meter.add(new TimeMeter(Time.MilliSeconds));
        meter.add(new CountingMeter());
        once = 0;
        each = 0;
        BenchmarkExecutor.initialize(meter, new BenchmarkResult(null));
    }

    /**
     * Simple tearDown.
     */
    @After
    public void tearDown() {
        meter.clear();
        meter = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#getExecutor(org.perfidix.element.BenchmarkElement)}
     * .
     */
    @Test
    public void testGetExecutor() {
        final GetTestClass getInstanceClass = new GetTestClass();
        final Method meth = getInstanceClass.getClass().getDeclaredMethods()[0];

        final BenchmarkMethod elem1 = new BenchmarkMethod(meth);
        final BenchmarkMethod elem2 = new BenchmarkMethod(meth);

        final BenchmarkExecutor exec1 =
                BenchmarkExecutor.getExecutor(new BenchmarkElement(elem1));
        final BenchmarkExecutor exec2 =
                BenchmarkExecutor.getExecutor(new BenchmarkElement(elem2));

        assertTrue(exec1 == exec2);
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#executeBeforeMethods(java.lang.Object)}
     * 
     * @throws Exception
     *             of any kind because of reflection
     */
    @Test
    public void testExecuteBeforeMethods() throws Exception {

        final BeforeTestClass getClass = new BeforeTestClass();
        final Method meth = getClass.getClass().getDeclaredMethods()[0];
        final Object objToExecute = getClass.getClass().newInstance();

        final BenchmarkMethod elem = new BenchmarkMethod(meth);

        final BenchmarkExecutor exec =
                BenchmarkExecutor.getExecutor(new BenchmarkElement(elem));

        exec.executeBeforeMethods(objToExecute);
        exec.executeBeforeMethods(objToExecute);

        assertEquals(1, once);
        assertEquals(2, each);

    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#executeBench(java.lang.Object)}
     * .
     */
    @Test
    public void testExecuteBench() {
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#executeAfterMethods(java.lang.Object)}
     * 
     * @throws Exception
     *             of any kind because of reflection.
     */
    @Test
    public void testExecuteAfterMethods() throws Exception {
        final AfterTestClass getClass = new AfterTestClass();
        final Method meth = getClass.getClass().getDeclaredMethods()[0];
        final Object objToExecute = getClass.getClass().newInstance();

        final BenchmarkMethod elem = new BenchmarkMethod(meth);

        final BenchmarkExecutor exec =
                BenchmarkExecutor.getExecutor(new BenchmarkElement(elem));

        exec.executeAfterMethods(objToExecute);
        exec.executeAfterMethods(objToExecute);

        assertEquals(1, once);
        assertEquals(2, each);
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#checkAndExecute(Object, Method)}
     * 
     * @throws Exception
     *             of any kind because of reflection
     */
    @Test
    public void testCheckAndExecute() throws Exception {
        final Object falseObj = new Object();
        final Object correctObj = new CheckAndExecuteTestClass();

        assertEquals(2, correctObj.getClass().getDeclaredMethods().length);

        final Method correctMethod =
                correctObj.getClass().getDeclaredMethods()[0];
        final Method falseMethod =
                correctObj.getClass().getDeclaredMethods()[1];

        try {
            BenchmarkExecutor.checkAndExecute(falseObj, correctMethod);
            fail("Should throw IllegalStateException!");
        } catch (Exception e) {
            assertTrue(e instanceof PerfidixMethodCheckException);
        }

        try {
            BenchmarkExecutor.checkAndExecute(correctObj, falseMethod);
            fail("Should throw IllegalAccessException!");
        } catch (Exception e) {
            assertTrue(e instanceof PerfidixMethodCheckException);
        }

        BenchmarkExecutor.checkAndExecute(correctObj, correctMethod);
        assertEquals(1, once);

    }
}

class CheckAndExecuteTestClass {

    public void correctMethod() {
        BenchmarkExecutorTest.once++;
    }

    static void incorrectMeth(final boolean falseParameter) {
    }

}

class GetTestClass {

    @Bench
    public void bench1() {
    }

}

class AfterTestClass {

    @Bench
    public void bench() {
    }

    @AfterLastRun
    public void afterLast() {
        BenchmarkExecutorTest.once++;
    }

    @AfterEachRun
    public void afterEach() {
        BenchmarkExecutorTest.each++;
    }

}

class BeforeTestClass {

    @Bench
    public void bench() {
    }

    @BeforeFirstRun
    public void beforeFirst() {
        BenchmarkExecutorTest.once++;
    }

    @BeforeEachRun
    public void beforeEach() {
        BenchmarkExecutorTest.each++;
    }

}
