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
 * $Id: MultipleTimersResultTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.perfidix.annotation.Bench;
import org.perfidix.meter.CountingMeter;
import org.perfidix.meter.IMeter;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.IResult;
import org.perfidix.result.MethodResult;
import org.perfidix.result.ResultContainer;
import org.perfidix.result.SingleResult;

public class MultipleTimersResultTest {

    @Test
    public void testCollectionSorting() {
        IMeter a = Perfidix.createMeter("a", "a");
        IMeter b = Perfidix.createMeter("b", "b");
        ArrayList<IMeter> list = new ArrayList<IMeter>();
        list.add(b);
        list.add(a);
        assertEquals(b, list.get(0));
        assertEquals(a, list.get(1));
    }

    @Test
    public void testPatternCompiling() {
        String str = " a\nb ";
        Pattern p = Pattern.compile(".*a.*b.*", Pattern.DOTALL);
        Matcher m = p.matcher(str);
        assertTrue(m.matches());
    }

    @Test
    public void testResultsAreSorted() {
        IMeter c = Perfidix.createMeter("hello", "cMeterMeter");
        IMeter d = Perfidix.createMeter("bello", "dMeterMeter");
        SingleResult a = new SingleResult("a", new long[] {}, c);
        SingleResult b = new SingleResult("a", new long[] {}, d);
        MethodResult bla = new MethodResult("bla");
        bla.append(a);
        bla.append(b);
        IResult cls = new ClassResult("class");
        // cls.append(bla);

        String result = cls.toString();

        Pattern p =
                Pattern.compile(".*cMeterMeter.*dMeterMeter.*", Pattern.DOTALL);
        Matcher m = p.matcher(result);
        // System.out.println(result);
        // System.exit(-1);
        // assertTrue(result, m.matches());

    }

    @Test
    public void testGetRegisteredMetersRecursive() {
        ResultContainer a = new ClassResult("blang");
        ResultContainer b = new ClassResult("bleng");
        ResultContainer c = new ClassResult("bling");

        a.append(b);
        b.append(c);

        IMeter ma = Perfidix.createMeter(getClass() + "ma", "mac");
        IMeter mb = Perfidix.createMeter(getClass() + "mb", "mbc");
        IMeter mc = Perfidix.createMeter(getClass() + "mc", "mcc");
        IMeter tim = Perfidix.defaultMeter();
        long[] data = new long[] { 1, 2, 3 };
        SingleResult r1 = new SingleResult("bl0", data, tim);
        SingleResult r2 = new SingleResult("bla", data, ma);
        SingleResult r3 = new SingleResult("ble", data, mb);
        SingleResult r4 = new SingleResult("bli", data, mc);
        SingleResult r5 = new SingleResult("blo", data, tim);
        SingleResult r6 = new SingleResult("blu", data, tim);

        c.append(r1);
        c.append(r2);
        c.append(r3);

        a.append(r4);
        a.append(r5);
        a.append(r6);

        /*
         * ok, the system is now: [blang] - [bleng] - [bling] - [bl0]: ms -
         * [bla]: ma - [ble]: mb - [bli]: mc - [blo]: ms - [blu]: ms [bling]
         * contains now 3 IMeters (ms, ma, mb) [bleng] contains 3 IMeters (ms,
         * ma, mb), no additionals. [blang] contains 4 meters: (ms, ma, mb, mc)
         */

        // final Set<IMeter> areg = a.getRegisteredMeters();
        // final Set<IMeter> aregContains = new TreeSet<IMeter>();
        // aregContains.add(tim);
        // aregContains.add(ma);
        // aregContains.add(mb);
        // aregContains.add(mc);
        //
        // assertEquals(4, areg.size());
        // assertTrue(areg.containsAll(aregContains));
        //
        // final Set<IMeter> breg = b.getRegisteredMeters();
        // final Set<IMeter> bregContains = new TreeSet<IMeter>();
        // bregContains.add(tim);
        // bregContains.add(ma);
        // bregContains.add(mb);
        //
        // assertEquals(3, breg.size());
        // assertTrue(breg.containsAll(bregContains));
        //
        // final Set<IMeter> creg = c.getRegisteredMeters();
        // assertEquals(3, creg.size());
        // assertTrue(creg.containsAll(bregContains));
    }

    /**
     * tests that the fetching of registered meters works somehow.
     */
    @Test
    public void testGetRegisteredMeters() {

        ResultContainer rc = new MethodResult("hello");

        Perfidix.createMeter("bla", "ding");
        rc.append(new SingleResult("aResult", new long[] {}, Perfidix
                .getMeter("bla")));

        rc.append(new SingleResult("bResult", new long[] {}, Perfidix
                .defaultMeter()));

        final Iterator<IMeter> meters = rc.getRegisteredMeters().iterator();

        final IMeter meter1 = meters.next();
        final IMeter meter2 = meters.next();

        assertFalse(meters.hasNext());

        assertEquals("ding", meter1.getUnit());
        assertEquals(Perfidix.defaultMeter().getUnit(), meter2.getUnit());
    }

    @Test
    public void testZero() {
        ResultContainer rc = new MethodResult("hello");
        rc.append(new SingleResult("aResult", new long[] { 1, 2, 3 }, Perfidix
                .createMeter("blingbling", "aDing")));

        rc.append(new SingleResult("bResult", new long[] {}, Perfidix
                .getMeter("blingbling")));

        ResultContainer mm = new MethodResult("cont");
        mm.append(rc);
        IMeter blingbling = Perfidix.getMeter("blingbling");
        // System.out.println(mm);
        assertEquals(6, mm.sum(blingbling));
        assertEquals(0, mm.min(blingbling));
        assertEquals(6, mm.max(blingbling));

    }

    @Test
    public void testTwo() {
        IMeter a = Perfidix.createMeter("hello", "a");
        IMeter b = Perfidix.createMeter("bello", "b");

        MethodResult method1 = new MethodResult("someNiceMethod");
        MethodResult method2 = new MethodResult("someNiceOtherMethod");
        SingleResult aResult = new SingleResult(new long[] { 11, 13 }, a);
        SingleResult bResult = new SingleResult(new long[] { 17, 19 }, b);
        SingleResult cResult = new SingleResult(new long[] { 23, 29 }, a);
        SingleResult dResult = new SingleResult(new long[] { 31, 37 }, b);
        IResult cls = new ClassResult("C");
        BenchmarkResult full = new BenchmarkResult("Benchmark A");

        method1.append(bResult);
        method1.append(aResult);
        method2.append(cResult);
        method2.append(dResult);
        // cls.append(method1);
        // cls.append(method2);
        // full.append(cls);
        // full.append(cls);
        // System.out.println();
        // System.out.println();
        // System.out.println(full);
        // assertEquals(24l + 52l, cls.sum(a));
        // assertEquals(36l + 68l, cls.sum(b));
        // assertEquals(2l * (24l + 52l), full.sum(a));
        // assertEquals(2l * (36l + 68l), full.sum(b));

    }

    /**
     * 
     *
     */
    @Test
    public void testOne() {
        // CountingMeter a = Perfidix.createMeter("someMeter", "a");
        //
        // CountingMeter b = Perfidix.createMeter("someOtherMeter", "b");
        // CountingMeter c = Perfidix.createMeter("thirdMeter", "c");
        //
        // Benchmark bm = new Benchmark("test benchmark");
        // bm.useMilliMeter();
        // bm.add(new A(a, b));
        // bm.register(b);
        // bm.register(a);
        // bm.add(new B(a, b, c));
        // // System.out.println("running ... ");
        //
        // IResult r = bm.run(3);
        // String[] s =
        // {
        // "benchOne", "a", "ms", "b", "benchSomeOtherMeter", "a",
        // "ms", "b", "benchSomeThird", "a", "ms", "b",
        // "summary for A", "b", "a", "ms",
        // "summary for test benchmark", "b", "a", "ms" };
        // String pattern = ".*" + NiceTable.Util.implode(".*", s) + ".*";
        // Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        // Matcher m = p.matcher(r.toString());
        //
        // assertTrue(m.matches());

    }

    /**
     * @author onea
     */
    public class A {
        private CountingMeter a;

        private CountingMeter b;

        /**
         * @param aa
         * @param bb
         */
        public A(final CountingMeter aa, final CountingMeter bb) {
            a = aa;
            b = bb;

        }

        @Bench
        public void benchOne() {
            a.tick();
            a.tick();
            tick();
        }

        @Bench
        public void benchSomeOtherMeter() {
            tick();
        }

        private boolean rand() {
            return Math.random() > 0.5;
        }

        private void tick() {
            if (rand()) {
                a.tick();
            } else {
                b.tick();
            }
        }

        @Bench
        public void benchSomeThird() {
            tick();
        }
    }

    public class B extends A {
        private CountingMeter c;

        public B(
                final CountingMeter aa, final CountingMeter bb,
                final CountingMeter cc) {
            super(aa, bb);
            c = cc;
        }

        @Bench
        public void benchFive() {
            c.tick();
            c.tick();
            c.tick();
        }
    }

}
