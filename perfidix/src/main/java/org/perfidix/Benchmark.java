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
import java.util.HashMap;
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
import org.perfidix.exceptions.PerfidixMethodCheckException;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;

/**
 * Class to hold all classes which want to be benchmarked.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class Benchmark {

    /** Set with all registered meters. */
    private transient final Set<AbstractMeter> meters;

    /** Set with all used classes. */
    private transient final Set<Class<?>> clazzes;

    /** Already instantiated objects */
    private transient final Set<Object> objects;

    /** Simple random for gc-prob */
    private transient static final Random RAN = new Random();

    /** Configuration of benchmark, holding everything. */
    private transient final AbstractConfig conf;

    /**
     * Constructor with a fixed set of used meters.
     * 
     * @param paramConf
     *            Configuration for Benchmark
     */
    public Benchmark(final AbstractConfig paramConf) {
        conf = paramConf;
        this.meters = new LinkedHashSet<AbstractMeter>();
        this.clazzes = new LinkedHashSet<Class<?>>();
        this.objects = new LinkedHashSet<Object>();

        for (final AbstractMeter meter : conf.getMeters()) {
            this.meters.add(meter);
        }
    }

    /**
     * Adding a class to bench to this benchmark. This class should contain
     * benchmarkable methods, otherwise it will be ignored.
     * 
     * @param clazz
     *            to be added.
     */
    public void add(final Class<?> clazz) {
        if (this.clazzes.contains(clazz)) {
            throw new IllegalArgumentException(
                    "Only one class-instance per benchmark allowed");
        } else {
            this.clazzes.add(clazz);
        }
    }

    /**
     * Adding a already instantiated objects to benchmark. Per benchmark, only
     * one objects of each class is allowed.
     * 
     * @param obj
     *            to be added
     */
    public void add(final Object obj) {
        final Class<?> clazz = obj.getClass();

        if (this.clazzes.contains(clazz)) {
            throw new IllegalArgumentException(
                    "Only one class-instance per benchmark allowed");
        } else {
            this.clazzes.add(clazz);
            this.objects.add(obj);
        }
    }

    /**
     * Getting the number of all methods and all runs
     * 
     * @return a map with all methods and the runs.
     */
    public Map<BenchmarkMethod, Integer> getNumberOfMethodsAndRuns() {
        final Map<BenchmarkMethod, Integer> returnVal =
                new HashMap<BenchmarkMethod, Integer>();
        final Set<BenchmarkMethod> meths = getBenchmarkMethods();
        for (final BenchmarkMethod meth : meths) {
            returnVal.put(meth, BenchmarkMethod.getNumberOfAnnotatedRuns(meth
                    .getMethodToBench()));
        }
        return returnVal;
    }

    /**
     * Running this benchmark
     * 
     * @return {@link BenchmarkResult} the result in an {@link BenchmarkResult}
     *         container.
     */
    public BenchmarkResult run() {
        final BenchmarkResult res = new BenchmarkResult(conf.getListener());
        BenchmarkExecutor.initialize(meters, res);

        // getting Benchmarkables
        final Set<BenchmarkElement> elements = getBenchmarkElements();

        // arranging them
        final AbstractMethodArrangement arrangement =
                AbstractMethodArrangement.getMethodArrangement(elements, conf
                        .getArrangement());

        // instantiate methods
        final Map<Class<?>, Object> instantiatedObj = instantiateMethods(res);

        // getting the mapping and executing beforemethod
        final Map<Class<?>, Object> objectsToExecute =
                executeBeforeBenchClass(instantiatedObj, res);

        // executing the bench for the arrangement
        for (final BenchmarkElement elem : arrangement) {
            final BenchmarkExecutor exec = BenchmarkExecutor.getExecutor(elem);

            final Object obj =
                    objectsToExecute.get(elem
                            .getMeth().getMethodToBench().getDeclaringClass());
            // check needed because of failed initialization of objects
            if (obj != null) {
                exec.executeBeforeMethods(obj);

                // invoking gc if possible
                if (RAN.nextDouble() < conf.getGcProb()) {
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
    private Map<Class<?>, Object> instantiateMethods(final BenchmarkResult res) {
        // datastructure initialization for all objects
        final Map<Class<?>, Object> objectsToUse =
                new Hashtable<Class<?>, Object>();

        // generating including already instaniated objects
        for (final Object obj : this.objects) {
            final Class<?> clazz = obj.getClass();
            objectsToUse.put(clazz, obj);
        }

        // generating objects for each registered class
        for (final Class<?> clazz : clazzes) {
            // generating a new instance on which the benchmark will be
            // performed if there isn't a user generated one
            if (!objectsToUse.containsKey(clazz)) {
                try {
                    final Object obj = clazz.newInstance();
                    objectsToUse.put(clazz, obj);
                    // otherwise adding an exception to the result
                } catch (final InstantiationException e) {
                    res.addException(new PerfidixMethodInvocationException(
                            e, BeforeBenchClass.class));
                } catch (final IllegalAccessException e) {
                    res.addException(new PerfidixMethodInvocationException(
                            e, BeforeBenchClass.class));
                }

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
    private Map<Class<?>, Object> executeBeforeBenchClass(
            final Map<Class<?>, Object> instantiatedObj,
            final BenchmarkResult res) {

        final Map<Class<?>, Object> returnVal =
                new Hashtable<Class<?>, Object>();

        // invoking before bench class
        for (final Class<?> clazz : instantiatedObj.keySet()) {

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
                if (beforeClassMeth == null) {
                    // ...either the objects is directly mapped to the class
                    // for executing the benches
                    returnVal.put(clazz, objectToUse);
                } else {
                    // ... or the beforeMethod will be executed and a
                    // possible exception stored to the result...
                    final PerfidixMethodCheckException beforeByCheck =
                            BenchmarkExecutor.checkMethod(
                                    objectToUse, beforeClassMeth,
                                    BeforeBenchClass.class);
                    if (beforeByCheck == null) {
                        final PerfidixMethodInvocationException beforeByInvok =
                                BenchmarkExecutor.invokeMethod(
                                        objectToUse, beforeClassMeth,
                                        BeforeBenchClass.class);
                        if (beforeByInvok == null) {
                            returnVal.put(clazz, objectToUse);
                        } else {
                            res.addException(beforeByInvok);
                        }
                    } else {
                        res.addException(beforeByCheck);
                    }
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
    private void tearDownObjectsToExecute(
            final Map<Class<?>, Object> objects, final BenchmarkResult res) {

        // executing tearDown for all clazzes registered in given Map
        for (final Class<?> clazz : objects.keySet()) {
            final Object objectToUse = objects.get(clazz);
            if (objectToUse != null) {
                // executing AfterClass for all objects.
                Method afterClassMeth = null;
                try {
                    afterClassMeth =
                            BenchmarkMethod.findAndCheckAnyMethodByAnnotation(
                                    clazz, AfterBenchClass.class);
                } catch (final PerfidixMethodCheckException e) {
                    res.addException(e);
                }
                // if afterClassMethod exists, the method will be executed and
                // possible failures will be stored in the BenchmarkResult
                if (afterClassMeth != null) {
                    final PerfidixMethodCheckException afterByCheck =
                            BenchmarkExecutor.checkMethod(
                                    objectToUse, afterClassMeth,
                                    AfterBenchClass.class);
                    if (afterByCheck == null) {
                        final PerfidixMethodInvocationException afterByInvok =
                                BenchmarkExecutor.invokeMethod(
                                        objectToUse, afterClassMeth,
                                        AfterBenchClass.class);
                        if (afterByInvok != null) {
                            res.addException(afterByInvok);
                        }
                    } else {
                        res.addException(afterByCheck);
                    }
                }
            }
        }
    }

    /**
     * Getting all Benchmarkable methods out of the registered class.
     * 
     * @return a Set with {@link BenchmarkMethod}
     */
    public Set<BenchmarkMethod> getBenchmarkMethods() {
        // Generating Set for returnVal
        final Set<BenchmarkMethod> elems = new LinkedHashSet<BenchmarkMethod>();
        // Getting all Methods and testing if its benchmarkable
        for (final Class<?> clazz : clazzes) {
            for (final Method meth : clazz.getDeclaredMethods()) {
                // Check if benchmarkable, if so, insert to returnVal;
                if (BenchmarkMethod.isBenchmarkable(meth)) {
                    final BenchmarkMethod benchmarkMeth =
                            new BenchmarkMethod(meth);
                    elems.add(benchmarkMeth);
                }
            }
        }
        return elems;
    }

    /**
     * Getting all benchmarkable objects out of the registered classes with the
     * annotated number of runs.
     * 
     * @return a Set with {@link BenchmarkMethod}
     */
    public Set<BenchmarkElement> getBenchmarkElements() {

        // Generating Set for returnVal
        final Set<BenchmarkElement> elems =
                new LinkedHashSet<BenchmarkElement>();

        final Set<BenchmarkMethod> meths = getBenchmarkMethods();

        for (final BenchmarkMethod meth : meths) {
            final int numberOfRuns =
                    BenchmarkMethod.getNumberOfAnnotatedRuns(meth
                            .getMethodToBench());

            // getting the number of runs and adding this number of
            // elements to the set to be evaluated.
            for (int i = 0; i < numberOfRuns; i++) {
                elems.add(new BenchmarkElement(meth));
            }
        }

        return elems;
    }
}
