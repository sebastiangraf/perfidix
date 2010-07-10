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

/**
 * Small enum to store the different kinds of memories.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public enum Memory {

    /** Enum for byte. */
    Byte("B", "byte", 1),
    /** Enum for KibiByte. */
    KibiByte("KiB", "kibiByte", 1024),
    /** Enum for MebiByte. */
    Mebibyte("MiB", "mebibyte", 1048576);

    /**
     * The unit of one size.
     */
    private final String unit;

    /**
     * The description of one size.
     */
    private final String unitDescription;

    /**
     * Number of bytes.
     */
    private final double numberOfBytes;

    /**
     * The constructor for the memory sizes.
     * 
     * @param paramUnit
     *            to give
     * @param paramUnitDesc
     *            to give
     * @param paramBytes
     *            to give
     */
    private Memory(
            final String paramUnit, final String paramUnitDesc,
            final int paramBytes) {
        unit = paramUnit;
        unitDescription = paramUnitDesc;
        numberOfBytes = paramBytes;
    }

    /**
     * Getting the number of bytes.
     * 
     * @return the number of bytes
     */
    public double getNumberOfBytes() {
        return numberOfBytes;
    }

    /**
     * Getting the unit.
     * 
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Getting the full unitname.
     * 
     * @return the full unitname
     */
    public String getUnitDescription() {
        return unitDescription;
    }

}
