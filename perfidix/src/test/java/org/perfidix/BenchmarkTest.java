/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.AbstractConfig.StandardConfig;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.benchmarktests.BeforeBenchClassError;
import org.perfidix.benchmarktests.NormalBenchForClassAndObjectAdd;
import org.perfidix.benchmarktests.NormalCompleteBench;
import org.perfidix.benchmarktests.NormalIncompleteBench;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.result.BenchmarkResult;

/**
 * Complete test for a normal Benchmark.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class BenchmarkTest {

    private transient Benchmark benchmark;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benchmark = new Benchmark(new StandardConfig());
    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        NormalCompleteBench.reset();
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#run()} .
     */
    @Test
    public void testRunBeforeClassError() {
        benchmark.add(BeforeBenchClassError.class);
        final BenchmarkResult benchRes = benchmark.run();
        assertEquals("Meters should be empty", 0, benchRes
                .getRegisteredMeters().size());
        assertEquals("One Exception should be registered", 1, benchRes
                .getExceptions().size());
        final AbstractPerfidixMethodException exec =
                benchRes.getExceptions().iterator().next();
        assertEquals(
                "The related Anno should be BeforeBenchClass",
                BeforeBenchClass.class, exec.getRelatedAnno());
        assertEquals(
                "The related Exception should be an IllegalStateException",
                IllegalStateException.class, exec.getExec().getClass());

    }

    /**
     * Test method for {@link org.perfidix.Benchmark#run()} .
     */
    @Test
    public void testNormalBenchrun() {
        benchmark.add(NormalCompleteBench.class);
        final Map<BenchmarkMethod, Integer> mapping =
                benchmark.getNumberOfMethodsAndRuns();
        assertEquals("The mapping of methods and runs should be 2", 2, mapping
                .size());
        assertTrue("The mapping contains the number of estimated runs", mapping
                .values().contains(NormalCompleteBench.RUNS));
        final BenchmarkResult benchRes = benchmark.run();
        assertEquals("Only one meter is registered", 1, benchRes
                .getRegisteredMeters().size());
        assertEquals("No expcetion was thrown", 0, benchRes
                .getExceptions().size());

        assertEquals(
                "The BeforeClass-method was invoked once", 1,
                NormalCompleteBench.getBeforeClassCounter());
        assertEquals(
                "The BeforeFirst-Run was invoked twice", 2, NormalCompleteBench
                        .getBeforeFirstRunCounter());
        assertEquals(
                "The number of runs should be equal to the before-each invocations",
                NormalCompleteBench.RUNS + new StandardConfig().getRuns(),
                NormalCompleteBench.getBeforeEachRunCounter());
        assertEquals(
                "The number of runs should be equal to the bench1 invocations",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getBenchCounter1());
        assertEquals(
                "The number of runs should be equal to the bench2 invocations",
                new StandardConfig().getRuns(), NormalCompleteBench
                        .getBenchCounter2());
        assertEquals(
                "The number of runs should be equal to the after-each invocations",
                NormalCompleteBench.RUNS + new StandardConfig().getRuns(),
                NormalCompleteBench.getAfterEachRunCounter());
        assertEquals(
                "The AfterLast-Run was invoked twice", 2, NormalCompleteBench
                        .getAfterLastRunCounter());
        assertEquals(
                "The AfterClass-method was invoked once", 1,
                NormalCompleteBench.getAfterClassCounter());
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#run()} .
     */
    @Test
    public void testIncompleteBenchrun() {
        benchmark.add(NormalIncompleteBench.class);
        final BenchmarkResult benchRes = benchmark.run();
        assertEquals("No Meter is given", 0, benchRes
                .getRegisteredMeters().size());
        assertEquals("No Exception is thrown", 0, benchRes
                .getExceptions().size());
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Class)} .
     */
    @Test
    public void testAddClazz() {
        benchmark.add(NormalCompleteBench.class);
        final BenchmarkResult benchRes = benchmark.run();
        assertEquals("One meter is registered", 1, benchRes
                .getRegisteredMeters().size());
        assertEquals("No exception is thrown", 0, benchRes
                .getExceptions().size());

        assertEquals("Before-Class is invoked once", 1, NormalCompleteBench
                .getBeforeClassCounter());
        assertEquals("Before-First is invoked twice", 2, NormalCompleteBench
                .getBeforeFirstRunCounter());
        assertEquals(
                "Before-Each is invoked as much as bench",
                NormalCompleteBench.RUNS + new StandardConfig().getRuns(),
                NormalCompleteBench.getBeforeEachRunCounter());
        assertEquals(
                "Bench is invoked as much as bench1", NormalCompleteBench.RUNS,
                NormalCompleteBench.getBenchCounter1());
        assertEquals("Bench is invoked as much as bench2", new StandardConfig()
                .getRuns(), NormalCompleteBench.getBenchCounter2());
        assertEquals(
                "After-Each is invoked as much as bench",
                NormalCompleteBench.RUNS + new StandardConfig().getRuns(),
                NormalCompleteBench.getAfterEachRunCounter());
        assertEquals("After-Last is invoked once", 2, NormalCompleteBench
                .getAfterLastRunCounter());
        assertEquals("After-Class is invoked once", 1, NormalCompleteBench
                .getAfterClassCounter());
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Object)} .
     */
    @Test
    public void testAddObject() {
        final NormalCompleteBench obj = new NormalCompleteBench();
        benchmark.add(obj);
        final BenchmarkResult benchRes = benchmark.run();
        assertEquals("One meter is registered", 1, benchRes
                .getRegisteredMeters().size());
        assertEquals("No exception is thrown", 0, benchRes
                .getExceptions().size());

        assertEquals("Before-Class is invoked once", 1, NormalCompleteBench
                .getBeforeClassCounter());
        assertEquals("Before-First is invoked twice", 2, NormalCompleteBench
                .getBeforeFirstRunCounter());
        assertEquals(
                "Before-Each is invoked as much as bench",
                NormalCompleteBench.RUNS + new StandardConfig().getRuns(),
                NormalCompleteBench.getBeforeEachRunCounter());
        assertEquals(
                "Bench is invoked as much as bench1", NormalCompleteBench.RUNS,
                NormalCompleteBench.getBenchCounter1());
        assertEquals("Bench is invoked as much as bench2", new StandardConfig()
                .getRuns(), NormalCompleteBench.getBenchCounter2());
        assertEquals(
                "After-Each is invoked as much as bench",
                NormalCompleteBench.RUNS + new StandardConfig().getRuns(),
                NormalCompleteBench.getAfterEachRunCounter());
        assertEquals("After-Last is invoked twice", 2, NormalCompleteBench
                .getAfterLastRunCounter());
        assertEquals("After-Class is invoked once", 1, NormalCompleteBench
                .getAfterClassCounter());
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Object)} .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateObject() {
        final NormalCompleteBench obj1 = new NormalCompleteBench();
        benchmark.add(obj1);
        final NormalCompleteBench obj2 = new NormalCompleteBench();
        benchmark.add(obj2);
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Object)} and
     * {@link org.perfidix.Benchmark#add(Class)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddObjectAndClass() {
        final NormalCompleteBench obj = new NormalCompleteBench();
        benchmark.add(obj);
        benchmark.add(NormalCompleteBench.class);
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Object)} and
     * {@link org.perfidix.Benchmark#add(Class)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddObjectAndClassWithoutBefore() {
        final NormalBenchForClassAndObjectAdd obj =
                new NormalBenchForClassAndObjectAdd();
        benchmark.add(obj);
        benchmark.add(NormalBenchForClassAndObjectAdd.class);
    }

}
