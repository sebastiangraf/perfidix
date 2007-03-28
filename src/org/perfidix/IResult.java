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
 * $Id: IResult.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.util.ArrayList;
import java.util.Iterator;

import org.perfidix.visitor.ResultVisitor;



/**
 * 
 * @author onea
 *
 */
public interface IResult {

  /**
   * computes the default meter, which will
   * be used as a reference when calling methods
   * without the IMeter parameter.
   * @return the default meter
   */
  public IMeter getDefaultMeter();

  /**
   * 
   * @return the result set of a benchmark.
   */
  public long[] getResultSet();

  public long getNumberOfRuns();

  /**
   * contains the single result for anything.
   * this is the 'leaf' result class, containing the 
   * real resulting values of a test run.
   * for each method run, an instance of SingleResult is being 
   * created and fed with the 
   * @author axo
   * @since 2005
   * 
   */
  public class SingleResult extends Result {

    private transient long[] resultSet;

    private String data;

    /** 
     * 
     */
    private Object[] returnValues;

    private IMeter theMeter;

    /**
     * convenience constructor.
     * @param aResultSet
     */
    public SingleResult(final long[] aResultSet) {
      this(aResultSet, Perfidix.defaultMeter());
    }

    /**
     * constructor.
     * @param aResultSet the result set.
     * @param meter the meter with which measurement was taken.
     */
    public SingleResult(final long[] aResultSet, final IMeter meter) {
      this(Result.DEFAULT_NAME, aResultSet, meter);
    }

    /**
     * returns the meter.
     * @return the meter
     */
    public IMeter getDefaultMeter() {
      return theMeter;
    }

    /**
     * read resolve.
     * @return the resolved object.
     */
    protected Object readResolve() {
      super.readResolve();
      String[] longData = NiceTable.Util.explode(',', data);
      resultSet = new long[longData.length];
      for (int i = 0; i < longData.length; i++) {
        resultSet[i] = Long.parseLong(longData[i]);
      }
      return this;
    }

    /** 
     * returns the return values given as a parameter to the constructor.
     * @return just guess, dude. 
     */
    public Object getReturnValues() {
      return returnValues;
    }

    /**
     * constructor.
     * 
     * @param aName
     *          the literal name you want to give the result.
     * @param aResultSet
     *          an array of long values to be computed.
     * @param theReturnValues
     *          an array of return values generated for each iteration.
     * @param whichMeter the meter with which the calculations were done.
     */
    public SingleResult(
        final String aName,
        final long[] aResultSet,
        final Object[] theReturnValues,
        final IMeter whichMeter) {
      this(aName, aResultSet, whichMeter);
      returnValues = theReturnValues;

    }

    /**
     * the standard constructor.
     * @param name the name of the result.
     * @param aResultSet the results.
     * @param whichMeter the meter with which the calculations were done.
     */
    public SingleResult(
        final String name,
        final long[] aResultSet,
        final IMeter whichMeter) {
      super();
      setName(name);
      /*  debug 
       System.out.println(getName() + " " + NiceTable.Util.implode(", ",aResultSet));
       */
      resultSet = filterNullValues(aResultSet);
      theMeter = whichMeter;
      data = NiceTable.Util.implode(",", resultSet);
    }

    /**
     *  
     * this is not a comment.
     * @see org.perfidix.IResult#getNumberOfRuns()
     */
    public long getNumberOfRuns() {
      // debug System.out.println(getName() + " # " + resultSet.length);
      // the result set does not contain the ignored runs.
      return resultSet.length;
    }

    /**
     * i think there must be a simpler approach to this problem.
     * the input array given could contain some Benchmark.LONG_NULLVALUEs,
     * which tell us that at that iteration, the method or whatever 
     * wasn't called.
     * for the current computations it's not important to show these
     * values, and such i remove these and return a copy of the array.
     * unfortunately, this renders the algorithm's complexity to O(n^2) 
     * when any null value is found, and to O(n) when no null value
     * was found in the first run.
     * because i first have to iterate through the original array to 
     * estimate the result array's length, and then i have to 
     * copy the values into the newly allocated array.
     * if there's a shorter method for this, feel free to tell me. 
     * @param array
     * @return the filtered array.
     */
    private long[] filterNullValues(final long[] array) {
      int resultSetLength = 0;

      for (int i = 0; i < array.length; i++) {
        if (array[i] == Benchmark.LONG_NULLVALUE) {
          // debug  System.out.println(getName() + " found null value.");
          continue;
        } else {
          // debug   System.out.println(
          //  getName() + ": " + array[i]   + "<>" + Benchmark.LONG_NULLVALUE);
        }
        resultSetLength++;
      }

      if (resultSetLength == array.length) {
        return array;
      }

      long[] res = new long[resultSetLength];
      int resultIdx = 0;
      for (int oldIdx = 0; oldIdx < array.length; oldIdx++) {
        if (array[oldIdx] == Benchmark.LONG_NULLVALUE) {
          continue;
        }
        res[resultIdx] = array[oldIdx];
        resultIdx++;
      }
      return res;
    }

    /** 
     * returns the result set.
     * this result set is filtered out of 
     * @return the result set. 
     */
    public long[] getResultSet() {
      return resultSet.clone();
    }

    /**
     * accepts a result visitor.
     * @param v the visitor.
     */
    public void accept(final ResultVisitor v) {
      v.visit(this);
    }

    /**
     * 
     * @return the unit with which the measurement was taken.
     */
    public String getUnit() {
      return theMeter.getUnit();
    }

    /**
     * returns the meter associated with 
     * this result.
     * @return the meter.
     */
    public IMeter getMeter() {
      return theMeter;
    }

  }

  /**
   * 
   * @author axo
   *
   */
  public class BenchmarkResult extends ResultContainer<IResult.ClassResult> {

    /**
     * default constructor.
     *
     */
    public BenchmarkResult() {
      this("");
    }

    /**
     * 
     * @param theName name
     */
    public BenchmarkResult(final String theName) {
      super(theName);
    }

  }

  /**
   * 
   * @author axo
   *
   */
  class ClassResult extends ResultContainer<IResult.MethodResult> {

    private String theClass;

    /**
     * 
     * @param theName the name.
     * @param classUnderTest the class name which is benchmarked.
     */
    ClassResult(final String theName, final String classUnderTest) {
      super(theName);
      theClass = classUnderTest;
    }

    /**
     * 
     * @param theName only the name.
     */
    public ClassResult(final String theName) {
      super(theName);
      theClass = null;
    }

    /**
     * 
     * @return the class under test. may be null!
     */
    public String getClassUnderTest() {
      return theClass;
    }

  }

  /**
   * 
   * @author onea
   *
   */
  class MethodResult extends ResultContainer<IResult.SingleResult> {

    /**
     * 
     * @param theName the name.
     */
    MethodResult(final String theName) {
      super(theName);
    }

    public long getNumberOfRuns() {
      /* debug 
       Iterator<SingleResult> it = getChildren().iterator();
       while (it.hasNext()) {
       SingleResult s = it.next();
       System.out.println(getName() 
       + " number of runs: " + s.getName() + " " + s.getNumberOfRuns());
       }
       */
      return getChildren().get(0).getNumberOfRuns();
    }

    /**
     * returns the children.
     * @return the children.
     */
    public ArrayList<IResult.SingleResult> getChildren() {
      ArrayList<IResult.SingleResult> children = super.getChildren();
      Iterator<ArrayList<IResult.SingleResult>> cust =
          this.getCustomChildren().values().iterator();
      while (cust.hasNext()) {
        children.addAll(cust.next());
      }
      return children;
    }

  }

}
