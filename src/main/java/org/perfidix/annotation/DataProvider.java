package org.perfidix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method as supplying data for a bench method. The data provider name
 * defaults to method name. The annotated method must return an Object[][] where
 * each Object[] can be assigned the parameter list of the test method. The @Bench
 * method that wants to receive data from this DataProvider needs to use a
 * dataProvider name equals to the name of this annotation.
 *
 * @author Nico Haase, Technische Universitaet Darmstadt
 * @see DataProvider within TestNG
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataProvider {

	/**
	 * Name for this Data provider.
	 * 
	 * @return String of name of datarprovider to make it identifable.
	 */
	String name() default "";
}
