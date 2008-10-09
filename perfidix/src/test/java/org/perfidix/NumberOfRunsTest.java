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
 * $Id: NumberOfRunsTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;
import org.perfidix.result.NiceTable;
import org.perfidix.result.SingleResult;

public class NumberOfRunsTest extends PerfidixTest {

    @Test
    public void testIgnoreMethods() {
        Benchmark b = new Benchmark("my test");
        PerfidixTest.BenchmarkableTestingStub stub =
                new PerfidixTest.BenchmarkableTestingStub();
        b.add(stub);
        BenchmarkResult r = (BenchmarkResult) b.run(10);

        ClassResult cls = r.getChildren().get(0);

        MethodResult benchA = cls.getChildren().get(0);
        MethodResult benchB = cls.getChildren().get(1);
        SingleResult benchAmillis = benchA.getChildren().get(0);
        printChildren(benchA);
        printChildren(benchB);
        // assertEquals(1, benchA.getChildren().size());
        assertEquals(10l, benchAmillis.getNumberOfRuns());
        assertEquals("benchA", benchA.getName());
        assertEquals(10l, benchA.getNumberOfRuns());
        assertEquals(10l, benchB.getNumberOfRuns());
        assertEquals(20l, cls.getNumberOfRuns());
        assertEquals(20l, r.getNumberOfRuns());
        System.out.println(r);
    }

    private void printChildren(MethodResult m) {
        Iterator<SingleResult> children = m.getChildren().iterator();
        while (children.hasNext()) {
            SingleResult myChild = children.next();
            System.out.println(" "
                    + myChild.getName()
                    + "  --- "
                    + myChild.getNumberOfRuns()
                    + NiceTable.Util.implode(",", myChild.getResultSet()));
        }
    }

}
