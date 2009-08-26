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

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.BenchClass;
import org.perfidix.annotation.SkipBench;
import org.perfidix.exceptions.PerfidixMethodCheckException;

/**
 * This class acts as a testcase for the BenchmarkElement-class.
 * 
 * @author Sebastian Graf, University of Konstanz
 */

public class BenchmarkMethodTest {

    private transient Object toTest;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     *             of any kind
     */
    @Before
    public void setUp() throws Exception {

        // Testing the bench-anno
        toTest = new TestClassCheckThisMethodAsBenchmarkable1();
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#isBenchmarkable(Method)} .
     */
    @Test
    public void testCheckThisMethodAsBenchmarkable1() {
        final Object[] param = {};
        try {

            final Method[] meths = toTest.getClass().getDeclaredMethods();
            int numOfMethods = 0;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod elem = new BenchmarkMethod(meth);
                    elem.getMethodToBench().invoke(toTest, param);
                    numOfMethods++;
                }
            }
            assertEquals("Number of methods should be 1", numOfMethods, 1);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#isBenchmarkable(Method)} .
     */
    @Test
    public void testCheckThisMethodAsBenchmarkable2() {

        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestClassCheckThisMethodAsBenchmarkable2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            int numOfMethods = 0;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod elem = new BenchmarkMethod(meth);
                    elem.getMethodToBench().invoke(toTest, param);
                    numOfMethods++;
                }
            }
            assertEquals("Number of methods should be 0", numOfMethods, 0);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findAndCheckAnyMethodByAnnotation(java.lang.Class,java.lang.Class)}
     * .
     * 
     * @throws PerfidixMethodCheckException
     *             should be thrown if something weird happen
     */
    @Test(expected = PerfidixMethodCheckException.class)
    public void testFindAndCheckAnyMethodByAnnotation()
            throws PerfidixMethodCheckException {
        toTest = new TestFindAndCheckBenchClass();
        BenchmarkMethod.findAndCheckAnyMethodByAnnotation(
                toTest.getClass(), ShouldOccureOnce.class);
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#isReflectedExecutable(java.lang.reflect.Method)}
     * .
     */
    @Test
    public void testIsReflectedExecutable() {
        try {
            final Object[] param = {};
            toTest = new TestIsReflectedExecutable();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            int numOfMethods = 0;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isReflectedExecutable(meth)) {
                    meth.invoke(toTest, param);
                    numOfMethods++;
                }
            }
            assertEquals("Number of methods should be 1", numOfMethods, 1);
        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findBeforeFirstRun()}.
     */
    @Test
    public void testFindBeforeFirstRun1() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestBeforeFirstRun1();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestBeforeFirstRun1 found!");
                    }
                }
            }

            final Method meth = elem.findBeforeFirstRun();
            meth.invoke(toTest, param);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findBeforeFirstRun()}.
     */
    @Test
    public void testFindBeforeFirstRun2() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestBeforeFirstRun2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestBeforeFirstRun2 found!");
                    }
                }
            }

            final Method meth = elem.findBeforeFirstRun();
            meth.invoke(toTest, param);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findBeforeEachRun()}.
     */
    @Test
    public void testFindBeforeEachRun1() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestBeforeEachRun1();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestBeforeEachRun1 found!");
                    }
                }
            }

            final Method meth = elem.findBeforeEachRun();
            meth.invoke(toTest, param);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findBeforeEachRun()}.
     */
    @Test
    public void testFindBeforeEachRun2() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestBeforeEachRun2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestBeforeFirstRun2 found!");
                    }
                }
            }

            final Method meth = elem.findBeforeEachRun();
            meth.invoke(toTest, param);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findBeforeEachRun()}.
     */
    @Test
    public void testFindAfterEachRun1() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestAfterEachRun1();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestAfterEachRun1 found!");
                    }
                }
            }

            final Method meth = elem.findAfterEachRun();
            meth.invoke(toTest, param);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findAfterEachRun()}.
     */
    @Test
    public void testFindAfterEachRun2() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestAfterEachRun2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestAfterEeachRun2 found!");
                    }
                }
            }

            final Method meth = elem.findAfterEachRun();
            meth.invoke(toTest, param);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findAfterLastRun()}.
     */
    @Test
    public void testFindAfterLastRun1() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestAfterLastRun1();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestAfterLastRun1 found!");
                    }
                }
            }

            final Method meth = elem.findAfterLastRun();
            meth.invoke(toTest, param);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#findAfterLastRun()}.
     */
    @Test
    public void testFindAfterLastRun2() {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestAfterLastRun2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestAfterLastRun2 found!");
                    }
                }
            }

            final Method meth = elem.findAfterLastRun();
            meth.invoke(toTest, param);

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkMethod#getNumberOfAnnotatedRuns(Method)}
     * .
     */
    @Test
    public void testNumberOfAnnotatedRuns() {
        try {
            toTest = new TestNumberOfAnnotatedRuns();
            final Method[] meths = toTest.getClass().getDeclaredMethods();

            assertEquals("3 methods should be found", 3, meths.length);

            assertEquals("Check of name of method1", "bench1", meths[0]
                    .getName());
            assertEquals("Check of runs of method1", 10, BenchmarkMethod
                    .getNumberOfAnnotatedRuns(meths[0]));
            assertEquals("Check of name of method2", "bench2", meths[1]
                    .getName());
            assertEquals("Check of runs of method2", 20, BenchmarkMethod
                    .getNumberOfAnnotatedRuns(meths[1]));
            assertEquals("Check of name of method3", "bench3", meths[2]
                    .getName());
            try {
                BenchmarkMethod.getNumberOfAnnotatedRuns(meths[2]);
                fail("Must throw IllegalStateException!");
            } catch (final IllegalArgumentException e) {
                assertTrue("Methodexception must match a pattern", e
                        .getMessage().startsWith("Method"));
            }

        } catch (final Exception e) {
            fail("Should never fail in testRuns!");
        }
    }

    @BenchClass(runs = 20)
    class TestNumberOfAnnotatedRuns {

        @Bench(runs = 10)
        public void bench1() {
            // Just for getting the method1
        }

        public void bench2() {
            // Just for getting the method2
        }

        public int bench3() {
            // Just for getting the method3
            return -1;
        }

    }

    class TestAfterLastRun2 {

        @AfterLastRun
        public final void afterLastRun() {
            // Just for getting the afterLastAnno
        }

        @Bench
        public final void bench() {
            // Just for getting the bench
        }
    }

    class TestAfterLastRun1 {

        @AfterLastRun
        public final void afterLastRunAnno() {
            fail("Should be ignored because of designated afterLastRun");
        }

        public final void afterLastRun() {
            // Just for having an AfterLastRun
        }

        @Bench(afterLastRun = "afterLastRun")
        public final void bench() {
            // Just a bench
        }
    }

    class TestAfterEachRun2 {

        @AfterEachRun
        public final void afterEachRun() {
            // Just for having an AfterEachRun
        }

        @Bench
        public final void bench() {
            // building the bench
        }
    }

    class TestAfterEachRun1 {

        @AfterEachRun
        public final void afterEachRunAnno() {
            fail("Should be ignored because of designated after each run");
        }

        public final void afterEachRun() {
            // Just the afterEachRun
        }

        @Bench(afterEachRun = "afterEachRun")
        public final void bench() {
            // Just the bench
        }
    }

    class TestBeforeEachRun2 {

        @BeforeEachRun
        public final void beforeEachRun() {
            // Just the before each run
        }

        @Bench
        public final void bench() {
            // Just the bench
        }
    }

    class TestBeforeEachRun1 {

        @BeforeEachRun
        public final void beforeEachRunAnno() {
            fail("Should be ignored because of designated before each run!");
        }

        public final void beforeEachRun() {
            // Just the before each run
        }

        @Bench(beforeEachRun = "beforeEachRun")
        public final void bench() {
            // Just the bench
        }
    }

    class TestBeforeFirstRun2 {

        @BeforeFirstRun
        public final void beforeFirstRun() {
            // Just the before first run
        }

        @Bench
        public final void bench() {
            // Just the bench
        }
    }

    class TestBeforeFirstRun1 {

        @BeforeFirstRun
        public final void beforeFirstRunAnno() {
            fail("Should be ignored because of designated before first anno");
        }

        public final void beforeFirstRun() {
            // Just the before first anno
        }

        @Bench(beforeFirstRun = "beforeFirstRun")
        public final void bench() {
            // Just the bench
        }
    }

    class TestIsReflectedExecutable {

        public final void paramMethod(final Object obj) {
            fail("Only param-less methods allowed");
        }

        public final void exceptionMethod() throws IOException {
            fail("Only methods without an exception allowed");
        }

        protected final void notPublicMethod() {
            fail("Only methods with public identifier allowed");
        }

        public final Object returnVal() {
            fail("Only methods without a returnVal allowed");
            return null;
        }

        public final void shouldBeInvoked() {
            // Should be invoked
        }
    }

    class TestFindAndCheckBenchClass {

        @ShouldOccureOnce
        public final void benchAnno1() {
            // Just for performing a search
        }

        @ShouldOccureOnce
        public final void benchAnno2() {
            // Just for performing a search
        }
    }

    class TestClassCheckThisMethodAsBenchmarkable1 {
        @Bench
        public final void benchAnno1() {
            // Just for performing a search
        }

        public final void benchAnno2() {
            fail("Should not be benched!");
        }
    }

    class TestClassCheckThisMethodAsBenchmarkable2 {

        @SkipBench
        @Bench
        public final void benchAnno3() {
            fail("Should not be benched!");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface ShouldOccureOnce {

    }
}
