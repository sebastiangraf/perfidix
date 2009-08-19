/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
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
package org.perfidix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.benchmarktestClasses.BeforeBenchClassError;
import org.perfidix.benchmarktestClasses.NormalBenchForClassAndObjectAdd;
import org.perfidix.benchmarktestClasses.NormalCompleteBench;
import org.perfidix.benchmarktestClasses.NormalIncompleteBench;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.result.BenchmarkResult;

/**
 * Complete test for a normal Benchmark.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class BenchmarkTest {

    private transient Benchmark benchmark;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benchmark = new Benchmark();
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
     * Test method for
     * {@link org.perfidix.Benchmark#run(double,org.perfidix.element.KindOfArrangement, org.perfidix.ouput.AbstractOutput[])}
     * .
     */
    @Test
    public final void testRunBeforeClassError() {
        benchmark.add(BeforeBenchClassError.class);
        final BenchmarkResult benchRes =
                benchmark.run(1.0, KindOfArrangement.NoArrangement);
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
     * Test method for
     * {@link org.perfidix.Benchmark#run(double,org.perfidix.element.KindOfArrangement, org.perfidix.ouput.AbstractOutput[])}
     * .
     */
    @Test
    public final void testNormalBenchrun() {
        benchmark.add(NormalCompleteBench.class);
        final Map<BenchmarkMethod, Integer> mapping =
                benchmark.getNumberOfMethodsAndRuns();
        assertEquals(
                "The mapping of methods and runs should be 1", 1, mapping
                        .size());
        assertTrue(
                "The mapping contains the number of estimated runs",
                mapping.values().contains(NormalCompleteBench.RUNS));
        final BenchmarkResult benchRes =
                benchmark.run(1.0, KindOfArrangement.NoArrangement);
        assertEquals("Only one meter is registered", 1, benchRes
                .getRegisteredMeters().size());
        assertEquals("No expcetion was thrown", 0, benchRes
                .getExceptions().size());

        assertEquals(
                "The BeforeClass-method was invoked once", 1,
                NormalCompleteBench.getBeforeClassCounter());
        assertEquals(
                "The BeforeFirst-Run was invoked once", 1,
                NormalCompleteBench.getBeforeFirstRunCounter());
        assertEquals(
                "The number of runs should be equal to the before-each invocations",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getBeforeEachRunCounter());
        assertEquals(
                "The number of runs should be equal to the bench invocations",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getBenchCounter());
        assertEquals(
                "The number of runs should be equal to the after-each invocations",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getAfterEachRunCounter());
        assertEquals(
                "The AfterLast-Run was invoked once", 1,
                NormalCompleteBench.getAfterLastRunCounter());
        assertEquals(
                "The AfterClass-method was invoked once", 1,
                NormalCompleteBench.getAfterClassCounter());
    }

    /**
     * Test method for
     * {@link org.perfidix.Benchmark#run(double,org.perfidix.element.KindOfArrangement, org.perfidix.ouput.AbstractOutput[])}
     * .
     */
    @Test
    public final void testIncompleteBenchrun() {
        benchmark.add(NormalIncompleteBench.class);
        final BenchmarkResult benchRes =
                benchmark.run(1.0, KindOfArrangement.NoArrangement);
        assertEquals("No Meter is given", 0, benchRes
                .getRegisteredMeters().size());
        assertEquals("No Exception is thrown", 0, benchRes
                .getExceptions().size());
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Class)} .
     */
    @Test
    public final void testAddClazz() {
        benchmark.add(NormalCompleteBench.class);
        final BenchmarkResult benchRes =
                benchmark.run(1.0, KindOfArrangement.NoArrangement);
        assertEquals("One meter is registered", 1, benchRes
                .getRegisteredMeters().size());
        assertEquals("No exception is thrown", 0, benchRes
                .getExceptions().size());

        assertEquals(
                "Before-Class is invoked once", 1, NormalCompleteBench
                        .getBeforeClassCounter());
        assertEquals(
                "Before-First is invoked once", 1, NormalCompleteBench
                        .getBeforeFirstRunCounter());
        assertEquals(
                "Before-Each is invoked as much as bench",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getBeforeEachRunCounter());
        assertEquals(
                "Bench is invoked as much as bench",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getBenchCounter());
        assertEquals(
                "After-Each is invoked as much as bench",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getAfterEachRunCounter());
        assertEquals("After-Last is invoked once", 1, NormalCompleteBench
                .getAfterLastRunCounter());
        assertEquals("After-Class is invoked once", 1, NormalCompleteBench
                .getAfterClassCounter());
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Object)} .
     */
    @Test
    public final void testAddObject() {
        final NormalCompleteBench obj = new NormalCompleteBench();
        benchmark.add(obj);
        final BenchmarkResult benchRes =
                benchmark.run(1.0, KindOfArrangement.NoArrangement);
        assertEquals("One meter is registered", 1, benchRes
                .getRegisteredMeters().size());
        assertEquals("No exception is thrown", 0, benchRes
                .getExceptions().size());

        assertEquals(
                "Before-Class is invoked once", 1, NormalCompleteBench
                        .getBeforeClassCounter());
        assertEquals(
                "Before-First is invoked once", 1, NormalCompleteBench
                        .getBeforeFirstRunCounter());
        assertEquals(
                "Before-Each is invoked as much as bench",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getBeforeEachRunCounter());
        assertEquals(
                "Bench is invoked as much as bench",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getBenchCounter());
        assertEquals(
                "After-Each is invoked as much as bench",
                NormalCompleteBench.RUNS, NormalCompleteBench
                        .getAfterEachRunCounter());
        assertEquals("After-Last is invoked once", 1, NormalCompleteBench
                .getAfterLastRunCounter());
        assertEquals("After-Class is invoked once", 1, NormalCompleteBench
                .getAfterClassCounter());
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Object)} and
     * {@link org.perfidix.Benchmark#add(Class)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testAddObjectAndClass() {
        final NormalCompleteBench obj = new NormalCompleteBench();
        benchmark.add(obj);
        benchmark.add(NormalCompleteBench.class);
    }

    /**
     * Test method for {@link org.perfidix.Benchmark#add(Object)} and
     * {@link org.perfidix.Benchmark#add(Class)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testAddObjectAndClassWithoutBefore() {
        final NormalBenchForClassAndObjectAdd obj =
                new NormalBenchForClassAndObjectAdd();
        benchmark.add(obj);
        benchmark.add(NormalBenchForClassAndObjectAdd.class);
    }

}
