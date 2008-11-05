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
 * Meter to bench the amount of memory used by the current Benchmark. Please
 * note that the results of this meter can only be seen as an approximation.
 * Because Perfidix-processes are influencing the used memory as well, a small
 * increasment of used memory is normal. However, because being based only on
 * the Runtime-class of Java, no extraction of the perfidix processes themselves
 * is possible.
 * 
 * @author Sebastian Graf, University of Kontanz
 */
public final class MemMeter extends AbstractMeter {

    private final static String DEFAULTNAME = "MemoryMeter";

    private long memAlreadyUsed;
    private long lastMemoryUsed;

    /**
     * Constructor.
     */
    public MemMeter() {
        memAlreadyUsed = 0;
        lastMemoryUsed = 0;
    }

    /**
     * Small enum to store the different kinds of memories.
     * 
     * @author Sebastian Graf, University of Konstanz
     */
    enum Memory {

        /** Enums for different sizes. */
        Byte("B", "byte", 1), KibiByte("KiB", "kibiByte", 1024), Mebibyte(
                "MiB", "mebibyte", 1048576);

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
        private final int numberOfBytes;

        /**
         * The constructor for the memory sizes.
         * 
         * @param paramUnit
         *            to give
         * @param paramUnitDesc
         *            to give
         */
        private Memory(
                final String paramUnit, final String paramUnitDesc,
                final int paramNumberOfBytes) {
            unit = paramUnit;
            unitDescription = paramUnitDesc;
            numberOfBytes = paramNumberOfBytes;
        }

        /**
         * Getting the number of bytes.
         * 
         * @return the number of bytes
         */
        int getNumberOfBytes() {
            return numberOfBytes;
        }

        /**
         * Getting the unit.
         * 
         * @return the unit
         */
        String getUnit() {
            return unit;
        }

        /**
         * Getting the full unitname.
         * 
         * @return the full unitname
         */
        String getUnitDescription() {
            return unitDescription;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getUnit() {
        if (lastMemoryUsed > Memory.Mebibyte.getNumberOfBytes()) {
            return Memory.Mebibyte.getUnit();
        } else if (lastMemoryUsed > Memory.KibiByte.getNumberOfBytes()) {
            return Memory.KibiByte.getUnit();
        } else {
            return Memory.Byte.getUnit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getUnitDescription() {
        if (lastMemoryUsed > Memory.Mebibyte.getNumberOfBytes()) {
            return Memory.Mebibyte.getUnitDescription();
        } else if (lastMemoryUsed > Memory.KibiByte.getNumberOfBytes()) {
            return Memory.KibiByte.getUnitDescription();
        } else {
            return Memory.Byte.getUnitDescription();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getValue() {
        final Runtime rt = Runtime.getRuntime();
        rt.gc();
        lastMemoryUsed = rt.totalMemory() - rt.freeMemory();
        memAlreadyUsed = memAlreadyUsed + lastMemoryUsed;
        return memAlreadyUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return MemMeter.DEFAULTNAME;
    }

}
