/**
 * The package is responsible for the model skeletons.
 * */
package org.perfidix.perclipse.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.util.TestUtilClass;
import org.perfidix.perclipse.model.BenchRunSession;
import org.perfidix.perclipse.model.BenchRunViewUpdater;
import org.perfidix.perclipse.model.JavaElementsWithTotalRuns;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.BenchRunViewUpdater}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchRunViewUpdaterTest {

    private BenchRunViewUpdater updater;
    private BenchRunSession session;
    private TestUtilClass utilClass;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {

        updater = new BenchRunViewUpdater();
        session = new BenchRunSession();
        List<JavaElementsWithTotalRuns> theList =
                new ArrayList<JavaElementsWithTotalRuns>();
        theList.add(new JavaElementsWithTotalRuns("MyName", 11));
        theList.add(new JavaElementsWithTotalRuns("AObject", 23));
        session.setBenchRunSession(123, theList);
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        updater = null;
        session = null;
         utilClass.setViewNull();
         utilClass=null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunViewUpdater#updateView(BenchRunSession)}
     * .
     */
    @Test
    public void testUpdateView() {
        updater.updateView(session);
        utilClass = new TestUtilClass();
        utilClass.setViewForTesting();
        updater.updateView(session);
        session = null;
        updater.updateView(session);
    }

}
