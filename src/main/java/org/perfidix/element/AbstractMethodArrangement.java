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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This method defines an order for the execution of all methods. A collection
 * with definitly benchmarkable methods is given in, shuffled in a way and
 * returned as an iterator. The kind of shuffling is set by the enum
 * {@link KindOfMethodArrangement}. ordered with the help of inheriting classes.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractMethodArrangement
        implements Iterable<BenchmarkElement> {

    /**
     * Enum for all implemented arrangements.
     */
    public static enum KindOfMethodArrangement {
        /** no method arrangement. */
        NoArrangement
    };

    /**
     * List to hold all benchmarkable elements in the correct order as a base
     * for the iterator.
     */
    private final List<BenchmarkElement> elementList;

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
    protected abstract List<BenchmarkElement> arrangeList(
            final Set<BenchmarkElement> methods);

    /** {@inheritDoc} */
    public final Iterator<BenchmarkElement> iterator() {
        return this.elementList.iterator();
    }

    /**
     * Factory method to get the method arrangement for a given set of classes.
     * The kind of arrangement is set by an instance of the enum
     * {@link KindOfMethodArrangement}.
     * 
     * @param elements
     *            to be benched
     * @param kind
     *            for the method arrangement
     * @return the arrangement, mainly an iterator
     */
    public final static AbstractMethodArrangement getMethodArrangement(
            final Set<BenchmarkElement> elements,
            final KindOfMethodArrangement kind) {
        switch (kind) {
        case NoArrangement:
            return new NoMethodArrangement(elements);
        default:
            throw new IllegalArgumentException("Kind not known!");

        }

    }

}
