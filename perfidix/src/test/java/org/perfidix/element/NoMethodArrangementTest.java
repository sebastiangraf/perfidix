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

import static org.junit.Assert.assertEquals;
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
 * Testcase for no method arrangement.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class NoMethodArrangementTest {

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
     * Test method for {@link org.perfidix.element.NoMethodArrangement} .
     */
    @Test
    public void test() {
        try {

            final AbstractMethodArrangement arrangement =
                    AbstractMethodArrangement.getMethodArrangement(
                            elemSet, KindOfArrangement.NoArrangement);
            final String[] expectedNames =
                    { "bench1", "bench2", "bench4" };
            final Iterator<BenchmarkElement> iterBench =
                    arrangement.iterator();
            assertEquals(
                    "Method name for first element", expectedNames[0],
                    iterBench
                            .next().getMeth().getMethodToBench().getName());
            assertEquals(
                    "Method name for second element", expectedNames[1],
                    iterBench
                            .next().getMeth().getMethodToBench().getName());
            assertEquals(
                    "Method name for third element", expectedNames[2],
                    iterBench
                            .next().getMeth().getMethodToBench().getName());
            assertFalse("No more elements avaliables", iterBench.hasNext());

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    class TestBenchClass {

        @Bench
        public void bench1() {
            // Just a method sekeleton
        }

        @Bench
        public void bench2() {
            // Just a method sekeleton
        }

        public void bench3() {
            // Just a method sekeleton
        }

        @Bench
        public void bench4() {
            // Just a method sekeleton
        }

    }

}
