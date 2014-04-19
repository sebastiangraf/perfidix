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

/**
 * For benchmarking in a widen sense, several different meters should be implemented. The first one is obviously the
 * time. Nevertheless, besides the time, other meters for special purposes can be implemented with this method.
 *
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractMeter {

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
     * The long description of the unit. can be empty but if available, it should provide one or two words about the
     * measure taker.
     *
     * @return the long description of the unit in which measurement takes place.
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

        builder.append(getName()).append("[").append(getUnit()).append("]");
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
