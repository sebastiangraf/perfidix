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

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

/**
 * Testcase for CountingMeter.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class CountingMeterTest {

    /**
     * Seed for random testing.
     */
    private static final long SEED = 1234;

    /**
     * Range of the countingMeter.
     */
    private static final int RANGE = Integer.MAX_VALUE / 2;

    /**
     * Instance to test.
     */
    private transient CountingMeter meter;

    /**
     * Setting up the meter.
     */
    @Before
    public void setUp() {
        meter = new CountingMeter();
    }

    /**
     * Test method for ticks and getValue(). Number of ticks must be the same
     * than the storedValue();
     */
    @Test
    public void test() {
        final Random ran = new Random(SEED);
        final int firstRun = Math.abs(ran.nextInt(RANGE));
        final int secondRun = Math.abs(ran.nextInt(RANGE));

        for (int i = 0; i < firstRun; i++) {
            meter.tick();
        }

        assertEquals(meter.getValue(), firstRun, 0);

        for (int i = 0; i < secondRun; i++) {
            meter.tick();
        }

        assertEquals(meter.getValue(), firstRun + secondRun, 0);

    }
}
