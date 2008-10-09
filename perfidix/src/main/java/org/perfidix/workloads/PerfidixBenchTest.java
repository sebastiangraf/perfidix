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
 * $Id: PerfidixBenchTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.workloads;

import org.perfidix.Benchmark;
import org.perfidix.IMeter;
import org.perfidix.Perfidix;
import org.perfidix.annotation.Bench;
import org.perfidix.result.GnuPlotData;
import org.perfidix.result.IResult;
import org.perfidix.result.Result;

/**
 * a benchmark test.
 */
public class PerfidixBenchTest {

    private long[] set;

    private Result r;

    private int num = 10;

    private int inputLength = 2000;

    /**
     * constructor.
     */
    public PerfidixBenchTest() {
        set = new long[inputLength];
        for (int i = 0; i < inputLength; i++) {
            set[i] = (int) (Math.random() * 20);
        }
        r = Perfidix.createSingleResult(set);
    }

    @Bench
    public void benchAvg() {

        for (int i = 0; i < num; i++) {
            r.avg();
        }
    }

    @Bench
    public void benchSum() {

        for (int i = 0; i < num; i++) {
            r.sum();
        }
    }

    @Bench
    public void benchMean() {

        for (int i = 0; i < num; i++) {
            r.mean();
        }
    }

    @Bench
    public void benchVariance() {

        for (int i = 0; i < num; i++) {
            r.variance();
        }
    }

    @Bench
    public void benchNumberResultSet() {

        for (int i = 0; i < num; i++) {
            r.resultCount();
        }
    }

    @Bench
    public void benchGetStdDev() {

        for (int i = 0; i < num; i++) {
            r.getStandardDeviation();
        }
    }

    @Bench
    public void benchMin() {

        for (int i = 0; i < num; i++) {
            r.min();
        }
    }

    @Bench
    public void benchMax() {

        for (int i = 0; i < num; i++) {
            r.min();
        }
    }

    @Bench
    public void benchConf95() {

        for (int i = 0; i < num; i++) {
            r.getConf95();
        }
    }

    @Bench
    public void benchConf99() {

        for (int i = 0; i < num; i++) {
            r.getConf99();
        }
    }

    @Bench
    public void benchGetSquareSum() {

        for (int i = 0; i < num; i++) {
            r.squareSum();
        }
    }

    @Bench
    public void benchMedian() {

        for (int i = 0; i < num; i++) {
            r.median();
        }
    }

    /**
     * @author axo
     */
    public class SampleBenchmarkClass {

        private static final int NUM = 50000;

        private IMeter oMeter;

        /**
         * default constructor.
         */
        public SampleBenchmarkClass() {
            Perfidix.createMeter("object creations", "o");
            oMeter = Perfidix.getMeter("object creations");
        }

        @Bench
        public void benchSampleMethod1() {
            long[] x = new long[SampleBenchmarkClass.NUM];
            for (int i = 0; i < x.length; i++) {
                x[i] = i;
            }
        }

        @Bench
        public void benchSampleMethod2() {
            SomeObj[] x = new SomeObj[SampleBenchmarkClass.NUM];
            for (int i = 0; i < x.length; i++) {
                x[i] = new SomeObj(i, new Unit("CFR"));
                oMeter.tick();
                oMeter.tick();
            }
        }

    }

    /**
     * @author axo
     */
    public class SomeObj {
        private Unit u;

        private long value;

        /**
         * @param aValue
         *            str
         * @param unit
         *            str
         */
        public SomeObj(final long aValue, final Unit unit) {
            this.value = aValue;
            this.u = unit;
        }

        /**
         * @return theUnit
         */
        Unit getUnit() {
            return u;
        }

        /**
         * @return theValue
         */
        long getValue() {
            return value;
        }

    }

    /**
     * @author axo
     */
    public class Unit {
        private String key;

        /**
         * @param aKey
         *            t
         */
        public Unit(final String aKey) {
            key = aKey;
        }

        /**
         * this is not a comment.
         * 
         * @see java.lang.Object#toString
         * @return the representation
         */
        @Override
        public String toString() {
            return key;
        }
    }

    /**
     * @param args
     *            nothing will be taken as input.
     */
    public static void main(final String[] args) {
        Benchmark b = new Benchmark("hello world.");

        try {
            b.add(PerfidixBenchTest.class);
        } catch (Exception e) {
            System.out.println("argh. could not add the PerfidixBenchTest.");
        }
        b.useNanoMeter();
        IResult r = b.run(10);
        System.out.println(r);

        Benchmark b2 = new Benchmark("EXAMPLE");
        b2.add(new PerfidixBenchTest().new SampleBenchmarkClass());
        // b2.useMilliMeter();
        IResult r2 = b2.run(10);
        System.out.println(r2);

        GnuPlotData v = new GnuPlotData();
        v.visit(r2);
        System.out.println("gnuplot output");
        System.out.println(v.toString());

    }

}
