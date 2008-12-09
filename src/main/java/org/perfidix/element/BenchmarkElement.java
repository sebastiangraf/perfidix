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
import java.lang.reflect.Modifier;
import java.util.Hashtable;

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.BenchClass;
import org.perfidix.annotation.SkipBench;

/**
 * Class to mark all elements which are possible benchmarkable. The method hold
 * helping methods and additional functionality for benchmarkable methods like
 * returning possible {@link BeforeBenchClass}, {@link BeforeFirstRun},
 * {@link BeforeEachRun}, {@link AfterEachRun}, {@link AfterLastRun} and
 * {@link AfterBenchClass} annotated related methods.
 * 
 * @see AfterBenchClass
 * @see AfterLastRun
 * @see AfterEachRun
 * @see BeforeEachRun
 * @see BeforeFirstRun
 * @see BeforeBenchClass
 * @author Sebastian Graf, University of Konstanz
 */
public final class BenchmarkElement {

    private final static Hashtable<Method, Integer> usedIDs =
            new Hashtable<Method, Integer>(0);

    /**
     * Method to be benched.
     */
    private final Method methodToBench;

    /**
     * ID of the run of this method
     */
    private int methodRunID;

    /**
     * Constructor, with a definite method to bench. The method has to be
     * checked with {@link BenchmarkElement#isBenchmarkable(Method)} first,
     * otherwise an IllegalArgumentException could arise.
     * 
     * @param paramMethodToBench
     *            method to be benched (eventually)
     * @throws IllegalArgumentException
     *             if the method is not benchmarkable.
     */
    public BenchmarkElement(final Method paramMethodToBench) {
        methodToBench = paramMethodToBench;
        if (!isBenchmarkable(methodToBench)) {
            throw new IllegalArgumentException(new StringBuilder(
                    "Only benchmarkable methods allowed but method ")
                    .append(paramMethodToBench)
                    .append(" is not benchmarkable.").toString());
        }

        if (!usedIDs.containsKey(methodToBench)) {
            usedIDs.put(methodToBench, 0);
        }
        methodRunID = usedIDs.get(methodToBench);
        usedIDs.put(methodToBench, methodRunID + 1);
    }

    /**
     * Method to find a {@link BeforeBenchClass} annotation (as set by the
     * parameter). This method should be invoked just once for all methods. The
     * corresponding class is searched after suitable methods and checks for
     * integrity are made. If there are multiple {@link BeforeBenchClass}
     * -annotated methods available, an exception is thrown.
     * 
     * @see BeforeBenchClass
     * @return Annotated method with BeforeBenchClass annotation, null of none
     *         exists
     * @throws IllegalAccessException
     *             if integrity check of class and method fails.
     */
    public final Method findBeforeBenchClass() throws IllegalAccessException {

        // Scanning the class file for the BeforeBenchClass-annotation
        final Method method =
                findAndCheckAnyMethodByAnnotation(getMethodToBench()
                        .getDeclaringClass(), BeforeBenchClass.class);

        if (method != null) {
            return method;
        } else {
            return null;
        }
    }

    /**
     * Method to find a {@link BeforeFirstRun} annotation. This method should be
     * invoked for all methods. The corresponding class is searched after
     * suitable methods and checks for integrity are made. If there are multiple
     * {@link BeforeFirstRun}-annotated methods available, an exception is
     * thrown. If there are designated special {@link BeforeFirstRun} methods as
     * given in the parameter of the {@link Bench}-annotation, this method is
     * taken with any further checking of the other methods in the class.
     * 
     * @see BeforeFirstRun
     * @see Bench
     * @return Annotated method with BeforeFirstRun annotation, null of none
     *         exists
     * @throws IllegalAccessException
     *             if integrity check of class and method fails.
     */
    public final Method findBeforeFirstRun() throws IllegalAccessException {

        Method method = null;

        final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
        if (benchAnno != null && !benchAnno.beforeFirstRun().equals("")) {
            try {
                // variable to instantiate the method by name.
                final Class<?>[] setUpParams = {};

                // getting the method by name
                method =
                        getMethodToBench()
                                .getDeclaringClass()
                                .getDeclaredMethod(
                                        benchAnno.beforeFirstRun(), setUpParams);
            } catch (SecurityException e) {
                throw new IllegalAccessException(new StringBuilder(benchAnno
                        .beforeFirstRun())
                        .append(" rises SecurityException: ").append(
                                e.toString()).toString());
            } catch (NoSuchMethodException e) {
                throw new IllegalAccessException(new StringBuilder(benchAnno
                        .beforeFirstRun())
                        .append(" rises NoSuchMethodException: ").append(
                                e.toString()).toString());
            }
        }

        // if there was no name, a scan over the class occurs, otherwise the
        // designated method is checked.
        if (method != null) {
            if (isReflectedExecutable(method)) {
                return method;
            } else {
                throw new IllegalAccessException(new StringBuilder(
                        "BeforeFirstRun-annotated method ")
                        .append(method).append(" is not executable.")
                        .toString());
            }
        } else {
            return findAndCheckAnyMethodByAnnotation(getMethodToBench()
                    .getDeclaringClass(), BeforeFirstRun.class);
        }

    }

    /**
     * Method to find a {@link BeforeEachRun} annotation. This method should be
     * invoked for all methods. The corresponding class is searched after
     * suitable methods and checks for integrity are made. If there are multiple
     * {@link BeforeEachRun}-annotated methods available, an exception is
     * thrown. If there are designated special {@link BeforeEachRun} methods as
     * given in the parameter of the {@link Bench}-annotation, this method is
     * taken with any further checking of the other methods in the class.
     * 
     * @see BeforeEachRun
     * @see Bench
     * @return Annotated method with BeforeEachRun annotation, null of none
     *         exists
     * @throws IllegalAccessException
     *             if integrity check of class and method fails.
     */
    public final Method findBeforeEachRun() throws IllegalAccessException {

        Method method = null;

        final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
        if (benchAnno != null && !benchAnno.beforeEachRun().equals("")) {
            try {
                // variable to instantiate the method by name.
                final Class<?>[] setUpParams = {};

                // getting the method by name
                method =
                        getMethodToBench()
                                .getDeclaringClass().getDeclaredMethod(
                                        benchAnno.beforeEachRun(), setUpParams);
            } catch (SecurityException e) {
                throw new IllegalAccessException(new StringBuilder(benchAnno
                        .beforeEachRun())
                        .append(" rises SecurityException: ").append(
                                e.toString()).toString());
            } catch (NoSuchMethodException e) {
                throw new IllegalAccessException(new StringBuilder(benchAnno
                        .beforeEachRun())
                        .append(" rises NoSuchMethodException: ").append(
                                e.toString()).toString());
            }
        }

        // if there was no name, a scan over the class occurs, otherwise the
        // designated method is checked.
        if (method != null) {
            if (isReflectedExecutable(method)) {
                return method;
            } else {
                throw new IllegalAccessException(new StringBuilder(
                        "BeforeEachRun-annotated method ")
                        .append(method).append(" is not executable.")
                        .toString());
            }
        } else {
            return findAndCheckAnyMethodByAnnotation(getMethodToBench()
                    .getDeclaringClass(), BeforeEachRun.class);
        }

    }

    /**
     * Method to find a {@link AfterEachRun} annotation. This method should be
     * invoked for all methods. The corresponding class is searched after
     * suitable methods and checks for integrity are made. If there are multiple
     * {@link AfterEachRun}-annotated methods available, an exception is thrown.
     * If there are designated special {@link AfterEachRun} methods as given in
     * the parameter of the {@link Bench}-annotation, this method is taken with
     * any further checking of the other methods in the class.
     * 
     * @see AfterEachRun
     * @see Bench
     * @return Annotated method with AfterEachRun annotation, null of none
     *         exists
     * @throws IllegalAccessException
     *             if integrity check of class and method fails.
     */
    public final Method findAfterEachRun() throws IllegalAccessException {

        Method method = null;

        final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
        if (benchAnno != null && !benchAnno.afterEachRun().equals("")) {
            try {
                // variable to instantiate the method by name.
                final Class<?>[] setUpParams = {};

                // getting the method by name
                method =
                        getMethodToBench()
                                .getDeclaringClass().getDeclaredMethod(
                                        benchAnno.afterEachRun(), setUpParams);
            } catch (SecurityException e) {
                throw new IllegalAccessException(new StringBuilder(benchAnno
                        .afterEachRun())
                        .append(" rises SecurityException: ").append(
                                e.toString()).toString());
            } catch (NoSuchMethodException e) {
                throw new IllegalAccessException(new StringBuilder(benchAnno
                        .afterEachRun())
                        .append(" rises NoSuchMethodException: ").append(
                                e.toString()).toString());
            }
        }

        // if there was no name, a scan over the class occurs, otherwise the
        // designated method is checked.
        if (method != null) {
            if (isReflectedExecutable(method)) {
                return method;
            } else {
                throw new IllegalAccessException(new StringBuilder(
                        "AfterEachRun-annotated method ")
                        .append(method).append(" is not executable.")
                        .toString());
            }
        } else {
            return findAndCheckAnyMethodByAnnotation(getMethodToBench()
                    .getDeclaringClass(), AfterEachRun.class);
        }
    }

    /**
     * Method to find a {@link AfterLastRun} annotation. This method should be
     * invoked for all methods. The corresponding class is searched after
     * suitable methods and checks for integrity are made. If there are multiple
     * {@link AfterLastRun}-annotated methods available, an exception is thrown.
     * If there are designated special {@link AfterLastRun} methods as given in
     * the parameter of the {@link Bench}-annotation, this method is taken with
     * any further checking of the other methods in the class.
     * 
     * @see AfterLastRun
     * @see Bench
     * @return Annotated method with AfterLastRun annotation, null of none
     *         exists
     * @throws IllegalAccessException
     *             if integrity check of class and method fails.
     */
    public final Method findAfterLastRun() throws IllegalAccessException {

        Method method = null;

        final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
        if (benchAnno != null && !benchAnno.afterLastRun().equals("")) {
            try {
                // variable to instantiate the method by name.
                final Class<?>[] setUpParams = {};

                // getting the method by name
                method =
                        getMethodToBench()
                                .getDeclaringClass().getDeclaredMethod(
                                        benchAnno.afterLastRun(), setUpParams);
            } catch (SecurityException e) {
                throw new IllegalAccessException(new StringBuilder(benchAnno
                        .afterLastRun())
                        .append(" rises SecurityException: ").append(
                                e.toString()).toString());
            } catch (NoSuchMethodException e) {
                throw new IllegalAccessException(new StringBuilder(benchAnno
                        .afterLastRun())
                        .append(" rises NoSuchMethodException: ").append(
                                e.toString()).toString());
            }
        }

        // if there was no name, a scan over the class occurs, otherwise the
        // designated method is checked.
        if (method != null) {
            if (isReflectedExecutable(method)) {
                return method;
            } else {
                throw new IllegalAccessException(new StringBuilder(
                        "AfterLastRun-annotated method ")
                        .append(method).append(" is not executable.")
                        .toString());
            }
        } else {
            return findAndCheckAnyMethodByAnnotation(getMethodToBench()
                    .getDeclaringClass(), AfterLastRun.class);
        }
    }

    /**
     * Method to execute a {@link AfterBenchClass} annotation (as set by the
     * parameter). This method should be invoked just once for all methods. The
     * corresponding class is searched after suitable methods and checks for
     * integrity are made. If there are multiple {@link AfterBenchClass}
     * -annotated methods available, an exception is thrown.
     * 
     * @see AfterBenchClass
     * @return Annotated method with AfterBenchClass annotation, null of none
     *         exists
     * @throws IllegalAccessException
     *             if integrity check of class and method fails.
     */
    public final Method findAfterBenchClass() throws IllegalAccessException {

        // Scanning the class file for the AfterBenchClass-annotation
        final Method method =
                findAndCheckAnyMethodByAnnotation(getMethodToBench()
                        .getDeclaringClass(), AfterBenchClass.class);

        if (method != null) {
            return method;
        } else {
            return null;
        }
    }

    /**
     * Simple getter for encapsulated method.
     * 
     * @return the methodToBench
     */
    public final Method getMethodToBench() {
        return methodToBench;
    }

    /**
     *Simple getter for encapsulated method.
     * 
     * @return the methodRunID
     */
    public final int getMethodRunID() {
        return methodRunID;
    }

    /**
     * Getting the number of runs corresponding to a given method. The method
     * MUST be a benchmarkable method, otherwise an IllegalStateException
     * exception arises. The number of runs of an annotated method is more
     * powerful than the number of runs as denoted by the benchclass annotation.
     * 
     * @param meth
     *            to be checked
     * @return the number of runs of this benchmarkable-method
     * @throws IllegalStateException
     *             if the given method is not benchmarkable.
     */
    public final static int getNumberOfAnnotatedRuns(final Method meth)
            throws IllegalStateException {
        if (!isBenchmarkable(meth)) {
            throw new IllegalArgumentException(new StringBuilder("Method ")
                    .append(meth).append(" must be a benchmarkable method.")
                    .toString());
        }
        final Bench benchAnno = meth.getAnnotation(Bench.class);
        final BenchClass benchClassAnno =
                meth.getDeclaringClass().getAnnotation(BenchClass.class);
        if (benchAnno != null) {
            return benchAnno.runs();
        } else {
            return benchClassAnno.runs();
        }
    }

    /**
     * This class finds any method with a given annotation. The method is
     * allowed to occure only once in the class and should match the
     * requirements for Perfidix for an execution by reflection.
     * 
     * @param anno
     *            of the method to be found
     * @param clazz
     *            class to be searched
     * @return a method annotated by the annotation given. The method occurs
     *         only once in the class and matched the requirements of
     *         perfidix-reflective-invocation.
     * @throws IllegalAccessException
     *             if these integrity checks fail
     */
    public final static Method findAndCheckAnyMethodByAnnotation(
            final Class<?> clazz, final Class<? extends Annotation> anno)
            throws IllegalAccessException {
        // needed variables, one for check for duplicates
        Method anyAnnotatedMethod = null;

        // Scanning all methods
        final Method[] possibleAnnotatedMethods = clazz.getDeclaredMethods();
        for (final Method meth : possibleAnnotatedMethods) {
            if (meth.getAnnotation(anno) != null) {
                // Check if there are multiple annotated methods, throwing
                // IllegalAccessException otherwise.
                if (anyAnnotatedMethod == null) {
                    // Check if method is valid (no param, no returnval,
                    // etc.), throwing IllegalAccessException otherwise.
                    if (isReflectedExecutable(meth)) {
                        anyAnnotatedMethod = meth;
                    } else {
                        throw new IllegalAccessException(new StringBuilder(anno
                                .toString())
                                .append("-annotated method ").append(meth)
                                .append(" is not executable.").toString());
                    }
                } else {
                    throw new IllegalAccessException(new StringBuilder(
                            "Please use only one ")
                            .append(anno.toString()).append(
                                    "-annotation in one class.").toString());
                }
            }
        }

        if (anyAnnotatedMethod != null) {
            return anyAnnotatedMethod;
        } else {
            return null;
        }
    }

    /**
     * This method should act as a check to guarantee that only specific
     * Benchmarkables are used for benching.
     * 
     * @param meth
     *            method to be checked.
     * @return true if an instance of this interface is benchmarkable, false
     *         otherwise.
     */
    public final static boolean isBenchmarkable(final Method meth) {
        boolean returnVal = true;

        // Check if bench-anno is given. For testing purposes against
        // before/after annos
        final Bench benchAnno = meth.getAnnotation(Bench.class);

        // if method is annotated with SkipBench, the method is never
        // benchmarkable.
        final SkipBench skipBenchAnno = meth.getAnnotation(SkipBench.class);
        if (skipBenchAnno != null) {
            returnVal = false;
        }

        // Check if method is defined as beforeClass, beforeFirstRun,
        // beforeEachRun, afterEachRun, afterLastRun, afterClass. A method can
        // either be a before/after class or afterwards be benchmarkable through
        // the BenchClass annotation.
        final BeforeBenchClass beforeClass =
                meth.getAnnotation(BeforeBenchClass.class);
        if (beforeClass != null && benchAnno == null) {
            returnVal = false;
        }

        final BeforeFirstRun beforeFirstRun =
                meth.getAnnotation(BeforeFirstRun.class);
        if (beforeFirstRun != null && benchAnno == null) {
            returnVal = false;
        }

        final BeforeEachRun beforeEachRun =
                meth.getAnnotation(BeforeEachRun.class);
        if (beforeEachRun != null && benchAnno == null) {
            returnVal = false;
        }

        final AfterEachRun afterEachRun =
                meth.getAnnotation(AfterEachRun.class);
        if (afterEachRun != null && benchAnno == null) {
            returnVal = false;
        }

        final AfterLastRun afterLastRun =
                meth.getAnnotation(AfterLastRun.class);
        if (afterLastRun != null && benchAnno == null) {
            returnVal = false;
        }

        final AfterBenchClass afterClass =
                meth.getAnnotation(AfterBenchClass.class);
        if (afterClass != null && benchAnno == null) {
            returnVal = false;
        }

        // if method is not annotated with Bench and class is not annotated with
        // BenchClass, the method is never benchmarkable.

        final BenchClass classBenchAnno =
                meth.getDeclaringClass().getAnnotation(BenchClass.class);
        if (benchAnno == null && classBenchAnno == null) {
            returnVal = false;
        }

        // check if method is executable for perfidix purposes.
        if (!isReflectedExecutable(meth)) {
            returnVal = false;
        }
        return returnVal;
    }

    /**
     * Checks if this method is executable via reflection for perfidix purposes.
     * That means that the method has no parameters, no return-value, is
     * non-static, is public and throws no exceptions.
     * 
     * @param meth
     *            method to be checked
     * @return true if method matches requirements.
     */
    public final static boolean isReflectedExecutable(final Method meth) {
        // if method has parameters, the method is not benchmarkable
        if (meth.getGenericParameterTypes().length > 0) {
            return false;
        }
        // if method is throwing exceptions, the method is not benchmarkable
        if (meth.getGenericExceptionTypes().length > 0) {
            return false;
        }
        // if method is static, the method is not benchmarkable
        if (Modifier.isStatic(meth.getModifiers())) {
            return false;
        }
        // if method is not public, the method is not benchmarkable
        if (!Modifier.isPublic(meth.getModifiers())) {
            return false;
        }
        // if method has another returnValue than void, the method is not
        // benchmarkable
        if (!meth.getGenericReturnType().equals(Void.TYPE)) {
            return false;
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + methodRunID;
        result =
                prime
                        * result
                        + ((methodToBench == null) ? 0 : methodToBench
                                .hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BenchmarkElement other = (BenchmarkElement) obj;
        if (methodRunID != other.methodRunID)
            return false;
        if (methodToBench == null) {
            if (other.methodToBench != null)
                return false;
        } else if (!methodToBench.equals(other.methodToBench))
            return false;
        return true;
    }

}
