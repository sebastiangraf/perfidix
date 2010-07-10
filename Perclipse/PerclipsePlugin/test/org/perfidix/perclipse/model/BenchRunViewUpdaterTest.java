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

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.util.TestUtilClass;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.BenchRunViewUpdater}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchRunViewUpdaterTest {

    private transient BenchRunViewUpdater updater;
    private transient BenchRunSession session;
    private transient TestUtilClass utilClass;

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
        final List<JavaElementsWithTotalRuns> theList =
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
        utilClass.setViewNull();
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.BenchRunViewUpdater#updateView(BenchRunSession)}
     * .
     */
    @Test
    public void testUpdateView() { // NOPMD by lewandow on 8/31/09 4:16 PM
        updater.updateView(session);
        utilClass = new TestUtilClass();
        utilClass.setViewForTesting();
        updater.updateView(session);
        session = null; // NOPMD by lewandow on 8/31/09 1:46 PM
        updater.updateView(session);
    }

}
