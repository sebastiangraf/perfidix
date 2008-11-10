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
 * $Id: SortingTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.perfidix.result.NiceTable;
import org.perfidix.result.SingleResult;

public class SortingTest extends PerfidixTest {

    @Test
    public void testSortingValuesReturnsSame() {
        double data[] = { 3, 2, 1, 4 };
        SingleResult r = new SingleResult(data);
        r.min(); // run once to invoke sorting.
        assertEquals("3.02.01.04.0", NiceTable.Util.implode("", r
                .getResultSet()));
    }

    @Test
    public void testCopy() {
        double a[] = { 3, 2, 4 };
        double b[] = a.clone();
        assertEquals(3, b.length);
        Arrays.sort(a);
        assertEquals("3.02.04.0", NiceTable.Util.implode("", b));
    }

}
