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
 * $Revision$
 * $Author$
 * $Date$
 *
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
    private final ThreadGroup topThreadGroup;

    /** Name of the Meter. */
    private static final String NAME = "ThreadMeter";

    /** Unit of the Meter. */
    private static final String UNIT = "threads";

    /** UnitDescription of the Meter. */
    private static final String DESCRIPTION = "Number of Threads";

    /**
     * Constructor;
     */
    public ThreadMeter() {
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
     * @param group
     *            the group of number of threads to analyse.
     */
    public ThreadMeter(final ThreadGroup group) {
        this.topThreadGroup = group;
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public final String getUnit() {
        return UNIT;
    }

    /** {@inheritDoc} */
    @Override
    public final String getUnitDescription() {
        return DESCRIPTION;
    }

    /** {@inheritDoc} */
    @Override
    public double getValue() {
        return topThreadGroup.activeCount();
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = prime;
        result = prime * result;

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        } else {
            return true;
        }
    }

}
