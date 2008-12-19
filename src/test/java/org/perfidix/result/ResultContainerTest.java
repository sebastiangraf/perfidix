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
package org.perfidix.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.meter.CountingMeter;

/**
 * Test class for the whole package of the results.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class ResultContainerTest {

    private final static int NUMBEROFTICKS = 10;

    private MethodResult methodRes;

    private CountingMeter meter;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        final Class<?>[] args = {};
        final Class1 class1 = new Class1();
        meter = new CountingMeter();

        methodRes =
                new MethodResult(class1.getClass().getDeclaredMethod(
                        "method", args));

        for (int i = 0; i < NUMBEROFTICKS; i++) {
            meter.tick();
            methodRes.addResult(meter, meter.getValue());
        }

    }

    /**
     * Test method for
     * {@link org.perfidix.result.AbstractResult#mean(org.perfidix.meter.AbstractMeter)}
     * ,
     * {@link org.perfidix.result.AbstractResult#min(org.perfidix.meter.AbstractMeter)}
     * ,
     * {@link org.perfidix.result.AbstractResult#max(org.perfidix.meter.AbstractMeter)}
     * ,
     * {@link org.perfidix.result.AbstractResult#getConf95(org.perfidix.meter.AbstractMeter)}
     * ,
     * {@link org.perfidix.result.AbstractResult#getConf99(org.perfidix.meter.AbstractMeter)}
     * ,
     * {@link org.perfidix.result.AbstractResult#sum(org.perfidix.meter.AbstractMeter)}
     * ,
     * {@link org.perfidix.result.AbstractResult#squareSum(org.perfidix.meter.AbstractMeter)}
     * .
     */
    @Test
    public void testStatisticalMethods() {
        try {
            assertEquals(5.5, methodRes.mean(meter), 0);
            assertEquals(1.0, methodRes.min(meter), 0);
            assertEquals(10.0, methodRes.max(meter), 0);
            assertEquals(10.0, methodRes.getConf95(meter), 0);
            assertEquals(10.0, methodRes.getConf99(meter), 0);
            assertEquals(3.0276503540974917, methodRes
                    .getStandardDeviation(meter), 0.000001);
            assertEquals(55.0, methodRes.sum(meter), 0);
            assertEquals(385.0, methodRes.squareSum(meter), 0);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Simple tearDown.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        methodRes = null;
        meter = null;
    }

    class Class1 {
        public void method() {
        }
    }

    class Class2 {
        public void method() {

        }
    }

}
