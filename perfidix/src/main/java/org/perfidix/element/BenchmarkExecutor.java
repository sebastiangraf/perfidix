/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.element;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.exceptions.PerfidixMethodCheckException;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
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
    private static final Map<BenchmarkMethod, BenchmarkExecutor> EXECUTOR =
            new Hashtable<BenchmarkMethod, BenchmarkExecutor>();

    /** Set with all meters to be benched automatically. */
    private static final Set<AbstractMeter> METERS_TO_BENCH =
            new LinkedHashSet<AbstractMeter>();

    /** Result for all Benchmarks. */
    private static BenchmarkResult benchRes;

    /** Boolean to be sure that the beforeFirstRun was not executed yet. */
    private transient boolean beforeFirstRun;

    /** Boolean to be sure that the afterLastRun was not executed yet. */
    private transient boolean afterLastRun;

    /**
     * Corresponding BenchmarkElement of this executor to get the before/after
     * methods.
     */
    private transient final BenchmarkMethod element;

    /**
     * Private constructor, just setting the booleans and one element to get the
     * before/after methods.
     * 
     * @param paramElement
     *            BenchmarkElement to provide easy access to the before/after
     *            methods.
     */
    private BenchmarkExecutor(final BenchmarkMethod paramElement) {
        beforeFirstRun = false;
        afterLastRun = false;
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
    public static BenchmarkExecutor getExecutor(final BenchmarkElement meth) {
        if (benchRes == null) {
            throw new IllegalStateException("Call initialize method first!");
        }

        // check if new instance needs to be created
        if (!EXECUTOR.containsKey(meth.getMeth())) {
            EXECUTOR.put(meth.getMeth(), new BenchmarkExecutor(meth.getMeth()));
        }

        // returning the executor
        return EXECUTOR.get(meth.getMeth());

    }

    /**
     * Initializing the executor.
     * 
     * @param meters
     *            to be benched
     * @param result
     *            to be stored to
     */
    public static void initialize(
            final Set<AbstractMeter> meters, final BenchmarkResult result) {
        METERS_TO_BENCH.clear();
        METERS_TO_BENCH.addAll(meters);
        EXECUTOR.clear();
        benchRes = result;

    }

    /**
     * Executing the {@link BeforeFirstRun}-annotated methods (if still wasn't)
     * and the {@link BeforeEachRun} methods.
     * 
     * @param obj
     *            the object of the class where the bench runs currently in.
     */
    public void executeBeforeMethods(final Object obj) {

        // invoking once the beforeFirstRun-method
        if (!beforeFirstRun) {
            beforeFirstRun = true;

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
    public void executeBench(final Object objToExecute) {

        final double[] meterResults = new double[METERS_TO_BENCH.size()];

        final Method meth = element.getMethodToBench();

        int meterIndex1 = 0;
        int meterIndex2 = 0;
        for (final AbstractMeter meter : METERS_TO_BENCH) {
            meterResults[meterIndex1] = meter.getValue();
            meterIndex1++;
        }

        final PerfidixMethodInvocationException res =
                invokeMethod(objToExecute, meth, Bench.class);

        for (final AbstractMeter meter : METERS_TO_BENCH) {
            meterResults[meterIndex2] =
                    meter.getValue() - meterResults[meterIndex2];
            meterIndex2++;
        }

        if (res == null) {
            meterIndex1 = 0;
            for (final AbstractMeter meter : METERS_TO_BENCH) {
                benchRes.addData(
                        element.getMethodToBench(), meter,
                        meterResults[meterIndex1]);
                meterIndex1++;
            }
        } else {
            benchRes.addException(res);
        }

    }

    /**
     * Executing the {@link AfterLastRun}-annotated methods (if still wasn't)
     * and the {@link AfterEachRun} methods.
     * 
     * @param obj
     *            the object of the class where the bench runs currently in.
     */
    public void executeAfterMethods(final Object obj) {

        // invoking once the beforeFirstRun-method
        if (!afterLastRun) {
            afterLastRun = true;
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
    private void checkAndExectuteBeforeAfters(
            final Object obj, final Method meth,
            final Class<? extends Annotation> anno) {
        final PerfidixMethodCheckException checkExc =
                checkMethod(obj, meth, anno);
        if (checkExc == null) {
            final PerfidixMethodInvocationException invoExc =
                    invokeMethod(obj, meth, anno);
            if (invoExc != null) {
                benchRes.addException(invoExc);
            }

        } else {
            benchRes.addException(checkExc);
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
    public static PerfidixMethodInvocationException invokeMethod(
            final Object obj, final Method meth,
            final Class<? extends Annotation> relatedAnno) {
        final Object[] args = {};
        PerfidixMethodInvocationException returnVal = null;
        try {
            meth.invoke(obj, args);
        } catch (final IllegalArgumentException e) {
            returnVal =
                    new PerfidixMethodInvocationException(e, meth, relatedAnno);
        } catch (final IllegalAccessException e) {
            returnVal =
                    new PerfidixMethodInvocationException(e, meth, relatedAnno);
        } catch (final InvocationTargetException e) {
            returnVal =
                    new PerfidixMethodInvocationException(
                            e.getCause(), meth, relatedAnno);
        }
        return returnVal;
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
    public static PerfidixMethodCheckException checkMethod(
            final Object obj, final Method meth,
            final Class<? extends Annotation> anno) {
        // check if the class of the object to be executed has the given method
        boolean classMethodCorr = false;
        for (final Method methodOfClass : obj.getClass().getDeclaredMethods()) {
            if (methodOfClass.equals(meth)) {
                classMethodCorr = true;
            }
        }

        PerfidixMethodCheckException returnVal = null;
        if (!classMethodCorr) {
            returnVal =
                    new PerfidixMethodCheckException(
                            new IllegalStateException(new StringBuilder(
                                    "Object to execute ").append(obj).append(
                                    " is not having a Method named ").append(
                                    meth).append(".").toString()), meth, anno);
        }

        // check if the method is reflected executable
        if (!BenchmarkMethod.isReflectedExecutable(meth)) {
            returnVal =
                    new PerfidixMethodCheckException(
                            new IllegalAccessException(new StringBuilder(
                                    "Method to execute ").append(meth).append(
                                    " is not reflected executable.").toString()),
                            meth, anno);
        }
        return returnVal;
    }

}
