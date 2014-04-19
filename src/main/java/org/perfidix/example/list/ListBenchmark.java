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
/**
 *
 */
package org.perfidix.example.list;


import org.perfidix.Benchmark;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.Bench;
import org.perfidix.example.Config;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;


/**
 * This benchmark benches [@link IntArrayList], [@link java.util.ArrayList] and [@link java.util.Vector]
 *
 * @author Nuray Guerler, University of Konstanz
 */
public class ListBenchmark extends ElementList {

    /**
     * Number of runs.
     */
    private static final int RUNS = 100;

    /**
     * Size of array to be tested.
     */
    private static final int ARRAYSIZE = 100;

    /**
     * Data to be written and read.
     */
    private transient int[] intData;

    /**
     * IntArrayList instance.
     */
    private transient IntArrayList list;

    /**
     * ArrayList instance
     */
    private transient ArrayList<Integer> arrayList;

    /**
     * vector instance
     */
    private transient Vector<Integer> vector;

    /**
     * Simple setUp of a benchmark. The {@link Benchmark} is initialized with two Meters (<code>TimeMeter</code> and
     * <code>MemMeter</code>). Afterwards the benchmark is running with a TabularOutput as a listener registered. The
     * result of the benchmark is displayed in a complete table at the end.
     *
     * @param args not used here
     */
    public static void main(String[] args) {
        final Benchmark bench = new Benchmark(new Config());
        bench.add(ListBenchmark.class);

        final BenchmarkResult res = bench.run();
        new TabularSummaryOutput().visitBenchmark(res);
    }

    /**
     * Generating the data, just once per runtime.
     */
    @BeforeBenchClass
    public void generateData() {
        final Random ran = new Random();
        intData = new int[ARRAYSIZE];
        int counter = 0;
        while (counter < ARRAYSIZE) {
            intData[counter] = ran.nextInt();
            counter++;
        }
    }

    /**
     * Bench for adding the data to the {@link IntArrayList}.
     */
    @Bench(runs = RUNS)
    public void intArrayListAdd() {
        list = new IntArrayList();
        list.add(intData);
    }

    /**
     * bench for retrieving an element at a specified index
     */
    @Bench(runs = RUNS, beforeEachRun = "intArrayListAdd")
    public void intArrayListGet() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i);
        }
    }

    /**
     * bench for adding data to the [@link ArrayList]
     */
    @Bench(runs = RUNS)
    public void arrayListAdd() {
        arrayList = new ArrayList<Integer>();
        for (final int i : intData) {
            arrayList.add(i);
        }
    }

    /**
     * benchmark for retrieving an element at a specified index
     */
    @Bench(runs = RUNS, beforeEachRun = "arrayListAdd")
    public void arrayListGet() {
        for (int i = 0; i < list.size(); i++) {
            arrayList.get(i);
        }
    }

    /**
     * benchmark for adding data to [@link java.util.Vector]
     */
    @Bench(runs = RUNS)
    public void vectorAdd() {
        vector = new Vector<Integer>();
        for (final int i : intData) {
            vector.add(i);
        }
    }

    /**
     * benchmark for retrieving an element at a specified index
     */
    @Bench(runs = RUNS, beforeEachRun = "vectorAdd")
    public void vectorGet() {
        for (int i = 0; i < vector.size(); i++) {
            vector.get(i);
        }
    }

}
