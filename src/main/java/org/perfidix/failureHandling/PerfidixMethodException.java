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
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class PerfidixMethodException extends Exception {
    /** Serial because of serializable */
    private static final long serialVersionUID = 5251116501564408317L;

    /**
     * Encapsulated Exception.
     */
    private final Exception exec;

    /**
     * Related method.
     */
    private final Method meth;

    /** Related annotation to this method */
    private final Class<? extends Annotation> relatedAnno;

    /**
     * Simple Constructor.
     * 
     * @param paramExec
     *            the exception to be hold in this container.
     * @param paramMeth
     *            the related method to the exception.
     * @param paramAnnotation
     *            the related annotation to this method
     */
    public PerfidixMethodException(
            final Exception paramExec, final Method paramMeth,
            final Class<? extends Annotation> paramAnnotation) {
        this.exec = paramExec;
        this.meth = paramMeth;
        this.relatedAnno = paramAnnotation;
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

    /**
     * Getter for annotation
     * 
     * @return the related annotation for this errored method
     */
    public final Class<? extends Annotation> getRelatedAnno() {
        return relatedAnno;
    }

}
