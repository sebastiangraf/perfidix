/*
 * Copyright 2007 University of Konstanz
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
 * $Id: ResultContainer.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.result;

import java.util.HashSet;
import java.util.Set;

import org.perfidix.meter.AbstractMeter;

/**
 * The result container contains more results. It is by definition recursive, so
 * it can handle as much diversity as possible
 * 
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 * @param <ResultType>
 *            the type of the children.
 */
public abstract class ResultContainer<ResultType extends AbstractResult>
        extends AbstractResult {

    /** Set of all elements */
    private final Set<ResultType> elements;

    /**
     * Constructor.
     * 
     * @param meters
     *            the meters of this result
     */
    protected ResultContainer(final Set<AbstractMeter> meters) {
        super(meters);
        elements = new HashSet<ResultType>();
    }

    /**
     * Clears the elements and appending the elements of the underlaying
     * elements.
     * 
     * @param res
     *            the result to append.
     */
    public final void setUpContainer(final Set<ResultType> res) {
        elements.clear();
        for (final AbstractMeter meter : getRegisteredMeters()) {
            for (final ResultType singleType : res) {
                getResultSet(meter).addAll(singleType.getResultSet(meter));
            }
        }
    }

}
