package org.perfidix.Perclipse.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.Perclipse.launcher.PerclipseActivator;

/**
 * This is the TestClass for {@link org.perfidix.Perclipse.model.BenchModel}
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
        model = PerclipseActivator.getDefault().getModel();
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * This method test the start() method :
     * {@link org.perfidix.Perclipse.model.BenchModel#start()}.
     */
    @Test
    public void testStart() {

        model.start();

    }

    /**
     * This method test the stop() method :
     * {@link org.perfidix.Perclipse.model.BenchModel#stop()}.
     */
    @Test
    public void testStop() {

        model.stop();
        model=null;

    }

}
