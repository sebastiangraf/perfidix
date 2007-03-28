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
 * $Id: Method.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ant;

import org.apache.tools.ant.BuildException;

/**
 * the possibility to make a method run.
 * @author onea
 * @since 06.2006
 *
 */
public class Method extends PerfidixTask {

  private String clazz;

  private String name;

  private double probability;

  private java.lang.reflect.Method method = null;

  /**
   * 
   * @param myClazz the class name
   */
  public void setClass(final String myClazz) {
    clazz = myClazz;
  }

  public double getProbability() {
    return probability;
  }

  /**
   * TODO further validation.
   * @param prob
   */
  public void setProbability(final String prob) {
    probability = Double.parseDouble(prob) / 100;

  }

  /**
   * 
   * @return the method's name.
   */
  public String getMethodName() {
    return name;
  }

  /**
   * 
   * @param myName the name of the method.
   */
  public void setName(final String myName) {
    name = myName;
  }

  /**
   * @throws BuildException when the execution did not work.
   */
  public void execute() throws BuildException {
    try {
      method = getClass().getClassLoader().loadClass(clazz).getMethod(name);
    } catch (SecurityException e) {
      debug("got a security exception here " + e.getMessage());
      debug(e.getMessage());
      throw new BuildException(e);
    } catch (NoSuchMethodException e) {
      debug("method does not exist.");
      throw new BuildException(e);
    } catch (ClassNotFoundException e) {
      debug("class not found.");
      throw new BuildException(e);
    }
  }

  /**
   * 
   * @return the invokable method.
   */
  public java.lang.reflect.Method getMethod() {
    return method;
  }

}
