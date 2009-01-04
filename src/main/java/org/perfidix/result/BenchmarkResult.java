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

import org.perfidix.Benchmark;
import org.perfidix.meter.AbstractMeter;

/**
 * This class holds the data of the whole benchmark in different
 * {@link ClassResult} objects.
 * 
 * @author Sebastian Graf, University of Konstanz
 * @author Alexander Onea, neue Couch
 */
public final class BenchmarkResult extends ResultContainer<ClassResult> {

    /**
     * Constructor.
     * 
     * @param benchmark
     *            related {@link Benchmark} instance for listener possibility
     */
    public BenchmarkResult(final Benchmark benchmark) {
        super(benchmark);
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

    }
}
