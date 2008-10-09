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
 * $Id: PerfidixTimerTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PerfidixTimerTest extends PerfidixTest {

    private final int numRuns = 10;

    /**
     * tests the new timer api.
     */
    @Test
    public void testAPI1() {
        Benchmark bm = new Benchmark();
        bm.add(new T());
        bm.useNanoMeter();
        Perfidix.createMeter("fileToucher", "ft");
        IResult r = bm.run(numRuns);
        r.toString(); // no debugging output.

        getLog().info(r.toString());
    }

    @Test
    public void testAPI2() {
        Benchmark bm = new Benchmark();
        bm.add(new T());
        bm.useMilliMeter();
        IResult r = bm.run(numRuns);
        // startDebug();
        getLog().info("\n\n" + r.toString());
        // stopDebug();
    }

    @Test
    public void testAPI3() {
        Perfidix.createMeter("myMeter", "ding");
        assertEquals(0l, Perfidix.getMeter("myMeter").getValue());
        Perfidix.getMeter("myMeter").tick();
        assertEquals(1l, Perfidix.getMeter("myMeter").getValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDoNotMixCarrotsWithPotatoes() {
        ResultContainer rc = new IResult.MethodResult("rabbit");
        IMeter carrotCounter =
                Perfidix.createMeter("carrotCounter", "vegetables");
        IMeter potatoeCounter =
                Perfidix.createMeter("potatoeCounter", "vegetables");
        // startDebug();
        rc.append(new IResult.SingleResult(
                "carrotEater", new long[] { 3 }, Perfidix
                        .getMeter("carrotCounter")));
        rc.append(new IResult.SingleResult("hello", new long[] { 7 }, Perfidix
                .getMeter("potatoeCounter")));
        rc
                .append(Perfidix.createSingleResult(
                        "time elapsed", new long[] { 5 }));

        assertEquals("the default max() action may only return the results for"
                + " elapsed time", 5l, rc.max());

        assertEquals(7l, rc.max(potatoeCounter));
        assertEquals(3l, rc.max(carrotCounter));

    }

    @Test
    public void testMixedCounters1() {
        ResultContainer<IResult.SingleResult> rc =
                new IResult.MethodResult("bla");
        Perfidix.createMeter("a", "foo");
        Perfidix.createMeter("b", "bar");
        rc.append(new IResult.SingleResult("a", new long[] { 3 }, Perfidix
                .getMeter("a")));
        rc.append(new IResult.SingleResult("b", new long[] { 7 }, Perfidix
                .getMeter("a")));
        rc.append(new IResult.SingleResult("c", new long[] { 5 }, Perfidix
                .getMeter("b")));

    }

    public class T {

        @Bench
        public void benchOne() {
            IMeter m = Perfidix.getMeter("fileToucher");
            // IMeter m2 = Perfidix.getMeter("dongToucher");
            for (int i = 0; i < 1000; i++) {
                Math.random();
                if (i % 10 == 0) {
                    m.tick();
                }
                if (i % 100 == 0) {
                    // m2.tick();
                }
            }

        }
    }

}
