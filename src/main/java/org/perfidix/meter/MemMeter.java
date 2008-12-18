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

    private static final String NAME = "MemMeter";

    private double memAlreadyUsed;

    private final Memory scale;

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
    public final double getValue() {
        final Runtime rt = Runtime.getRuntime();
        rt.gc();
        memAlreadyUsed = memAlreadyUsed + rt.totalMemory() - rt.freeMemory();
        return new BigDecimal(memAlreadyUsed, MathContext.DECIMAL128).divide(
                new BigDecimal(scale.getNumberOfBytes()),
                MathContext.DECIMAL128).doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public final String getUnit() {
        return scale.getUnit();
    }

    /** {@inheritDoc} */
    @Override
    public final String getUnitDescription() {
        return scale.getUnitDescription();
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = prime;
        long temp;
        temp = Double.doubleToLongBits(memAlreadyUsed);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((scale == null) ? 0 : scale.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        MemMeter other = (MemMeter) obj;
        if (Double.doubleToLongBits(memAlreadyUsed) != Double
                .doubleToLongBits(other.memAlreadyUsed))
            return false;
        if (scale == null) {
            if (other.scale != null)
                return false;
        } else if (!scale.equals(other.scale))
            return false;
        return true;
    }

}
