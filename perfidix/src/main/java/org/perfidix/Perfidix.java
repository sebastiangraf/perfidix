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

import org.perfidix.AbstractConfig.StandardConfig;
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
     * Running one Benchmark.
     * 
     * @param benchs
     *            to be inserted
     * @return one {@link BenchmarkResult} object with the results
     * @throws ClassNotFoundException
     *             if class cannot be found
     *@throws IllegalAccessException
     *             if conf cannot be instantiated
     * @throws InstantiationException
     *             if conf cannot be instantiated
     */
    public static BenchmarkResult runBenchs(final String[] benchs)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        final AbstractConfig conf = getConfiguration(benchs);

        final Benchmark bench = new Benchmark(conf);
        return setUpBenchmark(benchs, bench).run();
    }

    /**
     * Setting up an existing benchmark with the given number of class-files
     * 
     * @param classes
     *            to be benched
     * @param benchmark
     *            to be set up
     * @return the same {@link Benchmark} object with the classes
     * @throws ClassNotFoundException
     *             thrown if class was not found.
     */
    public static Benchmark setUpBenchmark(
            final String[] classes, final Benchmark benchmark)
            throws ClassNotFoundException {
        for (final String each : classes) {
            benchmark.add(Class.forName(each));
        }
        return benchmark;
    }

    /**
     * Getting a configuration out of the class-files. If none is avaliable, the
     * {@link StandardConfig} is given back. The {@link AbstractConfig} instance
     * has to have a parameter-free constructor.
     * 
     * @param classes
     *            to be examined. Only one instance of an {@link AbstractConfig}
     *            is allowed.
     * @return the {@link AbstractConfig} instance or the {@link StandardConfig}
     *         if none was found.
     * @throws ClassNotFoundException
     *             thrown if a class was not found.
     * @throws InstantiationException
     *             thrown if the {@link AbstractConfig} instance was not able to
     *             be instaniated
     * @throws IllegalAccessException
     *             thrown if the {@link AbstractConfig} instance was not able to
     *             be instaniated
     */
    public static AbstractConfig getConfiguration(final String[] classes)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        AbstractConfig conf = new StandardConfig();
        for (String each : classes) {
            final Class<?> clazz = Class.forName(each);
            final Class<?> superclazz = clazz.getSuperclass();
            if (superclazz.equals(AbstractConfig.class)) {
                if (conf == null) {
                    conf = (AbstractConfig) clazz.newInstance();
                } else {
                    throw new IllegalArgumentException(
                            "Only one config-class allowed");
                }
            }
        }
        return conf;
    }

    /**
     * Main method for invoking benchs with classes as strings.
     * 
     * @param args
     *            the classes
     * @throws ClassNotFoundException
     *             if class cannot be found
     * @throws IllegalAccessException
     *             if conf cannot be instantiated
     * @throws InstantiationException
     *             if conf cannot be instantiated
     */
    public static void main(final String[] args)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        final BenchmarkResult res = runBenchs(args);
        new TabularSummaryOutput().visitBenchmark(res);
    }
}
