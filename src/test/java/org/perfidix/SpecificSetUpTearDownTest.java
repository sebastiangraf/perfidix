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
 * $Id: SpecificSetUpTearDownTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.AfterEachBenchRun;
import org.perfidix.annotation.AfterLastBenchRun;
import org.perfidix.annotation.BeforeEachBenchRun;
import org.perfidix.annotation.BeforeFirstBenchRun;
import org.perfidix.annotation.Bench;

public class SpecificSetUpTearDownTest {

    private boolean firstRun;

    private boolean setUp;

    private boolean tearDown;

    private boolean lastRun;

    private boolean specialFirstRun;

    private boolean specialSetUp;

    private boolean specialTearDown;

    private boolean specialLastRun;

    private boolean benchWithout;

    private boolean benchWith;

    @Before
    public void setUp() {
        firstRun = false;
        setUp = false;
        tearDown = false;
        lastRun = false;
        specialFirstRun = false;
        specialSetUp = false;
        specialTearDown = false;
        specialLastRun = false;
        benchWithout = false;
        benchWith = false;
    }

    @Test
    public void testWithout() {
        final Without test = new Without();
        final Benchmark benchMark = new Benchmark();
        benchMark.add(test);
        benchMark.run(1);
        assertTrue(firstRun);
        assertTrue(setUp);
        assertTrue(tearDown);
        assertTrue(lastRun);
        assertFalse(specialFirstRun);
        assertFalse(specialSetUp);
        assertFalse(specialTearDown);
        assertFalse(specialLastRun);
        assertTrue(benchWithout);
        assertFalse(benchWith);
    }

    @Test
    public void testWith() {
        final With test = new With();
        final Benchmark benchMark = new Benchmark();
        benchMark.add(test);
        benchMark.run(1);
        assertFalse(firstRun);
        assertFalse(setUp);
        assertFalse(tearDown);
        assertFalse(lastRun);
        assertTrue(specialFirstRun);
        assertTrue(specialSetUp);
        assertTrue(specialTearDown);
        assertTrue(specialLastRun);
        assertFalse(benchWithout);
        assertTrue(benchWith);
    }

    @Test(expected = IllegalStateException.class)
    public void testException() {
        final WithException test = new WithException();
        final Benchmark benchMark = new Benchmark();
        benchMark.add(test);
        benchMark.run(1);
    }

    class Without {

        @BeforeFirstBenchRun
        public void firstRun() {
            firstRun = true;
        }

        @BeforeEachBenchRun
        public void setUp() {
            setUp = true;
        }

        @AfterEachBenchRun
        public void tearDown() {
            tearDown = true;
        }

        @AfterLastBenchRun
        public void lastRun() {
            lastRun = true;
        }

        public void specialFirstRun() {
            specialFirstRun = true;
        }

        public void specialSetUp() {
            specialSetUp = true;
        }

        public void specialTearDown() {
            specialTearDown = true;
        }

        public void specialLastRun() {
            specialLastRun = true;
        }

        @Bench
        public void bench() {
            benchWithout = true;
        }
    }

    class With {
        @BeforeFirstBenchRun
        public void firstRun() {
            firstRun = true;
        }

        @BeforeEachBenchRun
        public void setUp() {
            setUp = true;
        }

        @AfterEachBenchRun
        public void tearDown() {
            tearDown = true;
        }

        @AfterLastBenchRun
        public void lastRun() {
            lastRun = true;
        }

        public void specialFirstRun() {
            specialFirstRun = true;
        }

        public void specialSetUp() {
            specialSetUp = true;
        }

        public void specialTearDown() {
            specialTearDown = true;
        }

        public void specialLastRun() {
            specialLastRun = true;
        }

        @Bench(beforeEveryBenchRun = "specialSetUp", afterEveryBenchRun = "specialTearDown", beforeFirstBenchRun = "specialFirstRun", afterLastBenchRun = "specialLastRun")
        public void bench() {
            benchWith = true;
        }

    }

    class WithException {

        public Object specialSetUp() {
            specialSetUp = true;
            return null;
        }

        public void specialTearDown(Object test) {
            specialTearDown = true;
        }

        @Bench(beforeEveryBenchRun = "specialSetUp", afterEveryBenchRun = "specialTearDown")
        public void bench() {
            benchWith = true;
        }
    }

}
