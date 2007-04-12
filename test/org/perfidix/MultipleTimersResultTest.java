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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MultipleTimersResultTest extends PerfidixTest {


	@Before
  public void setUp() throws Exception {
    super.setUp();
  }

	@After
	public void tearDown() throws Exception {

    super.tearDown();
  }


	@Test
  public void testCollectionSorting() {
    IMeter a = Perfidix.createMeter("a", "a");
    IMeter b = Perfidix.createMeter("b", "b");
    ArrayList<IMeter> list = new ArrayList<IMeter>();
    list.add(b);
    list.add(a);
    assertEquals(b, list.get(0));
    assertEquals(a, list.get(1));
    Collections.sort(list);
    assertEquals(a, list.get(0));
    assertEquals(b, list.get(1));
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
    IResult.SingleResult a = new IResult.SingleResult("a", new long[] {}, c);
    IResult.SingleResult b = new IResult.SingleResult("a", new long[] {}, d);
    IResult.MethodResult bla = new IResult.MethodResult("bla");
    bla.append(a);
    bla.append(b);
    Result.ClassResult cls = new Result.ClassResult("class");
    cls.append(bla);

    String result = cls.toString();

    Pattern p = Pattern.compile(".*cMeterMeter.*dMeterMeter.*", Pattern.DOTALL);
    Matcher m = p.matcher(result);
    //System.out.println(result);
    // System.exit(-1);
    assertTrue(result, m.matches());

  }


	@Test
  public void testGetRegisteredMetersRecursive() {
    ResultContainer a = new IResult.ClassResult("blang");
    ResultContainer b = new IResult.ClassResult("bleng");
    ResultContainer c = new IResult.ClassResult("bling");

    a.append(b);
    b.append(c);

    IMeter ma = Perfidix.createMeter(getClass() + "ma", "mac");
    IMeter mb = Perfidix.createMeter(getClass() + "mb", "mbc");
    IMeter mc = Perfidix.createMeter(getClass() + "mc", "mcc");
    IMeter tim = Perfidix.defaultMeter();
    long[] data = new long[] { 1, 2, 3 };
    IResult.SingleResult r1 = new IResult.SingleResult("bl0", data, tim);
    IResult.SingleResult r2 = new IResult.SingleResult("bla", data, ma);
    IResult.SingleResult r3 = new IResult.SingleResult("ble", data, mb);
    IResult.SingleResult r4 = new IResult.SingleResult("bli", data, mc);
    IResult.SingleResult r5 = new IResult.SingleResult("blo", data, tim);
    IResult.SingleResult r6 = new IResult.SingleResult("blu", data, tim);

    c.append(r1);
    c.append(r2);
    c.append(r3);

    a.append(r4);
    a.append(r5);
    a.append(r6);

    /* 
     * ok, the system is now:
     * [blang]
     *  - [bleng]
     *     - [bling]
     *         - [bl0]: ms
     *         - [bla]: ma
     *         - [ble]: mb
     *     - [bli]: mc
     *     - [blo]: ms
     *     - [blu]: ms    
     *     
     * [bling] contains now 3 IMeters (ms, ma, mb)
     * [bleng] contains 3 IMeters (ms, ma, mb), no additionals.
     * [blang] contains 4 meters: (ms, ma, mb, mc)    
     */

    ArrayList<IMeter> areg = a.getRegisteredMeters();
    ArrayList<IMeter> aregContains = new ArrayList<IMeter>();
    aregContains.add(tim);
    aregContains.add(ma);
    aregContains.add(mb);
    aregContains.add(mc);

    assertEquals(4, areg.size());
    assertTrue(areg.containsAll(aregContains));

    ArrayList<IMeter> breg = b.getRegisteredMeters();
    ArrayList<IMeter> bregContains = new ArrayList<IMeter>();
    bregContains.add(tim);
    bregContains.add(ma);
    bregContains.add(mb);

    assertEquals(3, breg.size());
    assertTrue(breg.containsAll(bregContains));

    ArrayList<IMeter> creg = c.getRegisteredMeters();
    assertEquals(3, creg.size());
    assertTrue(creg.containsAll(bregContains));

  }

  /** 
   * tests that the fetching of registered meters works somehow.
   *  
   */
	@Test
  public void testGetRegisteredMeters() {
    ResultContainer rc = new IResult.MethodResult("hello");
    Perfidix.createMeter("bla", "ding");
    rc.append(new IResult.SingleResult("aResult", new long[] {}, Perfidix
        .getMeter("bla")));
    ArrayList<IMeter> meters = rc.getRegisteredMeters();
    assertEquals(1, meters.size());
    assertEquals("ding", meters.get(0).getUnit());

    rc.append(new IResult.SingleResult("bResult", new long[] {}, Perfidix
        .defaultMeter()));
    meters = rc.getRegisteredMeters();
    assertEquals(2, meters.size());
    assertEquals(Perfidix.defaultMeter().getUnit(), meters.get(0).getUnit());
    assertEquals("ding", meters.get(1).getUnit());
  }

	@Test
  public void testZero() {
    ResultContainer rc = new IResult.MethodResult("hello");
    rc.append(new IResult.SingleResult(
        "aResult",
        new long[] { 1, 2, 3 },
        Perfidix.createMeter("blingbling", "aDing")));

    rc.append(new IResult.SingleResult("bResult", new long[] {}, Perfidix
        .getMeter("blingbling")));

    ResultContainer mm = new IResult.MethodResult("cont");
    mm.append(rc);
    IMeter blingbling = Perfidix.getMeter("blingbling");
    // System.out.println(mm);
    assertEquals(new Long(6), mm.sum(blingbling));
    assertEquals(new Long(0), mm.min(blingbling));
    assertEquals(new Long(6), mm.max(blingbling));

  }

  @Test
  public void testTwo() {
    IMeter a = Perfidix.createMeter("hello", "a");
    IMeter b = Perfidix.createMeter("bello", "b");

    IResult.MethodResult method1 = new IResult.MethodResult("someNiceMethod");
    IResult.MethodResult method2 =
        new IResult.MethodResult("someNiceOtherMethod");
    IResult.SingleResult aResult =
        new IResult.SingleResult(new long[] { 11, 13 }, a);
    IResult.SingleResult bResult =
        new IResult.SingleResult(new long[] { 17, 19 }, b);
    IResult.SingleResult cResult =
        new IResult.SingleResult(new long[] { 23, 29 }, a);
    IResult.SingleResult dResult =
        new IResult.SingleResult(new long[] { 31, 37 }, b);
    Result.ClassResult cls = new IResult.ClassResult("C");
    Result.BenchmarkResult full = new Result.BenchmarkResult("Benchmark A");

    method1.append(bResult);
    method1.append(aResult);
    method2.append(cResult);
    method2.append(dResult);
    cls.append(method1);
    cls.append(method2);
    full.append(cls);
    full.append(cls);
    //System.out.println();
    //System.out.println();
    //System.out.println(full);
    assertEquals(24l + 52l, cls.sum(a));
    assertEquals(36l + 68l, cls.sum(b));
    assertEquals(2l * (24l + 52l), full.sum(a));
    assertEquals(2l * (36l + 68l), full.sum(b));

  }

  /**
   * 
   *
   */
  @Test
  public void testOne() {
    IMeter a = Perfidix.createMeter("someMeter", "a");

    IMeter b = Perfidix.createMeter("someOtherMeter", "b");
    IMeter c = Perfidix.createMeter("thirdMeter", "c");

    Benchmark bm = new Benchmark("test benchmark");
    bm.useMilliMeter();
    bm.add(new A(a, b));
    bm.register(b);
    bm.register(a);
    bm.add(new B(a, b, c));
    //System.out.println("running ... ");

    Result r = bm.run(3);
    String[] s =
        {
            "benchOne",
            "ms",
            "a",
            "b",
            "benchSomeOtherMeter",
            "ms",
            "a",
            "b",
            "benchSomeThird",
            "ms",
            "a",
            "b",
            "summary for A",
            "ms",
            "a",
            "b",
            "summary for test benchmark",
            "ms",
            "a",
            "b", };
    String pattern = ".*" + NiceTable.Util.implode(".*", s) + ".*";
    Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
    Matcher m = p.matcher(r.toString());

    assertTrue(m.matches());

  }

  /**
   * 
   * @author onea
   *
   */
  public class A {
    private IMeter a;

    private IMeter b;

    /**
     * 
     * @param aa
     * @param bb
     */
    public A(final IMeter aa, final IMeter bb) {
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
    private IMeter c;

    
    public B(final IMeter aa, final IMeter bb, final IMeter cc) {
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
