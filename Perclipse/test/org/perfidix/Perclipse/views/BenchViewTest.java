package org.perfidix.Perclipse.views;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.model.BenchRunSession;
import org.perfidix.Perclipse.model.JavaElementsWithTotalRuns;
import org.perfidix.Perclipse.util.TestUtilClass;
import org.perfidix.Perclipse.viewTreeData.TreeDataProvider;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.views.BenchView}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewTest {
    private BenchView view;
    private TestUtilClass utilClass;
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
        elementsList = new ArrayList<JavaElementsWithTotalRuns>();
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
        utilClass = null;
        view = null;
        elementsList = null;
        runSession = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.views.BenchView#BenchView()} .
     */
    @Test
    public void testBenchView() {
        view = new BenchView();
        assertNotNull(view);
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.views.BenchView#createImage(String)} .
     */
    @Test
    public void testCreateImage() {
        assertNotNull(BenchView.createImage("icons/time.png"));
        assertNull(BenchView.createImage("icons/timme.png"));
        assertNull(BenchView.createImage(null));
    }

    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.views.BenchView#startUpdateJobs(org.perfidix.Perclipse.model.BenchRunSession)}
     * .
     */
    @Test
    public void testStartUpdateJobs() {
        view.startUpdateJobs(null);
        view.startUpdateJobs(runSession);
    }
    
    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.views.BenchView#handleBenchSelection(org.perfidix.Perclipse.viewTreeData.TreeDataProvider)}
     * .
     */
    @Test
    public void testHandleBenchSelection(){
        view.handleBenchSelection(null);
        view.handleBenchSelection(new TreeDataProvider("SomeElement",55, 22));
    }

}
