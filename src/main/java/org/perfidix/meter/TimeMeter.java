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
 * Meter to bench the amount of time used by the current Benchmark. The
 * TimeMeter is in need of an instance of the {@link Time} enumeration to give
 * back the suitable time.
 * 
 * @see Time
 * @author Sebastian Graf, University of Konstanz
 */
public final class TimeMeter extends AbstractMeter {

    private static final String NAME = "TimeMeter";

    /**
     * Instance of the enum <code>Time</code> for correct formatting of the
     * time.
     */
    private final Time currentTime;

    /**
     * Constructor which is in need of a given time.
     * 
     * @param paramTime
     *            the time for the values.
     */
    public TimeMeter(final Time paramTime) {
        currentTime = paramTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double getValue() {
        return new BigDecimal(
                System.currentTimeMillis(), MathContext.DECIMAL128)
                .divide(
                        new BigDecimal(
                                currentTime.getNumberOfMilliSeconds(),
                                MathContext.DECIMAL128), MathContext.DECIMAL128)
                .doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public String getUnit() {
        return currentTime.getUnit();
    }

    /** {@inheritDoc} */
    @Override
    public String getUnitDescription() {
        return currentTime.getUnitDescription();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime;
        result =
                prime
                        * result
                        + ((currentTime == null) ? 0 : currentTime.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        TimeMeter other = (TimeMeter) obj;
        if (currentTime == null) {
            if (other.currentTime != null)
                return false;
        } else if (!currentTime.equals(other.currentTime))
            return false;
        return true;
    }

}