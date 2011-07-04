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
package org.perfidix.socketadapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.socketadapter.SocketAdapter}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class SocketAdapterTest {
    private transient PerclipseViewSkeletonSimulator skeletonSimulator = null;

    /**
     * Simple setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        skeletonSimulator = new PerclipseViewSkeletonSimulator(6777);
        skeletonSimulator.start();
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketAdapter#main(java.lang.String[])}
     * .
     * 
     * @throws InterruptedException
     *             Exception occurred.
     */
    @Test
    public void testMain() throws InterruptedException {
        final String[] args =
                {
                        "org.perfidix.example.Config",
                        "org.perfidix.example.StackBenchmark",
                        "org.perfidix.socketadapter.BenchWithException",
                        "-Port", "6777" };
        SocketAdapter.main(args);
        assertNotNull("Dummy", skeletonSimulator);
        Thread.sleep(10);
        assertEquals("Test if benchcounts are right", 100, skeletonSimulator
                .getElements()
                .get("org.perfidix.example.StackBenchmark.benchNormalIntPush")
                .getCurrentRun());
        Thread.sleep(10);
    }

}
