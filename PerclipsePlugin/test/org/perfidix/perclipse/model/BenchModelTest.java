package org.perfidix.perclipse.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.model.BenchModel;

/**
 * This is the TestClass for {@link org.perfidix.perclipse.model.BenchModel}
 * 
 * @author Lewandowski L., DiSy, University of Konstanz
 */
public class BenchModelTest {

    private BenchModel model;

    /**
     * Simple setUp method.
     * 
     * @throws Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        PerclipseActivator.getDefault();
        model = PerclipseActivator.getModel();
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * This method test the start() method :
     * {@link org.perfidix.perclipse.model.BenchModel#start()}.
     */
    @Test
    public void testStart() {

        model.start();

    }

    /**
     * This method test the stop() method :
     * {@link org.perfidix.perclipse.model.BenchModel#stop()}.
     */
    @Test
    public void testStop() {

        model.stop();
        model=null;

    }

}
