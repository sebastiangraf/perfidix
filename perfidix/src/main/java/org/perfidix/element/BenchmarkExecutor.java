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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;

import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.failureHandling.PerfidixMethodCheckException;
import org.perfidix.failureHandling.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;

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

    /** Set with all meters to be benched automatically */
    private final static LinkedHashSet<AbstractMeter> metersToBench =
            new LinkedHashSet<AbstractMeter>();

    /** Result for all Benchmarks */
    private static BenchmarkResult benchRes;

    /** Boolean to be sure that the beforeFirstRun was not executed yet. */
    private boolean beforeFirstRunExecuted;

    /** Boolean to be sure that the afterLastRun was not executed yet. */
    private boolean afterLastRunExecuted;

    /**
     * Corresponding BenchmarkElement of this executor to get the before/after
     * methods
     */
    private final BenchmarkMethod element;

    /**
     * Private constructor, just setting the booleans and one element to get the
     * before/after methods.
     * 
     * @param paramElement
     *            BenchmarkElement to provide easy access to the before/after
     *            methods.
     */
    private BenchmarkExecutor(final BenchmarkMethod paramElement) {
        beforeFirstRunExecuted = false;
        afterLastRunExecuted = false;
        element = paramElement;
    }

    /**
     * Getting the executor corresponding to a BenchmarkElement.
     * 
     * @param meth
     *            for the executor. If the underlaying {@link Method} was not
     *            registered, a new mapping-entry will be created.
     * @return the BenchmarkExecutor corresponding to the Method of the
     *         BenchmarkElement
     */
    public static final BenchmarkExecutor getExecutor(
            final BenchmarkElement meth) {
        if (benchRes == null) {
            throw new IllegalStateException("Call initialize method first!");
        }

        // check if new instance needs to be created
        if (!executor.containsKey(meth.getMeth())) {
            executor.put(meth.getMeth(), new BenchmarkExecutor(meth.getMeth()));
        }

        // returning the executor
        return executor.get(meth.getMeth());

    }

    /**
     * Initializing the executor.
     * 
     * @param meters
     *            to be benched
     * @param result
     *            to be stored to
     */
    public static final void initialize(
            final LinkedHashSet<AbstractMeter> meters,
            final BenchmarkResult result) {
        metersToBench.clear();
        metersToBench.addAll(meters);
        benchRes = result;
    }

    /**
     * Executing the {@link BeforeFirstRun}-annotated methods (if still wasn't)
     * and the {@link BeforeEachRun} methods.
     * 
     * @param obj
     *            the object of the class where the bench runs currently in.
     */
    public final void executeBeforeMethods(final Object obj) {

        // invoking once the beforeFirstRun-method
        if (!beforeFirstRunExecuted) {
            beforeFirstRunExecuted = true;

            Method beforeFirst = null;
            try {
                beforeFirst = element.findBeforeFirstRun();
            } catch (final PerfidixMethodCheckException e) {
                benchRes.addException(e);
            }
            if (beforeFirst != null) {
                checkAndExectuteBeforeAfters(
                        obj, beforeFirst, BeforeFirstRun.class);
            }
        }

        // invoking the beforeEachRun-method
        Method beforeEach = null;
        try {
            beforeEach = element.findBeforeEachRun();
        } catch (final PerfidixMethodCheckException e) {
            benchRes.addException(e);
        }
        if (beforeEach != null) {
            checkAndExectuteBeforeAfters(obj, beforeEach, BeforeEachRun.class);
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
    public final void executeBench(final Object objToExecute) {

        final double[] meterResults = new double[metersToBench.size()];

        final Method meth = element.getMethodToBench();

        int i = 0;
        for (final AbstractMeter meter : metersToBench) {
            meterResults[i] = meter.getValue();
            i++;
        }

        final PerfidixMethodInvocationException res =
                invokeReflectiveExecutableMethod(
                        objToExecute, meth, Bench.class);

        i = 0;
        for (final AbstractMeter meter : metersToBench) {
            meterResults[i] = meter.getValue() - meterResults[i];
            i++;
        }

        if (res != null) {
            benchRes.addException(res);
        } else {
            i = 0;
            for (final AbstractMeter meter : metersToBench) {
                benchRes.addData(
                        element.getMethodToBench(), meter, meterResults[i]);
                i++;
            }
        }

    }

    /**
     * Executing the {@link AfterLastRun}-annotated methods (if still wasn't)
     * and the {@link AfterEachRun} methods.
     * 
     * @param obj
     *            the object of the class where the bench runs currently in.
     */
    public final void executeAfterMethods(final Object obj) {

        // invoking once the beforeFirstRun-method
        if (!afterLastRunExecuted) {
            afterLastRunExecuted = true;
            Method afterLast = null;
            try {
                afterLast = element.findAfterLastRun();
            } catch (final PerfidixMethodCheckException e) {
                benchRes.addException(e);
            }
            if (afterLast != null) {
                checkAndExectuteBeforeAfters(obj, afterLast, AfterLastRun.class);
            }

        }

        // invoking the beforeEachRun-method
        Method afterEach = null;
        try {
            afterEach = element.findAfterEachRun();
        } catch (final PerfidixMethodCheckException e) {
            benchRes.addException(e);
        }
        if (afterEach != null) {
            checkAndExectuteBeforeAfters(obj, afterEach, AfterEachRun.class);
        }

    }

    /**
     * Checking and executing several before/after methods.
     * 
     * @param obj
     *            on which the execution should take place
     * @param meth
     *            to be executed
     * @param anno
     *            the related annotation
     */
    private final void checkAndExectuteBeforeAfters(
            final Object obj, final Method meth,
            final Class<? extends Annotation> anno) {
        final PerfidixMethodCheckException checkExc =
                checkReflectiveExecutableMethod(obj, meth, anno);
        if (checkExc != null) {
            benchRes.addException(checkExc);
        } else {
            final PerfidixMethodInvocationException invoExc =
                    invokeReflectiveExecutableMethod(obj, meth, anno);
            if (invoExc != null) {
                benchRes.addException(invoExc);
            }
        }
    }

    /**
     * Method to invoke a reflective invokable method.
     * 
     * @param obj
     *            on which the execution takes place
     * @param meth
     *            to be executed
     * @param relatedAnno
     *            related annotation for the execution
     * @return {@link PerfidixMethodInvocationException} if invocation fails,
     *         null otherwise.
     */
    public static final PerfidixMethodInvocationException invokeReflectiveExecutableMethod(
            final Object obj, final Method meth,
            final Class<? extends Annotation> relatedAnno) {
        final Object[] args = {};

        try {
            meth.invoke(obj, args);
        } catch (final IllegalArgumentException e) {
            return new PerfidixMethodInvocationException(e, meth, relatedAnno);
        } catch (final IllegalAccessException e) {
            return new PerfidixMethodInvocationException(e, meth, relatedAnno);
        } catch (final InvocationTargetException e) {
            return new PerfidixMethodInvocationException(
                    e.getCause(), meth, relatedAnno);
        }
        return null;
    }

    /**
     * Checking a method if it is reflective executable and if the mapping to
     * the object fits.
     * 
     * @param obj
     *            on which the execution takes place
     * @param meth
     *            to be checked
     * @param anno
     *            the related annotation for the check
     * @return {@link PerfidixMethodCheckException} if something is wrong in the
     *         mapping, null otherwise.
     */
    public static final PerfidixMethodCheckException checkReflectiveExecutableMethod(
            final Object obj, final Method meth,
            final Class<? extends Annotation> anno) {
        // check if the class of the object to be executed has the given method
        boolean correlationClassMethod = false;
        for (final Method methodOfClass : obj.getClass().getDeclaredMethods()) {
            if (methodOfClass.equals(meth)) {
                correlationClassMethod = true;
            }
        }

        if (!correlationClassMethod) {
            return new PerfidixMethodCheckException(new IllegalStateException(
                    new StringBuilder("Object to execute ")
                            .append(obj).append(
                                    " is not having a Method named ").append(
                                    meth).append(".").toString()), meth, anno);
        }

        // check if the method is reflected executable
        if (!BenchmarkMethod.isReflectedExecutable(meth)) {
            return new PerfidixMethodCheckException(
                    new IllegalAccessException(new StringBuilder(
                            "Method to execute ").append(meth).append(
                            " is not reflected executable.").toString()), meth,
                    anno);
        }
        return null;
    }

}
