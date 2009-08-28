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

import org.eclipse.swt.widgets.Composite;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
    private TestUtilClass utilClass;
    private BenchView view;
    private BenchViewer viewer;
    private BenchRunSession runSession;
    private List<JavaElementsWithTotalRuns> elementsList;

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
        viewer = view.getBenchViewer();
        runSession = new BenchRunSession();
        elementsList = null;
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
        utilClass = null;
        view = null;
        viewer = null;
        runSession = null;
        elementsList = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchViewer#BenchViewer(Composite, BenchView)}
     * .
     */
    @Test
    public void testBenchViewer() {
        assertNotNull(viewer);
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchViewer#processChangesInUI(org.perfidix.perclipse.model.BenchRunSession)}
     * .
     */
    @Test
    public void testProcessChangesInUI() {
        elementsList = new ArrayList<JavaElementsWithTotalRuns>();
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
        JavaElementsWithTotalRuns element =
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
    @Ignore // Has to be fixed. Tree has to be initialized.
    @Test
    public void testSelectionsInTree() {
        viewer.handleSelected();
        viewer.handleDefaultSelected();
    }

}
