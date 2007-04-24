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
 * $Id: ClassAnnoBenchmark.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.util.Random;

@BenchClass(runs = 10)
public class ClassAnnoBenchmark {

  CompressedHandler c;

  SimpleFileHandler s;

  String toTest;

  long testLength;

  @BeforeBenchClass
  public void beforeClass() {
    Math.abs(testLength = new Random().nextInt(10));
  }

  @BeforeFirstBenchRun
  public void beforeMethod() {
    for (int i = 0; i < testLength; i++) {
      toTest = toTest + (char) (new Random().nextInt(Character.MAX_VALUE + 1));
    }
  }

  @BeforeEachBenchRun
  public void beforeRun() {
    c = new CompressedHandler();
    s = new SimpleFileHandler();
  }

  @AfterEachBenchRun
  public void afterRun() {
    c = null;
    s = null;
  }

  @AfterLastBenchRun
  public void afterMethod() {
    toTest = null;
  }

  @AfterBenchClass
  public void afterClass() {
    testLength = -1;
  }

  public void benchCWrite() {
    c.write("hello world");
  }

  @Bench(runs = 60)
  public void benchSWrite() {
    s.write("hello world");
  }

  public void benchCRead() {
    c.read();
  }

  public void benchSRead() {
    s.read();
  }

}
