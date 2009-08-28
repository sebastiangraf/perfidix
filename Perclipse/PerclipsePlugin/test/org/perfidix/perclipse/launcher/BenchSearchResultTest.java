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
package org.perfidix.perclipse.launcher;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.IType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.launcher.BenchSearchResult;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.launcher.BenchSearchResult}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchSearchResultTest {
    private BenchSearchResult searchResult;
    private IType[] typeArray;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        typeArray = new IType[1];
        typeArray[0] = null;
        searchResult = new BenchSearchResult(typeArray);
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        typeArray = null;
        searchResult = null;

    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.BenchSearchResult#getTypes()}.
     */
    @Test
    public void testGetTypes() {
        assertArrayEquals(typeArray, searchResult.getTypes());
        assertNotNull(searchResult.getTypes());
    }

    /**
     * Test method for
     * {@link org.perfidix.perclipse.launcher.BenchSearchResult#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        assertFalse(searchResult.isEmpty());
        typeArray = new IType[0];
        searchResult = new BenchSearchResult(typeArray);
        assertTrue(searchResult.isEmpty());

    }

    /**
     * A DummyClass for Testing
     * 
     * @author Lukas Lewandowski
     */
    public class BenchClass {
        private int additionsErgebnis = 0;

        /**
         * A dummy add method.
         */
        public void benchMethod() {
            int zahl1 = 5;
            int zahl2 = 7;
            additionsErgebnis = zahl1 + zahl2;
        }

        /**
         * @return The result of an add function.
         */
        public int getErgebnis() {
            return additionsErgebnis;
        }
    }

}
