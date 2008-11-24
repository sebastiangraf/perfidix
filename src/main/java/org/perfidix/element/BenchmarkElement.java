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
 * returning possible <code>BeforeBenchClass</code>, <code>BeforeFirstRun</code>
 * , <code>BeforeEachRun</code>, <code>AfterEachRun</code>,
 * <code>AfterLastRun</code> and <code>AfterBenchClass</code> annotated related
 * methods. Implementing classes have to implement the Comparable-interface for
 * a designated order to execute.
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

    /**
     * Method to be benched.
     */
    private final Method methodToBench;

    /**
     * Constructor, with a possible method to bench.
     * 
     * @param paramMethodToBench
     *            method to be benched (eventually)
     */
    public BenchmarkElement(final Method paramMethodToBench) {
        methodToBench = paramMethodToBench;
    }

    /**
     * This method should act as a check to guarantee that only specific
     * Benchmarkables are used for benching.
     * 
     * @return true if an instance of this interface is benchmarkable, false
     *         otherwise.
     */
    public boolean checkThisMethodAsBenchmarkable() {

        // Check if bench-anno is given. For testing purposes against
        // before/after annos
        final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);

        // if method is annotated with SkipBench, the method is never
        // benchmarkable.
        final SkipBench skipBenchAnno =
                getMethodToBench().getAnnotation(SkipBench.class);
        if (skipBenchAnno != null) {
            return false;
        }

        // Check if method is defined as beforeClass, beforeFirstRun,
        // beforeEachRun, afterEachRun, afterLastRun, afterClass.
        final BeforeBenchClass beforeClass =
                getMethodToBench().getAnnotation(BeforeBenchClass.class);
        if (beforeClass != null && benchAnno == null) {
            return false;
        }

        final BeforeFirstRun beforeFirstRun =
                getMethodToBench().getAnnotation(BeforeFirstRun.class);
        if (beforeFirstRun != null && benchAnno == null) {
            return false;
        }

        final BeforeEachRun beforeEachRun =
                getMethodToBench().getAnnotation(BeforeEachRun.class);
        if (beforeEachRun != null && benchAnno == null) {
            return false;
        }

        final AfterEachRun afterEachRun =
                getMethodToBench().getAnnotation(AfterEachRun.class);
        if (afterEachRun != null && benchAnno == null) {
            return false;
        }

        final AfterLastRun afterLastRun =
                getMethodToBench().getAnnotation(AfterLastRun.class);
        if (afterLastRun != null && benchAnno == null) {
            return false;
        }

        final AfterBenchClass afterClass =
                getMethodToBench().getAnnotation(AfterBenchClass.class);
        if (afterClass != null && benchAnno == null) {
            return false;
        }

        // if method is not annotated with Bench and class is not annotated with
        // BenchClass, the method is never benchmarkable.

        final BenchClass classBenchAnno =
                getMethodToBench().getDeclaringClass().getAnnotation(
                        BenchClass.class);
        if (benchAnno == null && classBenchAnno == null) {
            return false;
        }

        // check if method is executable for perfidix purposes.
        if (!isReflectedExecutable(getMethodToBench())) {
            return false;
        }

        return true;
    }

    /**
     * Method to find a <code>BeforeBenchClass</code> annotation (as set by the
     * parameter). This method should be invoked just once for all methods. The
     * corresponding class is searched after suitable methods and checks for
     * integrity are made. If there are multiple <code>BeforeBenchClass</code>
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
     * Method to find a <code>BeforeFirstRun</code> annotation. This method
     * should be invoked for all methods. The corresponding class is searched
     * after suitable methods and checks for integrity are made. If there are
     * multiple <code>BeforeFirstRun</code>-annotated methods available, an
     * exception is thrown. If there are designated special
     * <code>BeforeFirstRun</code> methods as given in the parameter of the
     * <code>Bench</code>-annotation, this method is taken with any further
     * checking of the other methods in the class.
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
     * Method to find a <code>BeforeEachRun</code> annotation. This method
     * should be invoked for all methods. The corresponding class is searched
     * after suitable methods and checks for integrity are made. If there are
     * multiple <code>BeforeEachRun</code>-annotated methods available, an
     * exception is thrown. If there are designated special
     * <code>BeforeEachRun</code> methods as given in the parameter of the
     * <code>Bench</code>-annotation, this method is taken with any further
     * checking of the other methods in the class.
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
     * Method to find a <code>AfterEachRun</code> annotation. This method should
     * be invoked for all methods. The corresponding class is searched after
     * suitable methods and checks for integrity are made. If there are multiple
     * <code>AfterEachRun</code>-annotated methods available, an exception is
     * thrown. If there are designated special <code>AfterEachRun</code> methods
     * as given in the parameter of the <code>Bench</code>-annotation, this
     * method is taken with any further checking of the other methods in the
     * class.
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
     * Method to find a <code>AfterLastRun</code> annotation. This method should
     * be invoked for all methods. The corresponding class is searched after
     * suitable methods and checks for integrity are made. If there are multiple
     * <code>AfterLastRun</code>-annotated methods available, an exception is
     * thrown. If there are designated special <code>AfterLastRun</code> methods
     * as given in the parameter of the <code>Bench</code>-annotation, this
     * method is taken with any further checking of the other methods in the
     * class.
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
     * Method to execute a AfterBenchClass annotation (as set by the parameter).
     * This method should be invoked just once for all methods. The
     * corresponding class is searched after suitable methods and checks for
     * integrity are made. If there are multiple AfterBenchClass-annotated
     * methods available, an exception is thrown.
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
     * Getting the number of runs corresponding to a given method. The method
     * MUST be a benchmarkable method, otherwise an IllegalStateException
     * exception arises. The number of runs of an annotated method is more
     * powerful than the number of runs as denoted by the benchclass annotation.
     * 
     * @return the number of runs of this benchmarkable-method
     * @throws IllegalStateException
     *             if the given method is not benchmarkable.
     */
    public final int getNumberOfRuns() throws IllegalStateException {
        if (checkThisMethodAsBenchmarkable()) {
            final Bench benchAnno =
                    getMethodToBench().getAnnotation(Bench.class);
            final BenchClass benchClassAnno =
                    getMethodToBench().getDeclaringClass().getAnnotation(
                            BenchClass.class);
            if (benchAnno != null) {
                return benchAnno.runs();
            } else {
                return benchClassAnno.runs();
            }
        } else {
            throw new IllegalStateException(new StringBuilder("Method ")
                    .append(this.methodToBench.toString()).append(
                            " is actually not benchmarkable!").toString());
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
    public static final Method findAndCheckAnyMethodByAnnotation(
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
     * Checks if this method is executable via reflection for perfidix purposes.
     * That means that the method has no parameters, no return-value, is
     * non-static, is public and throws no exceptions.
     * 
     * @param meth
     *            method to be checked
     * @return true if method matches requirements.
     */
    public static boolean isReflectedExecutable(final Method meth) {
        // if method has parameters, the method is never benchmarkable
        if (meth.getGenericParameterTypes().length > 0) {
            return false;
        }
        // if method is throwing exceptions, the method is never benchmarkable
        if (meth.getGenericExceptionTypes().length > 0) {
            return false;
        }
        // if method is static, the method is never benchmarkable
        if (Modifier.isStatic(meth.getModifiers())) {
            return false;
        }
        // if method is not public, the method is never benchmarkable
        if (!Modifier.isPublic(meth.getModifiers())) {
            return false;
        }
        // if method has another returnValue than void, the method is never
        // benchmarkable
        if (!meth.getGenericReturnType().equals(Void.TYPE)) {
            return false;
        }

        return true;
    }

}
