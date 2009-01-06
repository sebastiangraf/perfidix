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
package org.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.benchmarktestClasses.BeforeBenchClassError;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.failureHandling.PerfidixMethodException;
import org.perfidix.result.BenchmarkResult;

/**
 * Complete test for a normal Benchmark.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class BenchmarkTest {

    private Benchmark benchmark;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benchmark = new Benchmark();
    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        benchmark = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.Benchmark#run(org.perfidix.element.KindOfArrangement, org.perfidix.ouput.AbstractOutput[])}
     * .
     */
    @Test
    public final void testRunBeforeClassError() {
        benchmark.add(BeforeBenchClassError.class);
        final BenchmarkResult benchRes =
                benchmark.run(KindOfArrangement.NoArrangement);
        assertEquals(0, benchRes.getRegisteredMeters().size());
        assertEquals(1, benchRes.getExceptions().size());
        final PerfidixMethodException exec =
                benchRes.getExceptions().iterator().next();
        assertEquals(BeforeBenchClass.class, exec.getRelatedAnno());
        assertEquals(IllegalStateException.class, exec.getExec().getClass());

    }

}
