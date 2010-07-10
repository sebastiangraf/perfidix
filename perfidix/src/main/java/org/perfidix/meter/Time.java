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
 * Small enum to store different times.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public enum Time {

    /** Enum for nano seconds. */
    NanoSeconds("ns", "nano seconds", 1),
    /** Enum for milli seconds. */
    MilliSeconds("ms", "milli seconds", 1000000),
    /** Enum for seconds. */
    Seconds("s", "second", 1000000000),
    /** Enum for minutes. */
    Minutes("min", "minutes", 60000000000.0);

    /**
     * The unit of the time.
     */
    private final String unit;

    /**
     * The description of the time.
     */
    private final String unitDescription;

    /**
     * Number of bytes.
     */
    private transient final double milliSeconds;

    /**
     * The constructor for the memory sizes.
     * 
     * @param paramUnit
     *            to give
     * @param paramUnitDesc
     *            to give
     * @param paramMillis
     *            to give
     */
    private Time(
            final String paramUnit, final String paramUnitDesc,
            final double paramMillis) {
        unit = paramUnit;
        unitDescription = paramUnitDesc;
        milliSeconds = paramMillis;
    }

    /**
     * Getting the number of milli seconds.
     * 
     * @return the number of milli seconds
     */
    public double getNumberOfMilliSeconds() {
        return milliSeconds;
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
