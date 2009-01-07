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
    private static final String DEFAULTUNITDESC =
            "Simple ticks for counting";

    /**
     * Counter for ticks.
     */
    private long counter;

    /**
     * Name for this counting meter.
     */
    private final String name;

    /**
     * Unit for this counting meter.
     */
    private final String unit;

    /**
     * Short description for the unit.
     */
    private final String unitDescription;

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
     * @param paramUnitDescription
     *            the description of this CountingMeter
     */
    public CountingMeter(
            final String paramName, final String paramUnit,
            final String paramUnitDescription) {
        super();
        name = paramName;
        unit = paramUnit;
        unitDescription = paramUnitDescription;
        counter = 0;
    }

    /**
     * Getting the name of this CountingMeter.
     * 
     * @return the name of this CountingMeter
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * The meter is ticking one forward.
     */
    public final void tick() {
        counter++;
    }

    /**
     * Getting the value of this CountingMeter.
     * 
     * @return the counter's value.
     */
    @Override
    public final double getValue() {
        return counter;
    }

    /**
     * Getting the name of this CountingMeter.
     * 
     * @return the unit of this CountingMeter
     */
    @Override
    public final String getUnit() {
        return unit;
    }

    /**
     * Getting the description of this CountingMeter.
     * 
     * @return the description of this CountingMeter
     */
    @Override
    public final String getUnitDescription() {
        return unitDescription;
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
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
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CountingMeter other = (CountingMeter) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (unit == null) {
            if (other.unit != null) {
                return false;
            }
        } else if (!unit.equals(other.unit)) {
            return false;
        }
        if (unitDescription == null) {
            if (other.unitDescription != null) {
                return false;
            } else {
                if (!unitDescription.equals(other.unitDescription)) {
                    return false;
                }
            }
        }
        return true;
    }

}
