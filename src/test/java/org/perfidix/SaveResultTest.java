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
 * $Id: SaveResultTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.meter.CountingMeter;
import org.perfidix.result.AbstractResult;
import org.perfidix.visitor.ResultToXml;

/**
 * checks how the saveResultVisitor works.
 * 
 * @author onea
 */
public class SaveResultTest extends PerfidixTest {

    private AbstractResult r;

    private CountingMeter theMeter;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        Benchmark b = new Benchmark("my benchmark");
        theMeter = new CountingMeter("hello", "world");
        b.useNanoMeter();
        b.register(theMeter);
        // b.add(new A( new IMeter.NanoMeter() ));
        b.add(new A(theMeter));
        r = b.run(10);
        ResultToXml v = new ResultToXml("hello.xml");
        v.visit(r);

    }

    @Test
    public void testXStream() {

        ResultToXml x = new ResultToXml();
        x.visit(r);
        String xml = x.getDocument().asXML();
        // String xml = xStream.toXML(r);

        System.out.println(xml);
        // FIXME remove this.

    }

    public class A {
        private CountingMeter aMeter;

        /**
         * constructor. getting a meter as a parameter
         * 
         * @param some
         *            the meter to use.
         */
        public A(final CountingMeter some) {
            aMeter = some;
        }

        /**
         * a benchmarkable method.
         */
        @Bench
        public void benchA() {
            aMeter.tick();
        }

    }

}
