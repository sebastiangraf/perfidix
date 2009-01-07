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
 * For benchmarking in a widen sense, several different meters should be
 * implemented. The first one is obviously the time. Nevertheless, besides the
 * time, other meters for special purposes can be implemented with this method.
 * 
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractMeter {

    /**
     * Constructor.
     */
    protected AbstractMeter() {
    }

    /**
     * Getting the current value for this meter.
     * 
     * @return the current tick value.
     */
    public abstract double getValue();

    /**
     * Returns the unit in which this measure taker computes its results.
     * 
     * @return the unit of this meter
     */
    public abstract String getUnit();

    /**
     * The long description of the unit. can be empty but if available, it
     * should provide one or two words about the measure taker.
     * 
     * @return the long description of the unit in which measurement takes
     *         place.
     */
    public abstract String getUnitDescription();

    /**
     * A short name of the meter.
     * 
     * @return the name.
     */
    public abstract String getName();

    /**
     * The String representation of this meter with its current value.
     * 
     * @return a string representation of this meter.
     */
    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();

        builder
                .append(getName()).append("[").append(getUnit()).append(
                        "]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int hashCode();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean equals(final Object obj);

}
