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
 * $Id: ExtendedApiTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.result.IResult;
import org.perfidix.result.ResultContainer;

/**
 * tests the extended api for the new system loaders.
 * 
 * @author onea
 */
public class ExtendedApiTest extends PerfidixTest {

    private ResultContainer<IResult.SingleResult> rc;

    private IMeter carrots;

    private IMeter potatoes;

    /**
     * @throws Exception
     *                 if parent did not work how it should.
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        rc = new IResult.MethodResult("foo");
        carrots = Perfidix.createMeter("carrots", "legumes");
        potatoes = Perfidix.createMeter("potatoes", "legumes");
        rc.append(new IResult.SingleResult(
                "single 1", new long[] { 3 }, carrots));
        rc.append(new IResult.SingleResult(
                "single 2", new long[] { 7 }, carrots));
        rc.append(new IResult.SingleResult(
                "single 3", new long[] { 5 }, potatoes));
        rc.append(Perfidix.createSingleResult("single 4", new long[] { 11 }));
        rc.append(Perfidix
                .createSingleResult("single 5", new long[] { 13, 17 }));
    }

    /**
     * @throws Exception
     *                 if the parent did throw an exception.
     */
    @Override
    @After
    public void tearDown() throws Exception {
        rc = null;
        super.tearDown();
    }

    @Test
    public void testTheMetersAreFetchedCorrectly() {
        assertEquals(carrots, Perfidix.getMeter("carrots"));
        assertEquals(potatoes, Perfidix.getMeter("potatoes"));
    }

    @Test
    public void testMax() {

        assertEquals(30l, rc.max());
        assertEquals(5l, rc.max(potatoes));
        assertEquals(7l, rc.max(Perfidix.getMeter("carrots")));

    }

    @Test
    public void testMin() {
        assertEquals(11l, rc.min());
        assertEquals(5l, rc.min(Perfidix.getMeter("potatoes")));
        assertEquals(3l, rc.min(Perfidix.getMeter("carrots")));
    }

    @Test
    public void testAvg() {
        assertEquals(20.5, rc.avg(), 0);
        assertEquals(5.0, rc.avg(Perfidix.getMeter("potatoes")), 0);
        assertEquals(5.0, rc.avg(Perfidix.getMeter("carrots")), 0);

    }

    @Test
    public void testMean() {
        assertEquals(20.5, rc.mean(), 0);
        assertEquals(5.0, rc.mean(Perfidix.getMeter("potatoes")), 0);
        assertEquals(5.0, rc.mean(Perfidix.getMeter("carrots")), 0);
    }

    @Test
    public void testMedian() {
        assertEquals(20.5, rc.median(), 0);
        assertEquals(5.0, rc.median(Perfidix.getMeter("potatoes")), 0);
        assertEquals(5.0, rc.median(Perfidix.getMeter("carrots")), 0);
    }

    @Test
    public void testConf99() {
        assertEquals(17.304317, rc.getConf99(), PerfidixTest.EPSILON);
        assertEquals(0.0, rc.getConf99(Perfidix.getMeter("potatoes")), 0);
        assertEquals(
                3.643014, rc.getConf99(Perfidix.getMeter("carrots")),
                PerfidixTest.EPSILON);
    }

    @Test
    public void testConf95() {
        assertEquals(13.1663282, rc.getConf95(), PerfidixTest.EPSILON);
        assertEquals(0.0, rc.getConf95(Perfidix.getMeter("potatoes")), 0);
        assertEquals(
                2.7718585, rc.getConf95(Perfidix.getMeter("carrots")),
                PerfidixTest.EPSILON);
    }

    @Test
    public void testVariance() {
        assertEquals(180.5, rc.variance(), 0);
        assertEquals(0.0, rc.variance(Perfidix.getMeter("potatoes")), 0);
        assertEquals(8.0, rc.variance(Perfidix.getMeter("carrots")), 0);
    }

    @Test
    public void testSquareSum() {
        assertEquals(1021l, rc.squareSum());
        assertEquals(25l, rc.squareSum(Perfidix.getMeter("potatoes")));
        assertEquals(58l, rc.squareSum(Perfidix.getMeter("carrots")));
    }

    @Test
    public void testResultCount() {
        assertEquals(2l, rc.resultCount());
        assertEquals(1, rc.resultCount(Perfidix.getMeter("potatoes")));
        assertEquals(2, rc.resultCount(Perfidix.getMeter("carrots")));
    }

    @Test
    public void testStandardDeviation() {
        assertEquals(9.5, rc.getStandardDeviation(), 0);
        assertEquals(0.0, rc
                .getStandardDeviation(Perfidix.getMeter("potatoes")), 0);
        assertEquals(
                2.0, rc.getStandardDeviation(Perfidix.getMeter("carrots")), 0);
    }

}
