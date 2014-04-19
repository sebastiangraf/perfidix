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


import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.AbstractOutput;

import java.util.HashSet;
import java.util.Set;


/**
 * This class holds the data of a complete Benchmark. Only one <code>BenchmarkResult</code> is generated per
 * Benchmark-Run. The different components are {@link ClassResult} objects. This class is the container for the results
 * of a Benchmark and therefore the input for different Outputs.
 *
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 * @see org.perfidix.Benchmark
 * @see AbstractOutput
 */
public final class BenchmarkResult extends AbstractResultContainer<ClassResult> {

    /**
     * All occured exceptions.
     */
    private transient final Set<AbstractPerfidixMethodException> exceptions;

    /**
     * Outputs for listeners.
     */
    private transient final AbstractOutput[] outputs;

    /**
     * Constructor.
     *
     * @param paramOutputs {@link AbstractOutput} instances for listener
     */
    public BenchmarkResult(final AbstractOutput... paramOutputs) {
        super(null);
        this.exceptions = new HashSet<AbstractPerfidixMethodException>();
        outputs = paramOutputs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElementName() {
        return "Benchmark";
    }

    /**
     * Adding a dataset to a given meter and adapting the underlaying result model.
     *
     * @param meth  where the result is corresponding to
     * @param meter where the result is corresponding to
     * @param data  the data itself
     */
    public void addData(final BenchmarkMethod meth, final AbstractMeter meter, final double data) {

        final Class<?> clazz = meth.getMethodToBench().getDeclaringClass();
        if (!elements.containsKey(clazz)) {
            elements.put(clazz, new ClassResult(clazz));
        }

        final ClassResult clazzResult = elements.get(clazz);

        if (!clazzResult.elements.containsKey(meth)) {
            clazzResult.elements.put(meth, new MethodResult(meth));
        }

        final MethodResult methodResult = clazzResult.elements.get(meth);
        methodResult.addData(meter, data);
        clazzResult.addData(meter, data);
        this.addData(meter, data);

        for (final AbstractOutput output : outputs) {
            output.listenToResultSet(meth, meter, data);
        }

    }

    /**
     * Adding an exception to this result.
     *
     * @param exec the exception stored to this result
     */
    public void addException(final AbstractPerfidixMethodException exec) {
        this.getExceptions().add(exec);
        for (final AbstractOutput output : outputs) {
            output.listenToException(exec);
        }
    }

    /**
     * Getter for member exceptions.
     *
     * @return the exceptions
     */
    public Set<AbstractPerfidixMethodException> getExceptions() {
        return exceptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(super.toString());
        builder.append("\nexceptions: ").append(getExceptions());
        return builder.toString();
    }

}
