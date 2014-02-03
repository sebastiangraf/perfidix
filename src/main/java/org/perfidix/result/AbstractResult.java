/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.result;


import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.primitives.adapters.CollectionDoubleCollection;
import org.apache.commons.math.stat.descriptive.AbstractUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.descriptive.rank.Min;
import org.apache.commons.math.stat.descriptive.rank.Percentile;
import org.apache.commons.math.stat.descriptive.summary.Sum;
import org.apache.commons.math.stat.descriptive.summary.SumOfSquares;
import org.perfidix.meter.AbstractMeter;


/**
 * Results which are generated through the benchmark are stored in the inherted implementation of this class. The
 * storage is done corresponding to the mapping of meters.
 * 
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 */
public abstract class AbstractResult {

    /** Related element of this container. Can be a method or a class. */
    private transient final Object relatedElement;

    /**
     * Results mapped to the meters.
     */
    private transient final Map<AbstractMeter , Collection<Double>> meterResults;

    /**
     * Constructor with a given name.
     * 
     * @param paramElement element to this result.
     */
    protected AbstractResult (final Object paramElement) {
        this.relatedElement = paramElement;
        this.meterResults = new Hashtable<AbstractMeter , Collection<Double>>();

    }

    /**
     * Returns the name of the related element where these results belong to.
     * 
     * @return the name as a String.
     */
    public abstract String getElementName ();

    /**
     * an array of all data items in the structure.
     * 
     * @param meter for the results wanted
     * @return the result set.
     */
    public final Collection<Double> getResultSet (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        return this.meterResults.get(meter);
    }

    /**
     * Getting all meters registered in this result.
     * 
     * @return a set of all meters.
     */
    public final Set<AbstractMeter> getRegisteredMeters () {
        return this.meterResults.keySet();
    }

    /**
     * Returns the arithmetic mean of the result set. avg() is an alias for this method.
     * 
     * @param meter the meter of the mean
     * @return the mean value.
     */
    public final double mean (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        final AbstractUnivariateStatistic mean = new Mean();
        final CollectionDoubleCollection doubleColl = new CollectionDoubleCollection(this.meterResults.get(meter));
        return mean.evaluate(doubleColl.toArray(), 0, doubleColl.toArray().length);
    }

    /**
     * Computes the square sum of the elements.
     * 
     * @param meter the meter of the mean
     * @return the square sum.
     */
    public final double squareSum (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        final AbstractUnivariateStatistic sqrSum = new SumOfSquares();
        final CollectionDoubleCollection doubleColl = new CollectionDoubleCollection(this.meterResults.get(meter));
        return sqrSum.evaluate(doubleColl.toArray(), 0, doubleColl.toArray().length);
    }

    /**
     * Computes the standard deviation.
     * 
     * @param meter the meter of the mean
     * @return the standard deviation
     */
    public final double getStandardDeviation (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        final AbstractUnivariateStatistic stdDev = new StandardDeviation();
        final CollectionDoubleCollection doubleColl = new CollectionDoubleCollection(this.meterResults.get(meter));
        return stdDev.evaluate(doubleColl.toArray(), 0, doubleColl.toArray().length);
    }

    /**
     * Computes the sum over all data items.
     * 
     * @param meter the meter of the mean
     * @return the sum of all runs.
     */
    public final double sum (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        final AbstractUnivariateStatistic sum = new Sum();
        final CollectionDoubleCollection doubleColl = new CollectionDoubleCollection(this.meterResults.get(meter));
        return sum.evaluate(doubleColl.toArray(), 0, doubleColl.toArray().length);
    }

    /**
     * Computes the minimum.
     * 
     * @param meter the meter of the mean
     * @return the minimum result value.
     */
    public final double min (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        final AbstractUnivariateStatistic min = new Min();
        final CollectionDoubleCollection doubleColl = new CollectionDoubleCollection(this.meterResults.get(meter));
        return min.evaluate(doubleColl.toArray(), 0, doubleColl.toArray().length);
    }

    /**
     * Computes the confidence 05 interval-factor. This value has to be combined with the mean to get the
     * confidence-interval.
     * 
     * @param meter the meter for the 05-confidence interval factor
     * @return the 99% confidence
     */
    public final double getConf05 (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        final AbstractUnivariateStatistic conf05 = new Percentile(5.0);
        final CollectionDoubleCollection doubleColl = new CollectionDoubleCollection(this.meterResults.get(meter));
        return conf05.evaluate(doubleColl.toArray(), 0, doubleColl.toArray().length);

    }

    /**
     * Computes the confidence 95 interval-factor. This value has to be combined with the mean to get the
     * confidence-interval.
     * 
     * @param meter the meter for the 95-confidence interval factor
     * @return the 95% confidence
     */
    public final double getConf95 (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        final AbstractUnivariateStatistic conf95 = new Percentile(95.0);
        final CollectionDoubleCollection doubleColl = new CollectionDoubleCollection(this.meterResults.get(meter));
        return conf95.evaluate(doubleColl.toArray(), 0, doubleColl.toArray().length);
    }

    /**
     * Computes the maximum.
     * 
     * @param meter the meter of the mean
     * @return the maximum result value.
     */
    public final double max (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        final AbstractUnivariateStatistic max = new Max();
        final CollectionDoubleCollection doubleColl = new CollectionDoubleCollection(this.meterResults.get(meter));
        return max.evaluate(doubleColl.toArray(), 0, doubleColl.toArray().length);
    }

    /**
     * Returning the number of results for one specific meter.
     * 
     * @param meter to get the number of results
     * @return the number of results of one meter
     */
    public final int getNumberOfResult (final AbstractMeter meter) {
        checkIfMeterExists(meter);
        return meterResults.get(meter).size();
    }

    /**
     * Getter for the related element where this container corresponds to.
     * 
     * @return the relatedElement
     */
    public final Object getRelatedElement () {
        return relatedElement;
    }

    /**
     * Adding a data to a meter.
     * 
     * @param meter the related meter
     * @param data the data to be added
     */
    protected final void addData (final AbstractMeter meter, final double data) {
        checkIfMeterExists(meter);
        meterResults.get(meter).add(data);
    }

    /**
     * Checking method if meter is registered, otherwise inserting a suitable data structure.
     * 
     * @param meter to be checked
     */
    private void checkIfMeterExists (final AbstractMeter meter) {
        if (!meterResults.containsKey(meter)) {
            meterResults.put(meter, new LinkedList<Double>());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString () {
        final StringBuilder builder = new StringBuilder();
        builder.append(getElementName()).append("\nmeters: ").append(getRegisteredMeters()).append("\nresults: ").append(meterResults);
        return builder.toString();
    }

}
