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
package org.perfidix.benchmarktests;


import org.perfidix.annotation.*;


/**
 * One complete Bench, just looking if everthing is going well.
 *
 * @author Sebastian Graf, University of Konstanz
 */
public final class NormalCompleteBench {

    /**
     * Static for setting number of runs
     */
    public final static int RUNS = 25;

    private static int beforeClassC = 0;
    private static int beforeFirstRunC = 0;
    private static int beforeEachRunC = 0;
    private static int benchC1 = 0;
    private static int benchC2 = 0;
    private static int afterEachC = 0;
    private static int afterLastC = 0;
    private static int afterClassC = 0;

    /**
     * Resetting everything
     */
    public static void reset() {
        beforeClassC = 0;
        beforeEachRunC = 0;
        beforeFirstRunC = 0;
        benchC1 = 0;
        benchC2 = 0;
        afterClassC = 0;
        afterEachC = 0;
        afterLastC = 0;
    }

    /**
     * Getter for member beforeClassC
     *
     * @return the beforeClassC
     */
    public static int getBeforeClassCounter() {
        return beforeClassC;
    }

    /**
     * Getter for member beforeFirstRunC
     *
     * @return the beforeFirstRunC
     */
    public static int getBeforeFirstRunCounter() {
        return beforeFirstRunC;
    }

    /**
     * Getter for member beforeEachRunC
     *
     * @return the beforeEachRunC
     */
    public static int getBeforeEachRunCounter() {
        return beforeEachRunC;
    }

    /**
     * Getter for member benchC
     *
     * @return the benchC
     */
    public static int getBenchCounter1() {
        return benchC1;
    }

    /**
     * Getter for member benchC
     *
     * @return the benchC
     */
    public static int getBenchCounter2() {
        return benchC2;
    }

    /**
     * Getter for member afterEachC
     *
     * @return the afterEachC
     */
    public static int getAfterEachRunCounter() {
        return afterEachC;
    }

    /**
     * Getter for member afterLastC
     *
     * @return the afterLastC
     */
    public static int getAfterLastRunCounter() {
        return afterLastC;
    }

    /**
     * Getter for member afterClassC
     *
     * @return the afterClassC
     */
    public static int getAfterClassCounter() {
        return afterClassC;
    }

    /**
     * before benchclass
     */
    @BeforeBenchClass
    public void beforeClass() {
        beforeClassC++;
    }

    /**
     * before firstrun
     */
    @BeforeFirstRun
    public void beforeFirstRun() {
        beforeFirstRunC++;
    }

    /**
     * before eachrun
     */
    @BeforeEachRun
    public void beforeEachRun() {
        beforeEachRunC++;
    }

    /**
     * Bench
     */
    @Bench(runs = RUNS)
    public void bench1() {
        benchC1++;
    }

    /**
     * Bench
     */
    @Bench
    public void bench() {
        benchC2++;
    }

    /**
     * after eachrun
     */
    @AfterEachRun
    public void afterEachRun() {
        afterEachC++;
    }

    /**
     * before eachrun
     */
    @AfterLastRun
    public void afterLastRun() {
        afterLastC++;
    }

    /**
     * after benchclass
     */
    @AfterBenchClass
    public void afterClass() {
        afterClassC++;
    }

}
