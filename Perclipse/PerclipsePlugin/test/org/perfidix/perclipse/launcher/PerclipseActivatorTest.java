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
package org.perfidix.perclipse.launcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.util.TestUtilClass;
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.launcher.PerclipseActivator}.
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
        utilClass = null;
        activator = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getDefault()}.
     */
    @Test
    public void testGetDefault() {
        assertNotNull(PerclipseActivator.getDefault());
        assertEquals(activator, PerclipseActivator.getDefault());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getPluginId()}.
     */
    @Test
    public void testGetPluginId() {
        assertEquals("org.perfidix.perclipse", PerclipseActivator.getPluginId());
        assertEquals(PerclipseActivator.PLUGIN_ID, PerclipseActivator
                .getPluginId());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#logInfo(java.lang.String)}
     * .
     */
    @Test
    public void testLogInfo() {
        PerclipseActivator.logInfo("Something to log");
        PerclipseActivator.logInfo(null);
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#log(java.lang.Throwable)}
     * .
     */
    @Test
    public void testLogThrowable() {
        PerclipseActivator.log(new RuntimeException("MyMessage"));

    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#log(java.lang.Throwable, java.lang.String)}
     * .
     */
    @Test
    public void testLogThrowableString() {
        PerclipseActivator.log(
                new RuntimeException("AnotherMesage"), "SomeText");
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
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getImageDescriptor(java.lang.String)}
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
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getActiveWorkbenchWindow()}
     * .
     */
    @Test
    public void testGetActiveWorkbenchWindow() {
        assertNotNull(PerclipseActivator.getActiveWorkbenchWindow());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getActivePage()}
     * .
     */
    @Test
    public void testGetActivePage() {
        assertNotNull(PerclipseActivator.getActivePage());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getModel()}.
     */
    @Test
    public void testGetModel() {
        assertNotNull(PerclipseActivator.getModel());

    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getBenchView()}
     * .
     */
    @Test
    public void testGetBenchView() {
        assertNull(activator.getBenchView());
        assertNull(PerclipseActivator.getDefault().getBenchView());
        utilClass.setViewForTesting();
        assertNotNull(PerclipseActivator.getDefault().getBenchView());
        utilClass.setViewNull();
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.PerclipseActivator#getWorkspace()}
     * .
     */
    @Test
    public void testGetWorkspace() {
        assertNotNull(activator.getWorkspace());
        assertNotNull(PerclipseActivator.getDefault().getWorkspace());
    }

}
