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
 * $Id: ResultVisitor.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ouput;

import java.lang.reflect.Method;
import java.util.Formatter;

import org.perfidix.failureHandling.PerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;

/**
 * The ResultVisitor is able to visit and view the results. The idea is that
 * every implementing class can offer all results as long as they are a
 * {@link BenchmarkResult}. The implementing class should know how to handle
 * these results. Additionally to the visitor pattern, all inheriting class have
 * to implement the listener pattern as well since every output class has to
 * provide functionality to handle listener events as well.
 * 
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 */
public abstract class AbstractOutput {

    /**
     * Constant to offer one fix format to display double-variables.
     */
    protected final static String FLOATFORMAT = "%05.2f";

    /**
     * Visiting the {@link BenchmarkResult} and do something with the result
     * 
     * @param res
     *            the {@link BenchmarkResult}
     */
    public abstract void visitBenchmark(final BenchmarkResult res);

    /**
     * Listening to a resultset and handling the data
     * 
     * @param meth
     *            the related {@link Method}
     * @param meter
     *            the corresponding {@link AbstractMeter} instance where the
     *            result is related to
     * @param data
     *            the related data
     */
    public abstract void listenToResultSet(
            final Method meth, final AbstractMeter meter, final double data);

    /**
     * Listening to an arised exception
     * 
     * @param exec
     *            an {@link PerfidixMethodException} instance
     */
    public abstract void listenToException(final PerfidixMethodException exec);

    /**
     * Formats a double.
     * 
     * @param i
     *            the number to format
     * @see java.util.Formatter for the documentation.
     * @return the formatted string.
     */
    protected static final String format(final double i) {
        return new Formatter().format(FLOATFORMAT, i).toString();
    }

}
