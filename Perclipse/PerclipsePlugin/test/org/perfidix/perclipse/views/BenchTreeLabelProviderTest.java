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
package org.perfidix.perclipse.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.perclipse.views.BenchTreeLabelProvider;
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchTreeLabelProviderTest {
    private BenchTreeLabelProvider labelProvider;
    private TreeDataProvider dataProvider;

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

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        labelProvider = null;
        dataProvider = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider#getImage(Object)}
     * .
     */
    @Test
    public void testGetImage() {
        // Currently images are not specified for the treeviewer
        assertNull(labelProvider.getImage(null));
        assertNull(labelProvider.getImage(dataProvider));
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.views.BenchTreeLabelProvider#getText(Object)}
     * .
     */
    @Test
    public void testGetText() {
        assertNull(labelProvider.getText(null));
        assertEquals("package.Class.element  (54/99)", labelProvider
                .getText(dataProvider));
        dataProvider.updateCurrentBenchError(2);
        assertEquals("package.Class.element  (54/99) Errors: 2", labelProvider
                .getText(dataProvider));

    }
}
