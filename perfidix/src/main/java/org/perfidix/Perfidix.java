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

import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * This is the main class, consisting of all the factory methods needed in order
 * to perform a benchmark run.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class Perfidix {

    /**
     * private constructor.
     */
    private Perfidix() {

    }

    /**
     * Running one Benchmark without any listener.
     * 
     * @param benchs
     *            to be inserted
     * @return one {@link BenchmarkResult} object with the results
     * @throws ClassNotFoundException
     *             if class cannot be found
     */
    public static BenchmarkResult runBenchs(final String[] benchs)
            throws ClassNotFoundException {
        final Benchmark bench = new Benchmark();
        for (String each : benchs) {
            bench.add(Class.forName(each));
        }
        return bench.run();
    }

    /**
     * Main method for invoking benchs with classes as strings.
     * 
     * @param args
     *            the classes
     * @throws ClassNotFoundException
     *             if class cannot be found
     */
    public static void main(final String[] args)
            throws ClassNotFoundException {
        final BenchmarkResult res = runBenchs(args);
        new TabularSummaryOutput().visitBenchmark(res);
    }
}
