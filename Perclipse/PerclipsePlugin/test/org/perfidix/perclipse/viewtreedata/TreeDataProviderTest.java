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
