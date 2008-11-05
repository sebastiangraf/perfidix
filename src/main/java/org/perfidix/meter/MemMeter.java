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

import org.perfidix.Perfidix;

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

    private long memUsed;

    /**
     * Constructor.
     */
    public MemMeter() {
        final Runtime rt = Runtime.getRuntime();
        memUsed = (rt.totalMemory() - rt.freeMemory()) * -1;
        rt.gc();
    }

    /**
     * Small enum to store the different kinds of memories.
     * 
     * @author Sebastian Graf, University of Konstanz
     */
    enum Memory {

        /** Enums for different sizes. */
        Byte("B", "byte"), KiloByte("K", "kilobyte"), MegaByte("M", "megabyte");

        /**
         * The unit of one size.
         */
        private final String unit;

        /**
         * The description of one size.
         */
        private final String unitDescription;

        /**
         * The constructor for the memory sizes.
         * 
         * @param paramUnit
         *            to give
         * @param paramUnitDesc
         *            to give
         */
        private Memory(final String paramUnit, final String paramUnitDesc) {
            unit = paramUnit;
            unitDescription = paramUnitDesc;
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
        String getUniDescription() {
            return unitDescription;
        }

    }

    @Override
    public final String getUnit() {
        return Perfidix.MEM_UNIT;
    }

    @Override
    public String getUnitDescription() {
        return Perfidix.MEM_DESCRIPTION;
    }

    @Override
    public long getValue() {
        final Runtime rt = Runtime.getRuntime();
        memUsed += rt.totalMemory() - rt.freeMemory();
        rt.gc();
        return memUsed;
    }

    @Override
    public String getName() {
        return Perfidix.MEM_DESCRIPTION;
    }

}
