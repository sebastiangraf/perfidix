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
 * $Id: GetResultSetTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.result.MethodResult;
import org.perfidix.result.NiceTable;
import org.perfidix.result.ResultContainer;
import org.perfidix.result.SingleResult;

/**
 * @author onea
 */
public class GetResultSetTest extends PerfidixTest {

    private ResultContainer<SingleResult> test;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        test = new MethodResult("hello world.");
    }

    /**
     * Test method for 'org.perfidix.perf.ResultContainer.getResultSet()'. the
     * ResultContainer contains one SingleResult with an empty array of values.
     * this results in an array { 0 } as the result set.
     */
    @Test
    public void testGetResultSetZeroValues() {

        test.append(Perfidix.createSingleResult("a", new long[] {}));
        assertEquals(new long[] { 0 }, test.getResultSet());

    }

    /**
     * checks that the Result appending won't break the code, but logs an error.
     */
    @Test
    public void testGetResultSetNullAppend() {
        test.append(null);
        assertEquals(test.getResultSet(), new long[] {});
    }

    @Test
    public void testGetResultSetOneAppend() {
        test.append(Perfidix.createSingleResult("a", new long[] { 1 }));
        assertEquals(new long[] { 1 }, test.getResultSet());
    }

    @Test
    public void testGetResultSetTwoAppend() {

        test.append(Perfidix.createSingleResult("a", new long[] { 1, 2, 3 }));
        test.append(Perfidix.createSingleResult("b", new long[] { 2, 3 }));
        assertEquals(new long[] { 6, 5 }, test.getResultSet());
    }

    @Test
    public void testGetResultSetMultiAppend() {
        test.append(Perfidix.createSingleResult("a", new long[] { 1 }));
        test.append(Perfidix.createSingleResult("b", new long[] { 2 }));
        test.append(Perfidix.createSingleResult(
                "c", new long[] { 15, 13, 2, 4 }));
        test.append(Perfidix.createSingleResult("d", new long[] { 3 }));
        assertEquals(new long[] { 1, 2, 34, 3 }, test.getResultSet());

    }

    /**
     * extends the assertEquals test for a value comparation of arrays. this is
     * not object equality, but value equality.
     * 
     * @param one
     *            array
     * @param two
     *            array
     */
    // TODO look if this works this way
    protected void assertEquals(final long[] one, final long[] two) {
        org.junit.Assert.assertEquals(NiceTable.Util.implode(", ", one)
                + " "
                + NiceTable.Util.implode(", ", two), one.length, two.length);
        for (int i = 0; i < one.length; i++) {
            org.junit.Assert.assertEquals(one[i], two[i]);
        }
    }

}
