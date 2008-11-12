/*
 * Copyright 2007 University of Konstanz
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
 * $Id: Bench.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Annotation marks a simple bench. Each Method annotated with
 * <code>Bench</code> is executed by perfidix.
 * 
 * @author Sebastian Graf, University of Konstanz
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bench {

    /**
     * Parameter of the method which works as a setUp-like, but just once for
     * all runs. For more information to setUp, look at JUnit.
     */
    String beforeFirstRun() default "";

    /**
     * Parameter of the method which works as a tearDown-like, but just once for
     * all runs. For more information to setUp, look at JUnit.
     */
    String afterLastRun() default "";

    /**
     * Parameter of the method which works as a setUp-like. For more information
     * to setUp, look at JUnit.
     */
    String beforeEachRun() default "";

    /**
     * Parameter of the method which works as a tearDown-like. For more
     * information to setUp, look at JUnit.
     */
    String afterEachRun() default "";

    /**
     * Parameter for the number of runs of this bench.
     */
    int runs() default -1;

}
