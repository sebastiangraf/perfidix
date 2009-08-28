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
