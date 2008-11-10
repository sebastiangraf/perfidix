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
 * $Id: MultipleResultTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.perfidix.result.MethodResult;
import org.perfidix.result.ResultContainer;
import org.perfidix.result.SingleResult;

/**
 * tests the multiple results.
 */
public class MultipleResultTest extends PerfidixTest {

    /**
     * tests that the calculations are invoked correctly.
     */
    @Test
    public void testCalc() {

        double[] r1Set = { 1, 2, 3 };
        double[] r2Set = { 4, 5, 6 };

        SingleResult r1 = Perfidix.createSingleResult(r1Set);
        SingleResult r2 = Perfidix.createSingleResult(r2Set);
        assertEquals(2.0, r1.avg(), 0);
        assertEquals(5.0, r2.avg(), 0);
        assertEquals(1.0, r1.min(), 0);
        assertEquals(4.0, r2.min(), 0);
        assertEquals(3.0, r1.max(), 0);
        assertEquals(6.0, r2.max(), 0);
        assertEquals(3, r2.resultCount());
        assertEquals(3, r1.resultCount());
        assertEquals(2.0, r1.median(), 0);
        assertEquals(5.0, r2.median(), 0);

        ResultContainer<SingleResult> res =
                new MethodResult("*** testing container ***");
        res.append(r1);
        res.append(r2);
        assertEquals(2, res.resultCount());
        assertEquals(21, res.sum());
        assertEquals(21.0 / 2.0, res.avg(), 0);
        assertEquals(6, res.min(), PerfidixTest.EPSILON);
        assertEquals(15, res.max(), PerfidixTest.EPSILON);
        assertEquals(10.5, res.median(), 0);

    }

    @Test
    public void testCalcTwo() {

        SingleResult r =
                Perfidix.createSingleResult("just another test", new double[] {
                        5, 6, 8, 9 });

        getLog().info("standardDeviation(): " + r.getStandardDeviation());

        assertEquals(1.581138, r.getStandardDeviation(), PerfidixTest.EPSILON);

    }

}
