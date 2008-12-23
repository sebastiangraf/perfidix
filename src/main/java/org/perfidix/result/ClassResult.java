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

import java.util.Set;

/**
 * This class holds all results related to a benchmarked class. That means that,
 * given a Set with {@link MethodResult} objects, these objects are stored in
 * this class plus the data is extracted from these {@link MethodResult} objects
 * and stored in this class. So, every statistical analysis is made on the base
 * of the overall benchmarked data.
 * 
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 */
public final class ClassResult extends ResultContainer<MethodResult> {

    /** Reference to the class related to these results. */
    private final Class<?> clazz;

    /**
     * Constructor-
     * 
     * @param paramClass
     *            class of these results
     * @param paramMethodResults
     *            the {@link MethodResult} which build up this result
     */
    public ClassResult(
            final Class<?> paramClass,
            final Set<MethodResult> paramMethodResults) {
        super();
        clazz = paramClass;
        if (!checkMethodResultCorrelation(paramMethodResults)) {
            throw new IllegalArgumentException(
                    new StringBuilder("Correlation ")
                            .append(paramMethodResults).append(
                                    " doesn't correspond to class ").append(
                                    clazz).toString());
        }
        setUpContainer(paramMethodResults);
    }

    /**
     * Getting the class related to these results.
     * 
     * @return the clazz related to these results
     */
    public final Class<?> getClazz() {
        return clazz;
    }

    /**
     * Method to check if the methodresults correspond to the class registered
     * with this result.
     * 
     * @param methResults
     *            results to check
     * @return true if matched, false otherwise
     */
    private final boolean checkMethodResultCorrelation(
            final Set<MethodResult> methResults) {
        for (final MethodResult res : methResults) {
            if (res.getMeth().getDeclaringClass() != clazz) {
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final String getElementName() {
        return clazz.getSimpleName();
    }
}