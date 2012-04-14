/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
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
package org.perfidix;

import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.ouput.AbstractOutput;

/**
 * Configuration for Benchmark. Simple pass this class through the
 * Perfidix-Class. Each setting is hold over here. This class must be inherited
 * by a constructor which has no arguments to provide easy instantiation. This
 * can be done in the following way:
 * <p>
 * 
 * <pre>
 * 
 * &#064;BenchmarkConfig
 * public class Config extends AbstractConfig {
 *     private final static int RUNS = 100;
 *     private final static AbstractMeter[] METERS = {
 *         new TimeMeter(Time.MilliSeconds), new MemMeter(Memory.Byte)
 *     };
 *     private final static AbstractOutput[] OUTPUT = {
 *         new TabularSummaryOutput()
 *     };
 *     private final static KindOfArrangement ARRAN = KindOfArrangement.SequentialMethodArrangement;
 *     private final static double GCPROB = 1.0d;
 * 
 *     public Config() {
 *         super(RUNS, METERS, OUTPUT, ARRAN, GCPROB);
 *     }
 * }
 * 
 * </pre>
 * 
 * </p>
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractConfig {

    /** Standard runs */
    private final static int RUNS = 10;

    /** Standard meters */
    private final static AbstractMeter[] METERS = {
        new TimeMeter(Time.MilliSeconds)
    };

    /** Standard listeners */
    private final static AbstractOutput[] LISTENERS = {};

    /** Standard arrangement */
    private final static KindOfArrangement ARRAN = KindOfArrangement.NoArrangement;

    /** Standard gc-prob */
    private final static double GARBAGE_PROB = 1d;

    /** actual value for runs */
    private transient final int runs;

    /** actual value for meters */
    private transient final AbstractMeter[] meters;

    /** actual value for listeners */
    private transient final AbstractOutput[] listeners;

    /** actual value for runs */
    private transient final KindOfArrangement arrangement;

    /** actual value for gcProb */
    private transient final double gcProb;

    /**
     * Simple constructor.
     */
    protected AbstractConfig(final int paramRuns, final AbstractMeter[] paramMeters,
        final AbstractOutput[] paramOutput, final KindOfArrangement paramArr, final double paramGC) {
        runs = paramRuns;

        meters = new AbstractMeter[paramMeters.length];
        System.arraycopy(paramMeters, 0, meters, 0, meters.length);

        listeners = new AbstractOutput[paramOutput.length];
        System.arraycopy(paramOutput, 0, listeners, 0, listeners.length);

        arrangement = paramArr;
        gcProb = paramGC;
    }

    /**
     * Getter for member meters
     * 
     * @return the meters
     */
    public final AbstractMeter[] getMeters() {
        final AbstractMeter[] returnVal = new AbstractMeter[meters.length];
        System.arraycopy(meters, 0, returnVal, 0, returnVal.length);
        return returnVal;
    }

    /**
     * Getter for member runs
     * 
     * @return the runs
     */
    public final int getRuns() {
        return runs;
    }

    /**
     * Getter for member gcProb
     * 
     * @return the gcProb
     */
    public final double getGcProb() {
        return gcProb;
    }

    /**
     * Getter for member listeners
     * 
     * @return the listeners
     */
    public final AbstractOutput[] getListener() {
        final AbstractOutput[] returnVal = new AbstractOutput[listeners.length];
        System.arraycopy(listeners, 0, returnVal, 0, returnVal.length);
        return returnVal;
    }

    /**
     * Getter for member arrangement
     * 
     * @return the arrangement
     */
    public final KindOfArrangement getArrangement() {
        return arrangement;
    }

    /**
     * Standard config.
     * 
     * @author Sebastian Graf, University of Konstanz
     */
    public static class StandardConfig extends AbstractConfig {

        /**
         * Constructor.
         */
        public StandardConfig() {
            super(RUNS, METERS, LISTENERS, ARRAN, GARBAGE_PROB);
        }

    }
}
