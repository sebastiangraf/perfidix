/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
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
public final class SequentialMethodArrangement
        extends AbstractMethodArrangement {

    /**
     * Simple Constructor-
     * 
     * @param elements
     */
    protected SequentialMethodArrangement(final Set<BenchmarkElement> elements) {
        super(elements);
    }

    /** {@inheritDoc} */
    @Override
    protected List<BenchmarkElement> arrangeList(
            final Set<BenchmarkElement> elements) {
        final Map<BenchmarkMethod, ArrayList<BenchmarkElement>> table =
                new Hashtable<BenchmarkMethod, ArrayList<BenchmarkElement>>();
        final List<BenchmarkElement> returnVal =
                new ArrayList<BenchmarkElement>();

        // Having a table
        for (final BenchmarkElement elem : elements) {
            if (!table.containsKey(elem.getMeth())) {
                table.put(elem.getMeth(), new ArrayList<BenchmarkElement>());
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
        for (final Entry<BenchmarkMethod, ArrayList<BenchmarkElement>> entry : table
                .entrySet()) {
            compareMethods.add(entry);
        }

        final ArrayList<BenchmarkMethod> methods =
                new ArrayList<BenchmarkMethod>();
        for (Entry<BenchmarkMethod, ArrayList<BenchmarkElement>> entry : compareMethods) {
            methods.add(entry.getKey());
        }

        for (int i = 0; i < numberOfElements; i++) {
            BenchmarkElement elem = null;
            int indexPart = 0;
            while (elem == null) {
                final int index = (i + indexPart) % methods.size();
                final BenchmarkMethod methodToInclude = methods.get(index);
                if (table.containsKey(methodToInclude)) {
                    elem = table.get(methodToInclude).remove(0);
                    if (table.get(methodToInclude).size() == 0) {
                        table.remove(methodToInclude);
                    }
                }
                indexPart++;
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
                final Entry<BenchmarkMethod, ArrayList<BenchmarkElement>> object1,
                final Entry<BenchmarkMethod, ArrayList<BenchmarkElement>> object2) {
            int returnVal = 0;
            if (object1.getValue().size() > object2.getValue().size()) {
                returnVal = -1;
            } else {
                returnVal = 1;
            }
            return returnVal;
        }

    }

}
