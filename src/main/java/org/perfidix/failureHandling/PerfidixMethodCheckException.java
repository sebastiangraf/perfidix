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

import java.lang.reflect.Method;

/**
 * This class acts as container for all {@link Exception} types thrown while
 * checking if a method is reflective executable via the Perfidix framework.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class PerfidixMethodCheckException extends Exception {

    /** Serial because of serializable */
    private static final long serialVersionUID = -4423444481252410126L;

    /**
     * Encapsulated Exception.
     */
    private final Exception exec;

    /**
     * Related method.
     */
    private final Method meth;

    /**
     * Simple Constructor.
     * 
     * @param paramExec
     *            the exception to be hold in this container.
     * @param paramMeth
     *            the related method to the exception.
     */
    public PerfidixMethodCheckException(
            final Exception paramExec, final Method paramMeth) {
        this.exec = paramExec;
        this.meth = paramMeth;
    }

    /**
     * Getter for the related {@link Method}.
     * 
     * @return the method for this exception.
     */
    public final Method getMethod() {
        return meth;
    }

    /**
     * Getter for the encapsulated {@link Exception}
     * 
     * @return the exec which is hold in this container.
     */
    public final Exception getExec() {
        return exec;
    }

}
