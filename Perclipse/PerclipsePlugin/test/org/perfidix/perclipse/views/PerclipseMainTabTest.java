/**
 * 
 */
package org.perfidix.perclipse.views;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.views.PerclipseMainTab;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.buildpath.BuildPathSupport}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseMainTabTest {
    private PerclipseMainTab mainTab;
    
    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        mainTab= new PerclipseMainTab();
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        mainTab=null;
    }

    /**
     * Test method for {@link org.perfidix.perclipse.views.PerclipseMainTab#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals("Benchs", mainTab.getName());
    }

    /**
     * Test method for {@link org.perfidix.perclipse.views.PerclipseMainTab#getImage()}.
     */
    @Test
    public void testGetImage() {
        assertNotNull(mainTab.getImage());
    }

}
