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
