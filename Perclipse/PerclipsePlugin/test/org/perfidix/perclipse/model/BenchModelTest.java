/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
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
package org.perfidix.perclipse.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This is the TestClass for {@link org.perfidix.perclipse.model.BenchModel}
 * 
 * @author Lewandowski L., DiSy, University of Konstanz
 */
public class BenchModelTest {

    private transient BenchModel model;

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
     * This method test the start() method :
     * {@link org.perfidix.perclipse.model.BenchModel#start()}.
     */
    @Test
    public void testStart() {
        model.start();
        assertNotNull("Test if exist", model);

    }

    /**
     * This method test the stop() method :
     * {@link org.perfidix.perclipse.model.BenchModel#stop()}.
     */
    @Test
    public void testStop() {
        model.stop();
        assertNotNull("Test if exist and no exception thrown", model);

    }

}
