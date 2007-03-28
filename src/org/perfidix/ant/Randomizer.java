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
 * $Id: Randomizer.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ant;

import java.util.Iterator;


import org.apache.tools.ant.BuildException;
import org.perfidix.IRandomizer;
import org.perfidix.exceptions.ProbabilityInvalidException;


/**
 * 
 * @author onea
 *
 */
public class Randomizer extends PerfidixTask {

  private Include include;

  private Exclude exclude;

  private Invoke invoke;

  private IRandomizer.Randomizer randomizer;

  IRandomizer.Randomizer getRandomizer() {
    return randomizer;
  }

  /**
   *  
   * this is not a comment.
   * @see org.apache.tools.ant.Task#execute()
   */
  public void execute() throws BuildException {
    debug("trying to build a randomizer...");
    randomizer = new IRandomizer.Randomizer();

    if (!(null == include)) {
      debug("setting probability to the includes.");
      setProbability(1.0, include);
    }
    if (!(null == exclude)) {
      debug("setting probability to the excludes.");
      setProbability(0.0, exclude);
    }
    if (!(null == invoke)) {
      debug("setting probability to the invokes.");
      setProbability(invoke);
    }
  }

  private void setProbability(final Invoke i) {
    i.execute();
    Iterator<Method> it = i.getMethodNames().iterator();
    while (it.hasNext()) {
      Method tmp = it.next();
      setProbability(tmp.getProbability(), tmp.getMethod());
    }
  }

  private void setProbability(double prob, final java.lang.reflect.Method m) {
    debug("trying to set probability " + prob + " to method " + m.getName());
    try {
      randomizer.setTreshold(m, prob);
    } catch (ProbabilityInvalidException e) {
      throw new BuildException("invalid treshold for " + m.getName(), e);
    }
  }

  private void setProbability(double prob, final MethodProcessorTask c) {
    c.execute();
    Iterator<java.lang.reflect.Method> it = c.getMethods().iterator();
    while (it.hasNext()) {
      setProbability(prob, it.next());
    }
  }

  public void addInclude(final Include i) {
    include = i;
  }

  public void addExclude(Exclude e) {
    exclude = e;
  }

  public void addInvoke(Invoke i) {
    invoke = i;
  }

}
