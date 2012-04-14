/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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

/**
 * Simple meter to count given ticks. The ticks are not resetted afterwards, the
 * 
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 */
public final class CountingMeter extends AbstractMeter {

    /**
     * Constant to store the default name.
     */
    private static final String DEFAULTNAME = "CountingMeter";

    /**
     * Constant to store the default unit.
     */
    private static final String DEFAULTUNIT = "ticks";

    /**
     * Constant to store the default unit description.
     */
    private static final String DEFAULTUNITDESC = "Simple ticks for counting";

    /**
     * Counter for ticks.
     */
    private transient long counter;

    /**
     * Name for this counting meter.
     */
    private transient final String name;

    /**
     * Unit for this counting meter.
     */
    private transient final String unit;

    /**
     * Short description for the unit.
     */
    private transient final String unitDescription;

    /**
     * Constructor, generates a simple CountingMeter.
     */
    public CountingMeter() {
        this(DEFAULTNAME, DEFAULTUNIT, DEFAULTUNITDESC);
    }

    /**
     * Constructor with a given name.
     * 
     * @param paramName
     *            the name of this CountingMeter
     */
    public CountingMeter(final String paramName) {
        this(paramName, DEFAULTUNIT, DEFAULTUNITDESC);
    }

    /**
     * Constructor with a given name and a given unit.
     * 
     * @param paramName
     *            the name of this CountingMeter
     * @param paramUnit
     *            the unit of this CountingMeter
     */
    public CountingMeter(final String paramName, final String paramUnit) {
        this(paramName, paramUnit, DEFAULTUNITDESC);
    }

    /**
     * Constructor with a given name and a given unit and a given unit
     * description.
     * 
     * @param paramName
     *            the name of this CountingMeter
     * @param paramUnit
     *            the unit of this CountingMeter
     * @param paramUnitDesc
     *            the description of this CountingMeter
     */
    public CountingMeter(final String paramName, final String paramUnit, final String paramUnitDesc) {
        super();
        name = paramName;
        unit = paramUnit;
        unitDescription = paramUnitDesc;
        counter = 0;
    }

    /**
     * Getting the name of this CountingMeter.
     * 
     * @return the name of this CountingMeter
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * The meter is ticking one forward.
     */
    public void tick() {
        counter++;
    }

    /**
     * Getting the value of this CountingMeter.
     * 
     * @return the counter's value.
     */
    @Override
    public double getValue() {
        return counter;
    }

    /**
     * Getting the name of this CountingMeter.
     * 
     * @return the unit of this CountingMeter
     */
    @Override
    public String getUnit() {
        return unit;
    }

    /**
     * Getting the description of this CountingMeter.
     * 
     * @return the description of this CountingMeter
     */
    @Override
    public String getUnitDescription() {
        return unitDescription;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime;
        if (name == null) {
            result = prime * result;
        } else {
            result = prime * result + name.hashCode();
        }
        if (unit == null) {
            result = prime * result;
        } else {
            result = prime * result + unit.hashCode();
        }
        if (unitDescription == null) {
            result = prime * result;
        } else {
            result = prime * result + unitDescription.hashCode();
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
        final CountingMeter other = (CountingMeter)obj;
        if (name == null) {
            if (other.name != null) {
                returnVal = false;
            }
        } else {
            if (!name.equals(other.name)) {
                returnVal = false;
            }
        }
        if (unit == null) {
            if (other.unit != null) {
                returnVal = false;
            }
        } else if (!unit.equals(other.unit)) {
            returnVal = false;
        }
        if (unitDescription == null) {
            if (other.unitDescription == null) {
                if (!unitDescription.equals(other.unitDescription)) {
                    returnVal = false;
                }
            } else {
                returnVal = false;
            }
        }
        return returnVal;
    }

}
