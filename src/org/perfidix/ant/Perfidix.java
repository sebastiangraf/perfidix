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
 * $Id: Perfidix.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ant;

import java.util.ArrayList;
import java.util.Iterator;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.perfidix.Benchmark;
import org.perfidix.IMeter;
import org.perfidix.IRandomizer;
import org.perfidix.Result;


/**
 * the ant task for running a task.
 * @author onea
 *
 */
public class Perfidix extends PerfidixTask implements TaskContainer {

  /**
   * the default invocation count.
   */
  private int iterations = Benchmark.BM_DEFAULT_INVOCATION_COUNT;

  /**
   * the randomizer to use.
   */
  private IRandomizer rand = new IRandomizer.DefaultRandomizer();

  /**
   * the class being aggregated.
   */
  private Benchmark benchmark = new Benchmark();

  /**
   * the meters to add, if any.
   */
  private ArrayList<Meter> metersToAdd = new ArrayList<Meter>();

  /**
   * a simple collection for the benchmarks.
   */
  private ArrayList<org.perfidix.ant.Benchmark> benchmarks =
      new ArrayList<org.perfidix.ant.Benchmark>();

  private ArrayList<org.perfidix.ant.Method> methods =
      new ArrayList<org.perfidix.ant.Method>();

  private Randomizer randomizerBuilder;

  /**
   *  
   * @param in the number of iterations
   */
  public void setIterations(final String in) {
    iterations = Integer.parseInt(in);
  }

  /**
   * sets the meter to use.
   * the meter's name can be either "nanometer", "millimeter"
   * @param in the meter's name.
   */
  public void setMeter(final String in) {

    if ("nanometer".equals(in.toLowerCase())) {
      benchmark.useNanoMeter();
    } else { // default, millimeter, everything else.
      benchmark.useMilliMeter();
    }

  }

  /**
   * 
   * @param n the name of the benchmark.
   */
  public void setName(final String n) {
    benchmark.setName(n);
  }

  /**
   * @throws BuildException on build failure.
   */
  public void execute() throws BuildException {

    Iterator<org.perfidix.ant.Benchmark> it =
        benchmarks.iterator();
    while (it.hasNext()) {
      debug("adding benchmarkable...");
      try {
        benchmark.add(it.next().getBenchmarkable());
      } catch (Exception e) {
        if (isDebugEnabled()) {
          e.printStackTrace();
        }
        throw new BuildException("sorry, could not add the "
            + "benchmarkable, ", e);
      }
    }
    addMeters();
    addMethods();
    addRandomizer();
    debug("running...");
    Result r = benchmark.run(iterations, rand);
    debug("done!");
    System.out.println(r.toString());

  }

  private void addRandomizer() {
    if (null == randomizerBuilder) {
      debug("no randomizer builder available.");
      return;
    }
    debug("executing the randomizerBuilder...");
    randomizerBuilder.execute();
    debug("randomizerBuilder execution done.");
    rand = randomizerBuilder.getRandomizer();
    if (null == rand) {
      debug("randomizer building failed. using default randomizer.");
      rand = new IRandomizer.DefaultRandomizer();
    }
  }

  private void addMethods() {
    Iterator<Method> m = methods.iterator();

    while (m.hasNext()) {
      Method next = m.next();
      debug("trying to add a method to the perfidix...");
      next.execute();
      benchmark.add(next.getMethod());
      debug("adding " + next.getMethodName() + " worked.");
    }

  }

  /**
   * adds the meters to the benchmark.
   *
   */
  private void addMeters() {
    debug("adding meters");
    Iterator<Meter> it = metersToAdd.iterator();
    debug("number of meters: " + metersToAdd.size());
    while (it.hasNext()) {
      Meter next = it.next();
      next.execute();
      IMeter m = next.getMeter();
      if (null == m) {
        debug("IMeter is null. doing nothing. ");
        continue;
      }
      debug("adding meter " + m.getName() + " to the benchmark");
      benchmark.register(m);
    }
  }

  /**
   * adds a default tasks. 
   * @param arg0 the task to add.
   */
  public void addTask(final Task arg0) {
    debug("should add a generic task.");
  }

  /**
   * adds a meter to the benchmark.
   * @param m hello
   */
  public void addMeter(final Meter m) {
    if (isDebugEnabled()) {
      m.setDebugOn();
    }
    debug("adding meter to my internal list.");
    metersToAdd.add(m);
  }

  /**
   * 
   * @param m a method.
   */
  public void addMethod(final Method m) {
    debug("should add a method");
    methods.add(m);
  }

  /**
   * adds a benchmark.
   * @param inner the benchmark to add.
   */
  public void addBenchmark(
      final org.perfidix.ant.Benchmark inner) {
    if (isDebugEnabled()) {
      inner.setDebugOn();
    }
    benchmarks.add(inner);
  }

  /**
   * adds a randomizer.
   * @param randomizer the randomizer.
   */
  public void addRandomizer(final Randomizer randomizer) {
    randomizerBuilder = randomizer;
  }

}
