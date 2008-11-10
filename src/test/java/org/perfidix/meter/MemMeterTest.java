/*
 * Copyright 2008 Distributed Systems Group, University of Konstanz
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testcase for MemMeter.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class MemMeterTest {

    /**
     * Constant for epsilon for testCases. Memory can change...
     */
    private static final double EPSILON = 16384d;

    /**
     * Factor for different size levels for Kibibytes, Mebibytes, etc.
     */
    private static final double SIZEFAKTOR = 3;

    /**
     * Byte meter variable.
     */
    private MemMeter byteMeter;

    /**
     * KibiByte meter variable.
     */
    private MemMeter kibiByteMeter;

    /**
     * MebiByte meter variable.
     */
    private MemMeter mebiByteMeter;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     *             of any kind
     */
    @Before
    public final void setUp() throws Exception {
        byteMeter = new MemMeter(Memory.Byte);
        kibiByteMeter = new MemMeter(Memory.KibiByte);
        mebiByteMeter = new MemMeter(Memory.Mebibyte);
    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     *             of any kind
     */
    @After
    public final void tearDown() throws Exception {
        byteMeter = null;
        kibiByteMeter = null;
        mebiByteMeter = null;
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getValue()}.
     */
    @Test
    public final void testGetValue() {
        final long dataB1 = byteMeter.getValue();
        final long dataKB1 = kibiByteMeter.getValue();
        final long dataMB1 = mebiByteMeter.getValue();

        assertTrue(dataB1 >= (Math.round(EPSILON
                / Memory.KibiByte.getNumberOfBytes())));
        assertTrue(dataKB1 >= (Math.round(EPSILON
                / Memory.KibiByte.getNumberOfBytes())));
        assertTrue(dataMB1 >= (Math.round(EPSILON
                / Memory.Mebibyte.getNumberOfBytes())));
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getUnit()}.
     */
    @Test
    public final void testGetUnit() {
        assertEquals(Memory.Byte.getUnit(), byteMeter.getUnit());
        assertEquals(Memory.KibiByte.getUnit(), kibiByteMeter.getUnit());
        assertEquals(Memory.Mebibyte.getUnit(), mebiByteMeter.getUnit());
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getUnitDescription()}.
     */
    @Test
    public final void testGetDescription() {
        assertEquals(Memory.Byte.getUnitDescription(), byteMeter
                .getUnitDescription());
        assertEquals(Memory.KibiByte.getUnitDescription(), kibiByteMeter
                .getUnitDescription());
        assertEquals(Memory.Mebibyte.getUnitDescription(), mebiByteMeter
                .getUnitDescription());

    }
}
