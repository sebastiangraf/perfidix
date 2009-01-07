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

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

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

    /** Map of all elements with the Mapping Method/Class -> ResultType. */
    protected final Map<Object, ResultType> elements;

    /**
     * Constructor.
     * 
     * @param paramRelatedElement
     *            related element
     */
    protected ResultContainer(final Object paramRelatedElement) {
        super(paramRelatedElement);
        elements = new Hashtable<Object, ResultType>();
    }

    /**
     * Updates the elements and appending the elements of the underlaying
     * elements.
     * 
     * @param type
     *            the related type.
     * @param meter
     *            the related meter
     * @param data
     *            the corresponding data
     */
    protected final void updateStructure(
            final ResultType type, final AbstractMeter meter,
            final double data) {
        type.addData(meter, data);
        addData(meter, data);

    }

    /**
     * Getting all elements which are included in this result. That means:
     * {@link BenchmarkResult} -> {@link ClassResult}; {@link ClassResult} ->
     * {@link MethodResult};
     * 
     * @return a {@link Collection} of the included results.
     */
    public final Collection<ResultType> getIncludedResults() {
        return elements.values();
    }

    /**
     * Getting the results for one object.
     * 
     * @param obj
     *            the object, can be a Class or a Method
     * @return the result for this object
     */
    public final ResultType getResultForObject(final Object obj) {
        return elements.get(obj);
    }

}
