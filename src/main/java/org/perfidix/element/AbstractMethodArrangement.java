/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.element;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This method defines an order for the execution of all methods. A collection
 * with definitly benchmarkable methods is given in, shuffled in a way and
 * returned as an iterator. The kind of shuffling is set by the enum {@link KindOfArrangement}. ordered with
 * the help of inheriting classes.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractMethodArrangement implements Iterable<BenchmarkElement> {

    /**
     * List to hold all benchmarkable elements in the correct order as a base
     * for the iterator.
     */
    private transient final List<BenchmarkElement> elementList;

    /**
     * Constructor which takes all benchmarkable methods. These methods are
     * afterwards shuffled with the help of the implementing class. Note that
     * the elements are fix after the arrangement.
     * 
     * @param elements
     *            definitly benchmarkable objects.
     */
    protected AbstractMethodArrangement(final Set<BenchmarkElement> elements) {
        final List<BenchmarkElement> arrangedList = arrangeList(elements);
        this.elementList = Collections.unmodifiableList(arrangedList);
    }

    /**
     * Method to arrange benchmarkable methods in different orders.
     * 
     * @param methods
     *            to be arranged
     * @return the arranged methods.
     */
    protected abstract List<BenchmarkElement> arrangeList(final Set<BenchmarkElement> methods);

    /** {@inheritDoc} */
    public final Iterator<BenchmarkElement> iterator() {
        return this.elementList.iterator();
    }

    /**
     * Factory method to get the method arrangement for a given set of classes.
     * The kind of arrangement is set by an instance of the enum {@link KindOfArrangement}.
     * 
     * @param elements
     *            to be benched
     * @param kind
     *            for the method arrangement
     * @return the arrangement, mainly an iterator
     */
    public static final AbstractMethodArrangement getMethodArrangement(final Set<BenchmarkElement> elements,
        final KindOfArrangement kind) {
        AbstractMethodArrangement arrang = null;
        switch (kind) {
        case NoArrangement:
            arrang = new NoMethodArrangement(elements);
            break;
        case ShuffleArrangement:
            arrang = new ShuffleMethodArrangement(elements);
            break;
        case SequentialMethodArrangement:
            arrang = new SequentialMethodArrangement(elements);
            break;
        default:
            throw new IllegalArgumentException("Kind not known!");
        }
        return arrang;

    }
}
