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
 * $Id: Result.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.result;

import java.util.Arrays;

import org.perfidix.Benchmark;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.visitor.AsciiTable;
import org.perfidix.visitor.ResultVisitor;

/**
 * a base class for result sets.
 * 
 * @author axo
 * @since 10.12.2005
 */
public abstract class AbstractResult {

    /**
     * if any result computation overflows (as can happen with the nanometer on
     * slow methods), this value will be taken as a response. i know that the
     * correct way would be to throw an exception, but this means that i would
     * have to perform try/catch routines on almost every bloody computation,
     * and i am really too lazy to do this.
     */
    public static final double OVERFLOW = 0xDEAD;

    /**
     * the name of the result.
     */
    private String name;

    /**
     * an indicator that a cache value is not set.
     */
    private static final int INTEGER_NOVALUE = -1;

    /**
     * the same indicator for doubles.
     */
    private static final double DOUBLE_NOVALUE = -1;

    /**
     * the default name of a result.
     */
    public static final String DEFAULT_NAME = "<noNameDefined>";

    /**
     * Factor of nearly 2 standardfailures
     */
    private static final double CONF95_FACTOR = 1.96;

    /**
     * Factor of 2 and a half standardfailures
     */
    private static final double CONF99_FACTOR = 2.576;

    /**
     * temp cache. this 'caching' ist not implemented as it should be. the
     * reason is that normally i should have implemented it with an ArrayList
     * containing Number objects or by simply using a proxy Result which
     * computes and stores the values. after a simple benchmark i saw that using
     * objects like that would result in a performance loss of about 40-60
     * percent with the computations. so i changed this to use the simplistic
     * but redundant approach of enumerated elements.
     */
    private transient double mean = AbstractResult.DOUBLE_NOVALUE;

    private transient double variance = AbstractResult.DOUBLE_NOVALUE;

    private transient long min = AbstractResult.INTEGER_NOVALUE;

    private transient long max = AbstractResult.INTEGER_NOVALUE;

    private transient long squareSum = AbstractResult.INTEGER_NOVALUE;

    private transient double median = AbstractResult.INTEGER_NOVALUE;

    private transient double standardDeviation = AbstractResult.DOUBLE_NOVALUE;

    private transient long resultCount = AbstractResult.INTEGER_NOVALUE;

    private transient long sum = AbstractResult.INTEGER_NOVALUE;

    /**
     * default constructor.
     */
    public AbstractResult() {
        init();
    }

    /**
     * initializes itself. needed for the xstream loading.
     */
    protected final void init() {
        resultCount = AbstractResult.INTEGER_NOVALUE;
        sum = AbstractResult.INTEGER_NOVALUE;
        standardDeviation = AbstractResult.DOUBLE_NOVALUE;
        median = AbstractResult.INTEGER_NOVALUE;
        squareSum = AbstractResult.INTEGER_NOVALUE;
        max = AbstractResult.INTEGER_NOVALUE;
        min = AbstractResult.INTEGER_NOVALUE;
        variance = AbstractResult.DOUBLE_NOVALUE;
        mean = AbstractResult.DOUBLE_NOVALUE;

    }

    /**
     * needed for the xstream implementation. obsolete
     * 
     * @return an initialized object instance.
     */
    protected Object readResolve() {
        this.init();
        return this;
    }

    /**
     * computes the default meter, which will be used as a reference when
     * calling methods without the IMeter parameter.
     * 
     * @return the default meter
     */
    public abstract AbstractMeter getDefaultMeter();

    /**
     * @return the result set of a benchmark.
     */

    public abstract long getNumberOfRuns();

    /**
     * simple getter.
     * 
     * @return the result name
     */
    public final String getName() {
        return name;
    }

    /**
     * simple setter.
     * 
     * @param someName
     *            to give the result a name.
     */
    protected final void setName(final String someName) {
        this.name = someName;
    }

    /**
     * an array of all data items in the structure.
     * 
     * @return the result set.
     */
    public abstract long[] getResultSet();

    /**
     * tells the object to accept a visitor.
     * 
     * @param r
     *            the visitor to accept.
     */
    public abstract void accept(ResultVisitor r);

    /**
     * computes the number of data fields available. this is the number of data
     * items for non-skipped runs.
     * 
     * @return number of data fields.
     */
    public final long resultCount() {
        if (resultCount != AbstractResult.INTEGER_NOVALUE) {
            return resultCount;
        }
        resultCount = getResultSet().length;
        return resultCount;
    }

    /**
     * returns the artithmetic mean of the result set. avg() is an alias for
     * this method.
     * 
     * @see #avg
     * @return the mean values.
     */
    public final double mean() {
        if (mean != AbstractResult.DOUBLE_NOVALUE) {
            return mean;
        }

        mean = computeMean(getResultSet());
        return mean;
    }

    /**
     * computes the variance.
     * 
     * @return double
     */
    public final double variance() {
        if (variance != AbstractResult.DOUBLE_NOVALUE) {
            return variance;
        }
        variance = computeVariance();
        return variance;
    }

    /**
     * the more exact computation of the variance.
     * 
     * @return the variance of the data set.
     */
    private final double computeVariance() {
        return computeVariance(getResultSet());

    }

    /**
     * computes the middle value of the set distribution. the value of this
     * median needs not necessarily be within the given resultSet length, but
     * will be the computed absolute value. the result set values are sorted
     * first, and if the result set is even, 0.5 * ( the two middle values) is
     * being taken. if the result set is odd, the middle value is being taken.
     * 
     * @return the median
     */
    public final double median() {

        if (median != AbstractResult.INTEGER_NOVALUE) {
            return median;
        }
        median = computeMedian(getResultSet());
        return median;
    }

    /**
     * computes the square sum of the elements.
     * 
     * @return the square sum.
     */
    public final long squareSum() {
        if (squareSum != AbstractResult.INTEGER_NOVALUE) {
            return squareSum;
        }
        squareSum = computeSquareSum(getResultSet());
        return squareSum;
    }

    /**
     * computes the standard deviation.
     * 
     * @return the standard deviation
     */
    public final double getStandardDeviation() {
        if (standardDeviation != AbstractResult.DOUBLE_NOVALUE) {
            return standardDeviation;
        }
        standardDeviation = computeStandardDeviation(getResultSet());
        return standardDeviation;
    }

    /**
     * bla.
     * 
     * @param resSet
     *            the data
     * @return bla.
     */
    protected final double computeStandardDeviation(final long[] resSet) {
        if (resSet.length < 1) {
            return 0.0;
        }
        return Math.sqrt((1.0 / resSet.length * computeSquareSum(resSet))
                - Math.pow(computeMean(resSet), 2.0));
    }

    /**
     * the bla.
     * 
     * @param resSet
     *            the bla.
     * @return bla.
     */
    protected final long computeSquareSum(final long[] resSet) {
        long s = 0;
        for (int i = 0; i < resSet.length; i++) {
            s += Math.pow(resSet[i], 2);
            if (s >= Long.MAX_VALUE) {
                throw new NumberFormatException();
            }
        }
        return s;
    }

    /**
     * the bla.
     * 
     * @param resSet
     *            the bla.
     * @return bla.
     */
    protected double computeVariance(final long[] resSet) {
        if (resSet.length < 2) {
            return 0.0;
        }
        long n = 0;
        double aS = 0.0;
        double aMean = 0;
        for (int i = 0; i < resSet.length; i++) {
            n++;
            double delta = resSet[i] - aMean;
            aMean += delta / n;
            aS += delta * (resSet[i] - aMean);
        }
        return (aS / (n - 1));
    }

    /**
     * the bla.
     * 
     * @param resSet
     *            the bla.
     * @return bla.
     */
    protected double computeMedian(final long[] aesSet) {
        if (aesSet.length < 1) {
            return 0.0;
        }
        long[] resSet = aesSet.clone();
        Arrays.sort(resSet);

        if (resSet.length % 2 == 1) {
            return resSet[resSet.length / 2];
        }
        int firstHalf = (resSet.length - 1) / 2;
        int secondHalf = firstHalf + 1;
        return (resSet[firstHalf] / 2.0 + resSet[secondHalf] / 2.0);
    }

    /**
     * the bla.
     * 
     * @param resSet
     *            the bla.
     * @return bla.
     */
    protected double computeConf99(final long[] resSet) {
        return computeConf(AbstractResult.CONF99_FACTOR, resSet);
    }

    /**
     * the bla.
     * 
     * @param resSet
     *            the bla.
     * @return bla.
     */
    protected double computeConf95(final long[] resSet) {
        return computeConf(AbstractResult.CONF95_FACTOR, resSet);
    }

    private double computeConf(final double staticFactor, final long[] resSet) {
        return staticFactor
                * (computeStandardDeviation(resSet) / Math.sqrt(resSet.length));
    }

    /**
     * computes the average value of the data set. alias for mean().
     * 
     * @see #mean()
     * @return the average runtime.
     */
    public final double avg() {
        return mean();
    }

    /**
     * computes the sum over all data items.
     * 
     * @return the sum of all runs.
     */
    public final long sum() {
        if (sum != AbstractResult.INTEGER_NOVALUE) {
            return sum;
        }
        sum = computeSum(getResultSet());
        return sum;
    }

    /**
     * computes the minimum.
     * 
     * @return the minimum result value.
     */
    public long min() {
        if (min != AbstractResult.INTEGER_NOVALUE) {
            return min;
        }
        min = computeMin(getResultSet());
        return min;
    }

    /**
     * returns the confidence 99 interval since it's a +/- thing, the first one
     * is the "+" one, the second array value is the "-" one.
     * 
     * @return the 99% confidence
     */
    public final double getConf99() {

        return computeConf99(getResultSet());

    }

    /**
     * the 95% confidence the "+" value is the first one.
     * 
     * @return the 95% confidence TODO enum implementation of CONF ?
     */
    public final double getConf95() {
        return computeConf95(getResultSet());
    }

    /**
     * computes the maximum value of our data set.
     * 
     * @return the maximum runtime
     */
    public final long max() {
        if (max != AbstractResult.INTEGER_NOVALUE) {
            return max;
        }
        max = computeMax(getResultSet());
        return max;
    }

    /**
     * computes the minimum value.
     * 
     * @param data
     *            the array,
     * @return the result.
     */
    protected final long computeMin(final long[] data) {
        if (data.length < 1) {
            return 0;
        }
        Arrays.sort(data);
        return data[0];
    }

    /**
     * computes the maximum from a given array.
     * 
     * @param data
     *            the array.
     * @return the maximum value.
     */
    protected final long computeMax(final long[] data) {
        if (data.length < 1) {
            return 0;
        }
        Arrays.sort(data);
        return data[data.length - 1];
    }

    /**
     * computes the sum, goddamn.
     * 
     * @param resSet
     *            the data.
     * @return the sum.
     */
    protected final long computeSum(final long[] resSet) {
        long theSum = 0;
        for (int i = 0; i < resSet.length; i++) {
            if (resSet[i] == Benchmark.LONG_NULLVALUE) {
                continue;
            }
            theSum += resSet[i];
            if (theSum < 0) {
                throw new NumberFormatException(
                        "sum() computed negative values, which means that an integer"
                                + " overflow occured. perhaps the meter you used to compute"
                                + " the result is too exact.");

            }
        }

        return theSum;
    }

    /**
     * computes the mean of a given result set.
     * 
     * @param resSet
     *            array.
     * @return mean value.
     */
    protected final double computeMean(final long[] resSet) {
        if (resSet.length < 1) {
            return 0.0;
        }
        return (double) computeSum(resSet) / (double) resSet.length;
    }

    /**
     * @param indent
     *            the number of indentations to use.
     * @param displayRawData
     *            whether to show the raw data also.
     * @return string
     */
    protected final String toString(
            final int indent, final boolean displayRawData) {

        AsciiTable v = new AsciiTable();
        v.visit(this);
        return v.toString()
                + (displayRawData ? "\n"
                        + NiceTable.Util.implode(",", getResultSet()) : "");
    }

    /**
     * @param displayRawData
     *            whether to display the raw results or not.
     * @return string
     */
    public final String toString(final boolean displayRawData) {

        return toString(-1, displayRawData);
    }

    /**
     * to string implementation.
     * 
     * @return the result.
     */
    @Override
    public final String toString() {
        return toString(-1, false);

    }

    /**
     * checks whether another result set equals the own one.
     * 
     * @return whether another object equals the current one.
     * @param r
     *            the result to compare with
     */
    public final boolean equals(final AbstractResult r) {

        if (!r.name.equals(this.name)) {
            return false;
        }
        long[] resultSet = r.getResultSet();
        long[] myResult = getResultSet();

        if (resultSet.length != myResult.length) {
            return false;
        }
        for (int i = 0; i < resultSet.length; i++) {
            if (resultSet[i] != myResult[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * computes the hash code. only to override the original implementation,
     * since it's not used here.
     * 
     * @return always 0
     */
    @Override
    public final int hashCode() {
        return 0;
    }

}
