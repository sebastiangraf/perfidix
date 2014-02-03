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
package org.perfidix.ouput;


import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.Locale;

import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;


/**
 * The ResultVisitor is able to visit and view the results. The idea is that every implementing class can offer all
 * results as long as they are a {@link BenchmarkResult}. The implementing class should know how to handle these
 * results. Additionally to the visitor pattern, all inheriting class have to implement the listener pattern as well
 * since every output class has to provide functionality to handle listener events as well.
 * 
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 */
public abstract class AbstractOutput {

    /**
     * Constant to offer one fix format to display double-variables.
     */
    protected static final String FLOATFORMAT = "%05.2f";

    /**
     * Visiting the {@link BenchmarkResult} and do something with the result.
     * 
     * @param res the {@link BenchmarkResult}
     */
    public abstract void visitBenchmark (final BenchmarkResult res);

    /**
     * Listening to a resultset and handling the data.
     * 
     * @param meth the related {@link Method}
     * @param meter the corresponding {@link AbstractMeter} instance where the result is related to
     * @param data the related data
     */
    public abstract boolean listenToResultSet (final BenchmarkMethod meth, final AbstractMeter meter, final double data);

    /**
     * Listening to an arised exception.
     * 
     * @param exec an {@link AbstractPerfidixMethodException} instance
     */
    public abstract boolean listenToException (final AbstractPerfidixMethodException exec);

    /**
     * Formats a double.
     * 
     * @param toFormat the number to format
     * @see java.util.Formatter for the documentation.
     * @return the formatted string.
     */
    protected static final String format (final double toFormat) {
        return new Formatter(Locale.US).format(FLOATFORMAT, toFormat).toString();
    }

}
