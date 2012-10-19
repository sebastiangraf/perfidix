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

import java.io.File;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * Testcase for FileMeter.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class FileMeterTest {

    /**
     * Seed for random testing.
     */
    private static final long SEED = 1234;

    /** Standard Random Generator. */
    private static final Random RAN = new Random(SEED);
    /**
     * Byte meter variable.
     */
    private transient FileMeter byteMeter;

    /**
     * KibiByte meter variable.
     */
    private transient FileMeter kibiByteMeter;

    /**
     * MebiByte meter variable.
     */
    private transient FileMeter mebiByteMeter;

    /** Variable to store the size while creation. */
    private transient long size;

    /**
     * Setting up the meter.
     */
    @Before
    public void setUp() {
        final File file = Files.createTempDir();
        byteMeter = new FileMeter(file, Memory.Byte);
        kibiByteMeter = new FileMeter(file, Memory.KibiByte);
        mebiByteMeter = new FileMeter(file, Memory.Mebibyte);
        initializeStorage();
    }

    /**
     * Test method for {@link org.perfidix.meter.FileMeter#getValue()}.
     */
    @Test
    public void testGetValue() {
        final double dataB1 = byteMeter.getValue();
        final double dataKB1 = kibiByteMeter.getValue();
        final double dataMB1 = mebiByteMeter.getValue();

        assertTrue("Data check for byte", dataB1 == size / Memory.Byte.getNumberOfBytes());
        assertTrue("Data check for KibiByte", dataKB1 == size / Memory.KibiByte.getNumberOfBytes());
        assertTrue("Data check for MebiByte", dataMB1 == size / Memory.Mebibyte.getNumberOfBytes());
    }

    /**
     * Test method for {@link org.perfidix.meter.FileMeter#getUnit()}.
     */
    @Test
    public void testGetUnit() {
        assertEquals("Data check for unit for byte", Memory.Byte.getUnit(), byteMeter.getUnit());
        assertEquals("Data check for unit for kibiByte", Memory.KibiByte.getUnit(), kibiByteMeter.getUnit());
        assertEquals("Data check for unit for mebiByte", Memory.Mebibyte.getUnit(), mebiByteMeter.getUnit());
    }

    /**
     * Test method for {@link org.perfidix.meter.FileMeter#getUnitDescription()}.
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

    /**
     * Method for initializing the storage for testing.
     * 
     * @return long regarding the fileserver.
     */
    private long initializeStorage() {
        
        
        
        return 0;
    }

}
