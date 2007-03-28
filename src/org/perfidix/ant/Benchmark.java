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
 * $Id: Benchmark.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ant;


import org.apache.tools.ant.BuildException;
import org.perfidix.Benchmarkable;


/**
 * the loader, which loads a benchmark from a perfidix ant task.
 * @author onea
 *
 */
public class Benchmark extends PerfidixTask {

  /**
   * the classname to load.
   * since i cannot throw an exception on illegal
   * arguments in the setter methods, i
   * have to aggregate the class name and 
   */
  private String className;

  /**
   * 
   * @param cls clas.
   */
  public void setClass(final String cls) {
    className = cls;
  }

  /**
   * 
   * @return the benchmarkable class.
   * @throws BuildException an exception when the benchmarkable 
   *  class could not be loaded.
   */
  public Benchmarkable getBenchmarkable() throws BuildException {
    debug("i shall load " + className);
    try {
      Object o = Class.forName(className).newInstance();
      if (null == o) {
        debug("class is null, could not be loaded!");
        return null;
      }
      debug("the loaded object's class is: " + o.getClass().getName());
      if (!(o instanceof Benchmarkable)) {
        debug("the loaded object is no instance of Benchmarkable!");
        return null;
      }
      return Benchmarkable.class.cast(o);
    } catch (Exception e) {
      if (isDebugEnabled()) {
        debug("the stack trace for this exception: ");
        e.printStackTrace();
      }
      throw new BuildException("Sorry, could not load "
          + className
          + ": "
          + e.getCause().getMessage(), e);
    }
  }

}
