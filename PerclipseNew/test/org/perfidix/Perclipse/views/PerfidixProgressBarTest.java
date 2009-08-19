/**
 * 
 */
package org.perfidix.Perclipse.views;

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.view.PerfidixProgressBar}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerfidixProgressBarTest {
    private BenchView view;
    private PerfidixProgressBar progressBar;
    private TestUtilClass utilClass;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        utilClass= new TestUtilClass();
        utilClass.setViewForTesting();
        view = PerclipseActivator.getDefault().getBenchView();
        progressBar=view.getProgressBar();

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
        progressBar = null;
        view = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.views.PerfidixProgressBar#PerfidixProgressBar(org.eclipse.swt.widgets.Composite)}
     * .
     */
    @Test
    public void testPerfidixProgressBar() {
      assertNotNull(progressBar);
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.views.PerfidixProgressBar#setMaximum(int)}.
     */
    @Test
    public void testSetMaximum() {
        progressBar.setMaximum(2555);
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.views.PerfidixProgressBar#reset()}.
     */
    @Test
    public void testReset() {
        progressBar.reset();
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.views.PerfidixProgressBar#reset(boolean, boolean, int, int)}
     * .
     */
    @Test
    public void testResetBooleanBooleanIntInt() {
        progressBar.reset(true, false, 50, 100);
        progressBar.reset(false, false, 33, 12);
        progressBar.reset(false, true, 99, 99);
        
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.views.PerfidixProgressBar#stopped()}.
     */
    @Test
    public void testStopped() {
        progressBar.stopped();
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.views.PerfidixProgressBar#computeSize(int, int, boolean)}
     * .
     */
    @Test
    public void testComputeSizeIntIntBoolean() {
        Point point = new Point(22,55);
        assertEquals(point,progressBar.computeSize(22, 55, true));
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.views.PerfidixProgressBar#step(int)}.
     */
    @Test
    public void testStep() {
        progressBar.step(22);
        progressBar.reset(true, true, 55, 55);
        progressBar.step(22);
        progressBar.reset(true, true, 54, 55);
        progressBar.step(22);
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.views.PerfidixProgressBar#refresh(boolean)}
     * .
     */
    @Test
    public void testRefresh() {
        progressBar.refresh(true);
        progressBar.refresh(false);
    }

}
