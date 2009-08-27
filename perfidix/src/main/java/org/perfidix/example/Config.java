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
package org.perfidix.example;

import org.perfidix.AbstractConfig;
import org.perfidix.annotation.BenchmarkConfig;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.MemMeter;
import org.perfidix.meter.Memory;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.TabularSummaryOutput;

/**
 * Public class to represent the settings for this benchmark.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
@BenchmarkConfig
public class Config extends AbstractConfig {

    private final static int RUNS = 100;
    private final static AbstractMeter[] METERS =
            { new TimeMeter(Time.MilliSeconds), new MemMeter(Memory.Byte) };
    private final static AbstractOutput[] OUTPUT =
            { new TabularSummaryOutput() };
    private final static KindOfArrangement ARRAN =
            KindOfArrangement.SequentialMethodArrangement;
    private final static double GCPROB = 1.0d;

    /**
     * Public constructor.
     */
    public Config() {
        super(RUNS, METERS, OUTPUT, ARRAN, GCPROB);

    }

}