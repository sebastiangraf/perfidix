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
package org.perfidix.perclipse.launcher;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.IType;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.launcher.BenchSearchResult}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchSearchResultTest {
    private transient BenchSearchResult searchResult;
    private transient IType[] typeArray;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        typeArray = new IType[1];
        searchResult = new BenchSearchResult(typeArray);
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.BenchSearchResult#getTypes()}.
     */
    @Test
    public void testGetTypes() {
        assertArrayEquals(
                "Tests if the arrays contain the equal values", typeArray,
                searchResult.getTypes());
        assertNotNull("Tests if the object is not null", searchResult
                .getTypes());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.BenchSearchResult#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        assertFalse("Test if searchResult is not empty", searchResult.isEmpty());
        typeArray = new IType[0];
        searchResult = new BenchSearchResult(typeArray);
        assertTrue("Test if searchResult is empty", searchResult.isEmpty());

    }

    /**
     * A DummyClass for Testing
     * 
     * @author Lukas Lewandowski
     */
    public class BenchClass {
        private transient int additionsErgebnis = 0;

        /**
         * A dummy add method.
         */
        public void benchMethod() {
            final int zahl1 = 5;
            final int zahl2 = 7;
            additionsErgebnis = zahl1 + zahl2;
        }

        /**
         * @return The result of an add function.
         */
        public int getErgebnis() {
            return additionsErgebnis;
        }
    }

}
