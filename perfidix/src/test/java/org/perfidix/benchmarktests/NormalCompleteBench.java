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
package org.perfidix.benchmarktests;

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;

/**
 * One complete Bench, just looking if everthing is going well.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class NormalCompleteBench {

    /** Static for setting number of runs */
    public final static int RUNS = 25;

    private static int beforeClassC = 0;
    private static int beforeFirstRunC = 0;
    private static int beforeEachRunC = 0;
    private static int benchC = 0;
    private static int afterEachC = 0;
    private static int afterLastC = 0;
    private static int afterClassC = 0;

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
    public void bench() {
        benchC++;
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

    /**
     * Resetting everything
     */
    public static void reset() {
        beforeClassC = 0;
        beforeEachRunC = 0;
        beforeFirstRunC = 0;
        benchC = 0;
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
    public static int getBenchCounter() {
        return benchC;
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

}
