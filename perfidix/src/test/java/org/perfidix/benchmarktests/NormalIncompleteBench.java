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

import static org.junit.Assert.fail;

import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.BenchClass;
import org.perfidix.annotation.SkipBench;

/**
 * Class with missing benchMethod
 * 
 * @author Sebastian Graf, University of Konstanz
 */
@BenchClass
public final class NormalIncompleteBench {

    /**
     * before firstrun
     */
    @BeforeFirstRun
    public void beforeFirstRun() {
        fail();
    }

    /**
     * before eachrun
     */
    @BeforeEachRun
    public void beforeEachRun() {
        fail();
    }

    /**
     * Bench
     */
    @SkipBench
    public void bench() {
        fail();
    }

    /**
     * after eachrun
     */
    @AfterEachRun
    public void afterEachRun() {
        fail();
    }

    /**
     * before eachrun
     */
    @AfterLastRun
    public void afterLastRun() {
        fail();
    }

}
