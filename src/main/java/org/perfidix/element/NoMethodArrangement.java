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
package org.perfidix.element;


import java.util.LinkedList;
import java.util.List;


/**
 * This class does no arrangement for methods. That means that the order of the input is given back as the order of the
 * output as well.
 *
 * @author Sebastian Graf, University of Konstanz
 */
public final class NoMethodArrangement extends AbstractMethodArrangement {

    /**
     * Constructor for no arrangement. That means that the order which is given as an input is also given back as the
     * output. The order is normally the order of occurrence of methods in the class.
     *
     * @param elements with benchmarkable elements.
     */
    protected NoMethodArrangement(final List<BenchmarkElement> elements) {
        super(elements);
    }

    /**
     * Not arranging the list in this case. That means normally that all elements are occuring in the same order than
     * defined in the class-file.
     *
     * @param elements to be arranged, or not in this case.
     * @return the input.
     */
    @Override
    protected List<BenchmarkElement> arrangeList(final List<BenchmarkElement> elements) {
        final List<BenchmarkElement> elementList = new LinkedList<BenchmarkElement>();
        elementList.addAll(elements);
        return elementList;
    }
}
