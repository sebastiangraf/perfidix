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
 * $Id: ReflectionErrorTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ReflectionErrorTest {

  @Test
  public void testParamError() {
    final ParamBenchmarkable test = new ParamBenchmarkable();
    final Benchmark bench = new Benchmark();
    assertFalse(bench.exceptionsThrown());
    bench.add(test);
    bench.run();
    assertTrue(bench.exceptionsThrown());
  }

  @Test
  public void testReturnError() {
    final ReturnBenchmarkable test = new ReturnBenchmarkable();
    final Benchmark bench = new Benchmark();
    assertFalse(bench.exceptionsThrown());
    bench.add(test);
    bench.run();
    assertTrue(bench.exceptionsThrown());
  }

  class ParamBenchmarkable extends Benchmarkable {
    public void benchTest(Object obj) {
      fail();
    }
  }

  class ReturnBenchmarkable extends Benchmarkable {
    public Object benchTest() {
      fail();
      return null;
    }
  }
}
