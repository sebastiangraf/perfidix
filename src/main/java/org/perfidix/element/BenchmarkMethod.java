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
package org.perfidix.element;

import org.perfidix.annotation.*;
import org.perfidix.exceptions.PerfidixMethodCheckException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to mark one method which are possible benchmarkable. The method hold
 * helping methods and additional functionality for benchmarkable methods like
 * returning possible {@link BeforeBenchClass}, {@link BeforeFirstRun},
 * {@link BeforeEachRun}, {@link AfterEachRun}, {@link AfterLastRun} and
 * {@link AfterBenchClass} annotated related methods.
 *
 * @author Sebastian Graf, University of Konstanz
 * @see AfterBenchClass
 * @see AfterLastRun
 * @see AfterEachRun
 * @see BeforeEachRun
 * @see BeforeFirstRun
 * @see BeforeBenchClass
 */
public final class BenchmarkMethod {

	/**
	 * Method to be benched.
	 */
	private transient final Method methodToBench;

	/**
	 * Getting dataprovider method.
	 */
	private transient Method dataProvider;

	/**
	 * Constructor, with a definite method to bench. The method has to be
	 * checked with {@link BenchmarkMethod#isBenchmarkable(Method)} first,
	 * otherwise an IllegalArgumentException could arise.
	 *
	 * @param paramMethod
	 *            method to be benched (eventually)
	 * @throws PerfidixMethodCheckException
	 *             if method is not benchmarkable
	 */
	public BenchmarkMethod(final Method paramMethod)
			throws PerfidixMethodCheckException {
		methodToBench = paramMethod;
		setDataProvider(findDataProvider());
	}

	/**
	 * Getting the number of runs corresponding to a given method. The method
	 * MUST be a benchmarkable method, otherwise an IllegalStateException
	 * exception arises. The number of runs of an annotated method is more
	 * powerful than the number of runs as denoted by the benchclass annotation.
	 *
	 * @param meth
	 *            to be checked
	 * @return the number of runs of this benchmarkable-method
	 */
	public static int getNumberOfAnnotatedRuns(final Method meth) {
		if (!isBenchmarkable(meth)) {
			throw new IllegalArgumentException("Method " + meth
					+ " must be a benchmarkable method.");
		}
		final Bench benchAnno = meth.getAnnotation(Bench.class);
		final BenchClass benchClassAnno = meth.getDeclaringClass()
				.getAnnotation(BenchClass.class);
		int returnVal;
		if (benchAnno == null) {
			returnVal = benchClassAnno.runs();
		} else {
			returnVal = benchAnno.runs();
			// use runs from @BenchClass if none is set on method (issue #4)
			if ((returnVal == Bench.NONE_RUN) && (benchClassAnno != null)) {
				returnVal = benchClassAnno.runs();
			}
		}
		return returnVal;
	}

	/**
	 * This class finds any method with a given annotation. The method is
	 * allowed to occur only once in the class and should match the requirements
	 * for Perfidix for an execution by reflection.
	 *
	 * @param anno
	 *            of the method to be found
	 * @param clazz
	 *            class to be searched
	 * @return a method annotated by the annotation given. The method occurs
	 *         only once in the class and matched the requirements of
	 *         perfidix-reflective-invocation.
	 * @throws PerfidixMethodCheckException
	 *             if these integrity checks fail
	 */
	public static Method findAndCheckAnyMethodByAnnotation(
			final Class<?> clazz, final Class<? extends Annotation> anno)
			throws PerfidixMethodCheckException {
		// needed variables, one for check for duplicates
		Method anyMethod = null;

		// Scanning all methods
		final Method[] possAnnoMethods = clazz.getDeclaredMethods();
		for (final Method meth : possAnnoMethods) {
			if (meth.getAnnotation(anno) != null) {
				// Check if there are multiple annotated methods, throwing
				// IllegalAccessException otherwise.
				if (anyMethod == null) {
					// Check if method is valid (no param, no returnval,
					// etc.), throwing IllegalAccessException otherwise.
					if (isReflectedExecutable(meth, anno)) {
						anyMethod = meth;
					} else {
						throw new PerfidixMethodCheckException(
								new IllegalAccessException(anno.toString()
										+ "-annotated method " + meth
										+ " is not executable."), meth, anno);
					}
				} else {
					throw new PerfidixMethodCheckException(
							new IllegalAccessException("Please use only one "
									+ anno.toString()
									+ "-annotation in one class."), meth, anno);
				}
			}
		}

		return anyMethod;
	}

	/**
	 * This method should act as a check to guarantee that only specific
	 * Benchmarkables are used for benching.
	 *
	 * @param meth
	 *            method to be checked.
	 * @return true if an instance of this interface is benchmarkable, false
	 *         otherwise.
	 */
	public static boolean isBenchmarkable(final Method meth) {
		boolean returnVal = true;

		// Check if bench-anno is given. For testing purposes against
		// before/after annos
		final Bench benchAnno = meth.getAnnotation(Bench.class);

		// if method is annotated with SkipBench, the method is never
		// benchmarkable.
		final SkipBench skipBenchAnno = meth.getAnnotation(SkipBench.class);
		if (skipBenchAnno != null) {
			returnVal = false;
		}

		// Check if method is defined as beforeClass, beforeFirstRun,
		// beforeEachRun, afterEachRun, afterLastRun, afterClass. A method can
		// either be a before/after class or afterwards be benchmarkable through
		// the BenchClass annotation.
		final BeforeBenchClass beforeClass = meth
				.getAnnotation(BeforeBenchClass.class);
		if (beforeClass != null && benchAnno == null) {
			returnVal = false;
		}

		final BeforeFirstRun beforeFirstRun = meth
				.getAnnotation(BeforeFirstRun.class);
		if (beforeFirstRun != null && benchAnno == null) {
			returnVal = false;
		}

		final BeforeEachRun beforeEachRun = meth
				.getAnnotation(BeforeEachRun.class);
		if (beforeEachRun != null && benchAnno == null) {
			returnVal = false;
		}

		final AfterEachRun afterEachRun = meth
				.getAnnotation(AfterEachRun.class);
		if (afterEachRun != null && benchAnno == null) {
			returnVal = false;
		}

		final AfterLastRun afterLastRun = meth
				.getAnnotation(AfterLastRun.class);
		if (afterLastRun != null && benchAnno == null) {
			returnVal = false;
		}

		final AfterBenchClass afterClass = meth
				.getAnnotation(AfterBenchClass.class);
		if (afterClass != null && benchAnno == null) {
			returnVal = false;
		}

		// if method is not annotated with Bench and class is not annotated with
		// BenchClass, the method is never benchmarkable.
		final BenchClass classBenchAnno = meth.getDeclaringClass()
				.getAnnotation(BenchClass.class);
		if (benchAnno == null && classBenchAnno == null) {
			returnVal = false;
		}

		// check if method is executable for perfidix purposes.
		if (!isReflectedExecutable(meth, Bench.class)) {
			returnVal = false;
		}
		return returnVal;
	}

	/**
	 * Checks if this method is executable via reflection for perfidix purposes.
	 * That means that the method has no parameters (except for , no
	 * return-value, is non-static, is public and throws no exceptions.
	 *
	 * @param meth
	 *            method to be checked
	 * @param anno
	 *            anno for method to be check, necessary since different
	 *            attributes are possible depending on the anno
	 * @return true if method matches requirements.
	 */
	public static boolean isReflectedExecutable(final Method meth,
			final Class<? extends Annotation> anno) {
		boolean returnVal = true;
		// Check if DataProvider is valid if set.
		if (anno.equals(DataProvider.class)
				&& !meth.getReturnType().isAssignableFrom(Object[][].class)) {
			returnVal = false;
		}
		// for all other methods, the return type must be void
		if (!anno.equals(DataProvider.class)
				&& !meth.getGenericReturnType().equals(Void.TYPE)) {
			returnVal = false;
		}

		// if method is static, the method is not benchmarkable
		if (!(anno.equals(BeforeBenchClass.class))
				&& Modifier.isStatic(meth.getModifiers())) {
			returnVal = false;
		}
		// if method is not public, the method is not benchmarkable
		if (!Modifier.isPublic(meth.getModifiers())) {
			returnVal = false;
		}

		return returnVal;
	}

	/**
	 * Method to find a {@link BeforeFirstRun} annotation. This method should be
	 * invoked for all methods. The corresponding class is searched after
	 * suitable methods and checks for integrity are made. If there are multiple
	 * {@link BeforeFirstRun} -annotated methods available, an exception is
	 * thrown. If there are designated special {@link BeforeFirstRun} methods as
	 * given in the parameter of the {@link Bench}-annotation, this method is
	 * taken with any further checking of the other methods in the class.
	 *
	 * @return Annotated method with BeforeFirstRun annotation, null of none
	 *         exists
	 * @throws PerfidixMethodCheckException
	 *             if integrity check of class and method fails.
	 * @see BeforeFirstRun
	 * @see Bench
	 */
	public Method[] findBeforeFirstRun() throws PerfidixMethodCheckException {

		Method method = null;

		final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
		if (benchAnno != null && !benchAnno.beforeFirstRun().equals("")) {
			List<Method> returnval = new ArrayList<Method>();

			try {
				// variable to instantiate the method by name.
				final Class<?>[] setUpParams = {};

				String[] methods = benchAnno.beforeFirstRun().split(",");
				for (String methodString : methods) {
					// getting the method by name
					method = getMethodToBench()
							.getDeclaringClass()
							.getDeclaredMethod(methodString.trim(), setUpParams);

					if (isReflectedExecutable(method, BeforeFirstRun.class)) {
						returnval.add(method);
					} else {
						throw new PerfidixMethodCheckException(
								new IllegalAccessException(
										"Failed to execute BeforeFirstRun-annotated method "
												+ method), method,
								BeforeFirstRun.class);
					}
				}
				return returnval.toArray(new Method[returnval.size()]);

			} catch (final SecurityException e) {
				throw new PerfidixMethodCheckException(e, method,
						BeforeFirstRun.class);
			} catch (final NoSuchMethodException e) {
				throw new PerfidixMethodCheckException(e, method,
						BeforeFirstRun.class);
			}
		}

		// if there was no name, a scan over the class occurs, otherwise the
		// designated method is checked.
		else {
			Method meth = findAndCheckAnyMethodByAnnotation(getMethodToBench()
					.getDeclaringClass(), BeforeFirstRun.class);
			if (meth == null) {
				return new Method[0];
			} else {
				return new Method[] { meth };
			}
		}

	}

	/**
	 * Method to find a {@link BeforeEachRun} annotation. This method should be
	 * invoked for all methods. The corresponding class is searched after
	 * suitable methods and checks for integrity are made. If there are multiple
	 * {@link BeforeEachRun} -annotated methods available, an exception is
	 * thrown. If there are designated special {@link BeforeEachRun} methods as
	 * given in the parameter of the {@link Bench}-annotation, this method is
	 * taken with any further checking of the other methods in the class.
	 *
	 * @return Annotated method with BeforeEachRun annotation, null of none
	 *         exists
	 * @throws PerfidixMethodCheckException
	 *             if integrity check of class and method fails.
	 * @see BeforeEachRun
	 * @see Bench
	 */
	public Method[] findBeforeEachRun() throws PerfidixMethodCheckException {

		Method method = null;

		final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
		if (benchAnno != null && !benchAnno.beforeEachRun().equals("")) {
			final List<Method> returnval = new ArrayList<>();
			try {

				String[] methods = benchAnno.beforeEachRun().split(",");
				for (String methodString : methods) {

					// getting the method by name
					method = getMethodToBench().getDeclaringClass()
							.getDeclaredMethod(methodString.trim());
					if (isReflectedExecutable(method, BeforeEachRun.class)) {
						returnval.add(method);
					} else {
						throw new PerfidixMethodCheckException(
								new IllegalAccessException(
										"Failed to execute BeforeEachRun-annotated method "
												+ method), method,
								BeforeEachRun.class);
					}
				}
				return returnval.toArray(new Method[returnval.size()]);

			} catch (SecurityException e) {
				throw new PerfidixMethodCheckException(e, method,
						BeforeEachRun.class);
			} catch (NoSuchMethodException e) {
				throw new PerfidixMethodCheckException(e, method,
						BeforeEachRun.class);
			}
		} else {

			// if there was no name, a scan over the class occurs, otherwise the
			// designated method is checked.
			Method meth = findAndCheckAnyMethodByAnnotation(getMethodToBench()
					.getDeclaringClass(), BeforeEachRun.class);
			if (meth == null) {
				return new Method[0];
			} else {
				return new Method[] { meth };
			}

		}
	}

	/**
	 * Method to find a {@link AfterEachRun} annotation. This method should be
	 * invoked for all methods. The corresponding class is searched after
	 * suitable methods and checks for integrity are made. If there are multiple
	 * {@link AfterEachRun} -annotated methods available, an exception is
	 * thrown. If there are designated special {@link AfterEachRun} methods as
	 * given in the parameter of the {@link Bench}-annotation, this method is
	 * taken with any further checking of the other methods in the class.
	 *
	 * @return Annotated method with AfterEachRun annotation, null of none
	 *         exists
	 * @throws PerfidixMethodCheckException
	 *             if integrity check of class and method fails.
	 * @see AfterEachRun
	 * @see Bench
	 */
	public Method[] findAfterEachRun() throws PerfidixMethodCheckException {

		Method method = null;

		final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
		if (benchAnno != null && !benchAnno.afterEachRun().equals("")) {
			List<Method> returnval = new ArrayList<Method>();
			try {

				String[] methods = benchAnno.afterEachRun().split(",");
				for (String methodString : methods) {

					// getting the method by name
					method = getMethodToBench().getDeclaringClass()
							.getDeclaredMethod(methodString.trim());
					if (isReflectedExecutable(method, AfterEachRun.class)) {
						returnval.add(method);
					} else {
						throw new PerfidixMethodCheckException(
								new IllegalAccessException(
										"AfterEachRun-annotated method "
												+ method
												+ " is not executable."),
								method, AfterEachRun.class);
					}
				}
				return returnval.toArray(new Method[returnval.size()]);
			} catch (SecurityException e) {
				throw new PerfidixMethodCheckException(e, method,
						AfterEachRun.class);
			} catch (NoSuchMethodException e) {
				throw new PerfidixMethodCheckException(e, method,
						AfterEachRun.class);
			}
		} else {
			// if there was no name, a scan over the class occurs, otherwise the
			// designated method is checked.
			Method meth = findAndCheckAnyMethodByAnnotation(getMethodToBench()
					.getDeclaringClass(), AfterEachRun.class);
			if (meth == null) {
				return new Method[0];
			} else {
				return new Method[] { meth };
			}

		}
	}

	/**
	 * Method to find a {@link AfterLastRun} annotation. This method should be
	 * invoked for all methods. The corresponding class is searched after
	 * suitable methods and checks for integrity are made. If there are multiple
	 * {@link AfterLastRun} -annotated methods available, an exception is
	 * thrown. If there are designated special {@link AfterLastRun} methods as
	 * given in the parameter of the {@link Bench}-annotation, this method is
	 * taken with any further checking of the other methods in the class.
	 *
	 * @return Annotated method with AfterLastRun annotation, null of none
	 *         exists
	 * @throws PerfidixMethodCheckException
	 *             if integrity check of class and method fails.
	 * @see AfterLastRun
	 * @see Bench
	 */
	public Method[] findAfterLastRun() throws PerfidixMethodCheckException {

		Method method = null;

		final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
		if (benchAnno != null && !benchAnno.afterLastRun().equals("")) {
			List<Method> returnval = new ArrayList<Method>();
			try {
				// variable to instantiate the method by name.
				final Class<?>[] setUpParams = {};

				String[] methods = benchAnno.afterLastRun().split(",");
				for (String methodString : methods) {
					// getting the method by name
					method = getMethodToBench()
							.getDeclaringClass()
							.getDeclaredMethod(methodString.trim(), setUpParams);

					if (isReflectedExecutable(method, AfterLastRun.class)) {
						returnval.add(method);
					} else {
						throw new PerfidixMethodCheckException(
								new IllegalAccessException(
										"AfterLastRun-annotated method "
												+ method
												+ " is not executable."),
								method, AfterLastRun.class);
					}
				}
				return returnval.toArray(new Method[returnval.size()]);
			} catch (final SecurityException e) {
				throw new PerfidixMethodCheckException(e, method,
						AfterLastRun.class);
			} catch (final NoSuchMethodException e) {
				throw new PerfidixMethodCheckException(e, method,
						AfterLastRun.class);
			}
		} else {
			// if there was no name, a scan over the class occurs, otherwise the
			// designated method is checked.
			Method meth = findAndCheckAnyMethodByAnnotation(getMethodToBench()
					.getDeclaringClass(), AfterLastRun.class);
			if (meth == null) {
				return new Method[0];
			} else {
				return new Method[] { meth };
			}
		}
	}

	/**
	 * This method checks whether a method uses a data provider for dynamic
	 * input
	 *
	 * @return true if method is parameterized, false otherwise
	 * @throws PerfidixMethodCheckException
	 */
	private Method findDataProvider() throws PerfidixMethodCheckException {

		final Bench benchAnno = getMethodToBench().getAnnotation(Bench.class);
		Method dataProvider = null;
		if (benchAnno != null && !benchAnno.dataProvider().equals("")) {
			try {
				// Getting the String name for the dataProvider
				final String dataProviderIdentifier = benchAnno.dataProvider();
				// Check if there is a Dataprovider with the name as identifier
				for (final Method eachMeth : getMethodToBench()
						.getDeclaringClass().getMethods()) {
					DataProvider anno = eachMeth
							.getAnnotation(DataProvider.class);
					if (anno != null
							&& anno.name().equals(dataProviderIdentifier)) {
						if (dataProvider == null) {
							dataProvider = eachMeth;
						} else {
							throw new PerfidixMethodCheckException(
									new RuntimeException(
											"Only one Dataprovider allowed, but found "
													+ dataProvider + " and "
													+ eachMeth), eachMeth,
									DataProvider.class);
						}
					}
				}
				// if not, try to find a method with the name
				if (dataProvider == null) {
					dataProvider = getMethodToBench().getDeclaringClass()
							.getDeclaredMethod(dataProviderIdentifier);
				}

				// perform the checks same as testng: two dimensional array for
				// return val, parameter is mapping of
				// class, array of instances.
				// checking return types of dataprovider method
				final Class<?> returnType = dataProvider.getReturnType();
				// final Class<?>[] parameters = meth.getParameterTypes();
				if (Object[][].class.isAssignableFrom(returnType)) {
					// checking input types of bench method against the return
					// types of the parameter
					// Class<Object[][]> castedReturnType = (Class<Object[][]>)
					// returnType;
					// TODO implement check
				}
			} catch (NoSuchMethodException | SecurityException e) {
				// no data provider method, will be returned as false anyhow
			}
		}
		return dataProvider;
	}

	/**
	 * Simple getter for encapsulated method.
	 *
	 * @return the methodToBench
	 */
	public Method getMethodToBench() {
		return methodToBench;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (methodToBench == null) {
			result = prime * result;
		} else {
			result = prime * result + methodToBench.hashCode();
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		return obj.hashCode() == this.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return methodToBench.getName();
	}

	/**
	 * This method returns the fully qualified name consisting of its own name
	 * and its class name
	 *
	 * @return the {@link String} name von the bench method consisting of fully
	 *         qualified name of its class and its own name
	 */
	public String getMethodWithClassName() {
		return methodToBench.getDeclaringClass().getName() + "."
				+ methodToBench.getName();
	}

	public Method getDataProvider() {
		return dataProvider;
	}

	void setDataProvider(Method dataProvider) {
		this.dataProvider = dataProvider;
	}

}
