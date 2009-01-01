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
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.element.AbstractMethodArrangement;
import org.perfidix.element.BenchmarkElement;
import org.perfidix.element.BenchmarkExecutor;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.element.AbstractMethodArrangement.KindOfElementArrangement;
import org.perfidix.failureHandling.PerfidixMethodCheckException;
import org.perfidix.failureHandling.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.NotAutomaticallyTicking;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.result.BenchmarkResult;

/**
 * Class to hold all classes which want to be benchmarked.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class Benchmark {

    /** Set with all registered meters. */
    private final LinkedHashSet<AbstractMeter> meters;

    /** Set with meters which are ticking automatically. */
    private final LinkedHashSet<AbstractMeter> automaticallyTickingMeters;

    /** Set with all used classes */
    private final Set<Class<?>> clazzes;

    /**
     * Constructor with a fixed set of used meters.
     * 
     * @param meters
     *            meters to use.
     */
    public Benchmark(final AbstractMeter... meters) {
        this.meters = new LinkedHashSet<AbstractMeter>();
        this.clazzes = new LinkedHashSet<Class<?>>();

        for (final AbstractMeter meter : meters) {
            this.meters.add(meter);
        }

        this.automaticallyTickingMeters = new LinkedHashSet<AbstractMeter>();
        for (final AbstractMeter meter : meters) {
            if (!(meter instanceof NotAutomaticallyTicking)) {
                this.automaticallyTickingMeters.add(meter);
            }
        }
    }

    /** Standard constructor. Only using a TimeMeter */
    public Benchmark() {
        this(new TimeMeter(Time.MilliSeconds));
    }

    /**
     * Adding a class to bench to this benchmark. This class should contain
     * benchmarkable methods, otherwise it will be ignored.
     * 
     * @param clazz
     *            to be added.
     */
    public final void add(final Class<?> clazz) {
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
    public final BenchmarkResult run(final KindOfElementArrangement kind) {

        final BenchmarkResult res = new BenchmarkResult(this);
        BenchmarkExecutor.initialize(automaticallyTickingMeters, res);

        // getting Benchmarkables
        final Set<BenchmarkElement> elements = getBenchmarkableMethods();

        // arranging them
        final AbstractMethodArrangement arrangement =
                AbstractMethodArrangement.getMethodArrangement(elements, kind);

        // getting the mapping and executing beforemethod
        final Map<Class<?>, Object> objectsToExecute =
                setUpObjectsToExecute(res);

        // executing the bench for the arrangement
        for (final BenchmarkElement elem : arrangement) {
            final BenchmarkExecutor exec = BenchmarkExecutor.getExecutor(elem);

            final Object obj =
                    objectsToExecute.get(elem
                            .getMeth().getMethodToBench().getDeclaringClass());

            exec.executeBeforeMethods(obj);
            exec.executeBench(obj);
            exec.executeAfterMethods(obj);

        }

        // cleaning up methods to benchmark
        tearDownObjectsToExecute(objectsToExecute, res);
        return null;
    }

    /**
     * Setting up executable object for all registered classes and executing
     * {@link BeforeBenchClass} annotated methods.
     * 
     * @return a mapping with class->object for all registered classes-
     * @throws IllegalAccessException
     *             if the instantiation of a class fails
     */
    private final Map<Class<?>, Object> setUpObjectsToExecute(
            final BenchmarkResult res) {
        // datastructure initialization for all objects
        final Map<Class<?>, Object> objectsToUse =
                new Hashtable<Class<?>, Object>();

        // generating objects for each registered class
        for (final Class<?> clazz : clazzes) {
            Object objectToUse = null;
            try {
                objectToUse = clazz.newInstance();
            } catch (final InstantiationException e) {
                res.addException(new PerfidixMethodInvocationException(
                        e, null, BeforeBenchClass.class));
            } catch (final IllegalAccessException e) {
                res.addException(new PerfidixMethodInvocationException(
                        e, null, BeforeBenchClass.class));
            }
            if (objectsToUse != null) {
                // executing the beforeBenchclass method
                Method beforeClassMeth = null;
                try {
                    beforeClassMeth =
                            BenchmarkMethod.findAndCheckAnyMethodByAnnotation(
                                    clazz, BeforeBenchClass.class);
                } catch (final PerfidixMethodCheckException e) {
                    res.addException(e);
                }
                if (beforeClassMeth != null) {
                    final PerfidixMethodCheckException e =
                            BenchmarkExecutor.checkReflectiveExecutableMethod(
                                    objectToUse, beforeClassMeth,
                                    BeforeBenchClass.class);
                    if (e == null) {
                        final PerfidixMethodInvocationException e2 =
                                BenchmarkExecutor
                                        .invokeReflectiveExecutableMethod(
                                                objectToUse, beforeClassMeth,
                                                BeforeBenchClass.class);
                        if (e2 == null) {
                            // putting the object to the mapping
                            objectsToUse.put(clazz, objectToUse);
                        } else {
                            res.addException(e2);
                        }
                    } else {
                        res.addException(e);
                    }
                }
            }

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
            final Map<Class<?>, Object> objects, final BenchmarkResult res) {

        // executing tearDown for all clazzes registered in given Map
        for (final Class<?> clazz : objects.keySet()) {
            final Object objectToUse = objects.get(clazz);

            // executing AfterClass for all objects.
            Method afterClassMeth = null;
            try {
                afterClassMeth =
                        BenchmarkMethod.findAndCheckAnyMethodByAnnotation(
                                clazz, AfterBenchClass.class);
            } catch (final PerfidixMethodCheckException e) {
                res.addException(e);
            }
            if (afterClassMeth != null) {
                final PerfidixMethodCheckException e1 =
                        BenchmarkExecutor.checkReflectiveExecutableMethod(
                                objectToUse, afterClassMeth,
                                AfterBenchClass.class);
                if (e1 == null) {
                    final PerfidixMethodInvocationException e2 =
                            BenchmarkExecutor.invokeReflectiveExecutableMethod(
                                    objectToUse, afterClassMeth,
                                    AfterBenchClass.class);
                    if (e2 != null) {
                        res.addException(e2);
                    }
                } else {
                    res.addException(e1);
                }
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
        final Set<BenchmarkElement> elems =
                new LinkedHashSet<BenchmarkElement>();
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
