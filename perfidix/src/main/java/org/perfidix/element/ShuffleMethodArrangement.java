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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * This class represents a shuffle random arrangement of elements. All elements
 * are shuffled and executed in a complete random order.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class ShuffleMethodArrangement extends AbstractMethodArrangement {

    /** Seed for the random arrangement of the elements. */
    private static final long SEED = 1L;

    /**
     * Constructor for shuffle arrangement. That means that the order which is
     * given as an input is shuffled in a random way and given back as the
     * output. The order is complete randomlike and depends on a seed.
     * 
     * @param elements
     *            with benchmarkable elements.
     */
    protected ShuffleMethodArrangement(final Set<BenchmarkElement> elements) {
        super(elements);
    }

    /** {@inheritDoc} */
    @Override
    protected List<BenchmarkElement> arrangeList(
            final Set<BenchmarkElement> methods) {
        final Random ran = new Random(SEED);
        final List<BenchmarkElement> inputList =
                new LinkedList<BenchmarkElement>();
        inputList.addAll(methods);
        Collections.shuffle(inputList, ran);
        return inputList;
    }
}
