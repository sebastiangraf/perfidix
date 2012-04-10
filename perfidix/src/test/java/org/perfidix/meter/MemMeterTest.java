/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

        assertTrue("Data check for byte", dataB1 > EPSILON / Memory.Byte.getNumberOfBytes());
        assertTrue("Data check for KibiByte", dataKB1 > EPSILON / Memory.KibiByte.getNumberOfBytes());
        assertTrue("Data check for MebiByte", dataMB1 > EPSILON / Memory.Mebibyte.getNumberOfBytes());
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getUnit()}.
     */
    @Test
    public void testGetUnit() {
        assertEquals("Data check for unit for byte", Memory.Byte.getUnit(), byteMeter.getUnit());
        assertEquals("Data check for unit for kibiByte", Memory.KibiByte.getUnit(), kibiByteMeter.getUnit());
        assertEquals("Data check for unit for mebiByte", Memory.Mebibyte.getUnit(), mebiByteMeter.getUnit());
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getUnitDescription()}.
     */
    @Test
    public void testGetDescription() {
        assertEquals("Data check for describtion for byte", Memory.Byte.getUnitDescription(), byteMeter
            .getUnitDescription());
        assertEquals("Data check for describtion for kibiByte", Memory.KibiByte.getUnitDescription(),
            kibiByteMeter.getUnitDescription());
        assertEquals("Data check for describtion for mebiByte", Memory.Mebibyte.getUnitDescription(),
            mebiByteMeter.getUnitDescription());

    }
}
