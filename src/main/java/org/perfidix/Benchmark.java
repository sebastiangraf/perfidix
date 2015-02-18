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
package org.perfidix;

import org.perfidix.AbstractConfig.StandardConfig;
import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.Bench;
import org.perfidix.element.AbstractMethodArrangement;
import org.perfidix.element.BenchmarkElement;
import org.perfidix.element.BenchmarkExecutor;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.PerfidixMethodCheckException;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.result.BenchmarkResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class to hold all classes which want to be benchmarked.
 * 
 * <pre>
 *
 *
 * public class MyBenchmark {
 * 
 * 	public static void main(final String[] args) {
 * 		// Build up own config
 * 		final AbstractConfig config = new MyConfig();
 * 
 * 		// Initialise benchmark with config
 * 		final Benchmark benchmark = new Benchmark(config);
 * 
 * 		// Adding Classes to benchmark
 * 		benchmark.add(Class1.class);
 * 		benchmark.add(Class2.class);
 * 		benchmark.add(Object3); // of Class3, every class is allowed to be
 * 								// inserted only once
 * 
 * 		// Running benchmark
 * 		final BenchmarkResult result = benchmark.run();
 * 
 * 		// Printing out Result in a Tabular, every AbstractOutput implementing
 * 		// class is allowed.
 * 		new TabularSummaryOutput().visitBenchmark(result);
 * 
 * 	}
 * }
 *
 * </pre>
 *
 * @author Sebastian Graf, University of Konstanz
 * @see AbstractConfig
 * @see BenchmarkResult
 */
public final class Benchmark {

	/**
	 * Simple random for gc-prob
	 */
	private transient static final Random RAN = new Random();
	/**
	 * Set with all used classes.
	 */
	private transient final Set<Class<?>> clazzes;
	/**
	 * Already instantiated objects
	 */
	private transient final Set<Object> objects;
	/**
	 * Configuration of benchmark, holding everything.
	 */
	private transient final AbstractConfig conf;

	/**
	 * Constructor with a fixed set of used meters.
	 *
	 * @param paramConf
	 *            Configuration for Benchmark
	 */
	public Benchmark(final AbstractConfig paramConf) {
		conf = paramConf;
		this.clazzes = new LinkedHashSet<Class<?>>();
		this.objects = new LinkedHashSet<Object>();

	}

	/**
	 * Convenience constructor using the {@link StandardConfig}
	 */
	public Benchmark() {
		this(new StandardConfig());
	}

	/**
	 * Getting the content of a dataprovider.
	 *
	 * @param meth
	 *            the dataprovider method
	 * @param toInvoke
	 *            the object on which the method is invoked
	 * @return a two-dimensional array containing multiple sets of parameters
	 *         representing single runs each.
	 */
	private static Object[][] getDataProviderContent(final Method meth,
			final Object toInvoke) {

		Object[][] res;
		try {
			res = (Object[][]) meth.invoke(toInvoke);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalArgumentException("Method " + meth
					+ " as data provider throws an exception on invocation");
		}

		return res;
	}

	/**
	 * Adding a class to bench to this benchmark. This class should contain
	 * benchmarkable methods, otherwise it will be ignored.
	 *
	 * @param clazz
	 *            to be added.
	 */
	public void add(final Class<?> clazz) {
		if (this.clazzes.contains(clazz)) {
			throw new IllegalArgumentException(
					"Only one class-instance per benchmark allowed");
		} else {
			this.clazzes.add(clazz);
		}
	}

	/**
	 * Adding a already instantiated objects to benchmark. Per benchmark, only
	 * one objects of each class is allowed.
	 *
	 * @param obj
	 *            to be added
	 */
	public void add(final Object obj) {
		final Class<?> clazz = obj.getClass();

		if (this.clazzes.contains(clazz)) {
			throw new IllegalArgumentException(
					"Only one class-instance per benchmark allowed");
		} else {
			this.clazzes.add(clazz);
			this.objects.add(obj);
		}
	}

	/**
	 * Getting the number of all methods and all runs
	 *
	 * @return a map with all methods and the runs.
	 * @throws PerfidixMethodCheckException
	 *             if methods are not bechmarkable
	 */
	public Map<BenchmarkMethod, Integer> getNumberOfMethodsAndRuns()
			throws PerfidixMethodCheckException {
		final Map<BenchmarkMethod, Integer> returnVal = new HashMap<BenchmarkMethod, Integer>();
		// instantiate objects, just for getting runs
		final List<BenchmarkMethod> meths = getBenchmarkMethods();
		for (final BenchmarkMethod meth : meths) {
			// TODO respect data provider
			int numberOfRuns = BenchmarkMethod.getNumberOfAnnotatedRuns(meth
					.getMethodToBench());
			if (numberOfRuns == Bench.NONE_RUN) {
				numberOfRuns = conf.getRuns();
			}
			returnVal.put(meth, numberOfRuns);
		}
		return returnVal;
	}

	/**
	 * Running this benchmark
	 *
	 * @return {@link BenchmarkResult} the result in an {@link BenchmarkResult}
	 *         container.
	 */
	public BenchmarkResult run() {
		final BenchmarkResult res = new BenchmarkResult(conf.getListener());
		BenchmarkExecutor.initialize(conf, res);

		// instantiate methods
		final Map<Class<?>, Object> instantiatedObjs = instantiateObjects(res);

		// getting Benchmarkables
		try {
			final List<BenchmarkElement> elements = getBenchmarkElements(instantiatedObjs);

			// arranging them
			final AbstractMethodArrangement arrangement = AbstractMethodArrangement
					.getMethodArrangement(elements, conf.getArrangement());

			// getting the mapping and executing beforemethod
			final Map<Class<?>, Object> objectsToExecute = executeBeforeBenchClass(
					instantiatedObjs, res);

			// executing the bench for the arrangement
			for (final BenchmarkElement elem : arrangement) {
				// invoking gc if possible
				if (RAN.nextDouble() < conf.getGcProb()) {
					System.gc();
				}

				final BenchmarkExecutor exec = BenchmarkExecutor
						.getExecutor(elem);

				final Object obj = objectsToExecute.get(elem.getMeth()
						.getMethodToBench().getDeclaringClass());
				// check needed because of failed initialization of objects
				if (obj != null) {
					exec.executeBeforeMethods(obj);
					exec.executeBench(obj, elem.getParameter());
					exec.executeAfterMethods(obj);
				}
			}

			// cleaning up methods to benchmark
			tearDownObjectsToExecute(objectsToExecute, res);
		} catch (PerfidixMethodCheckException exc) {
			res.addException(exc);
		}
		return res;
	}

	/**
	 * Setting up executable objects for all registered classes and executing
	 * {@link BeforeBenchClass} annotated methods. If an {@link Exception}
	 * occurs, this failure will be stored in the {@link BenchmarkResult} and
	 * the class will not be instantiated
	 *
	 * @param res
	 *            {@link BenchmarkResult} for storing possible failures.
	 * @return a mapping with class->objects for all registered classes-
	 */
	private Map<Class<?>, Object> instantiateObjects(final BenchmarkResult res) {
		// datastructure initialization for all objects
		final Map<Class<?>, Object> objectsToUse = new Hashtable<Class<?>, Object>();

		// generating including already instaniated objects
		for (final Object obj : this.objects) {
			final Class<?> clazz = obj.getClass();
			objectsToUse.put(clazz, obj);
		}

		// generating objects for each registered class
		for (final Class<?> clazz : clazzes) {
			// generating a new instance on which the benchmark will be
			// performed if there isn't a user generated one
			if (!objectsToUse.containsKey(clazz)) {
				try {
					final Object obj = clazz.newInstance();
					objectsToUse.put(clazz, obj);
					// otherwise adding an exception to the result
				} catch (final InstantiationException | IllegalAccessException e) {
					res.addException(new PerfidixMethodInvocationException(e,
							BeforeBenchClass.class));
				}
			}
		}

		return objectsToUse;
	}

	/**
	 * Executing beforeBenchClass if present.
	 *
	 * @param instantiatedObj
	 *            with the instantiatedObj;
	 * @param res
	 *            where the Exceptions should be stored to
	 * @return valid instances with valid beforeCall
	 */
	private Map<Class<?>, Object> executeBeforeBenchClass(
			final Map<Class<?>, Object> instantiatedObj,
			final BenchmarkResult res) {

		final Map<Class<?>, Object> returnVal = new Hashtable<Class<?>, Object>();

		// invoking before bench class
		for (final Class<?> clazz : instantiatedObj.keySet()) {

			final Object objectToUse = instantiatedObj.get(clazz);
			// ..the search for the beforeClassMeth begins...
			Method beforeClassMeth = null;
			boolean continueVal = true;
			try {
				beforeClassMeth = BenchmarkMethod
						.findAndCheckAnyMethodByAnnotation(clazz,
								BeforeBenchClass.class);
				// ... and if this search is throwing an exception, the
				// exception will be added and a flag is set to break up
			} catch (final PerfidixMethodCheckException e) {
				res.addException(e);
				continueVal = false;
			}
			// if everything worked well...
			if (continueVal) {
				if (beforeClassMeth == null) {
					// ...either the objects is directly mapped to the class
					// for executing the benches
					returnVal.put(clazz, objectToUse);
				} else {
					// ... or the beforeMethod will be executed and a
					// possible exception stored to the result...
					final PerfidixMethodCheckException beforeByCheck = BenchmarkExecutor
							.checkMethod(objectToUse, BeforeBenchClass.class,
									beforeClassMeth);
					if (beforeByCheck == null) {
						final PerfidixMethodInvocationException beforeByInvok = BenchmarkExecutor
								.invokeMethod(objectToUse,
										BeforeBenchClass.class, beforeClassMeth);
						if (beforeByInvok == null) {
							returnVal.put(clazz, objectToUse);
						} else {
							res.addException(beforeByInvok);
						}
					} else {
						res.addException(beforeByCheck);
					}
				}
			}
		}
		return returnVal;
	}

	/**
	 * Tear down executable objects for all registered classes and executing
	 * {@link AfterBenchClass} annotated methods.
	 *
	 * @param objects
	 *            a mapping with class->objects to be teared down
	 * @param res
	 *            the {@link BenchmarkResult} for storing possible failures.
	 */
	private void tearDownObjectsToExecute(final Map<Class<?>, Object> objects,
			final BenchmarkResult res) {

		// executing tearDown for all clazzes registered in given Map
		for (final Class<?> clazz : objects.keySet()) {
			final Object objectToUse = objects.get(clazz);
			if (objectToUse != null) {
				// executing AfterClass for all objects.
				Method afterClassMeth = null;
				try {
					afterClassMeth = BenchmarkMethod
							.findAndCheckAnyMethodByAnnotation(clazz,
									AfterBenchClass.class);
				} catch (final PerfidixMethodCheckException e) {
					res.addException(e);
				}
				// if afterClassMethod exists, the method will be executed and
				// possible failures will be stored in the BenchmarkResult
				if (afterClassMeth != null) {
					final PerfidixMethodCheckException afterByCheck = BenchmarkExecutor
							.checkMethod(objectToUse, AfterBenchClass.class,
									afterClassMeth);
					if (afterByCheck == null) {
						final PerfidixMethodInvocationException afterByInvok = BenchmarkExecutor
								.invokeMethod(objectToUse,
										AfterBenchClass.class, afterClassMeth);
						if (afterByInvok != null) {
							res.addException(afterByInvok);
						}
					} else {
						res.addException(afterByCheck);
					}
				}
			}
		}
	}

	/**
	 * Getting all Benchmarkable methods out of the registered class.
	 *
	 * @return a Set with {@link BenchmarkMethod}
	 * @throws PerfidixMethodCheckException
	 */
	List<BenchmarkMethod> getBenchmarkMethods()
			throws PerfidixMethodCheckException {
		// Generating Set for returnVal
		final List<BenchmarkMethod> elems = new ArrayList<BenchmarkMethod>();
		// Getting all Methods and testing if its benchmarkable
		for (final Class<?> clazz : clazzes) {
			for (final Method meth : clazz.getDeclaredMethods()) {
				// Check if benchmarkable, if so, insert to returnVal;
				if (BenchmarkMethod.isBenchmarkable(meth)) {
					final BenchmarkMethod benchmarkMeth = new BenchmarkMethod(
							meth);
					elems.add(benchmarkMeth);
				}
			}
		}
		return elems;
	}

	/**
	 * Getting all benchmarkable objects out of the registered classes with the
	 * annotated number of runs.
	 *
	 * @param paramObjs
	 *            a set with all existing objects for getting data from the
	 *            dataproviders
	 * @return a Set with {@link BenchmarkMethod}
	 * @throws PerfidixMethodCheckException
	 */
	private List<BenchmarkElement> getBenchmarkElements(
			final Map<Class<?>, Object> paramObjs)
			throws PerfidixMethodCheckException {

		final List<BenchmarkElement> elems = new ArrayList<BenchmarkElement>();
		final List<BenchmarkMethod> meths = getBenchmarkMethods();

		for (final BenchmarkMethod meth : meths) {

			final Method dataProv = meth.getDataProvider();
			// Test if benchmark is parameterized. If not..
			if (dataProv == null) {
				// ...simple execute the benchrun on the base of the runs set...
				int numberOfRuns = BenchmarkMethod
						.getNumberOfAnnotatedRuns(meth.getMethodToBench());
				if (numberOfRuns == Bench.NONE_RUN) {
					numberOfRuns = conf.getRuns();
				}

				// ...and adding this number of
				// elements to the set to be evaluated.
				for (int i = 0; i < numberOfRuns; i++) {
					elems.add(new BenchmarkElement(meth));
				}
			}// If the method is parameterized...
			else {
				// ..get the parameters
				final Object[][] dataProvider = getDataProviderContent(
						dataProv, paramObjs.get(meth.getMethodToBench()
								.getDeclaringClass()));
				for (final Object[] parameterSet : dataProvider) {
					elems.add(new BenchmarkElement(meth, parameterSet));
				}

				// TODO continue over here
			}
		}

		return elems;
	}
}
