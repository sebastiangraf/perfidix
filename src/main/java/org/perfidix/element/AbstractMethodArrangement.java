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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This method defines an order for the execution of all methods. Potential
 * Classes which can contain benchmarkable methods are given and afterwards
 * ordered with the help of inheriting classes.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractMethodArrangement
        implements Iterable<BenchmarkElement> {

    /** Variable to hold all benchmarkable elements */
    private final List<BenchmarkElement> elementList;

    /**
     * Constructor which takes several classes and extract benchmarkable
     * methods. These methods are afterwards shuffled with the help of the
     * implementing class. Note that the elements are fix after the arrangement.
     * 
     * @param classes
     *            potential benchmarkable objects.
     */
    protected AbstractMethodArrangement(final List<Class<?>> classes) {
        final List<BenchmarkElement> elementList =
                new LinkedList<BenchmarkElement>();
        for (final Class<?> clazz : classes) {
            for (final Method method : clazz.getDeclaredMethods()) {
                final BenchmarkElement benchElement =
                        new BenchmarkElement(method);
                if (benchElement.checkThisMethodAsBenchmarkable()) {
                    elementList.add(benchElement);
                }
            }
        }
        final List<BenchmarkElement> arrangedList = arrangeList(elementList);
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
            final List<BenchmarkElement> methods);

    /** {@inheritDoc} */
    public final Iterator<BenchmarkElement> iterator() {
        return this.elementList.iterator();
    }

    /**
     * Factory method to get the method arrangement for a given set of classes.
     * At the moment, no arrangement is given, so the only parameter is the list
     * of classes themselves.
     * 
     * @param clazzes
     *            to be benched
     * @return the arrangement, mainly an iterator
     */
    public final static AbstractMethodArrangement getMethodArrangement(
            final List<Class<?>> clazzes) {
        return new NoMethodArrangement(clazzes);
    }

}
