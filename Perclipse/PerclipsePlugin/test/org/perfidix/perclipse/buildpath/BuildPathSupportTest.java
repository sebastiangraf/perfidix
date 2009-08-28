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
package org.perfidix.perclipse.buildpath;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.buildpath.BuildPathSupport;
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.buildpath.BuildPathSupport}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BuildPathSupportTest {

    private PerclipseActivator activator;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
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
        activator = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.buildpath.BuildPathSupport#getPerfidixClasspathEntry()}
     * .
     */
    @Test
    public void testGetPerfidixClasspathEntry() {
        assertNotNull(BuildPathSupport.getPerfidixClasspathEntry());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.buildpath.BuildPathSupport#getBundleLocation()}
     * .
     */
    @Test
    public void testGetBundleLocation() {
        assertNotNull(BuildPathSupport.getBundleLocation());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.buildpath.BuildPathSupport#getPerfidixLibraryEntry()}
     * .
     */
    @Test
    public void testGetPerfidixLibraryEntry() {
        assertNotNull(BuildPathSupport.getPerfidixLibraryEntry());
    }

}
