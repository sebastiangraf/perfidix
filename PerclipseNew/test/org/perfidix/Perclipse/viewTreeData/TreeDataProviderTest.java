package org.perfidix.Perclipse.viewTreeData;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TestCase for testing the class TreeDataProvider.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class TreeDataProviderTest {

    /**
     * This method tests the getters for the class
     * {@link org.perfidix.Perclipse.viewTreeData.TreeDataProvider}.
     */
    @Test
    public void testGettersInClass() {
        String name = "MyElement";
        int total = 30;
        int current = 5;
        TreeDataProvider dataProvider =
                new TreeDataProvider(name, total, current);
        assertEquals(name, dataProvider.getParentElementName());
        assertEquals(total, dataProvider.getNumberOfBenchsForElement());
        assertEquals(current, dataProvider.getCurrentBench());
        assertNotNull(dataProvider.getChildElements());
        assertNotNull(dataProvider.getParent());
        assertEquals(dataProvider, dataProvider.getParent());
    }
    
    /**
     * Tests the constructor {@link org.perfidix.Perclipse.viewTreeData.TreeDataProvider#TreeDataProvider(String, int, int)}
     */
    @Test
    public void testTreeDataProvider(){
        
    }

}
