/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.meter;


import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Testcase for FileMeter.
 *
 * @author Sebastian Graf, University of Konstanz
 */
public class FileMeterTest {

    /**
     * Standard Random Generator.
     */
    private static final Random RAN = new Random();
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

    /**
     * Variable to store the size while creation.
     */
    private transient long size;

    /**
     * Creating a new file if not existing at the path defined in the config. Note that it is advised to create the file
     * beforehand.
     *
     * @param pConf configuration to be updated
     * @return true if creation successful, false if file already exists.
     * @throws IOException if anything weird happens
     */
    private static synchronized boolean createStorageVolume(final File pToCreate, final long pLength) throws IOException {
        try {
            // if file exists, remove it after questioning.
            if (pToCreate.exists()) {
                if (!pToCreate.delete()) {
                    return false;
                }
            }

            // create file
            final File parent = pToCreate.getCanonicalFile().getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new FileNotFoundException("Unable to create directory: " + parent.getAbsolutePath());
            }

            pToCreate.createNewFile();
            RandomAccessFile file = new RandomAccessFile(pToCreate, "rw");
            file.setLength(pLength);
            file.close();
            return true;
        } catch (IOException e) {
            throw e;
        }

    }

    /**
     * Setting up the meter.
     *
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        final File file = Files.createTempDir();
        byteMeter = new FileMeter(file, Memory.Byte);
        kibiByteMeter = new FileMeter(file, Memory.KibiByte);
        mebiByteMeter = new FileMeter(file, Memory.Mebibyte);
        size = initializeStorage(file);
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
     * Test method for {@link org.perfidix.meter.FileMeter#getUnitDescription()} .
     */
    @Test
    public void testGetDescription() {
        assertEquals("Data check for describtion for byte", Memory.Byte.getUnitDescription(), byteMeter.getUnitDescription());
        assertEquals("Data check for describtion for kibiByte", Memory.KibiByte.getUnitDescription(), kibiByteMeter.getUnitDescription());
        assertEquals("Data check for describtion for mebiByte", Memory.Mebibyte.getUnitDescription(), mebiByteMeter.getUnitDescription());

    }

    /**
     * Method for initializing the storage for testing.
     *
     * @return long regarding the fileserver.
     * @throws IOException
     */
    private long initializeStorage(File toCreate) throws IOException {
        long size = 0;
        final double goDownThreshold = 0.6;
        final double createThreshold = 0.8;
        final long maxSize = 500;
        int i = 1;
        do {
            toCreate = new File(toCreate, "folder" + i);
            toCreate.mkdir();
            int j = 0;
            do {
                File nextFile = new File(toCreate, "file" + j);
                long sizeToSet = Math.abs(RAN.nextLong() % maxSize);
                createStorageVolume(nextFile, sizeToSet);
                size = size + sizeToSet;
                j++;
            } while (RAN.nextDouble() < createThreshold);
            i++;
        } while (RAN.nextDouble() < goDownThreshold);

        return size;
    }
}
