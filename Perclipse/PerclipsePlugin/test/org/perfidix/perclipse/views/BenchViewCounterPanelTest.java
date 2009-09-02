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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.views.BenchViewCounterPanel}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewCounterPanelTest {
    private transient BenchViewCounterPanel counterPanel;
    private transient TestUtilClass utilClass;

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
        final BenchView view = PerclipseActivator.getDefault().getBenchView();
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
        // utilClass = null;
        // view = null;
        // counterPanel = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchViewCounterPanel#BenchViewCounterPanel(org.eclipse.swt.widgets.Composite)
     * }
     * .
     */
    @Test
    public void testBenchViewCounterPanel() {
        assertNotNull("Tests if counter panel is not null", counterPanel);
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
        assertEquals("Tests if total runs is 55", 55, counterPanel
                .getTotalRuns());
        counterPanel.resetRuns();
        assertEquals("Tests if total runs is 0", 0, counterPanel.getTotalRuns());
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
        assertEquals("Tests if total runs is 555", 555, counterPanel
                .getTotalRuns());

    }
    
}
