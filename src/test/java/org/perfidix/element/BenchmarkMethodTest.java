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
package org.perfidix.element;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import static org.junit.Assert.assertFalse;


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
     * @throws java.lang.Exception of any kind
     */
    @Before
    public void setUp () throws Exception {

        // Testing the bench-anno
        toTest = new TestClassCheckThisMethodAsBenchmarkable1();
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#isBenchmarkable(Method)} .
     */
    @Test
    public void testCheckThisMethodAsBenchmarkable1 () {
        final Object[] param = {};
        try {

            final Method[] meths = toTest.getClass().getDeclaredMethods();
            int numOfMethods = 0;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod elem = new BenchmarkMethod(meth, null);
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
     * Test method for {@link org.perfidix.element.BenchmarkMethod#isBenchmarkable(Method)} .
     */
    @Test
    public void testCheckThisMethodAsBenchmarkable2 () {

        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestClassCheckThisMethodAsBenchmarkable2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            int numOfMethods = 0;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod elem = new BenchmarkMethod(meth, null);
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
     * {@link org.perfidix.element.BenchmarkMethod#findAndCheckAnyMethodByAnnotation(java.lang.Class,java.lang.Class)} .
     * 
     * @throws PerfidixMethodCheckException should be thrown if something weird happen
     */
    @Test (expected = PerfidixMethodCheckException.class)
    public void testFindAndCheckAnyMethodByAnnotation () throws PerfidixMethodCheckException {
        toTest = new TestFindAndCheckBenchClass();
        BenchmarkMethod.findAndCheckAnyMethodByAnnotation(toTest.getClass(), ShouldOccureOnce.class);
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#isReflectedExecutable(java.lang.reflect.Method)} .
     */
    @Test
    public void testIsReflectedExecutable () {
        try {
            final Object[] param = {};
            toTest = new TestIsReflectedExecutable();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            int numOfMethods = 0;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isReflectedExecutable(meth, false, Bench.class)) {
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
     * Test method for {@link org.perfidix.element.BenchmarkMethod#findBeforeFirstRun()}.
     */
    @Test
    public void testFindBeforeFirstRun1 () {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestBeforeFirstRun1();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth, null);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestBeforeFirstRun1 found!");
                    }
                }
            }

            final Method[] returnedMeths = elem.findBeforeFirstRun();
            for (Method meth : returnedMeths) {
                meth.invoke(toTest, param);
            }

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#findBeforeFirstRun()}.
     */
    @Test
    public void testFindBeforeFirstRun2 () {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestBeforeFirstRun2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth, null);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestBeforeFirstRun2 found!");
                    }
                }
            }

            final Method[] returnedMeths = elem.findBeforeFirstRun();
            for (Method meth : returnedMeths) {
                meth.invoke(toTest, param);
            }

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#findBeforeEachRun()}.
     */
    @Test
    public void testFindBeforeEachRun1 () {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestBeforeEachRun1();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth, null);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestBeforeEachRun1 found!");
                    }
                }
            }

            final Method[] returnedMeths = elem.findBeforeEachRun(null);
            for (Method meth : returnedMeths) {
                meth.invoke(toTest, param);
            }

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#findBeforeEachRun()}.
     */
    @Test
    public void testFindBeforeEachRun2 () {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestBeforeEachRun2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth, null);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestBeforeFirstRun2 found!");
                    }
                }
            }

            final Method[] returnedMeths = elem.findBeforeEachRun(null);
            for (Method meth : returnedMeths) {
                meth.invoke(toTest, param);
            }

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#findBeforeEachRun()}.
     */
    @Test
    public void testFindAfterEachRun1 () {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestAfterEachRun1();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth, null);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestAfterEachRun1 found!");
                    }
                }
            }

            final Method[] returnedMeths = elem.findAfterEachRun(null);
            for (Method meth : returnedMeths) {
                meth.invoke(toTest, param);
            }

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#findAfterEachRun()}.
     */
    @Test
    public void testFindAfterEachRun2 () {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestAfterEachRun2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth, null);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestAfterEeachRun2 found!");
                    }
                }
            }

            final Method[] returnedMeths = elem.findAfterEachRun(null);
            for (Method meth : returnedMeths) {
                meth.invoke(toTest, param);
            }

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#findAfterLastRun()}.
     */
    @Test
    public void testFindAfterLastRun1 () {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestAfterLastRun1();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth, null);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestAfterLastRun1 found!");
                    }
                }
            }

            final Method[] returnedMeths = elem.findAfterLastRun();
            for (Method meth : returnedMeths) {
                meth.invoke(toTest, param);
            }

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#findAfterLastRun()}.
     */
    @Test
    public void testFindAfterLastRun2 () {
        try {
            final Object[] param = {};

            // Testing the bench-anno
            toTest = new TestAfterLastRun2();
            final Method[] meths = toTest.getClass().getDeclaredMethods();
            BenchmarkMethod elem = null;
            for (final Method meth : meths) {
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod checkElem = new BenchmarkMethod(meth, null);

                    if (elem == null) {
                        elem = checkElem;
                    } else {
                        fail("More than one elem in TestAfterLastRun2 found!");
                    }
                }
            }

            final Method[] returnedMeths = elem.findAfterLastRun();
            for (Method meth : returnedMeths) {
                meth.invoke(toTest, param);
            }

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkMethod#getNumberOfAnnotatedRuns(Method)} .
     */
    @Test
    public void testNumberOfAnnotatedRuns () {
        try {
            toTest = new TestNumberOfAnnotatedRuns();
            final Method[] meths = toTest.getClass().getDeclaredMethods();

            assertEquals("4 methods should be found", 4, meths.length);

            for (Method meth : meths) {

                if (meth.getName().equals("bench1")) {
                    assertEquals("Check of name of method1", "bench1", meth.getName());
                    assertEquals("Check of runs of method1", 10, BenchmarkMethod.getNumberOfAnnotatedRuns(meth));
                }
                if (meth.getName().equals("bench2")) {
                    assertEquals("Check of name of method2", "bench2", meth.getName());
                    assertEquals("Check of runs of method2", 20, BenchmarkMethod.getNumberOfAnnotatedRuns(meth));
                }
                if (meth.getName().equals("bench3")) {
                    assertEquals("Check of name of method3", "bench3", meth.getName());
                    try {
                        BenchmarkMethod.getNumberOfAnnotatedRuns(meth);
                        fail("Must throw IllegalStateException!");
                    } catch (final IllegalArgumentException e) {
                        assertTrue("Methodexception must match a pattern", e.getMessage().startsWith("Method"));
                    }
                }
                if (meth.getName().equals("bench4")) {
                    assertEquals("Check of name of method2", "bench4", meth.getName());
                    assertEquals("Check of runs of method4", 20, BenchmarkMethod.getNumberOfAnnotatedRuns(meth));
                }
            }

        } catch (final Exception e) {
            fail("Should never fail in testRuns!");
        }
    }

    @BenchClass (runs = 20)
    class TestNumberOfAnnotatedRuns {

        @Bench (runs = 10)
        public void bench1 () {
            // Just for getting the method1
        }

        public void bench2 () {
            // Just for getting the method2
        }

        public int bench3 () {
            // Just for getting the method3
            return -1;
        }

        @Bench
        public void bench4 () {
            // Just for getting the method4
        }

    }

    class TestAfterLastRun2 {

        @AfterLastRun
        public final void afterLastRun () {
            // Just for getting the afterLastAnno
        }

        @Bench
        public final void bench () {
            // Just for getting the bench
        }
    }

    class TestAfterLastRun1 {

        boolean order = true;

        @AfterLastRun
        public final void afterLastRunAnno () {
            fail("Should be ignored because of designated afterLastRun");
        }

        public final void afterLastRun () {
            assertTrue(order);
            order = false;
        }

        public final void afterLastRun2 () {
            assertFalse(order);
        }

        @Bench (afterLastRun = "afterLastRun, afterLastRun2")
        public final void bench () {
            // Just a bench
        }
    }

    class TestAfterEachRun2 {

        @AfterEachRun
        public final void afterEachRun () {
            // Just for having an AfterEachRun
        }

        @Bench
        public final void bench () {
            // building the bench
        }
    }

    class TestAfterEachRun1 {

        boolean order = true;

        @AfterEachRun
        public final void afterEachRunAnno () {
            fail("Should be ignored because of designated after each run");
        }

        public final void afterEachRun () {
            assertTrue(order);
            order = false;
        }

        public final void afterEachRun2 () {
            assertFalse(order);
        }

        @Bench (afterEachRun = "afterEachRun, afterEachRun2")
        public final void bench () {
            // Just the bench
        }
    }

    class TestBeforeEachRun2 {

        @BeforeEachRun
        public final void beforeEachRun () {
            // Just the before each run
        }

        @Bench
        public final void bench () {
            // Just the bench
        }
    }

    class TestBeforeEachRun1 {

        boolean order = true;

        @BeforeEachRun
        public final void beforeEachRunAnno () {
            fail("Should be ignored because of designated before each run!");
        }

        public final void beforeEachRun () {
            assertTrue(order);
            order = false;
        }

        public final void beforeEachRun2 () {
            assertFalse(order);
        }

        @Bench (beforeEachRun = "beforeEachRun, beforeEachRun2")
        public final void bench () {
            // Just the bench
        }
    }

    class TestBeforeFirstRun2 {

        @BeforeFirstRun
        public final void beforeFirstRun () {
            // Just the before first run
        }

        @Bench
        public final void bench () {
            // Just the bench
        }
    }

    class TestBeforeFirstRun1 {

        boolean order = true;

        @BeforeFirstRun
        public final void beforeFirstRunAnno () {
            fail("Should be ignored because of designated before first anno");
        }

        public final void beforeFirstRun () {
            assertTrue(order);
            order = false;
        }

        public final void beforeFirstRun2 () {
            assertFalse(order);
        }

        @Bench (beforeFirstRun = "beforeFirstRun, beforeFirstRun2")
        public final void bench () {
            // Just the bench
        }
    }

    class TestIsReflectedExecutable {

        public final void paramMethod (final Object obj) {
            fail("Only param-less methods allowed");
        }

        protected final void notPublicMethod () {
            fail("Only methods with public identifier allowed");
        }

        public final Object returnVal () {
            fail("Only methods without a returnVal allowed");
            return null;
        }

        public final void shouldBeInvoked () {
            // Should be invoked
        }
    }

    class TestFindAndCheckBenchClass {

        @ShouldOccureOnce
        public final void benchAnno1 () {
            // Just for performing a search
        }

        @ShouldOccureOnce
        public final void benchAnno2 () {
            // Just for performing a search
        }
    }

    class TestClassCheckThisMethodAsBenchmarkable1 {
        @Bench
        public final void benchAnno1 () {
            // Just for performing a search
        }

        public final void benchAnno2 () {
            fail("Should not be benched!");
        }
    }

    class TestClassCheckThisMethodAsBenchmarkable2 {

        @SkipBench
        @Bench
        public final void benchAnno3 () {
            fail("Should not be benched!");
        }
    }

    @Retention (RetentionPolicy.RUNTIME)
    @Target (ElementType.METHOD)
    @interface ShouldOccureOnce {

    }
}
