/**
 * 
 */
package org.perfidix.Perclipse.launcher;

import static org.junit.Assert.*;

import java.lang.reflect.Type;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.core.JavaProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.perfidix.Perclipse.viewTreeData.TreeDataProvider;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.view.PerfidixProgressBar}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchSearchResultTest {
    private BenchSearchResult searchResult;
    private IType[] typeArray;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        typeArray = new IType[1];
        typeArray[0]= null;
        searchResult= new BenchSearchResult(typeArray);
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        typeArray = null;
        searchResult = null;

    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.BenchSearchResult#getTypes()}.
     */
    @Test
    public void testGetTypes() {
        assertArrayEquals(typeArray, searchResult.getTypes());
        assertNotNull(searchResult.getTypes());
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.BenchSearchResult#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        assertFalse(searchResult.isEmpty());
        typeArray = new IType[0];
        searchResult = new BenchSearchResult(typeArray);
        assertTrue(searchResult.isEmpty());

    }

    /**
     * A DummyClass for Testing
     * 
     * @author Lukas Lewandowski
     */
    public class BenchClass {
        private int additionsErgebnis = 0;

        /**
         * A dummy add method.
         */
        public void benchMethod() {
            int zahl1 = 5;
            int zahl2 = 7;
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
