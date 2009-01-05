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

import org.perfidix.Benchmark;
import org.perfidix.failureHandling.PerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.BenchmarkListener;

/**
 * This class holds the data of the whole benchmark in different
 * {@link ClassResult} objects.
 * 
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 */
public final class BenchmarkResult extends ResultContainer<ClassResult> {

    /**
     * Used {@link BenchmarkListener} instance for getting notification about
     * upcoming results.
     */
    private final BenchmarkListener listener;

    /** All occured exceptions */
    private final Set<PerfidixMethodException> exceptions;

    /**
     * Constructor.
     * 
     * @param benchmark
     *            related {@link Benchmark} instance
     * @param paramListener
     *            bound {@link BenchmarkListener} instance
     */
    public BenchmarkResult(
            final Benchmark benchmark, final BenchmarkListener paramListener) {
        super(benchmark);
        this.listener = paramListener;
        this.exceptions = new HashSet<PerfidixMethodException>();
    }

    /**
     * Constructor.
     * 
     * @param benchmark
     *            related {@link Benchmark} instance
     */
    public BenchmarkResult(final Benchmark benchmark) {
        super(benchmark);
        this.listener = new DummyListener();
        this.exceptions = new HashSet<PerfidixMethodException>();
    }

    /** {@inheritDoc} */
    @Override
    public final String getElementName() {
        return "Benchmark";
    }

    /**
     * Adding a dataset to a given meter and adapting the underlaying result
     * model
     * 
     * @param meth
     *            where the result is corresponding to
     * @param meter
     *            where the result is corresponding to
     * @param data
     *            the data itself
     */
    public final void addData(
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

        clazzResult.updateStructure(methodResult, meter, data);
        this.updateStructure(clazzResult, meter, data);

        listener.notify(meth, meter, data);
    }

    /**
     * Adding an exception to this result
     * 
     * @param exec
     *            the exception stored to this result
     */
    public final void addException(final PerfidixMethodException exec) {
        this.exceptions.add(exec);
        listener.notifyException(exec);
    }

    /**
     * This class acts as a dummy for making nothing as a listener.
     * 
     * @author Sebastian Graf, University of Konstanz
     */
    private class DummyListener extends BenchmarkListener {

        /** {@inheritDoc} */
        @Override
        public void notify(Method meth, AbstractMeter meter, double data) {
        }

        /** {@inheritDoc} */
        @Override
        public void notifyException(PerfidixMethodException exec) {
        }

    }
}
