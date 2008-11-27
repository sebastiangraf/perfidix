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
package org.perfidix.result;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import org.perfidix.meter.AbstractMeter;

/**
 * Storing the results of each run to a given method and to a given meter. This
 * class wraps a datastructure consisting our of two related Hashtables plus one
 * List for the results itsel. This class is designed as a singleton, because
 * all relevant results of all benchs should live the whole lifetime of this
 * process plus there should be only one location to handle the results.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class Result {

    /** singleton instance */
    private static Result instance;

    /** Main datastructure to hold all the relevant data */
    private final Hashtable<Method, Hashtable<AbstractMeter, Collection<Long>>> results;

    /**
     * Gets the singleton.
     * 
     * @return the singleton instance.
     */
    public final static Result getInstance() {
        if (Result.instance == null) {
            instance = new Result();
        }
        return instance;
    }

    /**
     * Constructor.
     */
    private Result() {
        results =
                new Hashtable<Method, Hashtable<AbstractMeter, Collection<Long>>>();
    }

    /**
     * Adding a result result to this container-class. The place where the
     * result is added is determined by the method and the meter.
     * 
     * @param meth
     *            where the result should be stored to
     * @param meter
     *            where the result should be stored to
     * @param data
     *            to be stored
     */
    public final void addResult(
            final Method meth, final AbstractMeter meter, final long data) {

        // getting the meter-hashtable or create it of necessary
        Hashtable<AbstractMeter, Collection<Long>> resultForMethod;
        if (results.containsKey(meth)) {
            resultForMethod = results.get(meth);
        } else {
            resultForMethod = new Hashtable<AbstractMeter, Collection<Long>>();
        }

        // getting the result set
        Collection<Long> resultForMethodAndMeter;
        if (resultForMethod.containsKey(meter)) {
            resultForMethodAndMeter = resultForMethod.get(meter);
        } else {
            resultForMethodAndMeter = new LinkedList<Long>();
        }

        // adding result
        resultForMethodAndMeter.add(data);

        // combining the structures
        resultForMethod.put(meter, resultForMethodAndMeter);
        results.put(meth, resultForMethod);
    }

    /**
     * Getting the result of this container of a specific method.
     * 
     * @param meth
     *            of the result
     * @param meter
     *            of the result
     * @return a collection of all data of the method and the meter or
     *         IllegalArgumentException if not existing.
     */
    public final Collection<Long> getResult(
            final Method meth, final AbstractMeter meter) {

        // check if the results are existing related to the given
        // method
        if (!this.results.containsKey(meth)) {
            throw new IllegalArgumentException(new StringBuilder("Method ")
                    .append(meth).append(" is not existing in results")
                    .toString());
        }

        // getting the data related to the given method
        final Hashtable<AbstractMeter, Collection<Long>> methodResult =
                this.results.get(meth);

        // check if the methodresults are existing related to the given meter
        if (!methodResult.containsKey(meter)) {
            throw new IllegalArgumentException(new StringBuilder("Meter ")
                    .append(meter).append(
                            " is not existing in results with method ").append(
                            meth).toString());
        }

        // getting the data related to the given method and the given meter
        return methodResult.get(meter);
    }

    /**
     * Getting all available meters related to a method
     * 
     * @param meth
     *            the method to be evaluated
     * @return all AbstractMeters or IllegalArgumentException if not existing.
     */
    public final Set<AbstractMeter> getAvailableMeters(final Method meth) {
        // check if the results are existing related to the given
        // method
        if (!this.results.containsKey(meth)) {
            throw new IllegalArgumentException(new StringBuilder("Method ")
                    .append(meth).append(" is not existing in results")
                    .toString());
        }

        return this.results.get(meth).keySet();
    }

    /**
     * Getting all available methods.
     * 
     * @return all methods
     */
    public final Set<Method> getAvailableMethods() {
        return this.results.keySet();
    }

}
