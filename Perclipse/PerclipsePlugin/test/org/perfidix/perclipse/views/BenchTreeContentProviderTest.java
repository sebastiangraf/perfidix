/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
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
package org.perfidix.perclipse.views;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.views.BenchTreeContentProvider}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchTreeContentProviderTest {
    private transient BenchTreeContentProvider treeCProvider;
    private transient TreeDataProvider dataProvider;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        dataProvider = new TreeDataProvider("package.Class.TheElement", 25, 17);
        treeCProvider = new BenchTreeContentProvider();
    }

    // /**
    // * Simple tearDown - method.
    // *
    // * @throws java.lang.Exception
    // * The Exception occurred.
    // */
    // @After
    // public void tearDown() throws Exception {
    // dataProvider = null;
    // treeContentProvider = null;
    // }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeContentProvider#getChildren(Object)}
     * .
     */
    @Test
    public void testGetChildren() {
        // Expected no children because currently our objects has no children in
        // the treeviewer
        assertNotNull("Tests if  getChildren is not null", treeCProvider
                .getChildren(null));
        assertArrayEquals(
                "Tests if the both arrays are equal", new Object[0],
                treeCProvider.getChildren(null));
        assertArrayEquals(
                "Tests if arrays are equal", new Object[0], treeCProvider
                        .getChildren(dataProvider));

    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeContentProvider#hasChildren(Object)}
     * .
     */
    @Test
    public void testHasChildren() {
        // Expected no children because currently our objects has no children in
        // the treeviewer
        assertFalse("Test if has children.", treeCProvider.hasChildren(null));
        assertFalse("Test if provider has children", treeCProvider
                .hasChildren(dataProvider));
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeContentProvider#getParent(Object)}
     * .
     */
    @Test
    public void testGetParent() {
        assertNull("Test if provider is null", treeCProvider.getParent(null));
        assertNotNull("Tests if data provider is not null", treeCProvider
                .getParent(dataProvider));
        assertEquals(
                "Test if data provider exists and is equal.", dataProvider,
                treeCProvider.getParent(dataProvider));
        final TreeDataProvider newDP = new TreeDataProvider("A", 22, 9);
        assertFalse("Test if data provider is not the same.", newDP
                .equals(treeCProvider.getParent(dataProvider)));
    }
}
