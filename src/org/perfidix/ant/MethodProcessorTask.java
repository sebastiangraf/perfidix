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
 * $Id: MethodProcessorTask.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ant;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;

public abstract class MethodProcessorTask extends PerfidixTask {

  private ArrayList<Method> methodNames = new ArrayList<Method>();

  private ArrayList<java.lang.reflect.Method> builtMethods =
      new ArrayList<java.lang.reflect.Method>();

  final ArrayList<Method> getMethodNames() {
    return methodNames;
  }

  public final void execute() throws BuildException {
    Iterator<Method> it = methodNames.iterator();
    while (it.hasNext()) {
      Method my = it.next();
      my.execute();
      builtMethods.add(my.getMethod());
    }
  }

  final ArrayList<java.lang.reflect.Method> getMethods() {
    return builtMethods;
  }

  public final void addMethod(final Method m) {
    if (methodNames.contains(m)) {
      debug("my list already contains the method name " + m.getMethodName());
      return;
    }
    methodNames.add(m);
  }
}
