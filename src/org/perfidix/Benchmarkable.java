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
 * $Id: Benchmarkable.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * the abstract class each benchmarkable class should inherit from.
 *  
 * @author axo
 * @since 11.10.2005
 */
@Deprecated
public abstract class Benchmarkable {

  private String name;

  /** 
   * default constructor. 
   */
  @Deprecated
  public Benchmarkable() {
    name = getClass().getSimpleName();
  }

  /**
   * tyou can give the benchmark a name by calling this constructor.
   * @param benchmarkName the benchmark's name.
   */
  @Deprecated
  public Benchmarkable(final String benchmarkName) {
    name = benchmarkName;
  }

  /** 
   */
  @Deprecated
  public void setUp() {

  }

  /** 
   */
  @Deprecated
  public void tearDown() {

  }

  /**
   * performs a cleanUp after all methods were called and testing was 
   * finished.
   * extend this method to clean up whatever you want.
   */
  @Deprecated
  public void cleanUp() {
    Runtime.getRuntime().gc();
  }

  /**
   * this output is used in the benchmark result
   * as a human-readable identifier of the name.
   * as a Benchmarkable implementor, you can 
   * call the non-default constructor
   * Benchmarkable (String "theNameYouWant") .
   * 
   * @return the name of the benchmark.
   * 
   */
  @Deprecated
  public final String getName() {
    return name;
  }

  /**
   * checks whether a method is invokable or not.
   * FIXME isInvokable should check the getParameterTypes().length in order
   * to avoid calling methods requiring parameters.
   * to take only methods 
   * @param m
   *          the method in question
   * FIXME this method needs renaming, since it doesn't check whether
   * the method is invokable, but checks whether it matches bench_* .        
   */
  @Deprecated
  private boolean isBenchMethod(final Method m) {
    return (m.getName().length() >= Benchmark.BM_METHOD_PREFIX.length() && m
        .getName()
        .substring(0, Benchmark.BM_METHOD_PREFIX.length())
        .equals(Benchmark.BM_METHOD_PREFIX));
  }

  @Deprecated
  public ArrayList<Method> getInvokableMethods() {
    Class<? extends Benchmarkable> cl = getClass();
    Method[] methods = cl.getMethods();

    ArrayList<Method> invokableMethods = new ArrayList<Method>();
    for (int i = 0, m = methods.length; i < m; i++) {
      if (!isBenchMethod(methods[i])) {
        continue;
      }
      invokableMethods.add(methods[i]);
    }
    return invokableMethods;
  }

}
