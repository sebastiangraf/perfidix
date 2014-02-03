/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix;


import org.perfidix.AbstractConfig.StandardConfig;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;


/**
 * This is the main class, consisting of all the factory methods needed in order to perform a benchmark run. Instead of
 * running the bench with a {@link Benchmark} instance, this class just takes all classes as a String and gives less
 * control over the Benchmark itself. The Classes to be given are either Benchmark-Classes or (one single) Config-Class.
 * Additionaly, this class holds multiple utility-methods to get Bench-Classes and Config out of a set of Strings.
 * <p>
 * 
 * <pre>
 * 
 * public class MyBenchmark {
 * 
 *     public static void main (final String[] args) {
 *         final String classNames = { &quot;Class1&quot;, &quot;Class2&quot;, &quot;Class3&quot;, &quot;MyConfig1&quot; };
 * 
 *         // either...
 *         final BenchmarkResult result = Perfidix.runBenchs(classNames);
 *         new TabularSummaryOutput.visitBenchmark(result);
 * 
 *         // ...or...
 *         Perfidix.main(classNames);
 *     }
 * }
 * 
 * </pre>
 * 
 * </p>
 * 
 * @see AbstractConfig
 * @see BenchmarkResult
 * @author Sebastian Graf, University of Konstanz
 */
public final class Perfidix {

    /**
     * private constructor.
     */
    private Perfidix () {
    }

    /**
     * Running one Benchmark.
     * 
     * @param benchs to be inserted
     * @return one {@link BenchmarkResult} object with the results
     * @throws ClassNotFoundException if class cannot be found
     * @throws IllegalAccessException if conf cannot be instantiated
     * @throws InstantiationException if conf cannot be instantiated
     */
    public static BenchmarkResult runBenchs (final String[] benchs) throws ClassNotFoundException , InstantiationException , IllegalAccessException {
        final AbstractConfig conf = getConfiguration(benchs);

        final Benchmark bench = new Benchmark(conf);
        return setUpBenchmark(benchs, bench).run();
    }

    /**
     * Setting up an existing benchmark with the given number of class-files
     * 
     * @param classes to be benched
     * @param benchmark to be set up
     * @return the same {@link Benchmark} object with the classes
     * @throws ClassNotFoundException thrown if class was not found.
     */
    public static Benchmark setUpBenchmark (final String[] classes, final Benchmark benchmark) throws ClassNotFoundException {
        for (final String each : classes) {
            benchmark.add(Class.forName(each));
        }
        return benchmark;
    }

    /**
     * Getting a configuration out of the class-files. If none is avaliable, the {@link StandardConfig} is given back.
     * The {@link AbstractConfig} instance has to have a parameter-free constructor.
     * 
     * @param classes to be examined. Only one instance of an {@link AbstractConfig} is allowed.
     * @return the {@link AbstractConfig} instance or the {@link StandardConfig} if none was found.
     * @throws ClassNotFoundException thrown if a class was not found.
     * @throws InstantiationException thrown if the {@link AbstractConfig} instance was not able to be instaniated
     * @throws IllegalAccessException thrown if the {@link AbstractConfig} instance was not able to be instaniated
     */
    public static AbstractConfig getConfiguration (final String[] classes) throws ClassNotFoundException , InstantiationException , IllegalAccessException {
        AbstractConfig conf = null;
        for (String each : classes) {
            final Class<?> clazz = Class.forName(each);
            final Class<?> superclazz = clazz.getSuperclass();
            if (superclazz.equals(AbstractConfig.class)) {
                if (conf == null) {
                    conf = (AbstractConfig) clazz.newInstance();
                } else {
                    throw new IllegalArgumentException("Only one config-class allowed");
                }
            }
        }
        if (conf == null) {
            conf = new StandardConfig();
        }
        return conf;
    }

    /**
     * Main method for invoking benchs with classes as strings.
     * 
     * @param args the classes
     * @throws ClassNotFoundException if class cannot be found
     * @throws IllegalAccessException if conf cannot be instantiated
     * @throws InstantiationException if conf cannot be instantiated
     */
    public static void main (final String[] args) throws ClassNotFoundException , InstantiationException , IllegalAccessException {
        final BenchmarkResult res = runBenchs(args);
        new TabularSummaryOutput().visitBenchmark(res);
    }
}
