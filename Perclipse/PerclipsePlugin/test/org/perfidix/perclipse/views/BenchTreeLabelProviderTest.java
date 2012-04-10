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
package org.perfidix.perclipse.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchTreeLabelProviderTest {
    private transient BenchTreeLabelProvider labelProvider;
    private transient TreeDataProvider dataProvider;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        labelProvider = new BenchTreeLabelProvider();
        dataProvider = new TreeDataProvider("package.Class.element", 99, 54);
    }

    // /**
    // * Simple tearDown - method.
    // *
    // * @throws java.lang.Exception
    // * The Exception occurred.
    // */
    // @After
    // public void tearDown() throws Exception {
    // labelProvider = null;
    // dataProvider = null;
    // }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider#getImage(Object)}
     * .
     */
    @Test
    public void testGetImage() {
        // Currently images are not specified for the treeviewer
        assertNull("Test if not existing image is null", labelProvider
                .getImage(null));
        assertNull("Test if image is null", labelProvider
                .getImage(dataProvider));
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider#getText(Object)}
     * .
     */
    @Test
    public void testGetText() {
        assertNull("Test if the text is null", labelProvider.getText(null));
        assertEquals(
                "test if label provider is equal to the given class Class",
                "package.Class.element  (54/99)", labelProvider
                        .getText(dataProvider));
        dataProvider.updateCurrentBenchError(2);
        assertEquals(
                "Test if label provider with errors is equal too",
                "package.Class.element  (54/99) Errors: 2", labelProvider
                        .getText(dataProvider));

    }
}
