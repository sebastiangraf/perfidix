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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class does no arrangement for methods. That means that the order of the
 * input is given back as the order of the output as well.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class NoMethodArrangement extends AbstractMethodArrangement {

    /**
     * Constructor for no arrangement. That means that the order which is given
     * as an input is also given back as the output. The order is normally the
     * order of occurrence of methods in the class.
     * 
     * @param elements
     *            with benchmarkable elements.
     */
    protected NoMethodArrangement(final Set<BenchmarkElement> elements) {
        super(elements);
    }

    /**
     * Not arranging the list in this case. That means normally that all
     * elements are occuring in the same order than defined in the class-file.
     * 
     * @param elements
     *            to be arranged, or not in this case.
     * @return the input.
     */
    @Override
    protected final List<BenchmarkElement> arrangeList(
            Set<BenchmarkElement> elements) {
        final List<BenchmarkElement> elementList =
                new LinkedList<BenchmarkElement>();
        elementList.addAll(elements);
        return elementList;
    }
}
