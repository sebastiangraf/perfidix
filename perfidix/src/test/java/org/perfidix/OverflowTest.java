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
 * $Id: OverflowTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.exceptions.IntegerOverflowException;
import org.perfidix.result.IResult;

public class OverflowTest extends PerfidixTest {

    private IResult.SingleResult r;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        long[] theData = { Long.MAX_VALUE, Long.MAX_VALUE, };
        r = Perfidix.createSingleResult("bla", theData);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        r = null;
        super.tearDown();
    }

    @Test
    public void testMax() {
        assertEquals(Long.MAX_VALUE, r.max());
    }

    @Test
    public void testMin() {
        assertEquals(Long.MAX_VALUE, r.min());
    }

    @Test
    public void testConfOverflow() {
        assertIntegerOverflow("getConf95");
        assertIntegerOverflow("getConf99");
    }

    @Test
    public void testGetStandardDeviation() {
        assertIntegerOverflow("getStandardDeviation");
    }

    @Test
    public void testResultCount() {
        assertEquals(2l, r.resultCount());
    }

    @Test
    public void testVariance() {
        assertEquals(0.0, r.variance(), 0);
    }

    @Test
    public void testSumOverflow() {
        assertIntegerOverflow("sum");
    }

    @Test
    public void testSquareSum() {
        assertIntegerOverflow("squareSum");
    }

    @Test
    public void testAvg() {
        assertIntegerOverflow("avg");
    }

    /**
     * should not throw an integer overflow.
     */
    @Test
    public void testMedian() {
        assertEquals(9.223372036854776E18, r.median(), 0);
    }

    private void assertIntegerOverflow(final String methodName) {
        assertExceptionThrown(methodName, new IntegerOverflowException());
    }

    /**
     * helper method. checks that an exception of type e was thrown when calling
     * a given method.
     * 
     * @param methodName
     *                the name of the method.
     * @param e
     *                the exception prototype to be thrown.
     */
    private void assertExceptionThrown(
            final String methodName, final Exception e) {
        try {
            Method m = findMethod(r, methodName);
            assertNotNull(m);
            m.invoke(r, new Object[] {});
            fail("calling " + methodName + " should have thrown an exception");
        } catch (Exception thrown) {
            assertTrue(thrown.getCause().getClass().isInstance(e));

        }

    }

}
