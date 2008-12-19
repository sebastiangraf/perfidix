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

    @Test
    public void testMethodResult() {
        try {
            System.out.println(methodRes.mean(meter));
            System.out.println(methodRes.getConf95(meter));
            System.out.println(methodRes.getConf99(meter));
            System.out.println(methodRes.getStandardDeviation(meter));
            System.out.println(methodRes.sum(meter));
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
