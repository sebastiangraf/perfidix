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
package org.perfidix.perclipse.launcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.launcher.PerclipseActivator}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseActivatorTest {
    private transient TestUtilClass utilClass;
    private transient PerclipseActivator activator;
    private static final transient String NNULL_MESSAGE =
            "Tests if the object is not null";
    private transient MyLogListener loggerListener;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        loggerListener = new MyLogListener();
        PerclipseActivator.getDefault().getLog().addLogListener(loggerListener);
        utilClass = new TestUtilClass();
        activator = PerclipseActivator.getDefault();
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        PerclipseActivator.getDefault().getLog().removeLogListener(
                loggerListener);
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getDefault()}.
     */
    @Test
    public void testGetDefault() {
        assertNotNull(NNULL_MESSAGE, PerclipseActivator.getDefault());
        assertEquals(
                "Tests if the instances are equal", activator,
                PerclipseActivator.getDefault());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getPluginId()}.
     */
    @Test
    public void testGetPluginId() {
        assertEquals(
                "Tests if the plug-in id is equal to the one beside",
                "org.perfidix.perclipse", PerclipseActivator.getPluginId());
        assertEquals(
                "Tests if the constant is equal to the static get method",
                PerclipseActivator.PLUGIN_ID, PerclipseActivator.getPluginId());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#logInfo(java.lang.String)}
     * .
     */
    @Test
    public void testLogInfo() {
        PerclipseActivator.logInfo("Something to log");
        assertTrue("Test if has been logged", loggerListener
                .getStatusList().contains("Something to log"));
        PerclipseActivator.logInfo(null);

    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#log(java.lang.Throwable)}
     * .
     */
    @Test
    public void testLogThrowable() {
        PerclipseActivator.log(new IOException("MyMessage"));
        assertTrue("Tests if the log contains the MyMessage.", loggerListener
                .getStatusList().contains("MyMessage"));

    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#log(java.lang.Throwable, java.lang.String)}
     * .
     */
    @Test
    public void testLogThrowableString() {
        PerclipseActivator.log(new IOException(), "SomeText");
        assertTrue("Checks if the log contains the SomeText.", loggerListener
                .getStatusList().contains("SomeText"));
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#log(org.eclipse.core.runtime.IStatus)}
     * .
     */
    @Test
    public void testLogIStatus() {
        PerclipseActivator.log(new Status(
                IStatus.OK, PerclipseActivator.PLUGIN_ID, "MyStatus"));
        assertTrue("Tests if the log contains the MyStatus", loggerListener
                .getStatusList().contains("MyStatus"));
    }


    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getActiveWorkbenchWindow()}
     * .
     */
    @Test
    public void testGetActiveWorkbenchWindow() {
        assertNotNull(NNULL_MESSAGE, PerclipseActivator
                .getActiveWorkbenchWindow());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getActivePage()}
     * .
     */
    @Test
    public void testGetActivePage() {
        assertNotNull(NNULL_MESSAGE, PerclipseActivator.getActivePage());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getModel()}.
     */
    @Test
    public void testGetModel() {
        assertNotNull(NNULL_MESSAGE, PerclipseActivator.getModel());

    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getBenchView()}
     * .
     */
    @Test
    public void testGetBenchView() {
        assertNull("Tests if the object is null", activator.getBenchView());
        assertNull("Tests if the object is null", PerclipseActivator
                .getDefault().getBenchView());
        utilClass.setViewForTesting();
        assertNotNull(NNULL_MESSAGE, PerclipseActivator
                .getDefault().getBenchView());
        utilClass.setViewNull();
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getWorkspace()}
     * .
     */
    @Test
    public void testGetWorkspace() {
        assertNotNull(NNULL_MESSAGE, activator.getWorkspace());
        assertNotNull(NNULL_MESSAGE, PerclipseActivator
                .getDefault().getWorkspace());
    }

    /**
     * Test class to check logging.
     * 
     * @author lewandow
     */
    private class MyLogListener implements ILogListener {

        private final transient List<String> statusList =
                new ArrayList<String>();

        /*
         * (non-Javadoc)
         * @see
         * org.eclipse.core.runtime.ILogListener#logging(org.eclipse.core.runtime
         * .IStatus, java.lang.String)
         */
        public void logging(final IStatus status, final String plugin) {
            statusList.add(status.getMessage());
            statusList.add(status.getException().getMessage());
        }

        /**
         * The status list.
         * 
         * @return The list with the occurred status.
         */
        public List<String> getStatusList() {
            return statusList;
        }

    }
}
