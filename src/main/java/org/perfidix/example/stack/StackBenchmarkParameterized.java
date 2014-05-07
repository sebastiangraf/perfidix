/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.example.stack;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import org.perfidix.Benchmark;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.DataProvider;
import org.perfidix.example.Config;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;


/**
 * Benching {@link FastIntStack} against {@link Stack}. Just a simple example of Perfidix.
 *
 * @author Sebastian Graf, University of Konstanz
 */
public final class StackBenchmarkParameterized {

    private static final Config config = new Config();

    /**
     * Size of array to be tested.
     */
    private static final int ARRAYSIZE = 100;

    /**
     * FastStack instance.
     */
    private transient FastIntStack fastInt;

    /**
     * Stack instance.
     */
    private transient Stack<Integer> normalInt;

    /**
     * Deque instance.
     */
    private transient ArrayDeque<Integer> arrayDeque;

    /**
     * Simple setUp of a benchmark. The {@link Benchmark} is initialized with two Meters (<code>TimeMeter</code> and
     * <code>MemMeter</code>). Afterwards the benchmark is running with a TabularOutput as a listener registered. The
     * result of the benchmark is displayed in a complete table at the end.
     *
     * @param args not used here
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void main (final String[] args) throws NoSuchMethodException , SecurityException , IllegalAccessException , IllegalArgumentException , InvocationTargetException {

        final Benchmark bench = new Benchmark(config);
        bench.add(StackBenchmarkParameterized.class);

        final BenchmarkResult res = bench.run();
        new TabularSummaryOutput().visitBenchmark(res);
    }

    /**
     * Generating the data, just once per runtime.
     */
    @DataProvider (name = "generateData")
    public Object[][] generateData () {
        final Random ran = new Random();
        final Object[][] intData = new Object[Config.RUNS][];
        for (int i = 0; i < intData.length; i++) {
            final List<Integer> data = new ArrayList<Integer>();
            int counter = 0;
            while (counter < ARRAYSIZE) {
                data.add(ran.nextInt());
                counter++;
            }
            intData[i] = new Object[] { data };
        }
        return intData;
    }

    /**
     * Bench for pushing the data to the {@link FastIntStack}.
     */
    @Bench (dataProvider = "generateData")
    public void benchFastIntPush (final List<Integer> intData) {
        fastInt = new FastIntStack();
        for (final Object i : intData) {
            fastInt.push((Integer) i);
        }
    }

    public void benchFastIntPush () {
        Object[][] data = generateData();
        benchFastIntPush((List<Integer>) data[0][0]);
    }

    /**
     * Bench for popping the data from the {@link FastIntStack}.
     */
    @Bench (beforeEachRun = "benchFastIntPush")
    public void benchFastIntStackPop () {
        while (fastInt.size() > 0) {
            fastInt.pop();

        }
    }

    /**
     * Bench for pushing the data to the {@link Stack}.
     */
    @Bench (dataProvider = "generateData")
    public void benchNormalIntPush (final List<Integer> intData) {
        normalInt = new Stack<Integer>();
        for (final int i : intData) {
            normalInt.push(i);
        }
    }

    public void benchNormalIntPush () {
        Object[][] data = generateData();
        benchNormalIntPush((List<Integer>) data[0][0]);
    }

    /**
     * Bench for popping the data from the {@link Stack}.
     */
    @Bench (beforeEachRun = "benchNormalIntPush")
    public void benchNormalIntPop () {
        while (normalInt.size() > 0) {
            normalInt.pop();
        }
    }

    /**
     * Bench for pushing the data to the {@link ArrayDeque}.
     */
    @Bench (dataProvider = "generateData")
    public void benchArrayDequePush (final List<Integer> intData) {
        arrayDeque = new ArrayDeque<Integer>();
        for (final int i : intData) {
            arrayDeque.push(i);
        }
    }

    public void benchArrayDequePush () {
        Object[][] data = generateData();
        benchArrayDequePush((List<Integer>) data[0][0]);
    }

    /**
     * Bench for popping the data from the {@link ArrayDeque}.
     */
    @Bench (beforeEachRun = "benchArrayDequePush")
    public void benchArrayDequeStackPop () {

        while (arrayDeque.size() > 0) {
            arrayDeque.pop();
        }
    }

}
