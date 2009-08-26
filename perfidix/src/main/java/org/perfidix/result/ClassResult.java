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
public final class ClassResult extends AbstractResultContainer<MethodResult> {

    /**
     * Constructor.
     * 
     * @param paramClass
     *            class of these results
     */
    public ClassResult(final Class<?> paramClass) {
        super(paramClass);
    }

    /** {@inheritDoc} */
    @Override
    public String getElementName() {
        return ((Class<?>) getRelatedElement()).getSimpleName();
    }
}
