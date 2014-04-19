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
package org.perfidix.result;


import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;


/**
 * The result container contains more results. It is by definition recursive, so it can handle as much diversity as
 * possible
 *
 * @param <ResultType> the type of the children.
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractResultContainer<ResultType extends AbstractResult> extends AbstractResult {

    /**
     * Map of all elements with the Mapping Method/Class -> ResultType.
     */
    protected transient final Map<Object, ResultType> elements;

    /**
     * Constructor.
     *
     * @param paramElem related element
     */
    protected AbstractResultContainer(final Object paramElem) {
        super(paramElem);
        elements = new Hashtable<Object, ResultType>();
    }

    /**
     * Getting all elements which are included in this result. That means: {@link BenchmarkResult} ->
     * {@link ClassResult}; {@link ClassResult} -> {@link MethodResult};
     *
     * @return a {@link Collection} of the included results.
     */
    public final Collection<ResultType> getIncludedResults() {
        return elements.values();
    }

    /**
     * Getting the results for one object.
     *
     * @param obj the object, can be a Class or a Method
     * @return the result for this object
     */
    public final ResultType getResultForObject(final Object obj) {
        return elements.get(obj);
    }

}
