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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.failureHandling.AbstractInvocationReturn;
import org.perfidix.failureHandling.InvalidInvocationReturn;
import org.perfidix.failureHandling.PerfidixMethodCheckException;
import org.perfidix.failureHandling.ValidInvocationReturn;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Corresponding to each method, an executor is launched to execute
 * {@link BeforeFirstRun}, {@link BeforeEachRun}, {@link AfterEachRun} and
 * {@link AfterLastRun} classes. To store the data if the single-execute before
 * classes have been executed, this class is implemented as a singleton to store
 * this information related to the method. All the data comes from the
 * {@link BenchmarkMethod} class.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class BenchmarkExecutor {

    /**
     * Static mapping for all methods to be executed because of the single-runs
     * before/after methods.
     */
    private final static Map<BenchmarkMethod, BenchmarkExecutor> executor =
            new Hashtable<BenchmarkMethod, BenchmarkExecutor>();

    /** Boolean to be sure that the beforeFirstRun was not executed yet. */
    private boolean beforeFirstRunExecuted;
    /** Boolean to be sure that the afterLastRun was not executed yet. */
    private boolean afterLastRunExecuted;

    /**
     * Corresponding BenchmarkElement of this executor to get the before/after
     * methods
     */
    private final BenchmarkMethod element;

    // /** Resultset for this method. */
    // private final MethodResult methodRes;

    /** Set with all meters to be benched automatically */
    private final Set<AbstractMeter> metersToBench;

    /**
     * Private constructor, just setting the booleans and one element to get the
     * before/after methods.
     * 
     * @param paramElement
     *            BenchmarkElement to provide easy access to the before/after
     *            methods.
     * @paramMeters All meters that should be included in the execution process
     *              of the bench itself.
     */
    private BenchmarkExecutor(
            final BenchmarkMethod paramElement,
            final Set<AbstractMeter> paramMeters) {
        beforeFirstRunExecuted = false;
        afterLastRunExecuted = false;
        element = paramElement;
        // methodRes = new MethodResult(element.getMethodToBench());
        metersToBench = paramMeters;

    }

    /**
     * Getting the executor corresponding to a BenchmarkElement.
     * 
     * @param meth
     *            for the executor. If the underlaying {@link Method} was not
     *            registered, a new mapping-entry will be created.
     * @param meters
     *            meters to be benched, note that this set has to be equal for
     *            all executor-instance calls, that means the setup of the
     *            meters is not allowed to change in meantime because of the
     *            returnVal.
     * @return the BenchmarkExecutor corresponding to the Method of the
     *         BenchmarkElement
     */
    public static final BenchmarkExecutor getExecutor(
            final BenchmarkElement meth, final Set<AbstractMeter> meters) {
        // check if new instance needs to be created
        if (!executor.containsKey(meth.getMeth())) {
            executor.put(meth.getMeth(), new BenchmarkExecutor(
                    meth.getMeth(), meters));
        }

        // returning the executor
        return executor.get(meth.getMeth());

    }

    /**
     * Executing the {@link BeforeFirstRun}-annotated methods (if still wasn't)
     * and the {@link BeforeEachRun} methods.
     * 
     * @param objToExecute
     *            the object of the class where the bench runs currently in.
     * @return a List containing {@link AbstractInvocationReturn} objects.
     */
    public final AbstractInvocationReturn executeBeforeMethods(
            final Object objToExecute) {

        // invoking once the beforeFirstRun-method
        if (!beforeFirstRunExecuted) {
            beforeFirstRunExecuted = true;
            try {
                final Method beforeFirst = element.findBeforeFirstRun();
                if (beforeFirst != null) {
                    final AbstractInvocationReturn invoRes =
                            checkAndExecute(
                                    BeforeFirstRun.class, objToExecute,
                                    beforeFirst);
                    if (invoRes instanceof InvalidInvocationReturn) {
                        return invoRes;
                    }

                }
            } catch (final PerfidixMethodCheckException e) {
                return new InvalidInvocationReturn(
                        e, e.getMethod(), BeforeFirstRun.class);
            }

        }

        // invoking the beforeEachRun-method
        try {
            final Method beforeEach = element.findBeforeEachRun();
            if (beforeEach != null) {
                final AbstractInvocationReturn invoRes =
                        checkAndExecute(
                                BeforeEachRun.class, objToExecute, beforeEach);
                return invoRes;
            } else {
                return new ValidInvocationReturn(null, BeforeEachRun.class);
            }
        } catch (final PerfidixMethodCheckException e) {
            return new InvalidInvocationReturn(
                    e, e.getMethod(), BeforeEachRun.class);
        }

    }

    /**
     * Execution of bench method. All data is stored corresponding to the
     * meters.
     * 
     * @param objToExecute
     *            the instance of the benchclass where the method should be
     *            executed with.
     */
    public final AbstractInvocationReturn executeBench(final Object objToExecute) {

        final double[] meterResults = new double[metersToBench.size()];

        final Method meth = element.getMethodToBench();

        int i = 0;
        for (final AbstractMeter meter : metersToBench) {
            meterResults[i] = meter.getValue();
            i++;
        }

        final AbstractInvocationReturn res =
                invokeReflectiveExecutableMethod(
                        objToExecute, meth, Bench.class);

        i = 0;
        for (final AbstractMeter meter : metersToBench) {
            meterResults[i] = meter.getValue() - meterResults[i];
            i++;
        }

        if (res instanceof InvalidInvocationReturn) {
            return res;
        } else {
            return new ValidInvocationReturn(meth, Bench.class, meterResults);
        }

    }

    /**
     * Executing the {@link AfterLastRun}-annotated methods (if still wasn't)
     * and the {@link AfterEachRun} methods.
     * 
     * @param objToExecute
     *            the object of the class where the bench runs currently in.
     */
    public final AbstractInvocationReturn executeAfterMethods(
            final Object objToExecute) {

        // invoking once the beforeFirstRun-method
        if (!afterLastRunExecuted) {
            afterLastRunExecuted = true;
            try {
                final Method afterLast = element.findAfterLastRun();
                if (afterLast != null) {
                    final AbstractInvocationReturn invoRes =
                            checkAndExecute(
                                    AfterLastRun.class, objToExecute, afterLast);
                    if (invoRes instanceof InvalidInvocationReturn) {
                        return invoRes;
                    }
                }
            } catch (final PerfidixMethodCheckException e) {
                return new InvalidInvocationReturn(
                        e, e.getMethod(), AfterLastRun.class);
            }

        }

        // invoking the beforeEachRun-method
        try {
            final Method afterEach = element.findAfterEachRun();
            if (afterEach != null) {
                final AbstractInvocationReturn invoRes =
                        checkAndExecute(
                                AfterEachRun.class, objToExecute, afterEach);
                return invoRes;
            } else {
                return new ValidInvocationReturn(null, BeforeEachRun.class);
            }
        } catch (final PerfidixMethodCheckException e) {
            return new InvalidInvocationReturn(
                    e, e.getMethod(), AfterEachRun.class);
        }

    }

    /**
     * Checking if the method and the object correlate to each other and execute
     * it
     * 
     * @param relatedAnno
     *            annotation to be related to
     * @param obj
     *            to be checked with the method
     * @param meth
     *            to be checked with the class
     * @return {@link AbstractInvocationReturn} instance
     */
    public static final AbstractInvocationReturn checkAndExecute(
            final Class<? extends Annotation> relatedAnno, final Object obj,
            final Method meth) {

        final Exception e = checkMethod(obj, meth);
        if (e != null) {
            return new InvalidInvocationReturn(e, meth, relatedAnno);
        } else {
            return invokeReflectiveExecutableMethod(obj, meth, relatedAnno);
        }

    }

    /**
     * Method to invoke a reflective invokable method.
     * 
     * @param obj
     *            to be executed
     * @param meth
     *            to be executed
     * @throws IllegalAccessException
     *             if something is going wrong
     */
    private static final AbstractInvocationReturn invokeReflectiveExecutableMethod(
            final Object obj, final Method meth,
            final Class<? extends Annotation> relatedAnno) {
        final Object[] args = {};

        try {
            meth.invoke(obj, args);
            return new ValidInvocationReturn(meth, relatedAnno);
        } catch (Exception e) {
            return new InvalidInvocationReturn(e, meth, relatedAnno);
        }
    }

    /**
     * Checking a method if it is reflective executable and if the mapping to
     * the object fits.
     * 
     * @param obj
     *            to be checked.
     * @param meth
     *            to be checked
     * @return an Exception if something is wrong in the mapping, null
     *         otherwise.
     */
    private static final Exception checkMethod(
            final Object obj, final Method meth) {
        // check if the class of the object to be executed has the given method
        boolean correlationClassMethod = false;
        for (final Method methodOfClass : obj.getClass().getDeclaredMethods()) {
            if (methodOfClass.equals(meth)) {
                correlationClassMethod = true;
            }
        }

        if (!correlationClassMethod) {
            return new IllegalStateException(new StringBuilder(
                    "Object to execute ")
                    .append(obj).append(" is not having a Method named ")
                    .append(meth).append(".").toString());
        }

        // check if the method is reflected executable
        if (!BenchmarkMethod.isReflectedExecutable(meth)) {
            return new IllegalAccessException(new StringBuilder(
                    "Method to execute ").append(meth).append(
                    " is not reflected executable.").toString());
        }
        return null;
    }

    public static final BenchmarkResult getBenchmarkResult() {
        final Map<Class<?>, Set<MethodResult>> classTempResults =
                new Hashtable<Class<?>, Set<MethodResult>>();
        for (final BenchmarkExecutor exec : executor.values()) {

            final Class<?> clazz = exec.methodRes.getMeth().getDeclaringClass();
            if (!classTempResults.containsKey(clazz)) {
                classTempResults.put(clazz, new HashSet<MethodResult>());
            }
            classTempResults.get(clazz).add(exec.methodRes);
        }

        final Set<ClassResult> classResults = new HashSet<ClassResult>();
        for (final Class<?> clazz : classTempResults.keySet()) {
            classResults
                    .add(new ClassResult(clazz, classTempResults.get(clazz)));
        }

        return new BenchmarkResult(classResults);
    }
}
