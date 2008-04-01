package org.perfidix;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that it is not used for a bench, no matter if a Bench oder a
 * BenchClass annotation is present.
 * 
 * @author Sebastian Graf, University of Constance
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SkipBench {

}
