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
 * @author onea
 */
public abstract class AbstractMeter {

    /**
     * @return the current tick value.
     */
    public abstract long getValue();

    /**
     * returns the unit in which this measure taker computes its results.
     * 
     * @return the unit.
     */
    public abstract String getUnit();

    /**
     * the long description of the unit. can be empty but if available, it
     * should provide one or two words about the measure taker.
     * 
     * @return the long description of the unit in which measurement takes
     *         place.
     */
    public abstract String getUnitDescription();

    /**
     * @return the name.
     */
    public abstract String getName();

    /**
     * @return a string representation of this meter.
     */
    @Override
    public final String toString() {
        return getName()
                + "["
                + getUnit()
                + "]: ("
                + getValue()
                + ") "
                + getUnitDescription();
    }
}