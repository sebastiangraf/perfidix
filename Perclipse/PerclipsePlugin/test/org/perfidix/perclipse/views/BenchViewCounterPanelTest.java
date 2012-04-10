/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
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
