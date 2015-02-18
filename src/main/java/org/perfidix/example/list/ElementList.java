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


/**
 * This is an abstract class for storing elements of any kind in an array-based list.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
abstract class ElementList {
    /**
     * Initial hash capacity.
     */
    static final int CAP = 1 << 3;
    /**
     * Resize factor for extending the arrays.
     */
    double factor = Array.RESIZE;
    /**
     * Number of elements.
     */
    int size;

    /**
     * Default constructor.
     */
    ElementList() {
    }

    /**
     * Returns a new array size.
     *
     * @return new array size
     */
    final int newSize() {
        return Array.newSize(size, factor);
    }

    /**
     * Returns a new array size that is larger than or equal to the specified size.
     *
     * @param min minimum size
     * @return new array size
     */
    final int newSize(final int min) {
        return Math.max(newSize(), min);
    }

    /**
     * Returns the number of elements.
     *
     * @return number of elements
     */
    final int size() {
        return size;
    }

    /**
     * Sets the number of elements to the specified value.
     *
     * @param s number of elements
     */
    public final void size(final int s) {
        size = s;
    }

    /**
     * Tests is the container has no elements.
     *
     * @return result of check
     */
    public final boolean empty() {
        return size == 0;
    }

    /**
     * Resets the array size.
     */
    public final void reset() {
        size = 0;
    }
}
