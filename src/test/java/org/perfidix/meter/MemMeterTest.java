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
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Random;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.meter.MemMeter.Memory;

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
     * Meter variable.
     */
    private MemMeter meter;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     *             of any kind
     */
    @Before
    public final void setUp() throws Exception {
        meter = new MemMeter();
    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     *             of any kind
     */
    @After
    public final void tearDown() throws Exception {
        meter = null;
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getValue()}.
     */
    @Test
    public final void testGetValue() {
        final long value = meter.getValue();
        if (value < 0) {
            fail("Meter should be greater than 0 at the beginning");
        } else {
            final long value2 = meter.getValue() - value;
            if (Math.abs(value2 - value) > EPSILON) {
                fail("Difference "
                        + Math.abs(value2 - value)
                        + " bigger and therefore bigger than EPSILON.");
            }
        }
    }

    /**
     * Test method for {@link org.perfidix.meter.MemMeter#getUnit()} and
     * {@link org.perfidix.meter.MemMeter#getUnitDescription()}.
     */
    @Test
    public final void testGetUnitAndDescription() {
        final Collection<Long> datas = new Vector<Long>(0);
        final long dataB1 = meter.getValue();
        final long dataB2 = meter.getValue() - dataB1;
        assertTrue(Math.abs(dataB2 - dataB1) < Memory.Mebibyte
                .getNumberOfBytes());
        assertEquals(Memory.KibiByte.getUnit(), meter.getUnit());
        assertEquals(Memory.KibiByte.getUnitDescription(), meter
                .getUnitDescription());
        for (int i = 0; i < EPSILON * SIZEFAKTOR; i++) {
            datas.add(new Random().nextLong());
        }
        final long dataB3 = meter.getValue() - dataB2;
        assertTrue(Math.abs(dataB3 - dataB2) >= Memory.Mebibyte
                .getNumberOfBytes());
        assertEquals(Memory.Mebibyte.getUnit(), meter.getUnit());
        assertEquals(Memory.Mebibyte.getUnitDescription(), meter
                .getUnitDescription());
    }
}
