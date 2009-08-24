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
 * Testcase for TimeMeter.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class TimeMeterTest {

    /**
     * Instance for nano seconds.
     */
    private transient TimeMeter nano;

    /**
     * Instance for milli seconds.
     */
    private transient TimeMeter milli;

    /**
     * Instance for seconds.
     */
    private transient TimeMeter second;

    /**
     * Instance for minutes.
     */
    private transient TimeMeter minute;

    /**
     * Simple setUp.
     */
    @Before
    public void setUp() {
        nano = new TimeMeter(Time.NanoSeconds);
        milli = new TimeMeter(Time.MilliSeconds);
        second = new TimeMeter(Time.Seconds);
        minute = new TimeMeter(Time.Minutes);
    }

    /**
     * Test method for {@link org.perfidix.meter.TimeMeter#getValue()}.
     */
    @Test
    public void testGetValue() {
        final double dataNano1 = nano.getValue();
        final double dataMilli1 = milli.getValue();
        final double dataSecond1 = second.getValue();
        final double dataMinute1 = minute.getValue();

        final double dataNano2 = nano.getValue() - dataNano1;
        final double dataMilli2 = milli.getValue() - dataMilli1;
        final double dataSecond2 = second.getValue() - dataSecond1;
        final double dataMinute2 = minute.getValue() - dataMinute1;

        assertTrue("nanovalue has to be larger than 0", dataNano2 > 0);
        assertTrue("millivalue has to be larger than 0", dataMilli2 > 0);
        assertTrue("secondvalue has to be larger than 0", dataSecond2 > 0);
        assertTrue("minutevalue has to be larger than 0", dataMinute2 > 0);
    }

    /**
     * Test method for {@link org.perfidix.meter.TimeMeter#getUnit()}.
     */
    @Test
    public void testGetUnit() {
        assertEquals("Unit for nanos", Time.NanoSeconds.getUnit(), nano
                .getUnit());
        assertEquals("Unit for millis", Time.MilliSeconds.getUnit(), milli
                .getUnit());
        assertEquals("Unit for seconds", Time.Seconds.getUnit(), second
                .getUnit());
        assertEquals("Unit for minutes", Time.Minutes.getUnit(), minute
                .getUnit());
    }

    /**
     * Test method for {@link org.perfidix.meter.TimeMeter#getUnitDescription()}
     * .
     */
    @Test
    public void testGetDescription() {
        assertEquals("Description for nanos", Time.NanoSeconds
                .getUnitDescription(), nano.getUnitDescription());
        assertEquals("Description for millis", Time.MilliSeconds
                .getUnitDescription(), milli.getUnitDescription());
        assertEquals("Description for seconds", Time.Seconds
                .getUnitDescription(), second.getUnitDescription());
        assertEquals("Description for minutes", Time.Minutes
                .getUnitDescription(), minute.getUnitDescription());

    }

}
