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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.perfidix.visitor.ResultToXml;

/**
 * checks how the saveResultVisitor works.
 * 
 * @author onea
 * 
 */
public class SaveResultTest extends PerfidixTest {

  private Result r;

  private IMeter theMeter;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    Benchmark b = new Benchmark("my benchmark");
    theMeter = Perfidix.createMeter("hello", "world");
    b.useNanoMeter();
    b.register(theMeter);
    // b.add(new A( new IMeter.NanoMeter() ));
    b.add(new A(theMeter));
    r = b.run(10);
    ResultToXml v = new ResultToXml("hello.xml");
    v.visit(r);

  }

  /**
   * testing the behavior of equals etc.
   * 
   * @author onea
   * 
   */
  private class TreeMapObj {
    private String theValue;

    public TreeMapObj(final String value) {
      theValue = value;
    }

    public boolean equals(final Object o) {
      if (null == o) {
        return false;
      }
      if (this == o) {
        return true;
      }
      if (o.getClass() != getClass()) {
        return false;
      }
      return ((TreeMapObj) o).theValue.equals(theValue);
    }

    public String toString() {
      return theValue;
    }

    public int hashCode() {
      int result = 17;
      result = 37 * result + theValue.hashCode();
      return result;
    }
  }

  @Test
  @Ignore
  public void testTreeMapEquals() {
    assertFalse(new TreeMapObj("bla").equals(new TreeMapObj("blu")));
    assertTrue(new TreeMapObj("bla").equals(new TreeMapObj("bla")));
  }

  @Test
  @Ignore
  public void testTreeMapObjHashCode() {
    assertEquals(new TreeMapObj("world").hashCode(), new TreeMapObj("world")
        .hashCode());
  }

  @Test
  @Ignore
  public void testTreeHashing() {
    Hashtable<TreeMapObj, String> t = new Hashtable<TreeMapObj, String>();
    t.put(new TreeMapObj("hello"), "world");
    t.put(new TreeMapObj("world"), "hello");
    t.put(new TreeMapObj("hello"), "world");

    assertEquals(2, t.size());

  }

  @Test
  @Ignore
  public void testTreeMap() {

    Hashtable<IMeter, TreeMapObj> h = new Hashtable<IMeter, TreeMapObj>();
    h.put(new IMeter.MilliMeter(), new TreeMapObj("hello"));
    h.put(new IMeter.MilliMeter(), new TreeMapObj("hello"));
    assertEquals(1, h.size());
    assertTrue(new IMeter.MilliMeter().equals(new IMeter.MilliMeter()));
    assertTrue(new IMeter.MilliMeter().equals(new IMeter.MilliMeter()));
    assertTrue(h.containsKey(new IMeter.MilliMeter()));
  }

  @Test
  @Ignore
  public void testXStream() {

    ResultToXml x = new ResultToXml();
    x.visit(r);
    String xml = x.getDocument().asXML();
    // String xml = xStream.toXML(r);

    System.out.println(xml);
    // FIXME remove this.

  }

  public class A {
    private IMeter aMeter;

    /**
     * constructor. getting a meter as a parameter
     * 
     * @param some
     *            the meter to use.
     */
    public A(final IMeter some) {
      aMeter = some;
    }

    /**
     * a benchmarkable method.
     * 
     */
    @Bench
    public void benchA() {
      aMeter.tick();
    }

  }

}
