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
 * $Revision$
 * $Author$
 * $Date$
 *
 */
package org.perfidix.meter;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sebastian Graf, University of Konstanz
 */
public final class ThreadMeterTest {

    private ThreadMeter meter;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     *             for any Kind
     */
    @Before
    public final void setUp() throws Exception {
        meter = new ThreadMeter();
    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     *             for any Kind
     */
    @After
    public final void tearDown() throws Exception {
        meter = null;
    }

    /**
     * Test method for {@link org.perfidix.meter.ThreadMeter#getValue()}.
     * 
     * @throws Exception
     *             of any kind
     */
    @Test
    public final void testGetValue() throws Exception {
        final double threadMeter = meter.getValue();
        assertTrue(threadMeter >= 4);

    }

}
