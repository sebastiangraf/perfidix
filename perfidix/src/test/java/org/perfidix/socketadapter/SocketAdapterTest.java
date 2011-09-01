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
