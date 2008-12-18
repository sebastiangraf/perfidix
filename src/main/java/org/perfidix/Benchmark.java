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
package org.perfidix;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.element.AbstractMethodArrangement;
import org.perfidix.element.BenchmarkElement;
import org.perfidix.element.BenchmarkExecutor;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.element.AbstractMethodArrangement.KindOfElementArrangement;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;

/**
 * Class to hold all classes which want to be benchmarked.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class Benchmark {

    /** Set with all registered meters. */
    private final Set<AbstractMeter> meters;

    /** Set with all used classes */
    private final Set<Class<?>> clazzes;

    /**
     * Constructor with a fixed set of used meters.
     * 
     * @param paramMeters
     *            meters to use.
     */
    public Benchmark(final Set<AbstractMeter> paramMeters) {
        this.meters = paramMeters;
        this.clazzes = new HashSet<Class<?>>();
    }

    /** Standard constructor. Only using a TimeMeter */
    public Benchmark() {
        this.meters = new HashSet<AbstractMeter>();
        meters.add(new TimeMeter(Time.MilliSeconds));
        this.clazzes = new HashSet<Class<?>>();
    }

    /**
     * Adding a class to bench to this benchmark. This class should contain
     * benchmarkable methods, otherwise it will be ignored.
     * 
     * @param clazz
     *            to be added.
     */
    public void add(final Class<?> clazz) {
        this.clazzes.add(clazz);
    }

    /**
     * Running this benchmark with a given kind of arrangement. See @link
     * {@link AbstractMethodArrangement.KindOfElementArrangement} for different
     * kinds.
     * 
     * @param kind
     *            of methodArrangement.
     */
    public void run(final KindOfElementArrangement kind) {

        // getting Benchmarkables
        final Set<BenchmarkElement> elements = getBenchmarkableMethods();

        // arranging them
        final AbstractMethodArrangement arrangement =
                AbstractMethodArrangement.getMethodArrangement(elements, kind);

        // getting the mapping and executing beforemethod
        final Map<Class<?>, Object> objectsToExecute = setUpObjectsToExecute();

        // executing the bench for the arrangement
        for (final BenchmarkElement elem : arrangement) {
            final BenchmarkExecutor exec =
                    BenchmarkExecutor.getExecutor(elem, meters);

            final Object obj =
                    objectsToExecute.get(elem
                            .getMeth().getMethodToBench().getDeclaringClass());

            try {
                exec.executeBeforeMethods(obj);
                exec.executeBench(obj);
                exec.executeAfterMethods(obj);
            } catch (IllegalAccessException e) {

            }

        }

        // cleaning up methods to benchmark
        tearDownObjectsToExecute(objectsToExecute);

    }

    /**
     * Setting up executable object for all registered classes and executing
     * {@link BeforeBenchClass} annotated methods.
     * 
     * @return a mapping with class->object for all registered classes-
     * @throws IllegalAccessException
     *             if the instantiation of a class fails
     */
    private final Map<Class<?>, Object> setUpObjectsToExecute() {
        // datastructure initialization for all objects
        final Map<Class<?>, Object> objectsToUse =
                new Hashtable<Class<?>, Object>();

        // generating objects for each registered class
        for (final Class<?> clazz : clazzes) {
            Object objectToUse;
            try {
                objectToUse = clazz.newInstance();

                // executing the beforeBenchclass method
                final Method beforeClassMeth =
                        BenchmarkMethod.findAndCheckAnyMethodByAnnotation(
                                clazz, BeforeBenchClass.class);
                BenchmarkExecutor.checkAndExecute(objectToUse, beforeClassMeth);

            } catch (InstantiationException e) {
                objectToUse = e;
            } catch (IllegalAccessException e) {
                objectToUse = e;
            }
            // putting the object to the mapping
            objectsToUse.put(clazz, objectToUse);

        }
        return objectsToUse;
    }

    /**
     * Tear down executable object for all registered classes and executing
     * {@link AfterBenchClass} annotated methods.
     * 
     * @param objects
     *            a mapping with class->object to be teared down
     * @throws IllegalAccessException
     *             if the tear down of a class fails
     */
    private final void tearDownObjectsToExecute(
            final Map<Class<?>, Object> objects) {

        // executing tearDown for all clazzes registered in given Map
        for (final Class<?> clazz : objects.keySet()) {
            final Object objectToUse = objects.get(clazz);

            try {
                // executing AfterClass for all objects.
                final Method afterClassMeth =
                        BenchmarkMethod.findAndCheckAnyMethodByAnnotation(
                                clazz, AfterBenchClass.class);
                BenchmarkExecutor.checkAndExecute(objectToUse, afterClassMeth);
            } catch (IllegalAccessException e) {
                // TODO think about exception handling
            }

        }
    }

    /**
     * Getting all benchmarkable objects out of the registered classes with the
     * annotated number of runs.
     * 
     * @return a Set with {@link BenchmarkMethod}
     */
    private final Set<BenchmarkElement> getBenchmarkableMethods() {
        // Generating Set for returnVal
        final Set<BenchmarkElement> elems = new HashSet<BenchmarkElement>();
        // Getting all Methods and testing if its benchmarkable
        for (final Class<?> clazz : clazzes) {
            for (final Method meth : clazz.getDeclaredMethods()) {
                // Check if benchmarkable, if so, insert to returnVal;
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final int numberOfRuns =
                            BenchmarkMethod.getNumberOfAnnotatedRuns(meth);
                    final BenchmarkMethod benchmarkMeth =
                            new BenchmarkMethod(meth);

                    // getting the number of runs and adding this number of
                    // elements to the set to be evaluated.
                    for (int i = 0; i < numberOfRuns; i++) {
                        elems.add(new BenchmarkElement(benchmarkMeth));
                    }
                }
            }
        }
        return elems;
    }
}
