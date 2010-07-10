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
 *     private final static AbstractMeter[] METERS =
 *             { new TimeMeter(Time.MilliSeconds), new MemMeter(Memory.Byte) };
 *     private final static AbstractOutput[] OUTPUT =
 *             { new TabularSummaryOutput() };
 *     private final static KindOfArrangement ARRAN =
 *             KindOfArrangement.SequentialMethodArrangement;
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
    private final static AbstractMeter[] METERS =
            { new TimeMeter(Time.MilliSeconds) };

    /** Standard listeners */
    private final static AbstractOutput[] LISTENERS = {};

    /** Standard arrangement */
    private final static KindOfArrangement ARRAN =
            KindOfArrangement.NoArrangement;

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
    protected AbstractConfig(
            final int paramRuns, final AbstractMeter[] paramMeters,
            final AbstractOutput[] paramOutput,
            final KindOfArrangement paramArr, final double paramGC) {
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