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

import org.perfidix.Perfidix;

/**
 * @author axo
 */
// TODO TAKE A LOOK ON IT, public set method into constructor?
public class CountingMeter extends AbstractMeter {

    private transient long counter;

    private String name;

    private String unit = Perfidix.DEFAULT_UNIT;

    private String unitDescription = Perfidix.DEFAULT_DESCRIPTION;

    /**
     * this is not a comment.
     */
    public CountingMeter() {
        this(Perfidix.DEFAULT_COUNTER_INITVALUE);
    }

    /**
     * this is not a comment.
     * 
     * @param initialValue
     *            the initial value
     */
    CountingMeter(final int initialValue) {
        this(Perfidix.DEFAULT_COUNTER_NAME, initialValue);
    }

    /**
     * this is not a comment.
     * 
     * @param meterName
     *            the name.
     */
    CountingMeter(final String meterName) {
        this(meterName, Perfidix.DEFAULT_COUNTER_INITVALUE);
    }

    /**
     * this is not a comment.
     * 
     * @param meterName
     *            the name.
     * @param initialValue
     *            the initial value
     */
    public CountingMeter(final String meterName, final long initialValue) {
        name = meterName;
        counter = initialValue;
    }

    private Object readResolve() {
        counter = Perfidix.DEFAULT_COUNTER_INITVALUE;
        return this;
    }

    /**
     * returns the name.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * this is not a comment.
     * 
     * @see org.perfidix.meter.IMeter#tick()
     */
    public final void tick() {
        counter++;
    }

    /**
     * this is not a comment.
     * 
     * @see org.perfidix.meter.IMeter#getValue()
     * @return the counter's value.
     */
    public long getValue() {
        return counter;
    }

    /**
     * returns the measurement unit attached to this meter. set this up with
     * setUnit()
     * 
     * @see #setUnit
     * @return the unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * returns the unit description which was previously set with
     * setUnitDescription() .
     * 
     * @see #setUnitDescription
     * @return the unit description.
     */
    public String getUnitDescription() {
        return unitDescription;
    }

    /**
     * sets the unit.
     * 
     * @param theUnit
     *            a unit to use.
     */
    public void setUnit(final String theUnit) {
        unit = theUnit;
    }

    /**
     * sets the unit description.
     * 
     * @param theUnitDescription
     *            as a simple string.
     */
    public void setUnitDescription(final String theUnitDescription) {
        unitDescription = theUnitDescription;
    }

}