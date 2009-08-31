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
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
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
