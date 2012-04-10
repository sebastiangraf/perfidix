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
