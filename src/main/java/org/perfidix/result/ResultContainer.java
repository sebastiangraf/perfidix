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
 * the result container contains more results. it is by definition recursive, so
 * it can handle as much diversity as possible
 * 
 * @author axo axo@axolander.de
 * @since 08-2005
 * @version 0.8
 * @param <ResultType>
 *            the type of the children.
 */
public abstract class ResultContainer<ResultType extends AbstractResult>
        extends AbstractResult {

    private final Set<ResultType> elements;

    /**
     */
    protected ResultContainer(final Set<AbstractMeter> meters) {
        super(meters);
        elements = new HashSet<ResultType>();
    }

    /**
     * appends a result to the children.
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
