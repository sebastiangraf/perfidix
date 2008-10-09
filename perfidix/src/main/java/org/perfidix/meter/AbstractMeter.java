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
 * @author onea
 */
public abstract class AbstractMeter implements IMeter {

    /**
     * implements comparable. careful here. all ITimeMeters are sorted _before_
     * any other meters. so when the given arraylist mixes time meters and
     * custom meters, the time meters will be always first.
     * 
     * @param o
     *            the object to compare with.
     * @return integer
     */
    public final int compareTo(final IMeter o) {
        if (null == o) {
            return -1;
        }
        if (this instanceof AbsTimeMeter) {
            if (o instanceof AbsTimeMeter) {
                return doCompareValues(o);
            }
            return -1;
        } else {
            if (o instanceof AbsTimeMeter) {
                return 1;
            }
            return doCompareValues(o);
        }
    }

    /**
     * provides some comparation on the member values of the object.
     * essentially, i need this only to be able to sort the objects in the
     * views, and there i only sort by the unit.
     * 
     * @param o
     *            the meter to compare with.
     * @return the comparation value.
     */
    private int doCompareValues(final IMeter o) {
        int cmp = getUnit().compareTo(o.getUnit());
        if (0 != cmp) {
            return cmp;
        }
        cmp = getName().compareTo(o.getName());
        if (0 != cmp) {
            return cmp;
        }
        cmp = getUnitDescription().compareTo(o.getUnitDescription());
        if (0 != cmp) {
            return cmp;
        }
        // additional checks, if needed.
        return 0;
    }

    /**
     * checks whether two objects are the same or not.
     * 
     * @param obj
     *            the meter to compare with
     * @return whether they're equel.
     */
    @Override
    public final boolean equals(final Object obj) {
        if ((null == obj) || (obj.getClass() != this.getClass())) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        IMeter tmp = (IMeter) obj;
        return (getUnit().equals(tmp.getUnit())
                && getUnitDescription().equals(tmp.getUnitDescription()) && getName()
                .equals(tmp.getName()));

    }

    /**
     * computes the hash code needed for identity checking.
     * 
     * @return the hash code.
     */
    @Override
    public final int hashCode() {
        int result = Perfidix.HASHCODE_SEED;
        result = Perfidix.HASHCODE_PRIME * result + getUnit().hashCode();
        result =
                Perfidix.HASHCODE_PRIME
                        * result
                        + getUnitDescription().hashCode();
        result = Perfidix.HASHCODE_PRIME * result + getName().hashCode();
        return result;
    }

    /**
     * calls tick num times.
     * 
     * @parma num number of times to run tick()
     */
    public final void tick(int num) {
        for (int i = 0; i < num; i++) {
            tick();
        }
    }

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