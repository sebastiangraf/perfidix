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
package org.perfidix.failureHandling;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * This class acts as a container for invalid returnValues. Is holding
 * Exceptions as well.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class InvalidInvocationReturn extends AbstractInvocationReturn {

    /** encapsulated return exception */
    private final Exception exc;

    /**
     * Constructor
     * 
     * @param parameter
     *            exception to be encapsulated
     * @param meth
     *            {@link Method} instance to be referenced
     * @param relatedAnno
     *            the annotation related to this invalid invocation return.
     */
    public InvalidInvocationReturn(
            final Exception parameter, final Method meth,
            final Class<? extends Annotation> relatedAnno) {
        super(meth, relatedAnno);
        exc = parameter;
    }

    /**
     * Getter for encapsulated Exception
     * 
     * @return the {@link Exception} in the invalid return
     */
    public final Exception getExc() {
        return exc;
    }

}
