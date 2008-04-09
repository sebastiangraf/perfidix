/*
 * Copyright 2007 University of Konstanz
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
 * $Id: SwitcherTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * tests that the switcher works as expected.
 * 
 * @author onea
 */
public class SwitcherTest extends PerfidixTest {

    private IRandomizer.Switcher theSwitcher;

    private Method aMethod;

    /**
     * sets up the switcher.
     * 
     * @throws Exception
     *                 if the parent threw one.
     */
    @Before
    public void setUp() throws Exception {
        theSwitcher = new IRandomizer.Switcher();
        initMethod();
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        theSwitcher = null;
        aMethod = null;
        super.tearDown();
    }

    private void initMethod() {
        aMethod = this.getClass().getMethods()[0];
    }

    /**
     * Test method for 'Switcher.shouldRun(Method)'.
     */
    @Test
    public void testShouldRun() {
        assertTrue(theSwitcher.shouldRun(aMethod));
        assertFalse(theSwitcher.shouldRun(aMethod));
        assertTrue(theSwitcher.shouldRun(aMethod));
    }

    /**
     * checks that the switcher ignores any parameters given.
     */
    @Test
    public void testShouldRunIgnoreParameters() {
        assertTrue(theSwitcher.shouldRun(null));
        assertFalse(theSwitcher.shouldRun(null));
        assertTrue(theSwitcher.shouldRun(null));
    }

}
