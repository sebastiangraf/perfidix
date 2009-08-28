package org.perfidix.perclipse.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.views.BenchTreeLabelProvider;
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchTreeLabelProviderTest {
    private BenchTreeLabelProvider labelProvider;
    private TreeDataProvider dataProvider;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        labelProvider = new BenchTreeLabelProvider();
        dataProvider = new TreeDataProvider("package.Class.element", 99, 54);
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        labelProvider = null;
        dataProvider = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider#getImage(Object)}
     * .
     */
    @Test
    public void testGetImage() {
        // Currently images are not specified for the treeviewer
        assertNull(labelProvider.getImage(null));
        assertNull(labelProvider.getImage(dataProvider));
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider#getText(Object)}
     * .
     */
    @Test
    public void testGetText() {
        assertNull(labelProvider.getText(null));
        assertEquals("package.Class.element  (54/99)", labelProvider
                .getText(dataProvider));
        dataProvider.updateCurrentBenchError(2);
        assertEquals("package.Class.element  (54/99) Errors: 2", labelProvider
                .getText(dataProvider));

    }
}
