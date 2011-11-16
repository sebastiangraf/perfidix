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
package org.perfidix.perclipse.viewtreedata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * TestCase for testing the class TreeDataProvider.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class TreeDataProviderTest {

    private transient TreeDataProvider dataProvider;
    private static final transient String NOT_NULL =
            "Tests if object is not null";

    /**
     * Simple setUp method for our TreeDataProvider instance.
     */
    @Before
    public void setUp() {
        dataProvider = new TreeDataProvider("MyElement", 66, 22);
    }

    /**
     * This method tests the getters for the class
     * {@link org.perfidix.perclipse.viewtreedata.TreeDataProvider}.
     */
    @Test
    public void testGettersInClass() {
        assertEquals(
                "Tests if MyElement is equal to the data provider ones.",
                "MyElement", dataProvider.getParentElementName());
        assertEquals("Tests if number of benchs is 66.", 66, dataProvider
                .getNumberOfBenchsForElement());
        assertEquals("Tests if currentbench is 22.", 22, dataProvider
                .getCurrentBench());
        assertEquals(
                "Tests if number of current bench errors is 0", 0, dataProvider
                        .getCurrentBenchError());
        dataProvider.updateCurrentBenchError(5);
        assertEquals("Tests if bench error is 5", 5, dataProvider
                .getCurrentBenchError());
        assertNotNull(NOT_NULL, dataProvider.getChildElements());
        assertNotNull(NOT_NULL, dataProvider.getParent());
        assertEquals(
                "Tests if getParent is equals to dataProfier", dataProvider,
                dataProvider.getParent());

    }

    /**
     * Tests the constructor
     * {@link org.perfidix.perclipse.viewtreedata.TreeDataProvider#TreeDataProvider(String, int, int)}
     */
    @Test
    public void testTreeDataProvider() {
        assertNotNull(NOT_NULL, dataProvider);
        dataProvider = new TreeDataProvider(null, 55, 11);
        assertEquals("Test if getParent is null", null, dataProvider
                .getParent());
    }

    /**
     * Tests the method updateCurrentBench.
     * {@link org.perfidix.perclipse.viewtreedata.TreeDataProvider#updateCurrentBench(int)}
     */
    @Test
    public void testUpdateCurrentBench() {
        dataProvider.updateCurrentBench(23);
        assertEquals("Test if current bench is 23.", 23, dataProvider
                .getCurrentBench());
    }

    /**
     * Tests the method update error count.
     * {@link org.perfidix.perclipse.viewtreedata.TreeDataProvider#updateCurrentBenchError(int)}
     */
    @Test
    public void testUpdateCurrentBenchError() {
        dataProvider.updateCurrentBenchError(23);
        assertEquals("Test if current bench error is 23", 23, dataProvider
                .getCurrentBenchError());
    }

    /**
     * Tests the method toString.
     * {@link org.perfidix.perclipse.viewtreedata.TreeDataProvider#toString()}
     */
    @Test
    public void testToString() {
        assertTrue("Test if is not null", (dataProvider.toString() != null));
        dataProvider = new TreeDataProvider(null, 55, 11);
        assertTrue("Test if is null", (dataProvider.toString() == null));
    }

}
