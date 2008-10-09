/*
 * Copyright 2007 University of Konstanz
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
 * $Id: APITest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class APITest extends PerfidixTest {

    /**
     * running two methods: A -> 1,2,3 B -> 2,2,2 --------------- sum: (1+2+3) +
     * (2+2+2) = 6 + 6 = 12. avg: ((1+2+3) + (2+2+2)) / 2 = 12 / 2 = 6
     */
    @Test
    public void testMultipleAvg() {

        long[][] set = { { 1, 2, 3 }, { 2, 2, 2 }, };
        IResult.SingleResult r1 =
                Perfidix.createSingleResult("testMultipleAvg()A", set[0]);
        IResult.SingleResult r2 =
                Perfidix.createSingleResult("testMultipleAvg()B", set[1]);
        ResultContainer<IResult.SingleResult> cnt =
                new IResult.MethodResult("test_multipleAvg");
        cnt.append(r1);
        cnt.append(r2);

        assertEquals(12l, cnt.sum());
        assertEquals(6.0, cnt.avg(), 0);

    }

    @Test
    public void testMultipleMin() {

        long[][] set = { { 1, 2, 3 }, { 1, 2, 3 }, { 1, 2, 3 } };
        IResult.SingleResult r1 = Perfidix.createSingleResult(set[0]);
        IResult.SingleResult r2 = Perfidix.createSingleResult(set[1]);
        IResult.SingleResult r3 = Perfidix.createSingleResult(set[2]);
        ResultContainer<IResult.SingleResult> cnt =
                new IResult.MethodResult("has a name");
        cnt.append(r1);
        cnt.append(r2);
        cnt.append(r3);

        getLog().info("the resulting table looks like");
        getLog().info(NiceTable.Util.implode(",", cnt.getResultSet()));

        getLog().info("\n" + cnt);

        long[] resultSet = cnt.getResultSet();
        assertEquals(3, resultSet.length);
        assertEquals(6l, resultSet[0]);
        assertEquals(6l, resultSet[1]);
        assertEquals(6l, resultSet[2]);
        getLog().info("\n" + cnt);
        assertEquals(6l, cnt.min());

        assertEquals(6l, cnt.max());
        assertEquals(6.0, cnt.avg(), 0);
    }

    @Test
    public void test1() {

        Benchmark bench = new Benchmark();
        assertEquals(Benchmark.class.toString(), bench.getName());
        Benchmark bench2 = new Benchmark("foo");
        assertEquals("foo", bench2.getName());

    }

    /**
     * this one tries to run a test class. the BenchmarkContainer should run all
     * the methods involved. right now, there's only one method there, namely
     * "firstMethod" which should be called. the SomeTestClass keeps a boolean
     * telling that the first method was really involved.
     */
    @Test
    public void testRunMethods() {

        Benchmark bench = new Benchmark();
        SomeTestClass cl = new SomeTestClass();
        assertFalse(cl.firstMethodInvoked);
        cl.benchFirstMethod();
        assertTrue(cl.firstMethodInvoked);
        cl.firstMethodInvoked = false;
        cl.benchSecondMethod();
        assertTrue(cl.secondMethodInvoked);
        cl.secondMethodInvoked = false;

        bench.add(cl);
        bench.run();
        assertTrue(
                "should have invoked the first method", cl.firstMethodInvoked);
        assertTrue(
                "should have invoked the second method", cl.secondMethodInvoked);

    }

    /**
     * checks whether the number of invocations is handled correctly.
     */
    @Test
    public void testInvocations() {

        Benchmark bm = new Benchmark();
        InvocationTestClass ding = new InvocationTestClass();
        bm.add(ding);
        assertEquals(0, ding.numInvocations);
        int invokeCount = 5;
        bm.run(invokeCount);
        assertEquals(
                "it should run as many invocations i tell it....", invokeCount,
                ding.numInvocations);

        InvocationTestClass dong = new InvocationTestClass();
        Benchmark bmDong = new Benchmark();
        bmDong.add(dong);
        bmDong.run(32);
        assertEquals(32, dong.numInvocations);

    }

    @Test
    public void testFibIsWorking() {

        FibTestClass f = new FibTestClass();
        int[][] fResults =
                { { 1, 1 }, { 2, 2, }, { 3, 3 }, { 4, 5 }, { 5, 8 }, { 6, 13 } };
        for (int i = 0; i < fResults.length; i++) {
            assertEquals(fResults[i][1], f.fib(fResults[i][0]));
        }
    }

    @Test
    public void testGetResult() {

        Benchmark bm = new Benchmark();

        FibTestClass ding = new FibTestClass();
        bm.add(ding);
        IResult r = bm.run(5);
        getLog().info(r.toString());
    }

    @Test
    public void testTheResult() {

        long[] testResultSet = { 1, 2, 3 };
        Result res = Perfidix.createSingleResult(testResultSet);
        assertEquals(3l, res.resultCount());
        assertEquals(2.0, res.median(), 0);
        assertEquals(2.0, res.avg(), 0);
    }

    /**
     * tests a single median.
     */
    @Test
    public void testAnotherMedian() {

        long[] testResultSet = { 3, 4, 3, 5, 20, 50 };
        // sorted: 3,3,4,5,20,50
        Result res =
                new IResult.SingleResult("test", testResultSet, Perfidix
                        .defaultMeter());
        assertEquals(0.5 * (4.0 + 5.0), res.median(), 0);
    }

    /**
     * tests the computation of the average on a single result set.
     */
    @Test
    public void testAvg() {

        long[] testResultSet = { 7, 8, 9, 1, 2, 3, 6, 5, 4 };
        Result res =
                new IResult.SingleResult("test", testResultSet, Perfidix
                        .defaultMeter());
        assertEquals(5.0, res.avg(), 0);

    }

    /**
     * tests whether the average calculation works correctly on
     * BenchmarkContainers. changelog: ------------------- 07.03.2006 (axo): i
     * modified the average computation; thus the average of a method is the
     * overall-average of the runs and not the average of the sums.
     */
    @Test
    public void testAvgContainer() {

        long[][] testResultSet = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        ResultContainer<IResult.SingleResult> res =
                new IResult.MethodResult("test");
        for (int i = 0; i < testResultSet.length; i++) {
            res.append(new IResult.SingleResult(
                    "bla" + i, testResultSet[i], Perfidix.defaultMeter()));
        }
        assertEquals(3l, res.resultCount());
        assertEquals(6l + 15l + 24l, res.sum());
        assertEquals(
                (double) ((1l + 4l + 7l) + (2l + 5l + 8l) + (3l + 6l + 9l)) / 3l,
                res.avg(), 0l);
        assertEquals(15l, res.median(), 0);
    }

    @Test
    public void testMinMax() {

        long[] testResultSet = { 1, 2, 3 };
        Result res = Perfidix.createSingleResult(testResultSet);
        assertEquals(1l, res.min());
        assertEquals(3l, res.max());

        long[] trS = {};
        Result rs = Perfidix.createSingleResult(trS);
        assertEquals(0l, rs.min());
        assertEquals(0l, rs.max());

    }

    @Test
    public void testVariance() {

        long[] r = { 45, 23, 55, 32, 51, 91, 74, 53, 70, 84 };
        Result res = Perfidix.createSingleResult(r);
        assertEquals(57.8, res.mean(), 0);
        double resVariance = res.variance();
        assertEquals(
                "expecting 479.333, but receiving " + resVariance,
                479.7333333333, resVariance, PerfidixTest.EPSILON);
        long[] r2 = { 64, 75, 95, 56, 44, 130, 106, 80, 87, 115 };
        Result res2 = Perfidix.createSingleResult(r2);
        assertEquals(85.2, res2.mean(), 0);
        double res2Variance = res2.variance();
        double res2Expected = 728.62222222;
        assertEquals(
                "expected " + res2Expected + ", but received " + res2Variance,
                res2Variance, res2Expected, PerfidixTest.EPSILON);

    }

    @Test
    public void testSquareSum() {

        long[] r = { 1, 2, 3 };
        Result res = Perfidix.createSingleResult(r);
        assertEquals(14l, res.squareSum());
        assertEquals(0.81649, res.getStandardDeviation(), 0.0001);
        assertEquals(1.0, res.variance(), 0);

        long[] r2 = { 1, 2, 2, 3, 4, 4 };
        Result ra = Perfidix.createSingleResult(r2);
        assertEquals(2.666666, ra.avg(), PerfidixTest.EPSILON);
        assertEquals(50l, ra.squareSum());
        assertEquals(2.50, ra.median(), PerfidixTest.EPSILON);
        double expectedDeviation = Math.sqrt((50.0 / 6.0) - (64.0 / 9.0));

        assertEquals(
                expectedDeviation, ra.getStandardDeviation(),
                PerfidixTest.EPSILON);

        assertEquals(1.466666, ra.variance(), PerfidixTest.EPSILON);

    }

    @Test
    public void testConfidence() {

        long[] r = { 1, 2, 3 };
        Result res = Perfidix.createSingleResult(r);

        double a = res.getConf99();
        double b = res.getConf95();

        double aShouldBe =
                2.576 * (res.getStandardDeviation() / Math.sqrt(res
                        .resultCount()));
        double bShouldBe =
                1.96 * (res.getStandardDeviation() / Math.sqrt(res
                        .resultCount()));

        assertEquals(aShouldBe, a, 0);
        assertEquals(bShouldBe, b, 0);
    }

    @Test
    public void testStddev() {

        long[] r = { 1, 2, 3 };
        long[] s = { 1, 2, 3 };

        double expStandardDeviation = Math.sqrt((14.0 / 3.0) - 4.0);

        IResult.SingleResult a = Perfidix.createSingleResult(r);
        IResult.SingleResult b = Perfidix.createSingleResult(s);
        ResultContainer<IResult.SingleResult> c =
                new IResult.MethodResult("bla");
        c.append(a);
        c.append(b);

        assertEquals(
                expStandardDeviation, a.getStandardDeviation(),
                PerfidixTest.EPSILON);
        assertEquals(
                expStandardDeviation, b.getStandardDeviation(),
                PerfidixTest.EPSILON);

        assertEquals(6l, c.min());
        assertEquals(6l, c.max());
    }

    /**
     * just another test class, which is working a bit in order to be able to
     * calculate some details.
     */
    public class FibTestClass {

        @Bench
        public void benchA() {
            fib(30);
        }

        public int fib(final int num) {
            if (num < 2) {
                return 1;
            }
            return fib(num - 1) + fib(num - 2);
        }
    }

    /**
     * this is a test class, showing that the invocation of methods is
     * implemented and working.
     */
    public class SomeTestClass {

        private boolean firstMethodInvoked = false;

        private boolean secondMethodInvoked = false;

        @Bench
        public void benchFirstMethod() {

            firstMethodInvoked = true;
        }

        @Bench
        public void benchSecondMethod() {
            secondMethodInvoked = true;
        }
    }

    /**
     * a test class counting how many times a method was invoked.
     */
    public class InvocationTestClass {

        private int numInvocations = 0;

        @Bench
        public void benchSomeMethod() {
            numInvocations++;
        }

    }

}
