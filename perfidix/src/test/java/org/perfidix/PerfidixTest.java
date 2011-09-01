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
