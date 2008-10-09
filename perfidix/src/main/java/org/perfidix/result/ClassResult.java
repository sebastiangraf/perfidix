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

/**
 * @author axo
 */
public class ClassResult extends ResultContainer<MethodResult> {

    private String theClass;

    /**
     * @param theName
     *            the name.
     * @param classUnderTest
     *            the class name which is benchmarked.
     */
    public ClassResult(final String theName, final String classUnderTest) {
        super(theName);
        theClass = classUnderTest;
    }

    /**
     * @param theName
     *            only the name.
     */
    public ClassResult(final String theName) {
        super(theName);
        theClass = null;
    }

    /**
     * @return the class under test. may be null!
     */
    public String getClassUnderTest() {
        return theClass;
    }

}