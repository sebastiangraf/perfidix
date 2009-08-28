/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
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
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.perclipse.launcher;

import org.eclipse.jdt.core.IType;

/**
 * @author lewandow
 */
public class BenchSearchResult {

    private final IType[] fTypes;

    /**
     * The constructor sets the IType array fTypes with the given parameter.
     * 
     * @param types
     *            A type array for bench classes.
     */
    public BenchSearchResult(IType[] types) {
        fTypes = types;
    }

    /**
     * This method gives an array of type IType.
     * 
     * @return the IType array.
     */
    public IType[] getTypes() {
        return fTypes;
    }

    /**
     * This method tells you if the getTypes array is empty.
     * 
     * @return a boolean value for the isEmpty request, true if the getTypes
     *         array length is smaller than 1 or false if the getTypes array
     *         length is bigger than 0
     */
    boolean isEmpty() {
        return getTypes().length <= 0;
    }
}