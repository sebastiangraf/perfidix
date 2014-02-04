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
package org.perfidix.meter;

/**
 * This class measures the number of threads in the runtime.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class ThreadMeter extends AbstractMeter {

    /**
     * the threadgroup to analyse.
     */
    private transient final ThreadGroup topThreadGroup;

    /** Name of the Meter. */
    private static final String NAME = "ThreadMeter";

    /** Unit of the Meter. */
    private static final String UNIT = "threads";

    /** UnitDescription of the Meter. */
    private static final String DESCRIPTION = "Number of Threads";

    /**
     * Constructor;
     */
    public ThreadMeter () {
        super();
        final Thread ownThread = Thread.currentThread();
        final ThreadGroup ownGroup = ownThread.getThreadGroup();
        ThreadGroup group = null;
        do {
            group = ownGroup.getParent();
        } while (group.getParent() != null);
        this.topThreadGroup = group;
    }

    /**
     * Constructor.
     * 
     * @param group the group of number of threads to analyse.
     */
    public ThreadMeter (final ThreadGroup group) {
        super();
        this.topThreadGroup = group;
    }

    /** {@inheritDoc} */
    @Override
    public String getName () {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public String getUnit () {
        return UNIT;
    }

    /** {@inheritDoc} */
    @Override
    public String getUnitDescription () {
        return DESCRIPTION;
    }

    /** {@inheritDoc} */
    @Override
    public double getValue () {
        return topThreadGroup.activeCount();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = prime;
        result = prime * result;

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals (final Object obj) {
        boolean returnVal = true;
        if (this == obj) {
            returnVal = true;
        }
        if (getClass() == obj.getClass()) {
            returnVal = true;
        } else {
            returnVal = false;
        }
        return returnVal;
    }

}
