/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
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
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.perclipseAdapter;

import java.lang.reflect.Method;

import org.perfidix.failureHandling.PerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * Listener for Perclipse
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class PerclipseListener extends AbstractOutput {

    /**
     * Instance to the perclipse view.
     */
    private final PerclipseViewProgressUpdater view;

    /**
     * Constructor with the port for initalising the connection to the view.
     * 
     * @param paramView
     *            connection to the perclipse view
     */
    public PerclipseListener(final PerclipseViewProgressUpdater paramView) {
        view = paramView;
    }

    /** {@inheritDoc} */
    @Override
    public void listenToException(PerfidixMethodException exec) {
        // TODO to be implemented

    }

    /** {@inheritDoc} */
    @Override
    public void listenToResultSet(
            Method meth, AbstractMeter meter, double data) {
        view.updateCurrentElement(meth.getName());
    }

    /** {@inheritDoc} */
    @Override
    public void visitBenchmark(BenchmarkResult res) {
        throw new UnsupportedOperationException(
                "Operation is not permitted!");
    }

}
