/*
 * Copyright 2007 University of Konstanz
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
 * $Id: PerfidixTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * a possible superclass for all perfidix test cases. enables some common
 * functionality which all other test cases could use.
 * 
 * @author onea
 */
public abstract class PerfidixTest {

    private boolean isDebugging;

    /**
     * the default accuracy for computations.
     */
    public static final double EPSILON = 0.000001;

    /**
     * the testing logger. and NO, i do NOT want this variable to be private.
     * damn.
     */
    private Log log = LogFactory.getLog(getClass().getName());

    /**
     * always stops the debugger.
     * 
     * @throws Exception
     *                 on exceptionally intelligent input.
     */
    protected void setUp() throws Exception {
        stopDebug();
    }

    /**
     * tears down the unit test.
     * 
     * @throws Exception
     *                 on exceptionally intelligent input.
     */
    protected void tearDown() throws Exception {
        stopDebug();
    }

    /**
     * @return the current log object.
     */
    protected Log getLog() {
        return log;
    }

    private String getDebugMessageHeader() {
        return " .... -:[ " + getClass().getSimpleName() + " ]:- .... ";
    }

    /**
     * starts the debugger.
     */
    protected void startDebug() {
        isDebugging = true;
    }

    /**
     * stops the debugging run by configuring the log4j to produce less output.
     */
    protected void stopDebug() {
        if (!isDebugging) {
            return;
        }
        log.info(getDebugMessageHeader());
        isDebugging = false;
    }

    /**
     * finds a method within a given object.
     * 
     * @param o
     *                the object to find the method in.
     * @param methodName
     *                the method name to look for.
     * @return the method if found - null if not found.
     */
    protected Method findMethod(final Object o, final String methodName) {
        Method[] methods = o.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methodName.equals(methods[i].getName())) {
                return methods[i];
            }
        }
        return null;
    }

    public static class BenchmarkableTestingStub {

        @Bench
        public void benchA() {
            for (int i = 0; i < 0xFF; i++) {
                Math.random();
            }
        }

        @Bench
        public void benchB() {

            for (int i = 0; i < 0xFFF; i++) {
                Math.random();
            }
        }

    }

}
