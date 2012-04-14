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
        assertEquals("Unit for nanos", Time.NanoSeconds.getUnit(), nano.getUnit());
        assertEquals("Unit for millis", Time.MilliSeconds.getUnit(), milli.getUnit());
        assertEquals("Unit for seconds", Time.Seconds.getUnit(), second.getUnit());
        assertEquals("Unit for minutes", Time.Minutes.getUnit(), minute.getUnit());
    }

    /**
     * Test method for {@link org.perfidix.meter.TimeMeter#getUnitDescription()} .
     */
    @Test
    public void testGetDescription() {
        assertEquals("Description for nanos", Time.NanoSeconds.getUnitDescription(), nano
            .getUnitDescription());
        assertEquals("Description for millis", Time.MilliSeconds.getUnitDescription(), milli
            .getUnitDescription());
        assertEquals("Description for seconds", Time.Seconds.getUnitDescription(), second
            .getUnitDescription());
        assertEquals("Description for minutes", Time.Minutes.getUnitDescription(), minute
            .getUnitDescription());

    }

}
