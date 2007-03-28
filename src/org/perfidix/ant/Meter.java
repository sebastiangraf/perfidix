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
 * $Id: Meter.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ant;


import org.apache.tools.ant.BuildException;
import org.perfidix.IMeter;


/**
 * <meter name="hello" init="30" 
 * unit="breads" description="amount of bread used" />.
 * @author onea
 *
 */
public class Meter extends PerfidixTask {

  /**
   * the name of the meter.
   */
  private String theName = "";

  /**
   * the unit of the meter.
   */
  private String theUnit = "no unit";

  /**
   * the unit description.
   */
  private String theDescription = "no description";

  private int initialValue = 0;

  /**
   * the meter.
   */
  private IMeter meter = null;

  /**
   * sets the name of the meter.
   * @param name the name.
   */
  public void setName(final String name) {
    debug("setting name " + name);
    theName = name;
  }

  /**
   * sets the initial value of the meter.
   * @param i the initial value (should be an integer).
   */
  public void setInit(final String i) {
    debug("setting initial value " + i);
    initialValue = Integer.parseInt(i);
  }

  /**
   * sets the unit of the meter.
   * @param u the unit
   */
  public void setUnit(final String u) {
    debug("setting unit " + u);
    theUnit = u;
  }

  /**
   * sets the meter's description.
   * @param d the description.
   */
  public void setDescription(final String d) {
    debug("setting description to " + d);
    theDescription = d;
  }

  /**
   * executes.
   * @throws BuildException nothing.
   */
  public void execute() throws BuildException {
    debug("building meter...");
    meter =
        org.perfidix.Perfidix.createMeter(
            theName,
            theUnit,
            initialValue,
            theDescription);

  }

  /**
   * 
   * @return the registered meter.
   */
  IMeter getMeter() {
    return meter;
  }
}
