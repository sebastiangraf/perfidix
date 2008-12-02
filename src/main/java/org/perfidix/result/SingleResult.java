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
package org.perfidix.result;

import org.perfidix.Benchmark;
import org.perfidix.Perfidix;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.visitor.NiceTable;
import org.perfidix.visitor.ResultVisitor;

/**
 * contains the single result for anything. this is the 'leaf' result class,
 * containing the real resulting values of a test run. for each method run, an
 * instance of SingleResult is being created and fed with the
 * 
 * @author axo
 * @since 2005
 */
public class SingleResult extends AbstractResult {

    private transient double[] resultSet;

    private String data;

    private Object[] returnValues;

    private AbstractMeter theMeter;

    /**
     * convenience constructor.
     * 
     * @param aResultSet
     */
    public SingleResult(final double[] aResultSet) {
        this(aResultSet, Perfidix.DEFAULTMETER);
    }

    /**
     * constructor.
     * 
     * @param aResultSet
     *            the result set.
     * @param meter
     *            the meter with which measurement was taken.
     */
    public SingleResult(final double[] aResultSet, final AbstractMeter meter) {
        this(AbstractResult.DEFAULT_NAME, aResultSet, meter);
    }

    /**
     * returns the meter.
     * 
     * @return the meter
     */
    @Override
    public AbstractMeter getDefaultMeter() {
        return theMeter;
    }

    /**
     * read resolve.
     * 
     * @return the resolved object.
     */
    @Override
    protected Object readResolve() {
        super.readResolve();
        String[] longData = NiceTable.Util.explode(',', data);
        resultSet = new double[longData.length];
        for (int i = 0; i < longData.length; i++) {
            resultSet[i] = Long.parseLong(longData[i]);
        }
        return this;
    }

    /**
     * returns the return values given as a parameter to the constructor.
     * 
     * @return just guess, dude.
     */
    public Object getReturnValues() {
        return returnValues;
    }

    /**
     * constructor.
     * 
     * @param aName
     *            the literal name you want to give the result.
     * @param aResultSet
     *            an array of long values to be computed.
     * @param theReturnValues
     *            an array of return values generated for each iteration.
     * @param whichMeter
     *            the meter with which the calculations were done.
     */
    public SingleResult(
            final String aName, final double[] aResultSet,
            final Object[] theReturnValues, final AbstractMeter whichMeter) {
        this(aName, aResultSet, whichMeter);
        returnValues = theReturnValues;

    }

    /**
     * the standard constructor.
     * 
     * @param name
     *            the name of the result.
     * @param aResultSet
     *            the results.
     * @param whichMeter
     *            the meter with which the calculations were done.
     */
    public SingleResult(
            final String name, final double[] aResultSet,
            final AbstractMeter whichMeter) {
        super();
        setName(name);
        /*
         * debug System.out.println(getName() + " " +
         * NiceTable.Util.implode(", ",aResultSet));
         */
        resultSet = filterNullValues(aResultSet);
        theMeter = whichMeter;
        data = NiceTable.Util.implode(",", resultSet);
    }

    /**
     * this is not a comment.
     * 
     * @see org.perfidix.result.IResult#getNumberOfAnnotatedRuns()
     */
    @Override
    public long getNumberOfRuns() {
        // debug System.out.println(getName() + " # " + resultSet.length);
        // the result set does not contain the ignored runs.
        return resultSet.length;
    }

    /**
     * i think there must be a simpler approach to this problem. the input array
     * given could contain some Benchmark.LONG_NULLVALUEs, which tell us that at
     * that iteration, the method or whatever wasn't called. for the current
     * computations it's not important to show these values, and such i remove
     * these and return a copy of the array. unfortunately, this renders the
     * algorithm's complexity to O(n^2) when any null value is found, and to
     * O(n) when no null value was found in the first run. because i first have
     * to iterate through the original array to estimate the result array's
     * length, and then i have to copy the values into the newly allocated
     * array. if there's a shorter method for this, feel free to tell me.
     * 
     * @param array
     * @return the filtered array.
     */
    private double[] filterNullValues(final double[] array) {
        int resultSetLength = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == Benchmark.LONG_NULLVALUE) {
                // debug System.out.println(getName() + " found null
                // value.");
                continue;
            } else {
                // debug System.out.println(
                // getName() + ": " + array[i] + "<>" +
                // Benchmark.LONG_NULLVALUE);
            }
            resultSetLength++;
        }

        if (resultSetLength == array.length) {
            return array;
        }

        double[] res = new double[resultSetLength];
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
     * returns the result set. this result set is filtered out of
     * 
     * @return the result set.
     */
    @Override
    public double[] getResultSet() {
        return resultSet.clone();
    }

    /**
     * accepts a result visitor.
     * 
     * @param v
     *            the visitor.
     */
    @Override
    public void accept(final ResultVisitor v) {
        v.visit(this);
    }

    /**
     * @return the unit with which the measurement was taken.
     */
    public String getUnit() {
        return theMeter.getUnit();
    }

    /**
     * returns the meter associated with this result.
     * 
     * @return the meter.
     */
    public AbstractMeter getMeter() {
        return theMeter;
    }

}