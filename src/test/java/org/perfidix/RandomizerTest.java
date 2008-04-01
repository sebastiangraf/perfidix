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
 * $Id: RandomizerTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * testing the randomizer.
 */
public class RandomizerTest extends PerfidixTest {

    /**
     * a default randomizer.
     */
    private IRandomizer.DefaultRandomizer d;

    /**
     * a randomizer.
     */
    private IRandomizer.Randomizer r;

    /**
     * sets up, what else?
     * 
     * @throws Exception
     *                 like the parent's set up.
     * @overrides TestCase.setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        d = new IRandomizer.DefaultRandomizer();
        r = new IRandomizer.Randomizer(new Hashtable<Method, Double>());
    }

    @Test
    public void testTresholdComputation() {
        getLog()
                .info(
                        "this test could fail sometimes because it depends on"
                                + " random computation.\n "
                                + " it could be, although improbable, that this test fails "
                                + " although everything went well.");
        try {
            Method hello = loadMethod("hello");
            Method bello = loadMethod("bello");
            r.setTreshold(hello, 0.001);
            r.setTreshold(bello, 0.999);
            int helloShouldRun = 0;
            int belloShouldRun = 0;
            for (int i = 0; i < 10000; i++) {
                if (r.shouldRun(hello)) {
                    helloShouldRun++;
                }
                if (r.shouldRun(bello)) {
                    belloShouldRun++;
                }
            }
            getLog().info("belloShouldRun: " + belloShouldRun);
            getLog().info("helloShouldRun: " + helloShouldRun);
            assertTrue(belloShouldRun > helloShouldRun);
        } catch (Exception e) {
            fail("should never have thrown an exception");
        }

    }

    /**
     * this one is quite obsolete, but nevertheless i am not sure whether i want
     * this method out of here.
     */
    @Test
    public void testOne() {
        for (int i = 0; i < 10000; i++) {
            assertTrue(d.shouldRun(findMethod("hello", Bla.class)));
        }
    }

    /**
     * how can you test randomness? ;)
     */
    @Test
    public void testTwo() {

        Hashtable<Method, Double> h = new Hashtable<Method, Double>();
        h.put(loadMethod("hello"), new Double(0.5));
        h.put(loadMethod("bello"), new Double(1.0));
        r = new IRandomizer.Randomizer(h);
        int balanceHello = 0;
        int balanceBello = 0;
        int balanceNoneo = 0;
        int numRuns = 1000;
        for (int i = 0; i < numRuns; i++) {
            balanceHello += (r.shouldRun(loadMethod("hello")) ? 1 : -1);
            balanceBello += (r.shouldRun(loadMethod("bello")) ? 1 : -1);
            balanceNoneo += (r.shouldRun(loadMethod("noneo")) ? 1 : -1);
        }
        assertEquals(0, balanceHello, 100);
        assertEquals(numRuns, balanceBello, 0);
        assertEquals(numRuns, balanceNoneo, 0);
    }

    /**
     * tests that a non-initialized randomizer will simply run all the tests and
     * not interfer with anything.
     */
    @Test
    public void testTheImplementation() {

        Benchmark bench = new Benchmark();
        RandomBenchmark benchmark = new RandomBenchmark();
        bench.add(benchmark);
        bench.run(50, r);
        assertEquals(50, benchmark.benchOneRuns);
        assertEquals(50, benchmark.benchTwoRuns);
    }

    /**
     * tests the exceptions being thrown.
     */
    @Test
    public void testTheExceptions() {

        double[] invalidTresholds = { 99, -99, 2.0, -1.0, 2342232 };
        double[] validTresholds = { 1.0, 0.0, -0.0 };

        for (int i = 0; i < invalidTresholds.length; i++) {
            try {
                r.setTreshold(loadMethod("hello"), invalidTresholds[i]);
                fail("treshold " + invalidTresholds[i] + " is invalid.");
            } catch (Exception e) {
                assertTrue(true);
            }
        }

        for (int i = 0; i < validTresholds.length; i++) {
            try {
                r.setTreshold(loadMethod("hello"), validTresholds[i]);
                assertTrue(true);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                fail("treshold "
                        + validTresholds[i]
                        + " should not have thrown an exception.");
            }
        }
    }

    /**
     * tests the random boundaries (special cases) 0.0 and 1.0 .
     */
    @Test
    public void testTheImplementation2() {

        Benchmark bench = new Benchmark();
        RandomBenchmark benchmark = new RandomBenchmark();
        bench.add(benchmark);
        try {
            r.setTreshold(findMethod("benchOne", benchmark), 0.0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        bench.run(5, r);
        assertEquals(0, benchmark.benchOneRuns);
        assertEquals(5, benchmark.benchTwoRuns);

    }

    /**
     * this one tests that two methods with the same method name will remain
     * independent.
     */
    @Test
    public void testTheImplementation3() {
        Benchmark bench = new Benchmark();
        RandomBenchmark a = new RandomBenchmark();
        RandomBenchmark2 b = new RandomBenchmark2();
        bench.add(a);
        bench.add(b);
        try {
            Method m1 = findMethod("benchOne", a);
            r.setTreshold(m1, 0.0);
            Method m2 = findMethod("benchOne", b);
            r.setTreshold(m2, 1.0);
        } catch (Exception e) {
            fail("should not have thrown an Exception, but threw: "
                    + e.getMessage());
        }
        bench.run(5, r);
        assertEquals(0, a.benchOneRuns);
        assertEquals(5, b.benchOneRuns);

    }

    private Method findMethod(final String mName, final Object o) {
        Method[] methods = o.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(mName)) {
                return methods[i];
            }
        }
        return null;
    }

    /**
     * to shorten the initialization within the test cases.
     */
    protected Method loadMethod(final String mName) {
        return findMethod(mName, new Bla());
    }

    /**
     * needed only for testing purposes.
     */
    public class Bla {

        public void hello() {

        }

        public void bello() {

        }

        public void noneo() {

        }
    }

    /**
     * just for testing. this one has to hold the same method name as
     * RandomBenchmark, because i first implemented the Randomizer to allow
     * method names as hashtable values. this is incorrect, because two
     * different classes are likely to contain the same method name. so i need a
     * more explicit identifier, which is the Method itself at the moment.
     */
    public class RandomBenchmark2 {

        private int benchOneRuns = 0;

        @Bench
        public void benchOne() {
            benchOneRuns++;
        }
    }

    /**
     * just for testing.
     */
    public class RandomBenchmark {

        private int benchOneRuns = 0;

        private int benchTwoRuns = 0;

        @Bench
        public void benchOne() {
            benchOneRuns++;
        }

        @Bench
        public void benchTwo() {
            benchTwoRuns++;
        }
    }

}
