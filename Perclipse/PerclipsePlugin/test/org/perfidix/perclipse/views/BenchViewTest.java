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
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.views.BenchView}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewTest {
    private transient BenchView view;
    private transient TestUtilClass utilClass;
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
        view = PerclipseActivator.getDefault().getBenchView();
        final List<JavaElementsWithTotalRuns> elementsList =
                new ArrayList<JavaElementsWithTotalRuns>();
        elementsList.add(new JavaElementsWithTotalRuns(
                "mypackage.Class.method", 125));
        elementsList.add(new JavaElementsWithTotalRuns(
                "mypackage.Class.anotherMethod", 85));
        runSession = new BenchRunSession();
        runSession.setBenchRunSession(210, elementsList);
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
        // elementsList = null;
        // runSession = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchView#BenchView()} .
     */
    @Test
    public void testBenchView() {
        view = new BenchView();
        assertNotNull("Tests if view is not null", view);
    }


    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchView#startUpdateJobs(org.perfidix.perclipse.model.BenchRunSession)}
     * .
     */
    @Test
    public void testStartUpdateJobs() { // NOPMD by lewandow on 8/31/09 4:20 PM
        view.startUpdateJobs(null);
        view.startUpdateJobs(runSession);
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchView#handleBenchSelection(org.perfidix.perclipse.viewtreedata.TreeDataProvider)}
     * .
     */
    @Test
    public void testHandleBenchSelection() { // NOPMD by lewandow on 8/31/09
                                             // 4:20 PM
        view.handleBenchSelection(null);
        view.handleBenchSelection(new TreeDataProvider("SomeElement", 55, 22));
    }

}
