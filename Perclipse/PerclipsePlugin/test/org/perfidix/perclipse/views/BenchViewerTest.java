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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.model.BenchRunSession;
import org.perfidix.perclipse.model.JavaElementsWithTotalRuns;
import org.perfidix.perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.views.BenchViewer}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewerTest {
    private transient TestUtilClass utilClass;
    private transient BenchViewer viewer;
    private transient BenchRunSession runSession;

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
        viewer = view.getBenchViewer();
        runSession = new BenchRunSession();
        // elementsList = null;
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
        // viewer = null;
        // runSession = null;
        // elementsList = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchViewer#BenchViewer(Composite, BenchView)}
     * .
     */
    @Test
    public void testBenchViewer() {
        assertNotNull("Tests if viewer is not null", viewer);
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchViewer#processChangesInUI(org.perfidix.perclipse.model.BenchRunSession)}
     * .
     */
    @Test
    public void testProcessChangesInUI() { // NOPMD by lewandow on 8/31/09 4:19
                                           // PM
        List<JavaElementsWithTotalRuns> elementsList =
                new ArrayList<JavaElementsWithTotalRuns>();
        elementsList.add(new JavaElementsWithTotalRuns(
                "org.packaging.ClassA.methodA", 55));
        elementsList.add(new JavaElementsWithTotalRuns(
                "orog.packaging.ClassB.methodB", 88));
        runSession.setBenchRunSession(143, elementsList);
        viewer.processChangesInUI(runSession);
        viewer.processChangesInUI(runSession);
        elementsList = new ArrayList<JavaElementsWithTotalRuns>();
        elementsList.add(new JavaElementsWithTotalRuns(
                "org.packaging.ClassA.methodA", 55));
        final JavaElementsWithTotalRuns element =
                new JavaElementsWithTotalRuns(
                        "orog.packaging.ClassB.methodB", 88);
        element.updateCurrentRun();
        elementsList.add(element);
        runSession.setBenchRunSession(143, elementsList);
        viewer.processChangesInUI(runSession);
        viewer.processChangesInUI(null);
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchViewer#handleSelected()},
     * {@link org.perfidix.perclipse.views.BenchViewer#handleDefaultSelected()
     * }
     * .
     */
    @Test
    public void testSelectionsInTree() { // NOPMD by lewandow on 8/31/09 4:19 PM
        viewer.handleSelected();
        viewer.handleDefaultSelected();
    }

}
