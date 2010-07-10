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

/**
 * Class to hold the result related to one method. That means that all
 * information is inherited from the {@link ClassResult} plus the possibility to
 * add additional datasets.
 * 
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 */
public final class MethodResult extends AbstractResult {

    /**
     * Simple Constructor.
     * 
     * @param paramMethod
     *            , the method related to these results
     */
    public MethodResult(final Method paramMethod) {
        super(paramMethod);
    }

    /** {@inheritDoc} */
    @Override
    public String getElementName() {
        return ((Method) getRelatedElement()).getName();
    }

}
