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
