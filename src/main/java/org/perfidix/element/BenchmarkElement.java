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

import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.BenchClass;
import org.perfidix.annotation.SkipBench;

import com.sun.org.apache.bcel.internal.generic.Type;

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
    public boolean checkThisAsBenchmarkable() {
        // if method is annotated with SkipBench, the method is never
        // benchmarkable.
        final SkipBench skipBenchAnno =
                getMethodToBench().getAnnotation(SkipBench.class);
        if (skipBenchAnno != null) {
            return false;
        }

        // if method is not annotated with Bench and class is not annotated with
        // BenchClass, the method is never benchmarkable.
        final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
        final BenchClass classBenchAnno =
                getMethodToBench().getClass().getAnnotation(BenchClass.class);
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
     * Method to execute a BeforeBenchClass or AfterBenchClass annotation (as
     * set by the parameter). This method should be invoked just once for all
     * methods. The corresponding class is searched after suitable methods and
     * checks for integrity are made. If there are multiple
     * BeforeBenchClass-annotated or AfterBenchClass-annotated method available,
     * an exception is thrown.
     * 
     * @see BeforeBenchClass
     * @see AfterBenchClass
     * @param anno
     *            to be checked, must be of type BeforeBenchClass or
     *            AfterBenchClass
     * @return Annotated method with BeforeBenchClass or AfterBenchClass
     *         annotation, null of none exists
     * @throws IllegalAccessException
     *             if integrity check of class and method fails.
     */
    public final Method findAndCheckBeforeBenchClassOrAfterBenchClass(
            final Class<? extends Annotation> anno)
            throws IllegalAccessException {
        // Check if param is valid
        if (!anno.equals(BeforeBenchClass.class)
                && !anno.equals(AfterBenchClass.class)) {
            throw new IllegalArgumentException(
                    "Anno should be of type BeforeBenchClass or AfterBenchClass");
        }

        // needed variables, one for check for duplicates
        Method beforeOrAfterBenchClassMethod = null;

        // Scanning all methods
        final Method[] possibleBeforeOrAfterClassAnnos =
                getMethodToBench().getClass().getMethods();
        for (final Method meth : possibleBeforeOrAfterClassAnnos) {
            if (meth.getAnnotation(anno) != null) {
                // Check if there are multiple annotated methods, throwing
                // IllegalAccessException otherwise.
                if (beforeOrAfterBenchClassMethod == null) {
                    // Check if method is valid (no param, no returnval,
                    // etc.), throwing IllegalAccessException otherwise.
                    if (isReflectedExecutable(meth)) {
                        beforeOrAfterBenchClassMethod = meth;
                    } else {
                        throw new IllegalAccessException(new StringBuilder(
                                "BeforeBenchClass-annotated method ").append(
                                meth).append(" is not executable.").toString());
                    }
                } else {
                    throw new IllegalAccessException(
                            "Please use only one BeforeBenchClass annotation in one class.");
                }
            }
        }

        if (beforeOrAfterBenchClassMethod != null) {
            return beforeOrAfterBenchClassMethod;
        } else {
            return null;
        }
    }

    /**
     * Simple getter for encapsulated method.
     * 
     * @return the methodToBench
     */
    public Method getMethodToBench() {
        return methodToBench;
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

        // if method has another returnValue than void, the method is never
        // benchmarkable
        if (!meth.getGenericReturnType().equals(Type.VOID)) {
            return false;
        }

        return true;
    }

}
