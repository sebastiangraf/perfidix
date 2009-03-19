/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
 * This class represents an arrangement where each method is executed after
 * another. That means that <br/>
 * <code>
 * 
 * &#064;Bench(runs=3) public bench1(){ .. }<br/>
 * &#064;Bench(runs=2) public bench2(){ .. }<br/>
 * &#064;Bench(runs=1) public bench3(){ .. }<br/> </code><br/>
 * results in the following execution order:<br/>
 * <code>
 * bench1
 * bench2
 * bench3
 * bench1
 * bench2
 * bench1
 * </code>
 */
public final class SingleMethodCallArrangement
        extends AbstractMethodArrangement {

    /**
     * Simple Constructor-
     * 
     * @param elements
     */
    protected SingleMethodCallArrangement(
            final Set<BenchmarkElement> elements) {
        super(elements);
    }

    /** {@inheritDoc} */
    @Override
    protected final List<BenchmarkElement> arrangeList(
            final Set<BenchmarkElement> elements) {
        final Hashtable<BenchmarkMethod, ArrayList<BenchmarkElement>> table =
                new Hashtable<BenchmarkMethod, ArrayList<BenchmarkElement>>();
        final List<BenchmarkElement> returnVal =
                new ArrayList<BenchmarkElement>();

        // Having a table
        for (final BenchmarkElement elem : elements) {
            if (!table.containsKey(elem.getMeth())) {
                table.put(
                        elem.getMeth(), new ArrayList<BenchmarkElement>());
            }
            table.get(elem.getMeth()).add(elem);
        }

        // Computing complete number of elements
        int numberOfElements = 0;
        for (final BenchmarkMethod meth : table.keySet()) {
            numberOfElements = numberOfElements + table.get(meth).size();
        }

        // Defining order to execute, start with the one with the most elements
        final Set<Entry<BenchmarkMethod, ArrayList<BenchmarkElement>>> compareMethods =
                new TreeSet<Entry<BenchmarkMethod, ArrayList<BenchmarkElement>>>(
                        new BenchmarkElementComparator());
        compareMethods.addAll(table.entrySet());

        final ArrayList<BenchmarkMethod> methods =
                new ArrayList<BenchmarkMethod>();
        for (Entry<BenchmarkMethod, ArrayList<BenchmarkElement>> entry : compareMethods) {
            methods.add(entry.getKey());
        }

        for (int i = 0; i < numberOfElements; i++) {
            BenchmarkElement elem = null;
            int j = 0;
            while (elem == null) {
                int index = (i + j) % methods.size();
                final BenchmarkMethod methodToInclude = methods.get(index);
                if (table.containsKey(methodToInclude)) {
                    elem = table.get(methodToInclude).remove(0);
                    if (table.get(methodToInclude).size() == 0) {
                        table.remove(methodToInclude);
                    }
                }
                j++;
            }
            returnVal.add(elem);
        }
        return returnVal;
    }

    /**
     * Comparator to compare the different entries according to the size of the
     * underlaying arraylist
     * 
     * @author Sebastian Graf, University of Konstanz
     */
    class BenchmarkElementComparator
            implements
            Comparator<Entry<BenchmarkMethod, ArrayList<BenchmarkElement>>> {

        /** {@inheritDoc} */
        public int compare(
                final Entry<BenchmarkMethod, ArrayList<BenchmarkElement>> o1,
                final Entry<BenchmarkMethod, ArrayList<BenchmarkElement>> o2) {
            if (o1.getValue().size() > o2.getValue().size()) {
                return -1;
            } else if (o1.getValue().size() == o2.getValue().size()) {
                return 0;
            } else {
                return 1;
            }

        }

    }

}
