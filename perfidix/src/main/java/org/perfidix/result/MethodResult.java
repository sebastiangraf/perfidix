/*
 * Copyright 2008 Distributed Systems Group, University of Konstanz
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
 * $Revision$
 * $Author$
 * $Date$
 *
 */
package org.perfidix.result;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author onea
 */
public class MethodResult extends ResultContainer<SingleResult> {

    /**
     * @param theName
     *            the name.
     */
    public MethodResult(final String theName) {
        super(theName);
    }

    @Override
    public long getNumberOfRuns() {
        /*
         * debug Iterator<SingleResult> it = getChildren().iterator(); while
         * (it.hasNext()) { SingleResult s = it.next();
         * System.out.println(getName() + " number of runs: " + s.getName() +
         * " " + s.getNumberOfRuns()); }
         */
        return getChildren().get(0).getNumberOfRuns();
    }

    /**
     * returns the children.
     * 
     * @return the children.
     */
    @Override
    public ArrayList<SingleResult> getChildren() {
        ArrayList<SingleResult> children = super.getChildren();
        Iterator<ArrayList<SingleResult>> cust =
                this.getCustomChildren().values().iterator();
        while (cust.hasNext()) {
            children.addAll(cust.next());
        }
        return children;
    }

}