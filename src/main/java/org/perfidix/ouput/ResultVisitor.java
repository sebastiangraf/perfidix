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

import java.io.OutputStream;
import java.util.Formatter;

import org.perfidix.result.AbstractResult;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;
import org.perfidix.result.ResultContainer;

/**
 * The ResultVisitor is able to visit and view the results. The idea is that
 * every implementing class can offer all results as long as they are a
 * {@link ResultContainer}. The implementing class should know how to handle
 * these results.
 * 
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class ResultVisitor {

    private OutputStream currentBenchmarkOutputStream;

    private OutputStream currentClassOutputStream;

    private OutputStream currentMethodOutputStream;

    /**
     * Constant to offer one fix format to display double-variables.
     */
    protected final static String FLOATFORMAT = "%05.2f";

    /**
     * Visiting the results and invoking specific handling for elements.
     * 
     * @param benchRes
     *            the Result to visit.
     */
    public final void visit(final BenchmarkResult benchRes) {
        handleBenchmarkResult(benchRes);
        for (final AbstractResult absResult1 : benchRes.getIncludedResults()) {
            final ClassResult classRes = (ClassResult) absResult1;
            handleClassResult(classRes);
            for (final AbstractResult absResult2 : classRes
                    .getIncludedResults()) {
                final MethodResult methRes = (MethodResult) absResult2;
                handleMethodResult(methRes);
            }
        }
    }

    /**
     * Handling the {@link BenchmarkResult}. That means, printing out the name,
     * conf-intervals, raw data, etc. of the benchmark.
     * 
     * @param benchRes
     *            the benchresult to be evaluated.
     */
    protected abstract void handleBenchmarkResult(final BenchmarkResult benchRes);

    /**
     * Handling the {@link ClassResult}. That means, printing out the name,
     * conf-intervals, raw data, etc. of a class.
     * 
     * @param classRes
     *            the classResult to be evaluated.
     */
    protected abstract void handleClassResult(final ClassResult classRes);

    /**
     * Handling the {@link MethodResult}. That means, printing out the name,
     * conf-intervals, raw data, etc. of a class.
     * 
     * @param methRes
     *            the methResult to be evaluated.
     */
    protected abstract void handleMethodResult(final MethodResult methRes);

    /**
     * Formats a double.
     * 
     * @param i
     *            the number to format
     * @see java.util.Formatter for the documentation.
     * @return the formatted string.
     */
    protected final String format(final double i) {
        return new Formatter().format(FLOATFORMAT, i).toString();
    }

    /**
     * Getter for currentBenchmarkOutputStream.
     * 
     * @return the currentBenchmarkOutputStream
     */
    protected final OutputStream getCurrentBenchmarkOutputStream() {
        return currentBenchmarkOutputStream;
    }

    /**
     * Setter for currentBenchmarkOutputStream.
     * 
     * @param currentBenchmarkOutputStream
     *            the currentBenchmarkOutputStream to set
     */
    protected final void setCurrentBenchmarkOutputStream(
            OutputStream currentBenchmarkOutputStream) {
        this.currentBenchmarkOutputStream = currentBenchmarkOutputStream;
    }

    /**
     * Getter for currentClassOutputStream.
     * 
     * @return the currentClassOutputStream
     */
    protected final OutputStream getCurrentClassOutputStream() {
        return currentClassOutputStream;
    }

    /**
     * Setter for currentClassOutputStream.
     * 
     * @param currentClassOutputStream
     *            the currentClassOutputStream to set
     */
    protected final void setCurrentClassOutputStream(
            OutputStream currentClassOutputStream) {
        this.currentClassOutputStream = currentClassOutputStream;
    }

    /**
     * Getter for currentMethodOutputStream.
     * 
     * @return the currentMethodOutputStream
     */
    protected final OutputStream getCurrentMethodOutputStream() {
        return currentMethodOutputStream;
    }

    /**
     * Setter for currentMethodOutputStream.
     * 
     * @param currentMethodOutputStream
     *            the currentMethodOutputStream to set
     */
    protected final void setCurrentMethodOutputStream(
            OutputStream currentMethodOutputStream) {
        this.currentMethodOutputStream = currentMethodOutputStream;
    }

}
