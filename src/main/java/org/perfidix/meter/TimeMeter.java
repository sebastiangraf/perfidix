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


import java.math.BigDecimal;
import java.math.MathContext;


/**
 * Meter to bench the amount of time used by the current Benchmark. The TimeMeter is in need of an instance of the
 * {@link Time} enumeration to give back the suitable time.
 *
 * @author Sebastian Graf, University of Konstanz
 * @see Time
 */
public final class TimeMeter extends AbstractMeter {

    /**
     * Name of the Meter.
     */
    private static final String NAME = "TimeMeter";

    /**
     * Instance of the enum <code>Time</code> for correct formatting of the time.
     */
    private transient final Time currentTime;

    /**
     * Constructor which is in need of a given time.
     *
     * @param paramTime the time for the values.
     */
    public TimeMeter(final Time paramTime) {
        super();
        currentTime = paramTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValue() {
        return new BigDecimal(System.nanoTime(), MathContext.DECIMAL128).divide(new BigDecimal(currentTime.getNumberOfMilliSeconds(), MathContext.DECIMAL128), MathContext.DECIMAL128).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnit() {
        return currentTime.getUnit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnitDescription() {
        return currentTime.getUnitDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime;
        if (currentTime == null) {
            result = prime * result;
        } else {
            result = prime * result + currentTime.hashCode();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        boolean returnVal = true;
        if (this == obj) {
            returnVal = true;
        }
        if (getClass() != obj.getClass()) {
            returnVal = false;
        }
        final TimeMeter other = (TimeMeter) obj;
        if (currentTime == null) {
            if (other.currentTime != null) {
                returnVal = false;
            }
        } else {
            if (!currentTime.equals(other.currentTime)) {
                returnVal = false;
            }
        }
        return returnVal;
    }

}
