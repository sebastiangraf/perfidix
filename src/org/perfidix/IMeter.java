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

package org.perfidix;

/**
 * the public interface of perfidix meters.
 * each meter consists of a scalar value (e.g. 123) and a 
 * measurement unit, such as ns for nanoseconds, ms for milliseconds,
 * ft for file touches et cetera.
 * @author axo
 *
 */
public interface IMeter extends Comparable<IMeter> {

  /**
   * call this and the meter will increment its internal 
   * counter.
   * most meters will simply increment their counter.
   * main usage:
   * long t1 = IMeter.getValue();
   * // do something... and call  
   *                    IMeter.tick();
   *                ... as often as needed.
   * long t2 = IMeter.getValue();
   * long ticksNeeded = t2 - t1. 
   */
  public void tick();

  /**
   * ticks in num steps.
   * @param num
   */
  public void tick(final int num);

  /**
   * 
   * @return the current tick value. 
   * 
   */
  public long getValue();

  /**
   * returns the unit in which this measure taker 
   * computes its results. 
   * @return the unit.
   */
  public String getUnit();

  /**
   * the long description of the unit.
   * can be empty but if available, it should provide one or two words
   * about the measure taker.
   * @return the long description of the unit in which measurement
   *            takes place.
   */
  public String getUnitDescription();

  /**
   * 
   * @return the name.
   */
  public String getName();

  /**
   * 
   * @author onea
   *
   */
  abstract class AbstractMeter implements IMeter {

    /**
     * @return the name.
     */
    public String getName() {
      return "";
    }

    /**
     * implements comparable.
     * careful here. all ITimeMeters are sorted _before_ any
     * other meters. 
     * so when the given arraylist mixes time meters and 
     * custom meters, the time meters will be always first.
     * @param o the object to compare with.
     * @return integer
     */
    public final int compareTo(final IMeter o) {
      if (null == o) {
        return -1;
      }
      if (this instanceof ITimeMeter) {
        if (o instanceof ITimeMeter) {
          return doCompareValues(o);
        }
        return -1;
      } else {
        if (o instanceof ITimeMeter) {
          return 1;
        }
        return doCompareValues(o);
      }
    }

    /**
     * provides some comparation on the member values 
     * of the object.
     * essentially, i need this only to be able to sort the objects
     * in the views, and there i only sort by the unit.
     * @param o the meter to compare with.
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
     * @param obj the meter to compare with
     * @return whether they're equel.
     */
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
     * @return the hash code.
     */
    public final int hashCode() {
      int result = Perfidix.HASHCODE_SEED;
      result = Perfidix.HASHCODE_PRIME * result + getUnit().hashCode();
      result =
          Perfidix.HASHCODE_PRIME * result + getUnitDescription().hashCode();
      result = Perfidix.HASHCODE_PRIME * result + getName().hashCode();
      return result;
    }

    /**
     * calls tick num times.
     * @parma num number of times to run tick()
     */
    public final void tick(int num) {
      for (int i = 0; i < num; i++) {
        tick();
      }
    }

    /**
     * 
     * @return a string representation of this meter.
     */
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

  /**
   * @author axo
   *
   */
  abstract class ITimeMeter extends IMeter.AbstractMeter {

    /**
     * returns the current time.
     * depending on the subclass, the values of this method will differ.
     * usage example will be:
     * <pre>
     * double start, end, elapsed;
     * start = IMeter.getTime();
     *  // perform some work here ... 
     * end   = IMeter.getTime();
     * elapsed = (end - start);
     * </pre>
     * @return the current time measure.
     */
    public abstract long getTime();

    /**
     * does nothing. the timeMeters work on their own clock.
     */
    public final void tick() {
      // do nothing.
    }

    /**
     *  
     * this is not a comment.
     * @see org.perfidix.IMeter#getValue()
     * @return the time elapsed.
     */
    public final long getValue() {
      return getTime();
    }

  }

  /**
   * @author axo
   *
   */
  final class MilliMeter extends IMeter.ITimeMeter {

    /** 
     * computes the elapsed time in milliseconds.
     * depending on the OS, the values will differ.
     * @see ITimeMeter#getTime
     * @return System.currentTimeMillis();
     */
    public final long getTime() {
      return System.currentTimeMillis();
    }

    /**
     * @return the unit the values have to be interpreted with.
     */
    public String getUnit() {
      return Perfidix.MSEC_UNIT;
    }

    /**
     * @return the unit description.
     */
    public String getUnitDescription() {
      return Perfidix.MSEC_DESCRIPTION;
    }

  }


  final class SecondMeter extends IMeter.ITimeMeter {
      
      /** 
       * this is not a comment.
       * 
       * @see ITimeMeter#getTime
       * @return the system's nano time.
       */
      public final long getTime() {
        return Math.round(System.currentTimeMillis() / 1000);
      }

      /**
       * 
       * @return the short unit description.
       */
      public String getUnit() {
        return Perfidix.SECOND_UNIT;
      }

      /**
       * returns the unit description.
       * @return the unit description.
       */
      public String getUnitDescription() {
        return Perfidix.SECOND_DESCRIPTION;
      }
      
  }
  
  final class NanoMeter extends IMeter.ITimeMeter {

    /** 
     * this is not a comment.
     * 
     * @see ITimeMeter#getTime
     * @return the system's nano time.
     */
    public final long getTime() {
      return System.nanoTime();
    }

    /**
     * 
     * @return the short unit description.
     */
    public String getUnit() {
      return Perfidix.NANO_UNIT;
    }

    /**
     * returns the unit description.
     * @return the unit description.
     */
    public String getUnitDescription() {
      return Perfidix.NANO_DESCRIPTION;
    }

  }

  /**
   * @author axo
   *
   */
  class CountingMeter extends IMeter.AbstractMeter {

    private transient long counter;

    private String name;

    private String unit = Perfidix.DEFAULT_UNIT;

    private String unitDescription = Perfidix.DEFAULT_DESCRIPTION;

    /**
     * this is not a comment.
     *
     */
    CountingMeter() {
      this(Perfidix.DEFAULT_COUNTER_INITVALUE);
    }

    /**
     * this is not a comment.
     * @param initialValue the initial value
     */
    CountingMeter(final int initialValue) {
      this(Perfidix.DEFAULT_COUNTER_NAME, initialValue);
    }

    /**
     * this is not a comment.
     * @param meterName the name.
     */
    CountingMeter(final String meterName) {
      this(meterName, Perfidix.DEFAULT_COUNTER_INITVALUE);
    }

    /**
     * this is not a comment.
     * @param meterName the name.
     * @param initialValue the initial value
     */
    CountingMeter(final String meterName, final long initialValue) {
      name = meterName;
      counter = initialValue;
    }

    private Object readResolve() {
      counter = Perfidix.DEFAULT_COUNTER_INITVALUE;
      return this;
    }

    /**
     * returns the name.
     * @return name
     */
    public String getName() {
      return name;
    }

    /** 
     * this is not a comment.
     * @see org.perfidix.IMeter#tick()
     */
    public final void tick() {
      counter++;
    }

    /**
     *  
     * this is not a comment.
     * @see org.perfidix.IMeter#getValue()
     * @return the counter's value.
     */
    public long getValue() {
      return counter;
    }

    /**
     * returns the measurement unit attached to this meter.
     * set this up with setUnit()
     * @see #setUnit
     * @return the unit.
     */
    public String getUnit() {
      return unit;
    }

    /**
     * returns the unit description which was previously set with
     * setUnitDescription() .
     * @see #setUnitDescription
     * @return the unit description.
     */
    public String getUnitDescription() {
      return unitDescription;
    }

    /**
     * sets the unit. 
     * @param theUnit a unit to use.
     */
    void setUnit(final String theUnit) {
      unit = theUnit;
    }

    /**
     * sets the unit description.
     * @param theUnitDescription as a simple string.
     */
    void setUnitDescription(final String theUnitDescription) {
      unitDescription = theUnitDescription;
    }

  }

}
