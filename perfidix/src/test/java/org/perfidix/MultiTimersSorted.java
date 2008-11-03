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
 * $Id: MultiTimersSorted.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.meter.CountingMeter;

public class MultiTimersSorted {

    @Test
    public void testOne() {
        // CountingMeter a = Perfidix.createMeter("someMeter", "a");
        //
        // Benchmark bm = new Benchmark("test benchmark");
        // bm.useMilliMeter();
        // bm.add(new A(a));
        // bm.register(a);
        // // System.out.println("running ... ");
        // // startDebug();
        // IResult r = bm.run(1);
        // String[] s =
        // {
        // "benchOne", "ms", "a", "summary for A", "a", "ms",
        // "summary for test benchmark", "a", "ms" };
        // String pattern = ".*" + NiceTable.Util.implode(".*", s) + ".*";
        // Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        // Matcher m = p.matcher(r.toString());
        // assertTrue(m.matches());

    }

    public class A {
        private CountingMeter a;

        public A(final CountingMeter aa) {
            a = aa;
        }

        @Bench
        public void benchOne() {
            a.tick();
            a.tick();
            tick();
        }

        private boolean rand() {
            return Math.random() > 0.5;
        }

        private void tick() {
            if (rand()) {
                a.tick();
            }
        }

    }

}
