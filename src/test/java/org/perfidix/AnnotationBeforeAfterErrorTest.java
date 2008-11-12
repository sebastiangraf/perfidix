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
 * $Id: AnnotationBeforeAfterTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;

public class AnnotationBeforeAfterErrorTest {

    @Test(expected = IllegalStateException.class)
    public void testBeforeClassParam() {
        final BeforeClassParam test = new BeforeClassParam();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeforeClassReturn() {
        final BeforeClassReturn test = new BeforeClassReturn();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testAfterClassParam() {
        final AfterClassParam test = new AfterClassParam();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testAfterClassReturn() {
        final AfterClassReturn test = new AfterClassReturn();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateAfterClass() {
        final DuplicateAfterClass test = new DuplicateAfterClass();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateBeforeClass() {
        final DuplicateBeforeClass test = new DuplicateBeforeClass();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeforeFirstRunParam() {
        final BeforeMethodParam test = new BeforeMethodParam();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeforeFirstRunReturn() {
        final BeforeMethodReturn test = new BeforeMethodReturn();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testAfterLastRunParam() {
        final AfterMethodParam test = new AfterMethodParam();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testAfterLastRunReturn() {
        final AfterMethodReturn test = new AfterMethodReturn();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateAfterLastRun() {
        final DuplicateAfterMethod test = new DuplicateAfterMethod();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateBeforeFirstRun() {
        final DuplicateBeforeMethod test = new DuplicateBeforeMethod();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeforeEveryRunParam() {
        final BeforeParam test = new BeforeParam();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeforeEveryRunReturn() {
        final BeforeReturn test = new BeforeReturn();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testAfterEveryRunParam() {
        final AfterParam test = new AfterParam();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testAfterEveryRunReturn() {
        final AfterReturn test = new AfterReturn();
        final Benchmark bench = new Benchmark();
        bench.add(test);
        bench.run();
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateAfterEveryRun() {
        final DuplicateAfter test = new DuplicateAfter();
        final Benchmark bench = new Benchmark();
        assertFalse(bench.exceptionsThrown());
        bench.setLogger(true);
        bench.add(test);
        bench.run();
        assertTrue(bench.exceptionsThrown());
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateBeforeEveryRun() {
        final DuplicateBefore test = new DuplicateBefore();
        final Benchmark bench = new Benchmark();
        assertFalse(bench.exceptionsThrown());
        bench.setLogger(true);
        bench.add(test);
        bench.run();
        assertTrue(bench.exceptionsThrown());
    }

    class DuplicateBefore {
        @BeforeEachRun
        public void setUp1() {
        }

        @BeforeEachRun
        public void setUp2() {
        }

        @Bench
        public void bench() {
        }
    }

    class DuplicateAfter {
        @Bench
        public void bench() {
        }

        @AfterEachRun
        public void tearDown1() {
        }

        @AfterEachRun
        public void tearDown2() {
        }
    }

    class AfterReturn {
        @Bench
        public void bench() {
        }

        @AfterEachRun
        public Object tearDown() {
            return null;
        }
    }

    class AfterParam {
        @Bench
        public void bench() {
        }

        @AfterEachRun
        public void tearDown(Object obj) {
        }
    }

    class BeforeReturn {
        @BeforeEachRun
        public Object setUp() {
            return null;
        }

        @Bench
        public void bench() {
        }
    }

    class BeforeParam {
        @BeforeEachRun
        public void setUp(Object obj) {
        }

        @Bench
        public void bench() {
        }
    }

    class DuplicateBeforeMethod {
        @BeforeFirstRun
        public void build1() {
        }

        @BeforeFirstRun
        public void build2() {
        }

        @Bench
        public void bench() {
        }
    }

    class DuplicateAfterMethod {
        @Bench
        public void bench() {
        }

        @AfterLastRun
        public void cleanUp1() {
        }

        @AfterLastRun
        public void cleanUp2() {
        }
    }

    class AfterMethodReturn {
        @Bench
        public void bench() {
        }

        @AfterLastRun
        public Object cleanUp() {
            return null;
        }
    }

    class AfterMethodParam {
        @Bench
        public void bench() {
        }

        @AfterLastRun
        public void cleanUp(Object obj) {
        }
    }

    class BeforeMethodReturn {
        @BeforeFirstRun
        public Object build() {
            return null;
        }

        @Bench
        public void bench() {
        }
    }

    class BeforeMethodParam {
        @BeforeFirstRun
        public void build(Object obj) {
        }

        @Bench
        public void bench() {
        }
    }

    class DuplicateBeforeClass {
        @BeforeBenchClass
        public void build1() {
        }

        @BeforeBenchClass
        public void build2() {
        }

        @Bench
        public void bench() {
        }
    }

    class DuplicateAfterClass {
        @Bench
        public void bench() {
        }

        @AfterBenchClass
        public void cleanUp1() {
        }

        @AfterBenchClass
        public void cleanUp2() {
        }
    }

    class AfterClassReturn {
        @Bench
        public void bench() {
        }

        @AfterBenchClass
        public Object cleanUp() {
            return null;
        }
    }

    class AfterClassParam {
        @Bench
        public void bench() {
        }

        @AfterBenchClass
        public void cleanUp(Object obj) {
        }
    }

    class BeforeClassReturn {
        @BeforeBenchClass
        public Object build() {
            return null;
        }

        @Bench
        public void bench() {
        }
    }

    class BeforeClassParam {
        @BeforeBenchClass
        public void build(Object obj) {
        }

        @Bench
        public void bench() {
        }
    }

}
