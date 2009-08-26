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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.AbstractOutput;

/**
 * This class holds the data of the whole benchmark in different
 * {@link ClassResult} objects.
 * 
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 */
public final class BenchmarkResult extends AbstractResultContainer<ClassResult> {

    /** All occured exceptions. */
    private transient final Set<AbstractPerfidixMethodException> exceptions;

    /** Outputs for listeners. */
    private transient final AbstractOutput[] outputs;

    /**
     * Constructor.
     * 
     * @param paramOutputs
     *            {@link AbstractOutput} instances for listener
     */
    public BenchmarkResult(final AbstractOutput... paramOutputs) {
        super(null);
        this.exceptions = new HashSet<AbstractPerfidixMethodException>();
        outputs = paramOutputs;
    }

    /** {@inheritDoc} */
    @Override
    public String getElementName() {
        return "Benchmark";
    }

    /**
     * Adding a dataset to a given meter and adapting the underlaying result
     * model.
     * 
     * @param meth
     *            where the result is corresponding to
     * @param meter
     *            where the result is corresponding to
     * @param data
     *            the data itself
     */
    public void addData(
            final Method meth, final AbstractMeter meter, final double data) {

        final Class<?> clazz = meth.getDeclaringClass();
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
     * @param exec
     *            the exception stored to this result
     */
    public void addException(final AbstractPerfidixMethodException exec) {
        // TODO Exception-Name mitgeben
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
