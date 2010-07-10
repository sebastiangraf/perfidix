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

import java.util.Hashtable;
import java.util.Map;

/**
 * This class acts as a container related to one benchmarkable method. This
 * class has an unique identifier for one execution.
 * 
 * @see BenchmarkMethod
 * @author Sebastian Graf, University of Konstanz
 */
public final class BenchmarkElement {

    /** The BenchmarkMethod related to this element. */
    private transient final BenchmarkMethod meth;

    /** The unique elementId for this elements. */
    private transient final int elementId;

    /**
     * Static Mapping for BenchmarkMethod->Integer. Every BenchmarkMethod gains
     * one unique elementId from this mapping.
     */
    private static final Map<BenchmarkMethod, Integer> ID_MAPPING =
            new Hashtable<BenchmarkMethod, Integer>();

    /**
     * Constructor, simple taking the corresponding {@link BenchmarkMethod}.
     * 
     * @param paramMeth
     *            the related {@link BenchmarkMethod}
     */
    public BenchmarkElement(final BenchmarkMethod paramMeth) {
        meth = paramMeth;
        if (!ID_MAPPING.containsKey(paramMeth)) {
            ID_MAPPING.put(getMeth(), 0);
        }
        elementId = ID_MAPPING.get(getMeth()) + 1;
        ID_MAPPING.put(getMeth(), getId());

    }

    /**
     * Getter for the elementId.
     * 
     * @return the elementId of this element
     */
    public int getId() {
        return elementId;
    }

    /**
     * Getting the {@link BenchmarkMethod} related to this element.
     * 
     * @return the meth
     */
    public BenchmarkMethod getMeth() {
        return meth;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + elementId;
        if (meth == null) {
            result = prime * result;
        } else {
            result = prime * result + meth.hashCode();
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        boolean returnVal = true;
        if (this == obj) {
            returnVal = true;
        }
        if (obj == null) {
            returnVal = false;
        }
        if (getClass() != obj.getClass()) {
            returnVal = false;
        }
        final BenchmarkElement other = (BenchmarkElement) obj;
        if (elementId != other.elementId) {
            returnVal = false;
        }
        if (meth == null) {
            if (other.meth != null) {
                returnVal = false;
            }
        } else {
            if (!meth.equals(other.meth)) {
                returnVal = false;
            }
        }
        return returnVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder(meth.toString())
                .append(":").append(elementId).toString();
    }

}
