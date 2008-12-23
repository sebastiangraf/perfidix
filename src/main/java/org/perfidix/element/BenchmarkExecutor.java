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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.NotAutomaticallyTicking;
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

    /** Resultset for this method. */
    private final MethodResult methodRes;

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
        methodRes = new MethodResult(element.getMethodToBench());
        metersToBench = new HashSet<AbstractMeter>();
        for (final AbstractMeter meter : paramMeters) {
            if (!(meter instanceof NotAutomaticallyTicking)) {
                metersToBench.add(meter);
            }
        }

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
     * @throws IllegalAccessException
     *             if invocation fails
     */
    public final void executeBeforeMethods(final Object objToExecute)
            throws IllegalAccessException {

        // invoking once the beforeFirstRun-method
        if (!beforeFirstRunExecuted) {
            final Method meth = element.findBeforeFirstRun();
            checkAndExecute(objToExecute, meth);
            beforeFirstRunExecuted = true;
        }

        // invoking the beforeEachRun-method
        final Method meth = element.findBeforeEachRun();
        checkAndExecute(objToExecute, meth);
    }

    /**
     * Execution of bench method. All data is stored corresponding to the
     * meters.
     * 
     * @param objToExecute
     *            the instance of the benchclass where the method should be
     *            executed with.
     * @throws IllegalAccessException
     *             if invocation fails
     */
    public final void executeBench(final Object objToExecute)
            throws IllegalAccessException {

        final double[] meterResults = new double[metersToBench.size()];

        final Method meth = element.getMethodToBench();

        int i = 0;
        for (final AbstractMeter meter : metersToBench) {
            meterResults[i] = meter.getValue();
            i++;
        }

        invokeReflectiveExecutableMethod(objToExecute, meth);

        i = 0;
        for (final AbstractMeter meter : metersToBench) {
            methodRes.addResult(meter, meter.getValue() - meterResults[i]);
            i++;
        }

    }

    /**
     * Executing the {@link AfterLastRun}-annotated methods (if still wasn't)
     * and the {@link AfterEachRun} methods.
     * 
     * @param objToExecute
     *            the object of the class where the bench runs currently in.
     * @throws IllegalAccessException
     *             if invocation fails
     */
    public final void executeAfterMethods(final Object objToExecute)
            throws IllegalAccessException {

        // invoking once the afterLastRun-method
        if (!afterLastRunExecuted) {
            final Method meth = element.findAfterLastRun();
            checkAndExecute(objToExecute, meth);
            afterLastRunExecuted = true;
        }

        // invoking the afterEachRun-method
        final Method meth = element.findAfterEachRun();
        checkAndExecute(objToExecute, meth);
    }

    /**
     * Checking if the method and the object correlate to each other and execute
     * it
     * 
     * @param obj
     *            to be checked with the method
     * @param meth
     *            to be checked with the class
     * @throws IllegalAccessException
     *             if invocation fails
     */
    public static final void checkAndExecute(final Object obj, final Method meth)
            throws IllegalAccessException {
        if (meth != null) {
            final Exception e = checkMethod(obj, meth);
            if (e != null) {
                throw new IllegalAccessException(e.toString());
            } else {
                invokeReflectiveExecutableMethod(obj, meth);
            }
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
    private static final void invokeReflectiveExecutableMethod(
            final Object obj, final Method meth) throws IllegalAccessException {
        final Object[] args = {};

        try {
            meth.invoke(obj, args);
        } catch (IllegalArgumentException e) {
            new IllegalAccessException(e.toString());
        } catch (InvocationTargetException e) {
            new IllegalAccessException(e.toString());
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
