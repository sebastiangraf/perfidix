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

import org.perfidix.annotation.Bench;

/**
 * This class represents the valid return of an reflective invocation. This can
 * include results from different meters when invoking {@link Bench} annotated
 * methods.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class ValidInvocationReturn extends AbstractInvocationReturn {

    /**
     * Results from an invocation of {@link Bench} annotated methods. Can be
     * empty otherwise.
     */
    private final double[] values;

    /**
     * Constructor
     * 
     * @param meth
     *            the {@link Method} for this return
     * @param relatedAnno
     *            the related annotation
     * @param doubles
     *            the results, can be left out.
     */
    public ValidInvocationReturn(
            final Method meth, final Class<? extends Annotation> relatedAnno,
            final double... doubles) {
        super(meth, relatedAnno);
        this.values = doubles;
    }

    /**
     * Getter for the results, can be empty.
     * 
     * @return the values
     */
    public final double[] getValues() {
        return values;
    }

}
