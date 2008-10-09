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

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultiTimersSorted extends PerfidixTest {

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
        IMeter a = Perfidix.createMeter("someMeter", "a");

        Benchmark bm = new Benchmark("test benchmark");
        bm.useMilliMeter();
        bm.add(new A(a));
        bm.register(a);
        // System.out.println("running ... ");
        // startDebug();
        IResult r = bm.run(1);
        String[] s =
                {
                        "benchOne", "a", "ms", "summary for A", "ms", "a",
                        "summary for test benchmark", "ms", "a" };
        String pattern = ".*" + NiceTable.Util.implode(".*", s) + ".*";
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = p.matcher(r.toString());
        assertTrue(m.matches());

    }

    public class A {
        private IMeter a;

        public A(final IMeter aa) {
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
