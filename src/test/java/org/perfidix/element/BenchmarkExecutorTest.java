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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.AbstractConfig;
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
 * Test case for the BenchmarkExecutor. Note that all classes used in this testcase are not allowed to be internal
 * classes because of the reflective invocation. This is not working with encapsulated classes.
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
    public void setUp () {
        res = new BenchmarkResult();
        meter = new HashSet<AbstractMeter>();
        meter.add(new TimeMeter(Time.MilliSeconds));
        meter.add(new CountingMeter());

        once = 0;
        each = 0;
        BenchmarkExecutor.initialize(new CheckConfig(meter), res);
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkExecutor#getExecutor(org.perfidix.element.BenchmarkElement)}
     */
    @Test
    public void testGetExecutor () {
        final NormalClass getInstanceClass = new NormalClass();
        Method meth;
        try {
            meth = getInstanceClass.getClass().getMethod(METHODNAME);

            final BenchmarkMethod elem1 = new BenchmarkMethod(meth);
            final BenchmarkMethod elem2 = new BenchmarkMethod(meth);

            final BenchmarkExecutor exec1 = BenchmarkExecutor.getExecutor(new BenchmarkElement(elem1));
            final BenchmarkExecutor exec2 = BenchmarkExecutor.getExecutor(new BenchmarkElement(elem2));

            assertEquals("Singleton test of executor", exec1, exec2);
        } catch (final SecurityException e) {
            fail(e.getMessage());
        } catch (final NoSuchMethodException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkExecutor#executeBeforeMethods(java.lang.Object)}
     */
    @Test
    public void testExecuteBeforeMethods () {
        try {
            final Method meth = BeforeClass.class.getMethod("bench");
            final Object objToExecute = BeforeClass.class.newInstance();

            final BenchmarkMethod elem = new BenchmarkMethod(meth);

            final BenchmarkExecutor exec = BenchmarkExecutor.getExecutor(new BenchmarkElement(elem));

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
     * Test method for {@link org.perfidix.element.BenchmarkExecutor#executeBench(Object)} .
     */
    @Test
    public void testExecuteBench () {
        try {
            final Method meth = NormalClass.class.getMethod(METHODNAME);
            final Object objToExecute = NormalClass.class.newInstance();
            final BenchmarkMethod elem = new BenchmarkMethod(meth);
            final BenchmarkExecutor exec = BenchmarkExecutor.getExecutor(new BenchmarkElement(elem));
            exec.executeBench(objToExecute);

            assertEquals("Each is invoked just once", 1, each);
            assertEquals("Set should be included in the frameworks as well", meter, res.getRegisteredMeters());
            final Iterator<ClassResult> classResIter = res.getIncludedResults().iterator();
            final ClassResult classRes = classResIter.next();
            assertFalse("Iterator of classes should only contain one element", classResIter.hasNext());
            assertEquals("Meters should all be registered", meter, classRes.getRegisteredMeters());
            assertEquals("Classes has to be included in a correct way", objToExecute.getClass(), classRes.getRelatedElement());
            assertEquals("The NormalClass should be included", NormalClass.class, classRes.getRelatedElement());

            final Iterator<MethodResult> methResIter = classRes.getIncludedResults().iterator();
            final MethodResult methRes = methResIter.next();
            assertFalse("Only one result should be there", methResIter.hasNext());
            assertEquals("The set has to match", meter, methRes.getRegisteredMeters());
            assertEquals("The method should be the same than for the related element", new BenchmarkMethod(meth), methRes.getRelatedElement());
        } catch (final SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            fail(e.getMessage());
        }

    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkExecutor#executeAfterMethods(java.lang.Object)}
     */
    @Test
    public void testExecuteAfterMethods () {
        try {
            final Method meth = AfterClass.class.getMethod(METHODNAME);
            final Object objToExecute = AfterClass.class.newInstance();

            final BenchmarkMethod elem = new BenchmarkMethod(meth);

            final BenchmarkExecutor exec = BenchmarkExecutor.getExecutor(new BenchmarkElement(elem));

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
     * Test method for {@link org.perfidix.element.BenchmarkExecutor#checkMethod(Object, Method, Class)} and
     * {@link org.perfidix.element.BenchmarkExecutor#invokeMethod(Object, Method, Class)}
     */
    @Test
    public void testCheckAndExecute () {
        try {
            final Object falseObj = new Object();
            final Object correctObj = new CheckAndExecuteClass();

            assertEquals("Two methods should be included", 2, correctObj.getClass().getDeclaredMethods().length);

            final Method correctMethod = CheckAndExecuteClass.class.getMethod("correctMethod");
            final Method falseMethod = CheckAndExecuteClass.class.getMethod("incorrectMethod");

            final PerfidixMethodCheckException excep1 = BenchmarkExecutor.checkMethod(falseObj, SkipBench.class, correctMethod, null);
            assertNotNull("Exception 1 shouldn't be null", excep1);

            final PerfidixMethodCheckException excep2 = BenchmarkExecutor.checkMethod(correctObj, SkipBench.class, falseMethod, null);
            assertNotNull("Exception 2 shouldn't be null", excep2);

            final PerfidixMethodCheckException excep3 = BenchmarkExecutor.checkMethod(correctObj, SkipBench.class, correctMethod, null);
            assertNull("Exception 3 shouldn't be null", excep3);

            final PerfidixMethodInvocationException excep4 = BenchmarkExecutor.invokeMethod(correctObj, SkipBench.class, correctMethod, null);
            assertNull("Exception 4 shouldn't be null", excep4);

            assertEquals("invokation of beforeFirst should be occured just once", 1, once);
        } catch (final SecurityException e) {
            fail(e.getMessage());
        } catch (final NoSuchMethodException e) {
            fail(e.getMessage());
        }
    }
}


class CheckAndExecuteClass {

    public void correctMethod () {
        BenchmarkExecutorTest.once++;
    }

    public Object incorrectMethod () {
        return null;
    }

}


class NormalClass {

    @Bench
    public void bench () {
        BenchmarkExecutorTest.each++;
    }

}


class AfterClass {

    @Bench
    public void bench () {
        // empty method, just for counting
    }

    @AfterLastRun
    public void afterLast () {
        BenchmarkExecutorTest.once++;
    }

    @AfterEachRun
    public void afterEach () {
        BenchmarkExecutorTest.each++;
    }

}


class BeforeClass {

    @Bench
    public void bench () {
        // empty method, just for counting
    }

    @BeforeFirstRun
    public void beforeFirst () {
        BenchmarkExecutorTest.once++;
    }

    @BeforeEachRun
    public void beforeEach () {
        BenchmarkExecutorTest.each++;
    }

}


class CheckConfig extends AbstractConfig {

    protected CheckConfig (Set<AbstractMeter> meter) {
        super(1, meter, AbstractConfig.LISTENERS, AbstractConfig.ARRAN, AbstractConfig.GARBAGE_PROB);
    }

}
