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
package org.perfidix.perclipse.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.buildpath.BuildPathSupport}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseMainTabTest {
    private transient PerclipseMainTab mainTab;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        mainTab = new PerclipseMainTab();
    }

    // /**
    // * Simple tearDown - method.
    // *
    // * @throws java.lang.Exception
    // * The Exception occurred.
    // */
    // @After
    // public void tearDown() throws Exception {
    // mainTab = null;
    // }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerclipseMainTab#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals(
                "Tests the name of the created main tab.", "Benchs", mainTab
                        .getName());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerclipseMainTab#getImage()}.
     */
    @Test
    public void testGetImage() {
        assertNotNull(
                "Tests if the image of the main tab is not null.", mainTab
                        .getImage());
    }

}
