/*
 * Copyright 2008 Distributed Systems Group, University of Konstanz
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
package org.perfidix.element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;

/**
 * Testcase for shuffle method arrangement.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class ShuffleMethodArrangementTest {

    private transient Set<BenchmarkElement> elemSet;

    /**
     * Before method to setUp Benchmarkables.
     */
    @Before
    public void setUp() {
        elemSet = new HashSet<BenchmarkElement>();
        final Class< ? > testClazz = TestBenchClass.class;
        for (final Method meth : testClazz.getDeclaredMethods()) {
            if (BenchmarkMethod.isBenchmarkable(meth)) {
                elemSet
                        .add(new BenchmarkElement(
                                new BenchmarkMethod(meth)));
            }
        }
    }

    /**
     * Test method for {@link org.perfidix.element.ShuffleMethodArrangement} .
     */
    @Test
    public void test() {
        try {

            final AbstractMethodArrangement arrangement =
                    AbstractMethodArrangement.getMethodArrangement(
                            elemSet, KindOfArrangement.ShuffleArrangement);
            final String[] expectedNames =
                    { "bench1", "bench2", "bench4" };
            final Iterator<BenchmarkElement> iterBench =
                    arrangement.iterator();
            final BenchmarkElement elem1 = iterBench.next();
            final BenchmarkElement elem2 = iterBench.next();
            final BenchmarkElement elem3 = iterBench.next();
            if ((expectedNames[0].equals(elem1
                    .getMeth().getMethodToBench().getName()))
                    && (expectedNames[1].equals(elem2
                            .getMeth().getMethodToBench().getName()))
                    && (expectedNames[2].equals(elem3
                            .getMeth().getMethodToBench().getName()))) {
                fail("Something has to be arranged in a different way!");

            }
            assertFalse("No more elements should be left", iterBench
                    .hasNext());

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    class TestBenchClass {

        @Bench
        public void bench1() {
            // Just a bench-sekeleton
        }

        @Bench
        public void bench2() {
            // Just a bench-sekeleton
        }

        public void bench3() {
            // Just a bench-sekeleton
        }

        @Bench
        public void bench4() {
            // Just a bench-sekeleton
        }

    }

}
