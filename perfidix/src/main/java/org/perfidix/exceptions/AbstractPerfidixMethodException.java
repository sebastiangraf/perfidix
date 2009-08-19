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
package org.perfidix.exceptions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * This class acts as a super exception for all exception thrown by the Perfidix
 * framework while invoking methods.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractPerfidixMethodException extends Exception {
    /** Serial because of serializable. */
    private static final long serialVersionUID = 5251116501564408317L;

    /**
     * Encapsulated Exception.
     */
    private transient final Throwable exec;

    /**
     * Related method.
     */
    private transient final Method meth;

    /** Related annotation to this method. */
    private transient final Class< ? extends Annotation> relatedAnno;

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
    public AbstractPerfidixMethodException(
            final Throwable paramExec, final Method paramMeth,
            final Class< ? extends Annotation> paramAnnotation) {
        super();
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
     * Getter for the encapsulated {@link Exception}.
     * 
     * @return the exec which is hold in this container.
     */
    public final Throwable getExec() {
        return exec;
    }

    /**
     * Getter for annotation.
     * 
     * @return the related annotation for this errored method
     */
    public final Class< ? extends Annotation> getRelatedAnno() {
        return relatedAnno;
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        if (exec == null) {
            result = prime * result;
        } else {
            result = prime * result + exec.hashCode();
        }
        if (meth == null) {
            result = prime * result;
        } else {
            result = prime * result + meth.hashCode();
        }
        if (relatedAnno == null) {
            result = prime * result;
        } else {
            result = prime * result + relatedAnno.hashCode();
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(final Object obj) {
        boolean returnVal = true;
        if (this == obj) {
            returnVal = true;
        }
        if (obj == null) {
            returnVal = false;
        }
        if (getClass() != obj.getClass()) {
            returnVal = false;
        }
        final AbstractPerfidixMethodException other =
                (AbstractPerfidixMethodException) obj;
        if (exec == null) {
            if (other.exec != null) {
                returnVal = false;
            }
        } else {
            if (!exec.equals(other.exec)) {
                returnVal = false;
            }
        }
        if (meth == null) {
            if (other.meth != null) {
                returnVal = false;
            }
        } else {
            if (!meth.equals(other.meth)) {
                returnVal = false;
            }
        }
        if (relatedAnno == null) {
            if (other.relatedAnno != null) {
                returnVal = false;
            }
        } else {
            if (!relatedAnno.equals(other.relatedAnno)) {
                returnVal = false;
            }
        }
        return returnVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(":");
        builder.append(this.exec.getClass().getSimpleName());
        builder.append(":");
        builder.append(this.relatedAnno.getClass().getSimpleName());
        builder.append(":");
        builder.append(this.meth.getName());
        return builder.toString();
    }

}
