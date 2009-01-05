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
package org.perfidix.ouput;

import java.lang.reflect.Method;

import org.perfidix.failureHandling.PerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;

/**
 * This class acts as a listener for all upcoming results.It is registered in
 * the {@link BenchmarkResult} and gets notification for every
 * {@link BenchmarkResult#addData(Method, AbstractMeter, double)} or
 * {@link BenchmarkResult#addException(PerfidixMethodException)} invocation.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class BenchmarkListener {

    /**
     * Notification for upcoming normal result
     * 
     * @param meth
     *            the corresponding {@link Method}
     * @param meter
     *            the corresponding {@link AbstractMeter}
     * @param data
     *            the related data
     */
    public abstract void notify(
            final Method meth, final AbstractMeter meter, final double data);

    /**
     * Notification for upcoming exceptions
     * 
     * @param exec
     *            the upcoming {@link PerfidixMethodException}
     */
    public abstract void notifyException(final PerfidixMethodException exec);

}
