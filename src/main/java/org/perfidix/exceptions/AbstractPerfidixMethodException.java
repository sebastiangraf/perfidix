/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.exceptions;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


/**
 * This class acts as a super exception for all exception thrown by the Perfidix framework while invoking methods.
 *
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class AbstractPerfidixMethodException extends Exception {
    /**
     * Serial because of serializable.
     */
    private static final long serialVersionUID = 5251116501564408317L;

    /**
     * Encapsulated Exception.
     */
    private transient final Throwable exec;

    /**
     * Related method.
     */
    private transient final Method meth;

    /**
     * Related annotation to this method.
     */
    private transient final Class<? extends Annotation> relatedAnno;

    /**
     * Simple Constructor.
     *
     * @param paramExec       the exception to be hold in this container.
     * @param paramMeth       the related method to the exception.
     * @param paramAnnotation the related annotation to this method
     */
    AbstractPerfidixMethodException(final Throwable paramExec, final Method paramMeth, final Class<? extends Annotation> paramAnnotation) {
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
    public final Class<? extends Annotation> getRelatedAnno() {
        return relatedAnno;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
        final AbstractPerfidixMethodException other = (AbstractPerfidixMethodException) obj;
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
        return this.getClass().getSimpleName() + ":" + this.exec.getClass().getSimpleName() + ":" + this.relatedAnno.getClass().getSimpleName() + ":" + this.meth.getName();
    }

}
