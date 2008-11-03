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
 * $Id: ResultContainer.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.perfidix.Perfidix;
import org.perfidix.meter.AbsTimeMeter;
import org.perfidix.meter.AbstractMeter;

/**
 * the result container contains more results. it is by definition recursive, so
 * it can handle as much diversity as possible
 * 
 * @author axo axo@axolander.de
 * @since 08-2005
 * @version 0.8
 * @param <ResultType>
 *            the type of the children.
 */
public abstract class ResultContainer<ResultType extends Result> extends Result {

    private ArrayList<ResultType> children = new ArrayList<ResultType>();

    private Hashtable<AbstractMeter, ArrayList<SingleResult>> customChildren =
            new Hashtable<AbstractMeter, ArrayList<SingleResult>>();

    /**
     * default constructor.
     */
    public ResultContainer() {
        this(Result.DEFAULT_NAME);
    }

    /**
     * @param name
     *            the name of the result.
     */
    public ResultContainer(final String name) {
        super();
        setName(name);
    }

    /**
     * appends a result to the children.
     * 
     * @param res
     *            the result to append.
     */
    public void append(final ResultType res) {

        try {
            String logMessage = "" + res.getName() + " ... ";

            if (isCustomMeterResult(res)) {
                appendToCustomMeterStack((SingleResult) res);
                return;
            }
            logMessage += "" + res.getName() + " is a timed result.";
            children.add(res);

            if (res instanceof SingleResult) {
                appendToCustomMeterStack((SingleResult) res);
            }

        } catch (Exception e) {
            // do nothing.
        }
    }

    private void appendToCustomMeterStack(final SingleResult res) {
        String logMessage = "";

        AbstractMeter resMeter = res.getMeter();
        if (!customChildren.containsKey(resMeter)) {
            logMessage += " new meter " + resMeter.getUnit();
            customChildren.put(resMeter, new ArrayList<SingleResult>());
        }
        logMessage += " appending " + res.getName() + " ";
        customChildren.get(resMeter).add(res);
    }

    /**
     * checks whether it's a custom meter result or not.
     * 
     * @param res
     * @return
     */
    private boolean isCustomMeterResult(final ResultType res) {
        if (!(res instanceof SingleResult)) {
            return false;
        }
        if (((SingleResult) res).getMeter() instanceof AbsTimeMeter) {
            return false;
        }
        return true;
    }

    /**
     * returns the full result set on all children. this works recursively. the
     * recursion takes place by calling each child's sum() operation.
     * 
     * @return the result set.
     */
    @Override
    public long[] getResultSet() {
        int resultLength = children.size();
        long[] theResult = new long[resultLength];
        for (int i = 0; i < resultLength; i++) {
            theResult[i] = children.get(i).sum();
        }
        return theResult;
    }

    /**
     * @param meterName
     *            the unique identifier for the meter involved.
     * @return the maximum value.
     */
    public long max(final AbstractMeter meterName) {
        return computeMax(getResultSet(meterName));
    }

    /**
     * @param m
     *            the meter to fetch the result set for.
     * @return the result set allocated to the meter.
     */
    private long[] getResultSet(final AbstractMeter m) {
        if (null == m) {
            return new long[] {};
        }
        ArrayList<SingleResult> theSingleResults = getSingleResultsFor(m);
        long[] theResult = new long[theSingleResults.size()];
        for (int i = 0; i < theSingleResults.size(); i++) {
            theResult[i] = theSingleResults.get(i).sum();
        }

        return theResult;

    }

    /**
     * yes, the casting may be unchecked.
     * 
     * @param meter
     * @return
     */
    @SuppressWarnings("unchecked")
    private ArrayList<SingleResult> getSingleResultsFor(
            final AbstractMeter meter) {
        ArrayList<SingleResult> theList = new ArrayList<SingleResult>();

        if (customChildren.containsKey(meter)) {
            theList.addAll(customChildren.get(meter));
        } else {
            // do nothing.
        }

        for (int i = 0; i < children.size(); i++) {
            Result tmp = children.get(i);
            if (tmp instanceof ResultContainer) {
                theList.addAll(((ResultContainer) tmp)
                        .getSingleResultsFor(meter));
            }
        }
        return theList;
    }

    /**
     * returns all children.
     * 
     * @return the children being in this result container.
     */
    public ArrayList<ResultType> getChildren() {
        return children;
    }

    /**
     * accepts a result visitor.
     * 
     * @param v
     *            the visitor
     */
    @Override
    public void accept(final ResultVisitor v) {
        v.visit(this);

    }

    /**
     * computes the minimum result for the given meter name.
     * 
     * @param m
     *            the meter which is used.
     * @return the minimum value.
     */
    public long min(final AbstractMeter m) {
        return computeMin(getResultSet(m));
    }

    /**
     * this is not a comment.
     * 
     * @param m
     *            the meter which is used.
     * @return the result.
     */
    public double avg(final AbstractMeter m) {
        return computeMean(getResultSet(m));
    }

    /**
     * this is not a comment.
     * 
     * @param m
     *            the meter.
     * @return the result.
     */
    public double mean(final AbstractMeter m) {
        return computeMean(getResultSet(m));
    }

    /**
     * this is not a comment.
     * 
     * @param m
     *            the meter name.
     * @return the result.
     */
    public long squareSum(final AbstractMeter m) {
        return computeSquareSum(getResultSet(m));
    }

    /**
     * this is not a comment.
     * 
     * @param m
     *            the meter.
     * @return the result.
     */
    public int resultCount(final AbstractMeter m) {
        return getResultSet(m).length;
    }

    /**
     * this is not a comment.
     * 
     * @param m
     *            the meter.
     * @return the result.
     */
    public double variance(final AbstractMeter m) {
        return computeVariance(getResultSet(m));
    }

    /**
     * this is not a comment.
     * 
     * @param m
     *            the meter name.
     * @return the result.
     */
    public double median(final AbstractMeter m) {
        return computeMedian(getResultSet(m));
    }

    /**
     * @param m
     *            the meter name.
     * @return the sum.
     */
    public long sum(final AbstractMeter m) {
        return computeSum(getResultSet(m));
    }

    /**
     * this is not a comment.
     * 
     * @param meterName
     *            the meter name.
     * @return the result.
     */
    public double getConf99(final AbstractMeter meterName) {
        return computeConf99(getResultSet(meterName));
    }

    /**
     * this is not a comment.
     * 
     * @param meterName
     *            the meter name.
     * @return the result.
     */
    public double getConf95(final AbstractMeter meterName) {
        return computeConf95(getResultSet(meterName));
    }

    /**
     * this is not a comment.
     * 
     * @param meterName
     *            the meter name.
     * @return the result.
     */
    public double getStandardDeviation(final AbstractMeter meterName) {
        return computeStandardDeviation(getResultSet(meterName));
    }

    /**
     * @return bla.
     */
    public Hashtable<AbstractMeter, ArrayList<SingleResult>> getCustomChildren() {
        return customChildren;

    }

    /**
     * Returning all registered meters.
     * 
     * @return the meters
     */
    public Set<AbstractMeter> getRegisteredMeters() {
        final Set<AbstractMeter> meters = new HashSet<AbstractMeter>();
        for (final ResultType child : children) {
            if (child instanceof ResultContainer) {
                meters.addAll(((ResultContainer) child).getRegisteredMeters());
            }
            if (child instanceof SingleResult) {
                meters.add(((SingleResult) child).getMeter());
            }
        }
        for (final AbstractMeter meter : customChildren.keySet()) {
            meters.add(meter);
        }

        return meters;
    }

    /**
     * {@inheritDoc}
     */
    public AbstractMeter getDefaultMeter() {
        final Set<AbstractMeter> meters = getRegisteredMeters();
        if (meters.isEmpty()) {
            return Perfidix.DEFAULTMETER;
        }
        return meters.iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    public long getNumberOfRuns() {
        Iterator<ResultType> cust = getChildren().iterator();
        long numberOfRuns = 0;
        while (cust.hasNext()) {
            numberOfRuns += cust.next().getNumberOfRuns();
        }
        return numberOfRuns;
    }

}
