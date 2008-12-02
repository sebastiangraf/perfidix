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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.perfidix.annotation.Bench;

/**
 * Testcase for no method arrangement.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class NoMethodArrangementTest {

    /**
     * Test method for {@link org.perfidix.element.NoMethodArrangement} .
     */
    @Test
    public void test() {
        try {
            final List<Class<?>> clazzes = new LinkedList<Class<?>>();
            clazzes.add(new TestBenchClass().getClass());
            final AbstractMethodArrangement arrangement =
                    AbstractMethodArrangement.getMethodArrangement(clazzes);
            final String[] expectedNames = { "bench1", "bench2", "bench4" };
            final Iterator<BenchmarkElement> iterBench = arrangement.iterator();
            assertEquals(
                    iterBench.next().getMethodToBench().getName(),
                    expectedNames[0]);
            assertEquals(
                    iterBench.next().getMethodToBench().getName(),
                    expectedNames[1]);
            assertEquals(
                    iterBench.next().getMethodToBench().getName(),
                    expectedNames[2]);
            assertFalse(iterBench.hasNext());

        } catch (final Exception e) {
            fail(e.toString());
        }
    }

    class TestBenchClass {

        @Bench
        public void bench1() {
        }

        @Bench
        public void bench2() {
        }

        public void bench3() {
        }

        @Bench
        public void bench4() {
        }

    }

}
