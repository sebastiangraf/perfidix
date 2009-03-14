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
 * Testcase for TimeMeter.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class TimeMeterTest {

    /**
     * Instance for nano seconds.
     */
    private TimeMeter nano;

    /**
     * Instance for milli seconds.
     */
    private TimeMeter milli;

    /**
     * Instance for seconds.
     */
    private TimeMeter second;

    /**
     * Instance for minutes.
     */
    private TimeMeter minute;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     *             for any Kind
     */
    @Before
    public final void setUp() throws Exception {
        nano = new TimeMeter(Time.NanoSeconds);
        milli = new TimeMeter(Time.MilliSeconds);
        second = new TimeMeter(Time.Seconds);
        minute = new TimeMeter(Time.Minutes);
    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     *             for any Kind
     */
    @After
    public final void tearDown() throws Exception {
        nano = null;
        milli = null;
        second = null;
        minute = null;
    }

    /**
     * Test method for {@link org.perfidix.meter.TimeMeter#getValue()}.
     * 
     * @throws Exception
     *             of any kind
     */
    @Test
    public final void testGetValue() throws Exception {
        final double dataNano1 = nano.getValue();
        final double dataMilli1 = milli.getValue();
        final double dataSecond1 = second.getValue();
        final double dataMinute1 = minute.getValue();

        final double dataNano2 = nano.getValue() - dataNano1;
        final double dataMilli2 = milli.getValue() - dataMilli1;
        final double dataSecond2 = second.getValue() - dataSecond1;
        final double dataMinute2 = minute.getValue() - dataMinute1;

        assertTrue(dataNano2 > 0);
        assertTrue(dataMilli2 > 0);
        assertTrue(dataSecond2 > 0);
        assertTrue(dataMinute2 > 0);
    }

    /**
     * Test method for {@link org.perfidix.meter.TimeMeter#getUnit()}.
     */
    @Test
    public final void testGetUnit() {
        assertEquals(Time.NanoSeconds.getUnit(), nano.getUnit());
        assertEquals(Time.MilliSeconds.getUnit(), milli.getUnit());
        assertEquals(Time.Seconds.getUnit(), second.getUnit());
        assertEquals(Time.Minutes.getUnit(), minute.getUnit());
    }

    /**
     * Test method for {@link org.perfidix.meter.TimeMeter#getUnitDescription()}
     * .
     */
    @Test
    public final void testGetDescription() {
        assertEquals(Time.NanoSeconds.getUnitDescription(), nano
                .getUnitDescription());
        assertEquals(Time.MilliSeconds.getUnitDescription(), milli
                .getUnitDescription());
        assertEquals(Time.Seconds.getUnitDescription(), second
                .getUnitDescription());
        assertEquals(Time.Minutes.getUnitDescription(), minute
                .getUnitDescription());

    }

}
