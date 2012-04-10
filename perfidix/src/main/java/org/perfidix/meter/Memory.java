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
    private Memory(final String paramUnit, final String paramUnitDesc, final int paramBytes) {
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
