package org.perfidix.perclipse.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.util.TestUtilClass;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.views.BenchView;
import org.perfidix.perclipse.views.BenchViewCounterPanel;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.views.BenchViewCounterPanel}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewCounterPanelTest {
    private BenchViewCounterPanel counterPanel;
    private BenchView view;
    private TestUtilClass utilClass;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        utilClass = new TestUtilClass();
        utilClass.setViewForTesting();
        view = PerclipseActivator.getDefault().getBenchView();
        counterPanel = view.getBenchCounterPanel();
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        utilClass.setViewNull();
        utilClass = null;
        view = null;
        counterPanel = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchViewCounterPanel#BenchViewCounterPanel(org.eclipse.swt.widgets.Composite)
     * }
     * .
     */
    @Test
    public void testBenchViewCounterPanel() {
        assertNotNull(counterPanel);
    }

    // /**
    // * Tests the method
    // * {@link
    // org.perfidix.Perclipse.views.BenchViewCounterPanel#createLabel(String,
    // org.eclipse.swt.graphics.Image, String)
    // * }
    // * .
    // */
    // @Test
    // public void testCreateLabel() {
    // assertNull(counterPanel.createLabel(null, null, null));
    // assertNotNull(counterPanel.createLabel("MyName", null, "25"));
    // assertEquals("50", counterPanel
    // .createLabel("TheName", null, "50").getText());
    // }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchViewCounterPanel#resetRuns()} .
     */
    @Test
    public void testResetRuns() {
        counterPanel.setTotalRuns(55);
        assertEquals(55, counterPanel.getTotalRuns());
        counterPanel.resetRuns();
        assertEquals(0, counterPanel.getTotalRuns());
    }

    /**
     * Tests the methods
     * {@link org.perfidix.perclipse.views.BenchViewCounterPanel#setTotalRuns(int)}
     * ,
     * {@link org.perfidix.perclipse.views.BenchViewCounterPanel#setBenchRuns(int)}
     * ,
     * {@link org.perfidix.perclipse.views.BenchViewCounterPanel#setBenchErrors(int)}
     * .
     */
    @Test
    public void testErrorAndRunsSetter() {
        counterPanel.setBenchErrors(55);
        counterPanel.setBenchRuns(222);
        counterPanel.setTotalRuns(555);
        assertEquals(555, counterPanel.getTotalRuns());

    }
}
