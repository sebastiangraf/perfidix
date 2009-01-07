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
package org.perfidix.benchmarktestClasses;

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

    private static int beforeClassCounter = 0;
    private static int beforeFirstRunCounter = 0;
    private static int beforeEachRunCounter = 0;
    private static int benchCounter = 0;
    private static int afterEachRunCounter = 0;
    private static int afterLastRunCounter = 0;
    private static int afterClassCounter = 0;

    /**
     * before benchclass
     */
    @BeforeBenchClass
    public final void beforeClass() {
        beforeClassCounter++;
    }

    /**
     * before firstrun
     */
    @BeforeFirstRun
    public final void beforeFirstRun() {
        beforeFirstRunCounter++;
    }

    /**
     * before eachrun
     */
    @BeforeEachRun
    public final void beforeEachRun() {
        beforeEachRunCounter++;
    }

    /**
     * Bench
     */
    @Bench(runs = RUNS)
    public final void bench() {
        benchCounter++;
    }

    /**
     * after eachrun
     */
    @AfterEachRun
    public final void afterEachRun() {
        afterEachRunCounter++;
    }

    /**
     * before eachrun
     */
    @AfterLastRun
    public final void afterLastRun() {
        afterLastRunCounter++;
    }

    /**
     * after benchclass
     */
    @AfterBenchClass
    public final void afterClass() {
        afterClassCounter++;
    }

    /**
     * Getter for member beforeClassCounter
     * 
     * @return the beforeClassCounter
     */
    public static final int getBeforeClassCounter() {
        return beforeClassCounter;
    }

    /**
     * Getter for member beforeFirstRunCounter
     * 
     * @return the beforeFirstRunCounter
     */
    public static final int getBeforeFirstRunCounter() {
        return beforeFirstRunCounter;
    }

    /**
     * Getter for member beforeEachRunCounter
     * 
     * @return the beforeEachRunCounter
     */
    public static final int getBeforeEachRunCounter() {
        return beforeEachRunCounter;
    }

    /**
     * Getter for member benchCounter
     * 
     * @return the benchCounter
     */
    public static final int getBenchCounter() {
        return benchCounter;
    }

    /**
     * Getter for member afterEachRunCounter
     * 
     * @return the afterEachRunCounter
     */
    public static final int getAfterEachRunCounter() {
        return afterEachRunCounter;
    }

    /**
     * Getter for member afterLastRunCounter
     * 
     * @return the afterLastRunCounter
     */
    public static final int getAfterLastRunCounter() {
        return afterLastRunCounter;
    }

    /**
     * Getter for member afterClassCounter
     * 
     * @return the afterClassCounter
     */
    public static final int getAfterClassCounter() {
        return afterClassCounter;
    }

}
