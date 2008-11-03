/*
 * Copyright 2007 University of Konstanz
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
 * $Id: IMeter.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.meter;

/**
 * the public interface of perfidix meters. each meter consists of a scalar
 * value (e.g. 123) and a measurement unit, such as ns for nanoseconds, ms for
 * milliseconds, ft for file touches et cetera.
 * 
 * @author axo
 */
public interface IMeter {

    /**
     * @return the current tick value.
     */
    public long getValue();

    /**
     * returns the unit in which this measure taker computes its results.
     * 
     * @return the unit.
     */
    public String getUnit();

    /**
     * the long description of the unit. can be empty but if available, it
     * should provide one or two words about the measure taker.
     * 
     * @return the long description of the unit in which measurement takes
     *         place.
     */
    public String getUnitDescription();

    /**
     * @return the name.
     */
    public String getName();

}
