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
 * $Id: RandomResultTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.MethodResult;
import org.perfidix.result.ResultContainer;
import org.perfidix.result.SingleResult;

/**
 * CHANGELOG: 1.12.2005: fixed standard deviation error.
 */
public class RandomResultTest extends PerfidixTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testOne() {

        double[] data = { 1, Benchmark.LONG_NULLVALUE, 3 };
        AbstractResult r = Perfidix.createSingleResult(data);
        assertEquals(4l, r.sum());
        assertEquals(10l, r.squareSum());
        assertEquals(2.0, r.avg(), 0);
        assertEquals(2l, r.resultCount());
        double expectedDeviation = Math.sqrt(5.0 - 4.0);
        assertEquals(
                expectedDeviation, r.getStandardDeviation(),
                PerfidixTest.EPSILON);
        assertEquals(1, r.min(), PerfidixTest.EPSILON);
        assertEquals(3, r.max(), PerfidixTest.EPSILON);
    }

    /**
     * sum and average methods did not work when the computation included NULL
     * values.
     */
    @Test
    public void testTwo() {

        double[] data1 = { 1, Benchmark.LONG_NULLVALUE, 3 };
        double[] data2 = { Benchmark.LONG_NULLVALUE, 2, 3 };
        double[] data3 = { 1, 2, 3 };

        ResultContainer<SingleResult> container =
                new MethodResult("********BLA********");
        container.append(Perfidix.createSingleResult("hiho", data1));
        container.append(Perfidix.createSingleResult("hoho", data2));
        container.append(Perfidix.createSingleResult("bla", data3));

        assertEquals(15l, container.sum());
        assertEquals(3l, container.resultCount());

        assertEquals(5.0, container.avg(), 0);

        assertEquals(4, container.min(), PerfidixTest.EPSILON);

        assertEquals(6, container.max(), PerfidixTest.EPSILON);

        double[] myResult = { 4, 5, 6 };
        AbstractResult whatEver = Perfidix.createSingleResult(myResult);
        assertEquals(whatEver.getStandardDeviation(), container
                .getStandardDeviation(), 0);

    }

    @Test
    public void testFour() {

        double[] data1 = { 1, 3 };
        double[] data2 = { 1, Benchmark.LONG_NULLVALUE, 3 };
        AbstractResult r1 = Perfidix.createSingleResult("1,3", data1);
        AbstractResult r2 = Perfidix.createSingleResult("1,null,3", data2);
        assertEquals(r1.sum(), r2.sum());
        assertEquals(r1.avg(), r2.avg(), 0);
        assertEquals(r1.getConf95(), r2.getConf95(), 0);
        assertEquals(r1.getConf99(), r2.getConf99(), 0);
        assertEquals(r1.getStandardDeviation(), r2.getStandardDeviation(), 0);
    }

    @Test
    public void testFive() {

        ResultContainer<SingleResult> resultA;
        ResultContainer<SingleResult> resultB;
        ResultContainer rc;

        resultA = new MethodResult("class A");
        resultA.append(Perfidix.createSingleResult("a", new double[] { 1, 1 }));
        resultA.append(Perfidix.createSingleResult("b", new double[] { 1, 1 }));

        resultB = new MethodResult("class B");
        resultB.append(Perfidix.createSingleResult("c", new double[] { 3, 1 }));
        resultB.append(Perfidix.createSingleResult("d", new double[] { 1, 1 }));

        rc = new MethodResult("hellowho?");
        rc.append(resultA);
        rc.append(resultB);
        // startDebug();

        assertEquals(4l, resultA.sum());
        assertEquals(6l, resultB.sum());
        assertEquals(resultA.sum() + resultB.sum(), rc.sum());

    }

}
