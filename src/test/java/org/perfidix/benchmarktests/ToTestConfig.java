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
package org.perfidix.benchmarktests;


import org.perfidix.AbstractConfig;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.*;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.TabularSummaryOutput;

import java.util.HashSet;
import java.util.Set;


/**
 * Test config for the benchmark
 *
 * @author Sebastian Graf, University of Konstanz
 */
public final class ToTestConfig extends AbstractConfig {

    /**
     * Test runs
     */
    public final static int TESTRUNS = 54;

    /**
     * Test meters
     */
    public final static Set<AbstractMeter> TESTMETERS = new HashSet<AbstractMeter>();

    /**
     * Test listener
     */
    public final static Set<AbstractOutput> TESTLISTENER = new HashSet<AbstractOutput>();

    /**
     * Test arrangement
     */
    public final static KindOfArrangement TESTARR = KindOfArrangement.SequentialMethodArrangement;

    /**
     * Test gc-prob
     */
    public final static double TESTGC = 0d;

    static {
        TESTMETERS.add(new TimeMeter(Time.MilliSeconds));
        TESTMETERS.add(new MemMeter(Memory.Byte));
        TESTLISTENER.add(new TabularSummaryOutput());
    }

    /**
     * Simple Constructors, taking the statics
     */
    public ToTestConfig() {
        super(TESTRUNS, TESTMETERS, TESTLISTENER, TESTARR, TESTGC);
    }

}
