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

public final class SecondMeter extends AbsTimeMeter {

    /**
     * this is not a comment.
     * 
     * @see AbsTimeMeter#getTime
     * @return the system's nano time.
     */
    @Override
    public final long getTime() {
        return Math.round(System.currentTimeMillis() / 1000);
    }

    /**
     * @return the short unit description.
     */
    public String getUnit() {
        return Perfidix.SECOND_UNIT;
    }

    /**
     * returns the unit description.
     * 
     * @return the unit description.
     */
    public String getUnitDescription() {
        return Perfidix.SECOND_DESCRIPTION;
    }

}