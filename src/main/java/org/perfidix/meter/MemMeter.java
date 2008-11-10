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
package org.perfidix.meter;

/**
 * Meter to bench the amount of memory used by the current Benchmark. Please
 * note that the results of this meter can only be seen as an approximation.
 * Because Perfidix-processes are influencing the used memory as well, a small
 * increasment of used memory is normal. However, because being based only on
 * the Runtime-class of Java, no extraction of the perfidix processes themselves
 * is possible.
 * 
 * @author Sebastian Graf, University of Kontanz
 */
public final class MemMeter extends AbstractMeter {

    private static final String NAME = "MemMeter";

    private long memAlreadyUsed;

    private final Memory scale;

    /**
     * Constructor.
     * 
     * @param paramScale
     *            scale for this meter, can be any instance of Memory-enum
     */
    public MemMeter(final Memory paramScale) {
        memAlreadyUsed = 0;
        this.scale = paramScale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getValue() {
        final Runtime rt = Runtime.getRuntime();
        rt.gc();
        memAlreadyUsed = memAlreadyUsed + rt.totalMemory() - rt.freeMemory();
        return Math.round(memAlreadyUsed / scale.getNumberOfBytes());
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public String getUnit() {
        return scale.getUnit();
    }

    /** {@inheritDoc} */
    @Override
    public String getUnitDescription() {
        return scale.getUnitDescription();
    }

}
