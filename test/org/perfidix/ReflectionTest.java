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
 * $Id: ReflectionTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReflectionTest {

  private boolean buildUp;

  private boolean setUp;

  private boolean bench;

  private boolean tearDown;

  private boolean cleanUp;

  public void setUp() {
    buildUp = false;
    setUp = false;
    bench = false;
    tearDown = false;
    cleanUp = false;
  }

  @Test
  public void test() {
    final TestBench test = new TestBench();
    final Benchmark benchmark = new Benchmark();
    benchmark.add(test);
    benchmark.run(1);
    assertTrue(buildUp);
    assertTrue(setUp);
    assertTrue(bench);
    assertTrue(tearDown);
    assertTrue(cleanUp);

  }

  class TestBench extends Benchmarkable {

    public void build() {
      buildUp = true;
    }

    public void setUp() {
      setUp = true;
    }

    public void bench() {
      bench = true;
    }

    public void tearDown() {
      tearDown = true;
    }

    public void cleanUp() {
      cleanUp = true;
    }
  }
}
