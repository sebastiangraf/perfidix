/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
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

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Meter to bench the amount of memory used by the current Benchmark. Please
 * note that the results of this meter can only be seen as an approximation.
 * Because Perfidix-processes are influencing the used memory as well, a small
 * increasment of used memory is normal. However, because being based only on
 * the Runtime-class of Java, no extraction of the perfidix processes themselves
 * is possible. The MemMeter is only usable with an instance of the
 * {@link Memory} enumeration for formatting purposes. This choose must be made
 * by instantiation of the meter.
 * 
 * @see Memory
 * @author Sebastian Graf, University of Konstanz
 */
public final class MemMeter extends AbstractMeter {

    /** Name of the Meter. */
    private static final String NAME = "MemMeter";

    /** Amount of already used memory. */
    private transient double memAlreadyUsed;

    /** Scale of memory. */
    private transient final Memory scale;

    /**
     * Constructor.
     * 
     * @param paramScale
     *            scale for this meter, can be any instance of Memory-enum
     */
    public MemMeter(final Memory paramScale) {
        super();
        memAlreadyUsed = 0;
        this.scale = paramScale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValue() {
        final Runtime runtime = Runtime.getRuntime();
        memAlreadyUsed =
                memAlreadyUsed + runtime.totalMemory() - runtime.freeMemory();
        return new BigDecimal(memAlreadyUsed, MathContext.DECIMAL128).divide(
                new BigDecimal(scale.getNumberOfBytes()),
                MathContext.DECIMAL128).doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public String getUnit() {
        return scale.getUnit();
    }

    /** {@inheritDoc} */
    @Override
    public String getUnitDescription() {
        return scale.getUnitDescription();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime;
        if (scale == null) {
            result = prime * result;
        } else {
            result = prime * result + scale.hashCode();
        }

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        boolean returnVal = true;
        if (this == obj) {
            returnVal = true;
        }
        if (getClass() != obj.getClass()) {
            returnVal = false;
        }
        final MemMeter other = (MemMeter) obj;
        if (scale == null) {
            if (other.scale != null) {
                returnVal = false;
            }
        } else {
            if (!scale.equals(other.scale)) {
                returnVal = false;
            }
        }
        return returnVal;
    }

}
