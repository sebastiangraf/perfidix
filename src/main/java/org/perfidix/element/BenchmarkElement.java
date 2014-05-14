/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.element;


import java.util.Hashtable;
import java.util.Map;


/**
 * This class acts as a container related to one benchmarkable method. This class has an unique identifier for one
 * execution.
 *
 * @author Sebastian Graf, University of Konstanz
 * @see BenchmarkMethod
 */
public final class BenchmarkElement {

    /**
     * Static Mapping for BenchmarkMethod->Integer. Every BenchmarkMethod gains one unique elementId from this mapping.
     */
    private static final Map<BenchmarkMethod, Integer> ID_MAPPING = new Hashtable<>();
    /**
     * The BenchmarkMethod related to this element.
     */
    private transient final BenchmarkMethod meth;
    /**
     * The unique elementId for this elements.
     */
    private transient final int elementId;
    /**
     * Parameter for this benchmark.
     */
    private transient final Object[] parameter;

    /**
     * Constructor, simple taking the corresponding {@link BenchmarkMethod}.
     *
     * @param paramMeth      the related {@link BenchmarkMethod}
     * @param paramParameter the parameter for this element if dataprovider is used
     */
    public BenchmarkElement(final BenchmarkMethod paramMeth, Object... paramParameter) {
        meth = paramMeth;
        if (!ID_MAPPING.containsKey(paramMeth)) {
            ID_MAPPING.put(getMeth(), 0);
        }
        elementId = ID_MAPPING.get(getMeth()) + 1;
        ID_MAPPING.put(getMeth(), getId());
        this.parameter = paramParameter;
    }

    /**
     * @return the parameter
     */
    public Object[] getParameter() {
        return parameter;
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        return hashCode() == obj.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return meth.toString() + ":" + elementId;
    }

}
