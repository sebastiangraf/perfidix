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

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.CountingMeter;

/**
 * Test class for the whole package of the results.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class ResultContainerTest {

    private final static int NUMBEROFTICKS = 10;

    /** Used meters, single CountingMeter */
    private Set<AbstractMeter> usedMeters;

    private Class1 class1;

    private Class2 class2;

    private Class<?>[] args;

    /**
     * Simple setUp.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        usedMeters = new HashSet<AbstractMeter>();
        final CountingMeter meter1 = new CountingMeter();
        final CountingMeter meter2 = new CountingMeter();
        for (int i = 0; i < NUMBEROFTICKS; i++) {
            meter1.tick();
        }
        for (int i = 0; i < NUMBEROFTICKS; i++) {
            meter2.tick();
        }
        usedMeters.add(meter1);
        usedMeters.add(meter2);

        class1 = new Class1();
        class2 = new Class2();

        args = new Class<?>[0];
    }

    @Test
    public void testMethodResult() {
        try {
            final MethodResult res =
                    new MethodResult(class1.getClass().getDeclaredMethod(
                            "method", args));
            for (final AbstractMeter meter : usedMeters) {
                res.addResult(meter, meter.getValue());
            }

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
        usedMeters.clear();
        usedMeters = null;
        class1 = null;
        class2 = null;
        args = null;
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
