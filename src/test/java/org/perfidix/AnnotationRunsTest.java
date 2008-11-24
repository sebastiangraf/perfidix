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
 * $Id: AnnotationRunsTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.perfidix.Benchmark;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.BenchClass;

public class AnnotationRunsTest {

    private static final int benchRuns = 5;
    private static final int classRuns = 10;
    private static final int methodRuns = 15;
    private int bench1;
    private int bench2;
    private int bench3;

    public void setUp() {
        bench1 = 0;
        bench2 = 0;
        bench3 = 0;
    }

    @Test
    public void testRuns() {
        final Benchmark bench = new Benchmark();
        bench.add(new RunBench1());
        bench.add(new RunBench2());
        bench.run(benchRuns);
        assertEquals(benchRuns, bench1);
        assertEquals(classRuns, bench2);
        assertEquals(methodRuns, bench3);
    }

    class RunBench1 {
        @Bench
        public void benchRun() {
            bench1++;
        }
    }

    @BenchClass(runs = classRuns)
    class RunBench2 {
        public void classRun() {
            bench2++;
        }

        @Bench(runs = methodRuns)
        public void methodRun() {
            bench3++;
        }
    }
}
