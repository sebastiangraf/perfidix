/**
 * 
 */
package org.perfidix.Perclipse.launcher;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.Perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.view.PerfidixProgressBar}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseActivatorTest {
    private TestUtilClass utilClass;
    private PerclipseActivator activator;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        utilClass = new TestUtilClass();
        activator = new PerclipseActivator();
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        utilClass = null;
        activator = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#getDefault()}.
     */
    @Test
    public void testGetDefault() {
        assertNotNull(PerclipseActivator.getDefault());
        assertEquals(activator, PerclipseActivator.getDefault());
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#getPluginId()}.
     */
    @Test
    public void testGetPluginId() {
        assertEquals("org.perfidix.Perclipse", PerclipseActivator.getPluginId());
        assertEquals(PerclipseActivator.PLUGIN_ID, PerclipseActivator
                .getPluginId());
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#logInfo(java.lang.String)}
     * .
     */
    @Test
    public void testLogInfo() {
        PerclipseActivator.logInfo("Something to log");
        PerclipseActivator.logInfo(null);
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#log(java.lang.Throwable)}
     * .
     */
    @Test
    public void testLogThrowable() {
        PerclipseActivator.log(new RuntimeException("MyMessage"));

    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#log(java.lang.Throwable, java.lang.String)}
     * .
     */
    @Test
    public void testLogThrowableString() {
        PerclipseActivator.log(
                new RuntimeException("AnotherMesage"), "SomeText");
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#log(org.eclipse.core.runtime.IStatus)}
     * .
     */
    @Test
    public void testLogIStatus() {
        PerclipseActivator.log(new Status(
                Status.OK, PerclipseActivator.PLUGIN_ID, "MyStatus"));
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#getImageDescriptor(java.lang.String)}
     * .
     */
    @Test
    public void testGetImageDescriptor() {
        assertNotNull(PerclipseActivator.getImageDescriptor("icons/time.png"));
        assertNull(PerclipseActivator.getImageDescriptor("icons/tiiiime.png"));
        assertNull(PerclipseActivator.getImageDescriptor(null));
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#getActiveWorkbenchWindow()}
     * .
     */
    @Test
    public void testGetActiveWorkbenchWindow() {
        assertNotNull(PerclipseActivator.getActiveWorkbenchWindow());
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#getActivePage()}
     * .
     */
    @Test
    public void testGetActivePage() {
        assertNotNull(PerclipseActivator.getActivePage());
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#getModel()}.
     */
    @Test
    public void testGetModel() {
        assertNotNull(PerclipseActivator.getModel());
        
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#getBenchView()}
     * .
     */
    @Test
    public void testGetBenchView() {
       assertNull(activator.getBenchView());
       assertNull(PerclipseActivator.getDefault().getBenchView());
       utilClass.setViewForTesting();
       assertNotNull(PerclipseActivator.getDefault().getBenchView());
    }

    /**
     * Test method for
     * {@link org.perfidix.Perclipse.launcher.PerclipseActivator#getWorkspace()}
     * .
     */
    @Test
    public void testGetWorkspace() {
        fail("Not yet implemented"); // TODO
    }

}
