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
package org.perfidix.socketadapter;

import java.lang.reflect.Method;

import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * Listener for Perclipse
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class SocketListener extends AbstractOutput {

    /**
     * Instance to the perclipse view.
     */
    private transient final SocketViewProgressUpdater view;

    /**
     * Constructor with the port for initalising the connection to the view.
     * 
     * @param paramView
     *            connection to the perclipse view
     */
    public SocketListener(final SocketViewProgressUpdater paramView) {
        super();
        view = paramView;
    }

    /** {@inheritDoc} */
    @Override
    public void listenToException(final AbstractPerfidixMethodException exec) {
        try {
            view.updateErrorInElement((exec
                    .getMethod().getDeclaringClass().getName()
                    + "." + exec.getMethod().getName()), exec);
        } catch (final SocketViewException e) {
            throw new IllegalStateException(e);
        }

    }

    /** {@inheritDoc} */
    @Override
    public void listenToResultSet(
            final Method meth, final AbstractMeter meter, final double data) {
        try {
            view.updateCurrentElement(meter, (meth
                    .getDeclaringClass().getName()
                    + "." + meth.getName()));
        } catch (final SocketViewException e) {
            throw new IllegalStateException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void visitBenchmark(final BenchmarkResult res) {
        throw new UnsupportedOperationException("Operation is not permitted!");
    }

}
