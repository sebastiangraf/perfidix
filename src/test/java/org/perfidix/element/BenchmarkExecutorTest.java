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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.CountingMeter;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;

/**
 * Test case for the BenchmarkExecutor.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class BenchmarkExecutorTest {

    private Set<AbstractMeter> meter;
    /** static int to check the beforefirstcounter */
    public static int once;
    /** static int to check the beforeeachcounter */
    public static int each;

    /**
     * Simple SetUp.
     */
    @Before
    public void setUp() {
        meter = new HashSet<AbstractMeter>();
        meter.add(new TimeMeter(Time.MilliSeconds));
        meter.add(new CountingMeter());
        once = 0;
        each = 0;
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
     * {@link org.perfidix.element.BenchmarkExecutor#getExecutor(org.perfidix.element.BenchmarkElement, java.util.Set)}
     * .
     */
    @Test
    public void testGetExecutor1() {
        final GetTestClass getClass = new GetTestClass();
        final Method meth = getClass.getClass().getDeclaredMethods()[0];

        final BenchmarkElement elem1 = new BenchmarkElement(meth);
        final BenchmarkElement elem2 = new BenchmarkElement(meth);

        final BenchmarkExecutor exec1 =
                BenchmarkExecutor.getExecutor(elem1, meter);
        final BenchmarkExecutor exec2 =
                BenchmarkExecutor.getExecutor(elem2, meter);

        assertTrue(exec1 == exec2);
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#getExecutor(org.perfidix.element.BenchmarkElement, java.util.Set)}
     * .
     */
    @Test(expected = IllegalStateException.class)
    public void testGetExecutor2() {
        final Set<AbstractMeter> meter = new HashSet<AbstractMeter>();
        meter.add(new TimeMeter(Time.MilliSeconds));
        final GetTestClass getClass = new GetTestClass();
        final Method meth = getClass.getClass().getDeclaredMethods()[0];

        final BenchmarkElement elem1 = new BenchmarkElement(meth);
        final BenchmarkElement elem2 = new BenchmarkElement(meth);

        BenchmarkExecutor.getExecutor(elem1, this.meter);
        BenchmarkExecutor.getExecutor(elem2, meter);
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

        final BenchmarkElement elem = new BenchmarkElement(meth);

        final BenchmarkExecutor exec =
                BenchmarkExecutor.getExecutor(elem, meter);

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

        final BenchmarkElement elem = new BenchmarkElement(meth);

        final BenchmarkExecutor exec =
                BenchmarkExecutor.getExecutor(elem, meter);

        exec.executeAfterMethods(objToExecute);
        exec.executeAfterMethods(objToExecute);

        assertEquals(1, once);
        assertEquals(2, each);
    }

    class GetTestClass {

        @Bench
        public void bench1() {
        }

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
