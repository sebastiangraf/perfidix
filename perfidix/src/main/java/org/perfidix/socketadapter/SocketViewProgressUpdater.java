/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.meter.AbstractMeter;

/**
 * This class creates the connection to the eclipse view via
 * {@link SocketViewStub}. It contains the methods which update the view to
 * inform about the bench process progress.
 * 
 * @author Lewandowski Lukas, University of Konstanz
 */
public final class SocketViewProgressUpdater {

    private final transient SocketViewStub viewStub;
    private transient int meterHash = 0;
    private transient boolean regMeterHash = false;

    /**
     * The constructor initializes the host and port for creation a client
     * socket.
     * 
     * @param host
     *            Host is the host name of the view provider.
     * @param port
     *            Port represent the port number of the eclipse view.
     * @throws SocketViewException
     */
    public SocketViewProgressUpdater(final String host, final int port)
            throws SocketViewException {
        String strubParam = host;
        if (host == null || host.equals("")) {
            strubParam = "localhost";
        }
        viewStub = new SocketViewStub(strubParam, port);

    }

    /**
     * This method initializes the values of the eclipse view and resets the
     * progress bar.
     * 
     * @param mapping
     *            a mapping with all methods to benchmark and the related runs
     * @throws SocketViewException
     */
    public void initProgressView(final Map<BenchmarkMethod, Integer> mapping)
            throws SocketViewException {
        if (mapping != null) {
            final Set<BenchmarkMethod> methodSet = mapping.keySet();

            final Map<String, Integer> finalMap =
                    new HashMap<String, Integer>();
            for (BenchmarkMethod benchmarkMethod : methodSet) {

                finalMap.put(benchmarkMethod.getMethodWithClassName(), mapping
                        .get(benchmarkMethod));
            }

            viewStub.initTotalBenchProgress(finalMap);
        }
    }

    /**
     * This method notifies the eclipse view which element is currently benched.
     * 
     * @param meter
     *            The current meter.
     * @param name
     *            This param represents the java element which is currently
     *            benched and which is fully qualified.
     * @throws SocketViewException
     */
    public void updateCurrentElement(
            final AbstractMeter meter, final String name)
            throws SocketViewException {
        if (meter != null && !regMeterHash) {
            registerFirstMeterHash(meter);
        }
        if (name != null && meter.hashCode() == (getRegMeter())) {
            viewStub.updateCurrentRun(name);
        }
    }

    /**
     * This method informs the view that an error occurred while benching the
     * current element.
     * 
     * @param name
     *            Element represents the java element which has not been
     *            executed successfully.
     * @param exception
     *            The exception caused by the element.
     * @throws SocketViewException
     */
    public void updateErrorInElement(
            final String name, final Exception exception)
            throws SocketViewException {
        if (name != null && exception != null) {
            if (exception instanceof AbstractPerfidixMethodException) {
                final AbstractPerfidixMethodException exc =
                        (AbstractPerfidixMethodException) exception;
                viewStub.updateError(name, exc
                        .getExec().getClass().getSimpleName());
            }
            if (exception instanceof SocketViewException) {
                final SocketViewException viewException =
                        (SocketViewException) exception;
                viewStub.updateError(name, viewException
                        .getExc().getClass().getSimpleName());
            }
        }
    }

    /**
     * This method notifies the view that all benches have been executed and the
     * bench progress is finished.
     * 
     * @throws SocketViewException
     */
    public void finished() throws SocketViewException {
        viewStub.finishedBenchRuns();
        regMeterHash = false;
    }

    /**
     * Register the meter hash for update the view, so only one time the current
     * view element will be updated and not for every meter.
     * 
     * @param meterHash
     *            The hash of the meter.
     */
    private void registerFirstMeterHash(final AbstractMeter meterHash) {
        this.meterHash = meterHash.hashCode();
        regMeterHash = true;
    }

    /**
     * The meter hash.
     * 
     * @return The meter hash of the registered hash;
     */
    private int getRegMeter() {
        return meterHash;
    }

}
