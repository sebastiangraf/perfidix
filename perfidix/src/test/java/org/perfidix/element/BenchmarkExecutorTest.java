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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.SkipBench;
import org.perfidix.exceptions.PerfidixMethodCheckException;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.CountingMeter;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Test case for the BenchmarkExecutor. Note that all classes used in this
 * testcase are not allowed to be internal classes because of the reflective
 * invocation. This is not working with encapsulated classes.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class BenchmarkExecutorTest {

    /** Method name to test */
    private final static String METHODNAME = "bench";

    private transient Set<AbstractMeter> meter;
    /** static int to check the beforefirstcounter */
    public static int once;
    /** static int to check the beforeeachcounter */
    public static int each;

    private transient BenchmarkResult res;

    /**
     * Simple SetUp.
     */
    @Before
    public void setUp() {
        res = new BenchmarkResult();
        meter = new LinkedHashSet<AbstractMeter>();
        meter.add(new TimeMeter(Time.MilliSeconds));
        meter.add(new CountingMeter());
        once = 0;
        each = 0;
        BenchmarkExecutor.initialize(meter, res);
    }

    /**
     * Simple tearDown.
     */
    @After
    public void tearDown() {
        meter.clear();
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#getExecutor(org.perfidix.element.BenchmarkElement)}
     */
    @Test
    public void testGetExecutor() {
        final NormalClass getInstanceClass = new NormalClass();
        Method meth;
        try {
            meth = getInstanceClass.getClass().getMethod(METHODNAME);

            final BenchmarkMethod elem1 = new BenchmarkMethod(meth);
            final BenchmarkMethod elem2 = new BenchmarkMethod(meth);

            final BenchmarkExecutor exec1 =
                    BenchmarkExecutor.getExecutor(new BenchmarkElement(
                            elem1));
            final BenchmarkExecutor exec2 =
                    BenchmarkExecutor.getExecutor(new BenchmarkElement(
                            elem2));

            assertEquals("Singleton test of executor", exec1, exec2);
        } catch (final SecurityException e) {
            fail(e.getMessage());
        } catch (final NoSuchMethodException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#executeBeforeMethods(java.lang.Object)}
     */
    @Test
    public void testExecuteBeforeMethods() {
        try {
            final Method meth = BeforeClass.class.getMethod("bench");
            final Object objToExecute = BeforeClass.class.newInstance();

            final BenchmarkMethod elem = new BenchmarkMethod(meth);

            final BenchmarkExecutor exec =
                    BenchmarkExecutor.getExecutor(new BenchmarkElement(
                            elem));

            exec.executeBeforeMethods(objToExecute);
            exec.executeBeforeMethods(objToExecute);

            assertEquals("Once should be inoked once", 1, once);
            assertEquals("Each should be invoked twice", 2, each);
        } catch (final SecurityException e) {
            fail(e.getMessage());
        } catch (final NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (final InstantiationException e) {
            fail(e.getMessage());
        } catch (final IllegalAccessException e) {
            fail(e.getMessage());
        }

    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#executeBench(Object)} .
     */
    @Test
    public void testExecuteBench() {
        try {
            final Method meth = NormalClass.class.getMethod(METHODNAME);
            final Object objToExecute = NormalClass.class.newInstance();
            final BenchmarkMethod elem = new BenchmarkMethod(meth);
            final BenchmarkExecutor exec =
                    BenchmarkExecutor.getExecutor(new BenchmarkElement(
                            elem));
            exec.executeBench(objToExecute);

            assertEquals("Each is invoked just once", 1, each);
            assertEquals(
                    "Set should be included in the frameworks as well",
                    meter, res.getRegisteredMeters());
            final Iterator<ClassResult> classResIter =
                    res.getIncludedResults().iterator();
            final ClassResult classRes = classResIter.next();
            assertFalse(
                    "Iterator of classes should only contain one element",
                    classResIter.hasNext());
            assertEquals(
                    "Meters should all be registered", meter, classRes
                            .getRegisteredMeters());
            assertEquals(
                    "Classes has to be included in a correct way",
                    objToExecute.getClass(), classRes.getRelatedElement());
            assertEquals(
                    "The NormalClass should be included",
                    NormalClass.class, classRes.getRelatedElement());

            final Iterator<MethodResult> methResIter =
                    classRes.getIncludedResults().iterator();
            final MethodResult methRes = methResIter.next();
            assertFalse("Only one result should be there", methResIter
                    .hasNext());
            assertEquals("The set has to match", meter, methRes
                    .getRegisteredMeters());
            assertEquals(
                    "The method should be the same than for the related element",
                    meth, methRes.getRelatedElement());
        } catch (final SecurityException e) {
            fail(e.getMessage());
        } catch (final NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (final InstantiationException e) {
            fail(e.getMessage());
        } catch (final IllegalAccessException e) {
            fail(e.getMessage());
        }

    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#executeAfterMethods(java.lang.Object)}
     */
    @Test
    public void testExecuteAfterMethods() {
        try {
            final Method meth = AfterClass.class.getMethod(METHODNAME);
            final Object objToExecute = AfterClass.class.newInstance();

            final BenchmarkMethod elem = new BenchmarkMethod(meth);

            final BenchmarkExecutor exec =
                    BenchmarkExecutor.getExecutor(new BenchmarkElement(
                            elem));

            exec.executeAfterMethods(objToExecute);
            exec.executeAfterMethods(objToExecute);

            assertEquals("Once should be invoked once", 1, once);
            assertEquals("Each should be invoked twice", 2, each);
        } catch (final SecurityException e) {
            fail(e.getMessage());
        } catch (final NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (final InstantiationException e) {
            fail(e.getMessage());
        } catch (final IllegalAccessException e) {
            fail(e.getMessage());
        }

    }

    /**
     * Test method for
     * {@link org.perfidix.element.BenchmarkExecutor#checkMethod(Object, Method, Class)}
     * and
     * {@link org.perfidix.element.BenchmarkExecutor#invokeMethod(Object, Method, Class)}
     */
    @Test
    public void testCheckAndExecute() {
        try {
            final Object falseObj = new Object();
            final Object correctObj = new CheckAndExecuteClass();

            assertEquals("Two methods should be included", 2, correctObj
                    .getClass().getDeclaredMethods().length);

            final Method correctMethod =
                    CheckAndExecuteClass.class.getMethod("correctMethod");
            final Method falseMethod =
                    CheckAndExecuteClass.class
                            .getMethod("incorrectMethod");

            final PerfidixMethodCheckException excep1 =
                    BenchmarkExecutor.checkMethod(
                            falseObj, correctMethod, SkipBench.class);
            assertNotNull("Exception 1 shouldn't be null", excep1);

            final PerfidixMethodCheckException excep2 =
                    BenchmarkExecutor.checkMethod(
                            correctObj, falseMethod, SkipBench.class);
            assertNotNull("Exception 2 shouldn't be null", excep2);

            final PerfidixMethodCheckException excep3 =
                    BenchmarkExecutor.checkMethod(
                            correctObj, correctMethod, SkipBench.class);
            assertNull("Exception 3 shouldn't be null", excep3);

            final PerfidixMethodInvocationException excep4 =
                    BenchmarkExecutor.invokeMethod(
                            correctObj, correctMethod, SkipBench.class);
            assertNull("Exception 4 shouldn't be null", excep4);

            assertEquals(
                    "invokation of beforeFirst should be occured just once",
                    1, once);
        } catch (final SecurityException e) {
            fail(e.getMessage());
        } catch (final NoSuchMethodException e) {
            fail(e.getMessage());
        }
    }
}

class CheckAndExecuteClass {

    public void correctMethod() {
        BenchmarkExecutorTest.once++;
    }

    public Object incorrectMethod() {
        return null;
    }

}

class NormalClass {

    @Bench
    public void bench() {
        BenchmarkExecutorTest.each++;
    }

}

class AfterClass {

    @Bench
    public void bench() {
        // empty method, just for counting
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

class BeforeClass {

    @Bench
    public void bench() {
        // empty method, just for counting
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
