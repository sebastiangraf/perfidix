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
 * $Id: GnuPlotVisitorTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.GnuPlotData;

public class GnuPlotVisitorTest extends PerfidixTest {

    private GnuPlotData v;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        v = new GnuPlotData();
    }

    @Test
    public void testVisit() {

        Benchmark b = new Benchmark();
        b.add(new PerfidixTest.BenchmarkableTestingStub());
        AbstractResult r = b.run(100);
        v.visit(r);
        System.out.println(v.toString());

    }

    @Test
    public void testGnuPlotVisitor() {

    }

}
