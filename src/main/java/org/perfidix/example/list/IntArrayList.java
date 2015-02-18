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
package org.perfidix.example.list;


import java.util.Iterator;


/**
 * This is a simple container for native integer arrays.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public final class IntArrayList extends ElementList implements Iterable<int[]> {
    /**
     * Elements container.
     */
    int[][] list;

    /**
     * Default constructor.
     */
    public IntArrayList() {
        this(CAP);
    }

    /**
     * Constructor, specifying an initial array capacity.
     *
     * @param c initial capacity
     */
    public IntArrayList(final int c) {
        list = new int[c][];
    }

    /**
     * Adds an element.
     *
     * @param e element to be added
     */
    public void add(final int[] e) {
        if (size == list.length) list = Array.copyOf(list, newSize());
        list[size++] = e;
    }

    /**
     * Returns the element at the specified index.
     *
     * @param i index
     * @return element
     */
    public int[] get(final int i) {
        return list[i];
    }

    /**
     * Sets an element at the specified index.
     *
     * @param i index
     * @param e element to be set
     */
    public void set(final int i, final int[] e) {
        if (i >= list.length) list = Array.copyOf(list, newSize(i + 1));
        list[i] = e;
        size = Math.max(size, i + 1);
    }

    @Override
    public Iterator<int[]> iterator() {
        return new Iterator<int[]>() {
            private int c = -1;

            @Override
            public boolean hasNext() {
                return ++c < size;
            }

            @Override
            public int[] next() {
                return list[c];
            }

            @Override
            public void remove() {
                // Util.notexpected();
            }
        };
    }
}
