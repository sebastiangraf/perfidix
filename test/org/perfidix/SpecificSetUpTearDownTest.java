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
 * $Id: SpecificSetUpTearDownTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SpecificSetUpTearDownTest {

  private boolean setUp;

  private boolean tearDown;

  private boolean specialSetUp;

  private boolean specialTearDown;

  private boolean benchWithout;

  private boolean benchWith;

  @Before
  public void setUp() {
    setUp = false;
    tearDown = false;
    specialSetUp = false;
    specialTearDown = false;
    benchWithout = false;
    benchWith = false;
  }

  @Test
  public void testWithout() {
    final Without test = new Without();
    final Benchmark benchMark = new Benchmark();
    benchMark.add(test);
    benchMark.run(1);
    assertTrue(setUp);
    assertTrue(tearDown);
    assertFalse(specialSetUp);
    assertFalse(specialTearDown);
    assertTrue(benchWithout);
    assertFalse(benchWith);
  }

  @Test
  public void testWith() {
    final With test = new With();
    final Benchmark benchMark = new Benchmark();
    benchMark.add(test);
    benchMark.run(1);
    assertFalse(setUp);
    assertFalse(tearDown);
    assertTrue(specialSetUp);
    assertTrue(specialTearDown);
    assertFalse(benchWithout);
    assertTrue(benchWith);
  }

  class Without {
    @BeforeEachBenchRun
    public void setUp() {
      setUp = true;
    }

    @AfterEachBenchRun
    public void tearDown() {
      tearDown = true;
    }

    public void specialSetUp() {
      specialSetUp = true;
    }

    public void specialTearDown() {
      specialTearDown = true;
    }

    @Bench
    public void bench() {
      benchWithout = true;
    }
  }

  class With {
    @BeforeEachBenchRun
    public void setUp() {
      setUp = true;
    }

    @AfterEachBenchRun
    public void tearDown() {
      tearDown = true;
    }

    public void specialSetUp() throws Exception {
      specialSetUp = true;
    }

    public void specialTearDown() {
      specialTearDown = true;
    }

    @Bench(beforeEveryBenchRun = "specialSetUp", afterEveryBenchRun = "specialTearDown")
    public void bench() {
      benchWith = true;
    }
  }

}
