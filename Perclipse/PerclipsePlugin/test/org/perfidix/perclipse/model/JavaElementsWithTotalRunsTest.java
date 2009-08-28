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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.JavaElementsWithTotalRuns}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class JavaElementsWithTotalRunsTest {

    private JavaElementsWithTotalRuns elements;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        elements = new JavaElementsWithTotalRuns("TheObject", 555);
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        elements = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.JavaElementsWithTotalRuns#JavaElementsWithTotalRuns(String, int)}
     * .
     */
    @Test
    public void testJavaElementsWithTotalRuns() {
        assertEquals(0, elements.getCurrentRun());
        assertEquals(0, elements.getErrorCount());
        assertEquals(555, elements.getTotalRuns());
        assertEquals("TheObject", elements.getJavaElement());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.JavaElementsWithTotalRuns#updateCurrentRun()}
     * .
     */
    @Test
    public void testUpdateCurrentRun() {
        assertEquals(0, elements.getCurrentRun());
        elements.updateCurrentRun();
        assertEquals(1, elements.getCurrentRun());
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.JavaElementsWithTotalRuns#updateErrorCount()}
     * .
     */
    @Test
    public void testUpdateErrorCount() {
        assertEquals(0, elements.getErrorCount());
        elements.updateErrorCount();
        assertEquals(1, elements.getErrorCount());
    }
}
