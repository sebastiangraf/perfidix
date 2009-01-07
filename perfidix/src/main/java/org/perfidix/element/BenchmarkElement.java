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
    private final BenchmarkMethod meth;

    /** The unique id for this elements. */
    private final int id;

    /**
     * Static Mapping for BenchmarkMethod->Integer. Every BenchmarkMethod gains
     * one unique id from this mapping.
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
        id = ID_MAPPING.get(getMeth()) + 1;
        ID_MAPPING.put(getMeth(), getId());

    }

    /**
     * Getter for the id.
     * 
     * @return the id of this element
     */
    public final int getId() {
        return id;
    }

    /**
     * Getting the {@link BenchmarkMethod} related to this element.
     * 
     * @return the meth
     */
    public final BenchmarkMethod getMeth() {
        return meth;
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        if (meth == null) {
            result = prime * result;
        } else {
            result = prime * result + meth.hashCode();
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BenchmarkElement other = (BenchmarkElement) obj;
        if (id != other.id) {
            return false;
        }
        if (meth == null) {
            if (other.meth != null) {
                return false;
            }
        } else {
            if (!meth.equals(other.meth)) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return new StringBuilder(meth.toString())
                .append(":").append(id).toString();
    }

}
