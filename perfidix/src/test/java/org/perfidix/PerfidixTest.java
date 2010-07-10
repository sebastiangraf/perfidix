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
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.perfidix.benchmarktests.ToTestConfig;

/**
 * Testcase for {@link Perfidix}
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class PerfidixTest {

    /**
     * Test method for
     * {@link org.perfidix.Perfidix#runBenchs(java.lang.String[])}.
     */
    @Test
    public void testRunBenchs() {
        final String[] benchs =
                { "org.perfidix.benchmarktests.NormalCompleteBench" };
        try {
            Perfidix.runBenchs(benchs);
        } catch (final ClassNotFoundException e) {
            fail(e.toString());
        } catch (final InstantiationException e) {
            fail(e.toString());
        } catch (final IllegalAccessException e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.Perfidix#setUpBenchmark(java.lang.String[], org.perfidix.Benchmark)}
     * .
     */
    @Test
    public void testSetUpBenchmark() {
        final String[] benchs =
                { "org.perfidix.benchmarktests.NormalCompleteBench" };
        final Benchmark bench = new Benchmark();
        try {
            Perfidix.setUpBenchmark(benchs, bench);
        } catch (final ClassNotFoundException e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for
     * {@link org.perfidix.Perfidix#getConfiguration(java.lang.String[])}.
     */
    @Test
    public void testGetConfiguration() {
        final String[] confs = { "org.perfidix.benchmarktests.ToTestConfig" };
        try {
            final AbstractConfig conf = Perfidix.getConfiguration(confs);
            assertEquals(
                    "runs must be the same", conf.getRuns(),
                    ToTestConfig.TESTRUNS);
            assertArrayEquals(
                    "meters must be the same", conf.getMeters(),
                    ToTestConfig.TESTMETERS);
            assertArrayEquals(
                    "listeners must be the same", conf.getListener(),
                    ToTestConfig.TESTLISTENER);
            assertEquals(
                    "arrangement must be the same", conf.getArrangement(),
                    ToTestConfig.TESTARR);
            assertEquals(
                    "gc prob must be the same", conf.getGcProb(),
                    ToTestConfig.TESTGC, 0);
        } catch (final ClassNotFoundException e) {
            fail(e.toString());
        } catch (final InstantiationException e) {
            fail(e.toString());
        } catch (final IllegalAccessException e) {
            fail(e.toString());
        }
    }

    /**
     * Test method for {@link org.perfidix.Perfidix#main(java.lang.String[])}.
     */
    @Test
    public void testMain() {
        final String[] benchs =
                { "org.perfidix.benchmarktests.NormalCompleteBench" };
        try {
            Perfidix.runBenchs(benchs);
        } catch (final ClassNotFoundException e) {
            fail(e.toString());
        } catch (final InstantiationException e) {
            fail(e.toString());
        } catch (final IllegalAccessException e) {
            fail(e.toString());
        }
    }
}
