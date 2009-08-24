package org.perfidix.Perclipse.viewTreeData;

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

    private TreeDataProvider dataProvider;

    /**
     * Simple setUp method for our TreeDataProvider instance.
     */
    @Before
    public void setUp() {
        dataProvider = new TreeDataProvider("MyElement", 66, 22);
    }

    /**
     * This method tests the getters for the class
     * {@link org.perfidix.Perclipse.viewTreeData.TreeDataProvider}.
     */
    @Test
    public void testGettersInClass() {
        assertEquals("MyElement", dataProvider.getParentElementName());
        assertEquals(66, dataProvider.getNumberOfBenchsForElement());
        assertEquals(22, dataProvider.getCurrentBench());
        assertEquals(0, dataProvider.getCurrentBenchError());
        dataProvider.updateCurrentBenchError(5);
        assertEquals(5, dataProvider.getCurrentBenchError());
        assertNotNull(dataProvider.getChildElements());
        assertNotNull(dataProvider.getParent());
        assertEquals(dataProvider, dataProvider.getParent());

    }

    /**
     * Tests the constructor
     * {@link org.perfidix.Perclipse.viewTreeData.TreeDataProvider#TreeDataProvider(String, int, int)}
     */
    @Test
    public void testTreeDataProvider() {
        assertNotNull(dataProvider);
        dataProvider = new TreeDataProvider(null, 55, 11);
        assertEquals(null, dataProvider.getParent());
    }

    /**
     * Tests the method updateCurrentBench.
     * {@link org.perfidix.Perclipse.viewTreeData.TreeDataProvider#updateCurrentBench(int)}
     */
    @Test
    public void testUpdateCurrentBench() {
        dataProvider.updateCurrentBench(23);
        assertEquals(23, dataProvider.getCurrentBench());
    }

    /**
     * Tests the method update error count.
     * {@link org.perfidix.Perclipse.viewTreeData.TreeDataProvider#updateCurrentBenchError(int)}
     */
    @Test
    public void testUpdateCurrentBenchError() {
        dataProvider.updateCurrentBench(23);
        assertEquals(23, dataProvider.getCurrentBench());
    }

    /**
     * Tests the method toString.
     * {@link org.perfidix.Perclipse.viewTreeData.TreeDataProvider#toString()}
     */
    @Test
    public void testToString() {
        assertTrue((dataProvider.toString() != null));
        dataProvider = new TreeDataProvider(null, 55, 11);
        assertTrue((dataProvider.toString() == null));
    }

}
