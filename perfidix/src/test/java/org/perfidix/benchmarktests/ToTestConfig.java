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
package org.perfidix.benchmarktests;

import org.perfidix.AbstractConfig;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.MemMeter;
import org.perfidix.meter.Memory;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.TabularSummaryOutput;

/**
 * Test config for the benchmark
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class ToTestConfig extends AbstractConfig {

    /** Test runs */
    public final static int TESTRUNS = 54;

    /** Test meters */
    public final static AbstractMeter[] TESTMETERS =
            { new TimeMeter(Time.MilliSeconds), new MemMeter(Memory.Byte) };

    /** Test listener */
    public final static AbstractOutput[] TESTLISTENER =
            { new TabularSummaryOutput() };

    /** Test arrangement */
    public final static KindOfArrangement TESTARR =
            KindOfArrangement.SequentialMethodArrangement;

    /** Test gc-prob */
    public final static double TESTGC = 0d;

    /**
     * Simple Constructors, taking the statics
     */
    public ToTestConfig() {
        super(TESTRUNS, TESTMETERS, TESTLISTENER, TESTARR, TESTGC);
    }

}
