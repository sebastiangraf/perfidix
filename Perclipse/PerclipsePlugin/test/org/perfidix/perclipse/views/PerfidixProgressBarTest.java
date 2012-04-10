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

import org.eclipse.swt.graphics.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.view.PerfidixProgressBar}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerfidixProgressBarTest {
    private transient PerfidixProgressBar progressBar;
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
        progressBar = view.getProgressBar();

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
        // progressBar = null;
        // view = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerfidixProgressBar#PerfidixProgressBar(org.eclipse.swt.widgets.Composite)}
     * .
     */
    @Test
    public void testPerfidixProgressBar() {
        assertNotNull("Test if progress bar is not null", progressBar);
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerfidixProgressBar#setMaximum(int)}.
     */
    @Test
    public void testSetMaximum() { // NOPMD by lewandow on 8/31/09 4:24 PM
        progressBar.setMaximum(2555);
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerfidixProgressBar#reset()}.
     */
    @Test
    public void testReset() { // NOPMD by lewandow on 8/31/09 4:24 PM
        progressBar.reset();
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerfidixProgressBar#reset(boolean, boolean, int, int)}
     * .
     */
    @Test
    public void testResetBooleanBooleanIntInt() { // NOPMD by lewandow on
                                                  // 8/31/09 4:24 PM
        progressBar.reset(true, false, 50, 100);
        progressBar.reset(false, false, 33, 12);
        progressBar.reset(false, true, 99, 99);

    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerfidixProgressBar#stopped()}.
     */
    @Test
    public void testStopped() { // NOPMD by lewandow on 8/31/09 4:24 PM
        progressBar.stopped();
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerfidixProgressBar#computeSize(int, int, boolean)}
     * .
     */
    @Test
    public void testComputeSizeIntIntBoolean() {
        final Point point = new Point(22, 55);
        assertEquals(
                "Test if a created point is equals to the progress bar point",
                point, progressBar.computeSize(22, 55, true));
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerfidixProgressBar#step(int)}.
     */
    @Test
    public void testStep() { // NOPMD by lewandow on 8/31/09 4:24 PM
        progressBar.step(22);
        progressBar.reset(true, true, 55, 55);
        progressBar.step(22);
        progressBar.reset(true, true, 54, 55);
        progressBar.step(22);
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.views.PerfidixProgressBar#refresh(boolean)}
     * .
     */
    @Test
    public void testRefresh() { // NOPMD by lewandow on 8/31/09 4:24 PM
        progressBar.refresh(true);
        progressBar.refresh(false);
    }

}
