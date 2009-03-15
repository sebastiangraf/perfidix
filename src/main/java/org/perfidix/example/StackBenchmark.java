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
 * $Revision$
 * $Author$
 * $Date$
 *
 */
package org.perfidix.example;

import java.util.Random;
import java.util.Stack;

import org.perfidix.Benchmark;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.Bench;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.MemMeter;
import org.perfidix.meter.Memory;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * Benching {@link FastIntStack} against {@link Stack}. Just a simple example of
 * Perfidix.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class StackBenchmark {

    /**
     * Number of runs.
     */
    private static final int RUNS = 100;

    /**
     * Size of array to be tested.
     */
    private static final int ARRAYSIZE = 100;

    /** Data to be written and read. */
    private int[] intData;

    /** FastStack instance. */
    private FastIntStack fastInt;

    /** Stack instance. */
    private Stack<Integer> normalInt;

    /**
     * Generating the data, just once per runtime.
     */
    @BeforeBenchClass
    public void generateData() {
        final Random ran = new Random();
        intData = new int[ARRAYSIZE];
        int i = 0;
        while (i < ARRAYSIZE) {
            intData[i] = ran.nextInt();
            i++;
        }
    }

    /**
     * Bench for pushing the data to the {@link FastIntStack}.
     */
    @Bench(runs = RUNS)
    public void benchFastIntPush() {
        fastInt = new FastIntStack();
        for (final int i : intData) {
            fastInt.push(i);
        }
    }

    /**
     * Bench for popping the data from the {@link FastIntStack}.
     */
    @Bench(runs = RUNS, beforeEachRun = "benchFastIntPush")
    public void benchFastIntStackPop() {

        while (fastInt.size() > 0) {
            fastInt.pop();

        }
    }

    /**
     * Bench for pushing the data to the {@link Stack}.
     */
    @Bench(runs = RUNS)
    public void benchNormalIntPush() {
        normalInt = new Stack<Integer>();
        for (final int i : intData) {
            normalInt.push(i);
        }
    }

    /**
     * Bench for popping the data from the {@link Stack}.
     */
    @Bench(runs = RUNS, beforeEachRun = "benchNormalIntPush")
    public void benchNormalIntPop() {
        while (normalInt.size() > 0) {
            normalInt.pop();
        }
    }

    /**
     * Simple setUp of a benchmark. The {@link Benchmark} is initialized with
     * two Meters ({@link TimeMeter} and {@link MemMeter}). Afterwards the
     * benchmark is running with a TabularOutput as a listener registered. The
     * result of the benchmark is displayed in a complete table at the end.
     * 
     * @param args
     *            not used here
     */
    public static void main(final String[] args) {
        final Benchmark bench =
                new Benchmark(
                        new TimeMeter(Time.MilliSeconds), new MemMeter(
                                Memory.KibiByte));
        bench.add(StackBenchmark.class);

        final BenchmarkResult res =
                bench.run(1.0, KindOfArrangement.ShuffleArrangement);
        new TabularSummaryOutput().visitBenchmark(res);
    }

}
