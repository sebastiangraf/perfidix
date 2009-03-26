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
import java.util.Random;
import java.util.Set;

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.element.AbstractMethodArrangement;
import org.perfidix.element.BenchmarkElement;
import org.perfidix.element.BenchmarkExecutor;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.element.NoMethodArrangement;
import org.perfidix.failureHandling.PerfidixMethodCheckException;
import org.perfidix.failureHandling.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * Class to hold all classes which want to be benchmarked.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class Benchmark {

    /** Set with all registered meters. */
    private final LinkedHashSet<AbstractMeter> meters;

    /** Set with all used classes. */
    private final Set<Class< ? >> clazzes;

    /** Already instantiated objects */
    private final Set<Object> objects;

    /**
     * Constructor with a fixed set of used meters.
     * 
     * @param paramMeters
     *            meters to use.
     */
    public Benchmark(final AbstractMeter... paramMeters) {
        this.meters = new LinkedHashSet<AbstractMeter>();
        this.clazzes = new LinkedHashSet<Class< ? >>();
        this.objects = new LinkedHashSet<Object>();

        for (final AbstractMeter meter : paramMeters) {
            this.meters.add(meter);
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
    public final void add(final Class< ? > clazz) {
        if (!this.clazzes.contains(clazz)) {
            this.clazzes.add(clazz);
        } else {
            throw new IllegalArgumentException(
                    "Only one class-instance per benchmark allowed");
        }

    }

    /**
     * Adding a already instantiated objects to benchmark. Per benchmark, only
     * one objects of each class is allowed.
     * 
     * @param obj
     *            to be added
     */
    public final void add(final Object obj) {
        final Class< ? > clazz = obj.getClass();

        if (!this.clazzes.contains(clazz)) {
            this.clazzes.add(clazz);
            this.objects.add(obj);
        } else {
            throw new IllegalArgumentException(
                    "Only one class-instance per benchmark allowed");
        }
    }

    /**
     * Running this benchmark with no arrangement. Then the
     * {@link NoMethodArrangement} is chosen.
     * 
     * @param visitor
     *            possible visitors
     * @param gcProb
     *            probability to invoke gc
     * @return the result of the Benchmark
     */
    public final BenchmarkResult run(
            final double gcProb, final AbstractOutput... visitor) {
        return run(gcProb, KindOfArrangement.NoArrangement, visitor);
    }

    /**
     * Running this benchmark with no arrangement and gc invoking for every
     * bench. The {@link NoMethodArrangement} is chosen.
     * 
     * @param visitor
     *            possible visitors
     * @return the result of the Benchmark
     */
    public final BenchmarkResult run(final AbstractOutput... visitor) {
        return run(1.0d, visitor);
    }

    /**
     * Running this benchmark with a given arrangement and gc invoking for every
     * bench.
     * 
     * @param visitor
     *            possible visitors
     * @param kind
     *            of methodArrangement.
     * @return the result of the Benchmark
     */
    public final BenchmarkResult run(
            final KindOfArrangement kind, final AbstractOutput... visitor) {
        return run(1.0d, kind, visitor);
    }

    /**
     * Running this benchmark with a given kind of arrangement. See @link
     * {@link KindOfArrangement} for different kinds.
     * 
     * @param kind
     *            of methodArrangement.
     * @param visitor
     *            {@link AbstractOutput} instances which are listening to
     *            upcoming results. Note that this is not an alternative for the
     *            visit-functionality with the {@link BenchmarkResult} returnval
     *            of the {@link AbstractOutput}
     * @param gcProb
     *            probability to invoke gc
     * @return {@link BenchmarkResult} the result in an {@link BenchmarkResult}
     *         container.
     */
    public final BenchmarkResult run(
            final double gcProb, final KindOfArrangement kind,
            final AbstractOutput... visitor) {
        final Random ran = new Random();
        final BenchmarkResult res = new BenchmarkResult(visitor);
        BenchmarkExecutor.initialize(meters, res);

        // getting Benchmarkables
        final Set<BenchmarkElement> elements = getBenchmarkableMethods();

        // arranging them
        final AbstractMethodArrangement arrangement =
                AbstractMethodArrangement.getMethodArrangement(
                        elements, kind);

        // instantiate methods
        final Map<Class< ? >, Object> instantiatedObj =
                instantiateMethods(res);

        // getting the mapping and executing beforemethod
        final Map<Class< ? >, Object> objectsToExecute =
                executeBeforeBenchClass(instantiatedObj, res);

        // executing the bench for the arrangement
        for (final BenchmarkElement elem : arrangement) {
            final BenchmarkExecutor exec =
                    BenchmarkExecutor.getExecutor(elem);

            final Object obj =
                    objectsToExecute.get(elem
                            .getMeth().getMethodToBench()
                            .getDeclaringClass());
            // check needed because of failed initialization of objects
            if (obj != null) {
                exec.executeBeforeMethods(obj);

                // invoking gc if possible
                if (ran.nextDouble() < gcProb) {
                    System.gc();
                }

                exec.executeBench(obj);
                exec.executeAfterMethods(obj);
            }
        }

        // cleaning up methods to benchmark
        tearDownObjectsToExecute(objectsToExecute, res);
        return res;
    }

    /**
     * Setting up executable objects for all registered classes and executing
     * {@link BeforeBenchClass} annotated methods. If an {@link Exception}
     * occurs, this failure will be stored in the {@link BenchmarkResult} and
     * the class will not be instantiated
     * 
     * @param res
     *            {@link BenchmarkResult} for storing possible failures.
     * @return a mapping with class->objects for all registered classes-
     */
    private final Map<Class< ? >, Object> instantiateMethods(
            final BenchmarkResult res) {
        // datastructure initialization for all objects
        final Map<Class< ? >, Object> objectsToUse =
                new Hashtable<Class< ? >, Object>();

        // generating including already instaniated objects
        for (final Object obj : this.objects) {
            final Class< ? > clazz = obj.getClass();
            objectsToUse.put(clazz, obj);
        }

        // generating objects for each registered class
        for (final Class< ? > clazz : clazzes) {
            // generating a new instance on which the benchmark will be
            // performed if there isn't a user generated one
            if (!objectsToUse.containsKey(clazz)) {
                Object obj = null;
                try {
                    obj = clazz.newInstance();
                    // otherwise adding an exception to the result
                } catch (final InstantiationException e) {
                    res
                            .addException(new PerfidixMethodInvocationException(
                                    e, BeforeBenchClass.class));
                } catch (final IllegalAccessException e) {
                    res
                            .addException(new PerfidixMethodInvocationException(
                                    e, BeforeBenchClass.class));
                }
                objectsToUse.put(clazz, obj);
            }
        }

        return objectsToUse;
    }

    /**
     * Executing beforeBenchClass if present.
     * 
     * @param instantiatedObj
     *            with the instantiatedObj;
     * @param res
     *            where the Exceptions should be stored to
     * @return valid instances with valid beforeCall
     */
    private final Map<Class< ? >, Object> executeBeforeBenchClass(
            final Map<Class< ? >, Object> instantiatedObj,
            final BenchmarkResult res) {

        final Map<Class< ? >, Object> returnVal =
                new Hashtable<Class< ? >, Object>();

        // invoking before bench class
        for (final Class< ? > clazz : instantiatedObj.keySet()) {

            final Object objectToUse = instantiatedObj.get(clazz);

            // ..the search for the beforeClassMeth begins...
            Method beforeClassMeth = null;
            boolean continueVal = true;
            try {
                beforeClassMeth =
                        BenchmarkMethod.findAndCheckAnyMethodByAnnotation(
                                clazz, BeforeBenchClass.class);
                // ... and if this search is throwing an exception, the
                // exception will be added and a flag is set to break up
            } catch (final PerfidixMethodCheckException e) {
                res.addException(e);
                continueVal = false;
            }
            // if everything worked well...
            if (continueVal) {
                // ... either the beforeMethod will be executed and a
                // possible exception stored to the result...
                if (beforeClassMeth != null) {
                    final PerfidixMethodCheckException e =
                            BenchmarkExecutor.checkMethod(
                                    objectToUse, beforeClassMeth,
                                    BeforeBenchClass.class);
                    if (e == null) {
                        final PerfidixMethodInvocationException e2 =
                                BenchmarkExecutor.invokeMethod(
                                        objectToUse, beforeClassMeth,
                                        BeforeBenchClass.class);
                        if (e2 == null) {
                            returnVal.put(clazz, objectToUse);
                        } else {
                            res.addException(e2);
                        }
                    } else {
                        res.addException(e);
                    }
                } else {
                    // ...or the objects is directly mapped to the class
                    // for
                    // executing the benches
                    returnVal.put(clazz, objectToUse);
                }
            }
        }
        return returnVal;
    }

    /**
     * Tear down executable objects for all registered classes and executing
     * {@link AfterBenchClass} annotated methods.
     * 
     * @param objects
     *            a mapping with class->objects to be teared down
     * @param res
     *            the {@link BenchmarkResult} for storing possible failures.
     */
    private final void tearDownObjectsToExecute(
            final Map<Class< ? >, Object> objects,
            final BenchmarkResult res) {

        // executing tearDown for all clazzes registered in given Map
        for (final Class< ? > clazz : objects.keySet()) {
            final Object objectToUse = objects.get(clazz);
            if (objectToUse != null) {
                // executing AfterClass for all objects.
                Method afterClassMeth = null;
                try {
                    afterClassMeth =
                            BenchmarkMethod
                                    .findAndCheckAnyMethodByAnnotation(
                                            clazz, AfterBenchClass.class);
                } catch (final PerfidixMethodCheckException e) {
                    res.addException(e);
                }
                // if afterClassMethod exists, the method will be executed and
                // possible failures will be stored in the BenchmarkResult
                if (afterClassMeth != null) {
                    final PerfidixMethodCheckException e1 =
                            BenchmarkExecutor.checkMethod(
                                    objectToUse, afterClassMeth,
                                    AfterBenchClass.class);
                    if (e1 == null) {
                        final PerfidixMethodInvocationException e2 =
                                BenchmarkExecutor.invokeMethod(
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
        for (final Class< ? > clazz : clazzes) {
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
