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
 * $Id: IFileHandlerImpl.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

/**
 * an implementation of the file handler,
 * including some common behavior for both methods.
 * @author onea
 *
 */
public abstract class IFileHandlerImpl implements IFileHandler {

  private String myResult;

  /**
   * sleeping some seconds to emulate some workload.
   *
   */
  protected void sleep() {
    int max = (int) (Math.random() * 0xFFFF);
    int myResult = 0;
    for (int i = 0; i < max; i++) {
      myResult += Math.random(); // perform some random action.
    }
    myResult += 42;
  }

  protected final void writeToFile(final String contents) {
    sleep();
    myResult = contents;
  }

  protected final String readFromFile() {
    sleep();
    return myResult;
  }

}
