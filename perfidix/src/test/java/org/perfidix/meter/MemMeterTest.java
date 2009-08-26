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

import org.junit.Before;
import org.junit.Test;

/**
 * Testcase for MemMeter.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class MemMeterTest {

    /**
     * Constant for epsilon for testCases. Memory can change...
     */
    private static final double EPSILON = 16384;

    /**
     * Byte meter variable.
     */
    private transient MemMeter byteMeter;

    /**
     * KibiByte meter variable.
     */
    private transient MemMeter kibiByteMeter;

    /**
     * MebiByte meter variable.
     */
    private transient MemMeter mebiByteMeter;

    /**
     * Simple setUp.
     */
    @Before
    public void setUp() {
        byteMeter = new MemMeter(Memory.Byte);
        kibiByteMeter = new MemMeter(Memory.KibiByte);
        mebiByteMeter = new MemMeter(Memory.Mebibyte);
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getValue()}.
     */
    @Test
    public void testGetValue() {
        final double dataB1 = byteMeter.getValue();
        final double dataKB1 = kibiByteMeter.getValue();
        final double dataMB1 = mebiByteMeter.getValue();

        assertTrue("Data check for byte", dataB1 > EPSILON
                / Memory.Byte.getNumberOfBytes());
        assertTrue("Data check for KibiByte", dataKB1 > EPSILON
                / Memory.KibiByte.getNumberOfBytes());
        assertTrue("Data check for MebiByte", dataMB1 > EPSILON
                / Memory.Mebibyte.getNumberOfBytes());
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getUnit()}.
     */
    @Test
    public void testGetUnit() {
        assertEquals(
                "Data check for unit for byte", Memory.Byte.getUnit(),
                byteMeter.getUnit());
        assertEquals("Data check for unit for kibiByte", Memory.KibiByte
                .getUnit(), kibiByteMeter.getUnit());
        assertEquals("Data check for unit for mebiByte", Memory.Mebibyte
                .getUnit(), mebiByteMeter.getUnit());
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getUnitDescription()}.
     */
    @Test
    public void testGetDescription() {
        assertEquals("Data check for describtion for byte", Memory.Byte
                .getUnitDescription(), byteMeter.getUnitDescription());
        assertEquals("Data check for describtion for kibiByte", Memory.KibiByte
                .getUnitDescription(), kibiByteMeter.getUnitDescription());
        assertEquals("Data check for describtion for mebiByte", Memory.Mebibyte
                .getUnitDescription(), mebiByteMeter.getUnitDescription());

    }
}
