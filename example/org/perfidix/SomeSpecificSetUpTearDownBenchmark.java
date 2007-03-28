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
 * $Id: SomeSpecificSetUpTearDownBenchmark.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

public class SomeSpecificSetUpTearDownBenchmark {

  CompressedHandler c;

  SimpleFileHandler s;

  public void setUpCompressed() {
    c = new CompressedHandler();
  }

  public void tearDownCompressed() {
    c = null;
  }

  public void setUpSimple() {
    s = new SimpleFileHandler();
  }

  public void tearDownSimple() {
    s = null;
  }

  @Bench(beforeMethod = "setUpCompressed", afterMethod = "tearDownCompressed", runs = 20)
  public void benchCWrite() {
    c.write("hello world");
  }

  @Bench(beforeMethod = "setUpSimple", afterMethod = "tearDownSimple")
  public void benchSWrite() {
    s.write("hello world");
  }

  @Bench(beforeMethod = "setUpCompressed", afterMethod = "tearDownCompressed")
  public void benchCRead() {
    c.read();
  }

  @Bench(beforeMethod = "setUpSimple", afterMethod = "tearDownSimple")
  public void benchSRead() {
    s.read();
  }

}