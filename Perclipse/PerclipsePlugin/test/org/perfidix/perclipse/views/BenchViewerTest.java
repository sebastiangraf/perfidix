package org.perfidix.perclipse.views;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.util.TestUtilClass;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.model.BenchRunSession;
import org.perfidix.perclipse.model.JavaElementsWithTotalRuns;
import org.perfidix.perclipse.views.BenchView;
import org.perfidix.perclipse.views.BenchViewer;

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
    @Test
    public void testSelectionsInTree() {
        viewer.handleSelected();
        viewer.handleDefaultSelected();
    }

}
