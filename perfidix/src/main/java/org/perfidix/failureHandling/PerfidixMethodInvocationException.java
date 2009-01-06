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
 * This class acts as container for all {@link Exception} types thrown while
 * invoking a method which is reflective executable via the Perfidix framework.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class PerfidixMethodInvocationException
        extends PerfidixMethodException {

    /** serializable id */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param paramExec
     *            encapsulated exception
     * @param paramMeth
     *            the method where the error occured
     * @param paramAnnotation
     *            the related annotation of the method
     */
    public PerfidixMethodInvocationException(
            final Throwable paramExec, final Method paramMeth,
            final Class<? extends Annotation> paramAnnotation) {
        super(paramExec, paramMeth, paramAnnotation);
    }

    /**
     * Constructor.
     * 
     * @param paramExec
     *            encapsulated exception
     * @param paramAnnotation
     *            the related annotation of the method
     */
    public PerfidixMethodInvocationException(
            final Throwable paramExec,
            final Class<? extends Annotation> paramAnnotation) {
        super(paramExec, null, paramAnnotation);
    }

}
