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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.BenchClass;
import org.perfidix.annotation.SkipBench;

/**
 * This class acts as a testcase for the BenchmarkElement-class.
 * 
 * @author Sebastian Graf, University of Konstanz
 */

public class BenchmarkElementTest {

    private Object currentClassToTest;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     *             of any kind
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     *             of any kind
     */
    @After
    public void tearDown() throws Exception {
        currentClassToTest = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#checkThisMethodAsBenchmarkable()}
     * .
     */
    @Test
    public void testCheckThisMethodAsBenchmarkable1() {

        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestClassCheckThisMethodAsBenchmarkable1();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            int numberOfFoundMethods = 0;
            for (final Method meth : meths) {
                final BenchmarkElement elem = new BenchmarkElement(meth);
                if (elem.checkThisMethodAsBenchmarkable()) {
                    elem.getMethodToBench().invoke(currentClassToTest, param);
                    numberOfFoundMethods++;
                }
            }
            assertEquals(numberOfFoundMethods, 1);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#checkThisMethodAsBenchmarkable()}
     * .
     */
    @Test
    public void testCheckThisMethodAsBenchmarkable2() {

        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestClassCheckThisMethodAsBenchmarkable2();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            int numberOfFoundMethods = 0;
            for (final Method meth : meths) {
                final BenchmarkElement elem = new BenchmarkElement(meth);
                if (elem.checkThisMethodAsBenchmarkable()) {
                    elem.getMethodToBench().invoke(currentClassToTest, param);
                    numberOfFoundMethods++;
                }
            }
            assertEquals(numberOfFoundMethods, 0);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findAndCheckAnyMethodByAnnotation(java.lang.Class,java.lang.Class)}
     * .
     * 
     * @throws IllegalAccessException
     *             should be thrown if something weird happen
     */
    @Test(expected = IllegalAccessException.class)
    public void testFindAndCheckAnyMethodByAnnotation()
            throws IllegalAccessException {
        try {
            currentClassToTest = new TestFindAndCheckBenchClass();
            BenchmarkElement.findAndCheckAnyMethodByAnnotation(
                    currentClassToTest.getClass(), ShouldOccureOnce.class);
        } catch (IllegalAccessException e) {
            throw e;
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#isReflectedExecutable(java.lang.reflect.Method)}
     * .
     */
    @Test
    public void testIsReflectedExecutable() {
        try {
            final Object[] param = {};
            currentClassToTest = new TestIsReflectedExecutable();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            int numberOfInvokedMethods = 0;
            for (final Method meth : meths) {
                if (BenchmarkElement.isReflectedExecutable(meth)) {
                    meth.invoke(currentClassToTest, param);
                    numberOfInvokedMethods++;
                }
            }
            assertEquals(numberOfInvokedMethods, 1);
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findBeforeFirstRun()}.
     */
    @Test
    public void testFindBeforeFirstRun1() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestBeforeFirstRun1();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                final BenchmarkElement checkElem = new BenchmarkElement(meth);
                if (checkElem.checkThisMethodAsBenchmarkable()) {
                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("Should be only one element!");
                    }
                }
            }

            final Method meth = elem.findBeforeFirstRun();
            meth.invoke(currentClassToTest, param);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findBeforeFirstRun()}.
     */
    @Test
    public void testFindBeforeFirstRun2() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestBeforeFirstRun2();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                final BenchmarkElement checkElem = new BenchmarkElement(meth);
                if (checkElem.checkThisMethodAsBenchmarkable()) {
                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("Should be only one element!");
                    }
                }
            }

            final Method meth = elem.findBeforeFirstRun();
            meth.invoke(currentClassToTest, param);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findBeforeEachRun()}.
     */
    @Test
    public void testFindBeforeEachRun1() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestBeforeEachRun1();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                final BenchmarkElement checkElem = new BenchmarkElement(meth);
                if (checkElem.checkThisMethodAsBenchmarkable()) {
                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("Should be only one element!");
                    }
                }
            }

            final Method meth = elem.findBeforeEachRun();
            meth.invoke(currentClassToTest, param);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findBeforeEachRun()}.
     */
    @Test
    public void testFindBeforeEachRun2() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestBeforeEachRun2();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                final BenchmarkElement checkElem = new BenchmarkElement(meth);
                if (checkElem.checkThisMethodAsBenchmarkable()) {
                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("Should be only one element!");
                    }
                }
            }

            final Method meth = elem.findBeforeEachRun();
            meth.invoke(currentClassToTest, param);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findBeforeEachRun()}.
     */
    @Test
    public void testFindAfterEachRun1() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestAfterEachRun1();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                final BenchmarkElement checkElem = new BenchmarkElement(meth);
                if (checkElem.checkThisMethodAsBenchmarkable()) {
                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("Should be only one element!");
                    }
                }
            }

            final Method meth = elem.findAfterEachRun();
            meth.invoke(currentClassToTest, param);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findAfterEachRun()}.
     */
    @Test
    public void testFindAfterEachRun2() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestAfterEachRun2();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                final BenchmarkElement checkElem = new BenchmarkElement(meth);
                if (checkElem.checkThisMethodAsBenchmarkable()) {
                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("Should be only one element!");
                    }
                }
            }

            final Method meth = elem.findAfterEachRun();
            meth.invoke(currentClassToTest, param);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findAfterLastRun()}.
     */
    @Test
    public void testFindAfterLastRun1() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestAfterLastRun1();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                final BenchmarkElement checkElem = new BenchmarkElement(meth);
                if (checkElem.checkThisMethodAsBenchmarkable()) {
                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("Should be only one element!");
                    }
                }
            }

            final Method meth = elem.findAfterLastRun();
            meth.invoke(currentClassToTest, param);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#findAfterLastRun()}.
     */
    @Test
    public void testFindAfterLastRun2() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            currentClassToTest = new TestAfterLastRun2();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                final BenchmarkElement checkElem = new BenchmarkElement(meth);
                if (checkElem.checkThisMethodAsBenchmarkable()) {
                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("Should be only one element!");
                    }
                }
            }

            final Method meth = elem.findAfterLastRun();
            meth.invoke(currentClassToTest, param);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkElement#getNumberOfRuns()}.
     */
    @Test
    public void testRuns1() {
        try {
            currentClassToTest = new TestRuns();
            final Method[] meths =
                    currentClassToTest.getClass().getDeclaredMethods();
            BenchmarkElement elem = null;
            for (final Method meth : meths) {
                elem = new BenchmarkElement(meth);
                if (meth.getName().equals("bench1")) {
                    assertEquals(10, elem.getNumberOfRuns());
                } else if (meth.getName().equals("bench2")) {
                    assertEquals(20, elem.getNumberOfRuns());
                } else if (meth.getName().equals("bench3")) {
                    try {
                        elem.getNumberOfRuns();
                        fail("Must throw IllegalStateException!");
                    } catch (IllegalStateException e) {
                        assertTrue(e.getMessage().startsWith("Method"));
                    }
                } else {
                    fail("Should never occur!");
                }
            }
        } catch (Exception e) {
            fail("Should never fail in testRuns!");
        }
    }

    @BenchClass(runs = 20)
    class TestRuns {

        @Bench(runs = 10)
        public void bench1() {
        }

        public void bench2() {
        }

        public int bench3() {
            return -1;
        }

    }

    class TestAfterLastRun2 {

        @AfterLastRun
        public final void afterLastRun() {
        }

        @Bench
        public final void bench() {
        }
    }

    class TestAfterLastRun1 {

        @AfterLastRun
        public final void afterLastRunAnno() {
            fail("Should be ignored!");
        }

        public final void afterLastRun() {
        }

        @Bench(afterLastRun = "afterLastRun")
        public final void bench() {
        }
    }

    class TestAfterEachRun2 {

        @AfterEachRun
        public final void afterEachRun() {
        }

        @Bench
        public final void bench() {
        }
    }

    class TestAfterEachRun1 {

        @AfterEachRun
        public final void afterEachRunAnno() {
            fail("Should be ignored!");
        }

        public final void afterEachRun() {
        }

        @Bench(afterEachRun = "afterEachRun")
        public final void bench() {
        }
    }

    class TestBeforeEachRun2 {

        @BeforeEachRun
        public final void beforeEachRun() {
        }

        @Bench
        public final void bench() {
        }
    }

    class TestBeforeEachRun1 {

        @BeforeEachRun
        public final void beforeEachRunAnno() {
            fail("Should be ignored!");
        }

        public final void beforeEachRun() {
        }

        @Bench(beforeEachRun = "beforeEachRun")
        public final void bench() {
        }
    }

    class TestBeforeFirstRun2 {

        @BeforeFirstRun
        public final void beforeFirstRun() {
        }

        @Bench
        public final void bench() {
        }
    }

    class TestBeforeFirstRun1 {

        @BeforeFirstRun
        public final void beforeFirstRunAnno() {
            fail("Should be ignored!");
        }

        public final void beforeFirstRun() {
        }

        @Bench(beforeFirstRun = "beforeFirstRun")
        public final void bench() {
        }
    }

    class TestIsReflectedExecutable {

        public final void paramMethod(final Object obj) {
            fail("Only param-less methods allowed");
        }

        public final void exceptionMethod() throws Exception {
            fail("Only methods without an exception allowed");
        }

        final void notPublicMethod() {
            fail("Only methods with public identifier allowed");
        }

        public final Object returnVal() {
            fail("Only methods without a returnVal allowed");
            return null;
        }

        public final void shouldBeInvoked() {

        }
    }

    class TestFindAndCheckBenchClass {

        @ShouldOccureOnce
        public final void testBenchAnno1() {
        }

        @ShouldOccureOnce
        public final void testBenchAnno2() {
        }
    }

    class TestClassCheckThisMethodAsBenchmarkable1 {
        @Bench
        public final void testBenchAnno1() {
        }

        public final void testBenchAnno2() {
            fail("Should not be benched!");
        }
    }

    class TestClassCheckThisMethodAsBenchmarkable2 {

        @SkipBench
        @Bench
        public final void testBenchAnno3() {
            fail("Should not be benched!");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface ShouldOccureOnce {

    }
}
