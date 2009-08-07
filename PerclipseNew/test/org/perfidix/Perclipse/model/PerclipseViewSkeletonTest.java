package org.perfidix.Perclipse.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.model.PerclipseViewSkeleton}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseViewSkeletonTest {
    
    private PerclipseViewSkeleton skeleton;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        skeleton=new PerclipseViewSkeleton(8989);
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        skeleton=null;
    }
    
    /**
     * Tests the method
     * {@link org.perfidix.Perclipse.model.PerclipseViewSkeleton#PerclipseViewSkeleton(int)}
     * .
     */
    @Test
    public void testPerclipseViewSkeleton(){
        
    }
}
