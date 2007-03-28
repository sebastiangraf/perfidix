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
 * $Id: PerfidixTask.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ant;

import java.util.Date;

import org.apache.tools.ant.Task;

/**
 * the generic task, containing common behavior of our prefidix tasks.
 * @author onea
 *
 */
public class PerfidixTask extends Task {
  /**
   * whether to debug or not to debug.
   */
  private boolean debug = false;

  /**
   * sends a debug message.
   * @param message the message to send debug info.
   */
  protected void debug(final String message) {
    if (debug) {
      System.out.println(new Date().toString() + ": " + message);
    }
  }

  /**
   * whether debugging is enabled.
   * @return whether to debug or not.
   */
  protected boolean isDebugEnabled() {
    return debug;
  }

  /**
   * sets the debug mode=on.
   * @param in the debug mode. either "true" or "false"
   */
  public void setDebug(final String in) {
    debug = in.toLowerCase().equals("true");
  }

  /**
   * sets the debug mode on.
   *
   */
  public void setDebugOn() {
    debug = true;
  }

}
