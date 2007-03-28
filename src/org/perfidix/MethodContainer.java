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
 * $Id: MethodContainer.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * an internal class containing methods to be invoked.
 * @author onea
 * @TODO this does not work with the current implementation.
 * the declaring class _still_ must be an instance of 
 * benchmarkable in order to be able to invoke the method.
 * an interesting approach would be to override the invoke(String method)
 * of the Class object ... it could be that this won't work.
 * 
 */
@Deprecated
class MethodContainer extends Benchmarkable {

  private ArrayList<Method> methods = new ArrayList<Method>();

  /**
   * 
   * @param someMethod method bla.
   */
  @Deprecated
  public void add(final Method someMethod) {
    if (methods.contains(someMethod)) {
      return;
    }
    methods.add(someMethod);
  }

  /**
   * 
   * @return an array of methods.
   */
  @Deprecated
  public ArrayList<Method> getInvokableMethods() {
    return methods;
  }
  
}
