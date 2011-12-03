/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    private transient final IUpdater view;

    /**
     * Constructor with the port for initalising the connection to the view.
     * 
     * @param paramView
     *            connection to the perclipse view
     */
    public SocketListener(final IUpdater paramView) {
        super();
        view = paramView;
    }

    /** {@inheritDoc} */
    @Override
    public boolean listenToException(final AbstractPerfidixMethodException exec) {
        try {
            // [SG] nicht einfach return true zurückgeben sondern den Rückgabewert der Methode
            view.updateErrorInElement((exec.getMethod().getDeclaringClass().getName() + "." + exec
                .getMethod().getName()), exec);
            return true;
        } catch (final SocketViewException e) {
            throw new IllegalStateException(e);
        }

    }

    /** {@inheritDoc} */
    @Override
    public boolean listenToResultSet(final Method meth, final AbstractMeter meter, final double data) {
        try {
            return view.updateCurrentElement(meter, (meth.getDeclaringClass().getName() + "." + meth.getName()));
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
