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
 * $Id: Benchmark.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;
import org.perfidix.exceptions.PerfidixMethodException;

/**
 * this is the generic benchmark container. TODO autosensing of runtime... some
 * warmUp() method perhaps.
 * 
 * @author axo
 * @since 04.10.2005 changelog: the timer usage is deprecated now. old code:
 * @since 07.03.2007 changelog: usage of bench.. method os deprecated. Use
 *        annotations instead.
 * 
 * For usage please read the Readme!
 * 
 */
public class Benchmark {

	private MethodContainer methodContainer = null;

	private final static Log LOGGER = LogFactory.getLog("Benchmark");

	/**
	 * Defines how often the methods will be invoked when no invocation count is
	 * given.
	 */
	public static final int BM_DEFAULT_INVOCATION_COUNT = 1;

	/**
	 * the prefix each benchmarkable method has to have in order to be
	 * recognized as benchmarkable.
	 */
	@Deprecated
	public static final String BM_METHOD_PREFIX = "bench";

	/**
	 * the null value of a long.
	 */
	public static final long LONG_NULLVALUE = -1;

	@Deprecated
	private final ArrayList<Benchmarkable> benchmarkableChilds;

	/**
	 * List with all objects to bench
	 */
	private final ArrayList<Object> children;

	/**
	 * name of this bench
	 */
	private String name = "";

	/**
	 * the array list of the meters. the first one is always the time meter.
	 */
	private ArrayList<IMeter> meters = new ArrayList<IMeter>();

	/**
	 * index of the timer in the IMeter-arraylist
	 */
	private final int timeMeterIndex = 0;

	/**
	 * boolean if the exceptions should be logged
	 */
	private boolean logger = true;

	/**
	 * boolean if any exception is thrown by the bench
	 */
	private boolean exceptionsThrown = false;

	/**
	 * boolean if exceptions of any kind should be thrown to the benched class.
	 */
	private boolean shouldThrowException = true;

	/**
	 * the standard constructor to initiate a benchmark container.
	 */
	public Benchmark() {
		this(Benchmark.class.toString());
	}

	/**
	 * you can give a benchmark container a name.
	 * 
	 * @param theName
	 *            the name the benchmark container will have.
	 */
	public Benchmark(final String theName) {
		this.name = theName;
		benchmarkableChilds = new ArrayList<Benchmarkable>();
		children = new ArrayList<Object>();
		meters.add(timeMeterIndex, new IMeter.MilliMeter());
	}

	/**
	 * Setter if you want to get all your thrown Exception to the benched class.
	 * 
	 * @param value
	 */
	public void shouldThrowException(final boolean value) {
		shouldThrowException = value;
	}

	/**
	 * sets the name of the benchmark.
	 * 
	 * @param n
	 *            string
	 */
	public void setName(final String n) {
		name = n;
	}

	/**
	 * obvious, huh?
	 * 
	 * @return string
	 */
	public String getName() {
		return name;
	}

	/**
	 * tells whether exceptions were thrown during the run.
	 * 
	 * @return boolean
	 */
	public boolean exceptionsThrown() {
		return exceptionsThrown;
	}

	/**
	 * sets the logger. If false, nothing will be logged.
	 * 
	 * @param boolean
	 *            if logger should be set.
	 */
	public void setLogger(final boolean logger) {
		this.logger = logger;
	}

	/**
	 * Central method for logging. Sets a boolean if an exception is fired!
	 * 
	 * @param loglevel
	 *            where the log should written
	 * @param message
	 *            to be written.
	 */
	private void appendToLogger(final int loglevel, final String message) {
		if (logger) {
			switch (loglevel) {
			case SimpleLog.LOG_LEVEL_TRACE:
				LOGGER.trace(message);
				break;
			case SimpleLog.LOG_LEVEL_DEBUG:
				LOGGER.debug(message);
				break;
			case SimpleLog.LOG_LEVEL_INFO:
				LOGGER.info(message);
				break;
			case SimpleLog.LOG_LEVEL_WARN:
				LOGGER.warn(message);
				break;
			case SimpleLog.LOG_LEVEL_ERROR:
				LOGGER.error(message);
				break;
			case SimpleLog.LOG_LEVEL_FATAL:
				LOGGER.fatal(message);
				break;
			default:
				LOGGER.error("Not known log level!");
			}
		}
		if (loglevel == SimpleLog.LOG_LEVEL_ERROR
				|| loglevel == SimpleLog.LOG_LEVEL_FATAL) {
			exceptionsThrown = true;
		}
	}

	/**
	 * adds a method.
	 * 
	 * @param someMethod
	 *            a method to use.
	 * @TODO this does not work like that.
	 * @deprecated uses reflection and not be used any more.
	 */
	@Deprecated
	public void add(final Method someMethod) {
		if (null == methodContainer) {
			methodContainer = new MethodContainer();
			add(methodContainer);
		}
		if (someMethod.getExceptionTypes().length > 0) {
			appendToLogger(SimpleLog.LOG_LEVEL_INFO,
					"the method you're trying to add - "
							+ someMethod.getDeclaringClass().getSimpleName()
							+ "::" + someMethod.getName() + "() - \n"
							+ "has some throws declarations. please note \n"
							+ "that your results could be incorrect.");
		}
		// FIXME this one throws an exception.
		// i cannot run the method alone, without having the
		// parent class in my scope.
		// i'll have to rewrite this and keep a reference
		// to the object itself also and then invoke the object.
		methodContainer.add(someMethod);
	}

	/**
	 * configures the benchmark to use the NanoTimer for time measurement.
	 * 
	 */
	public void useNanoMeter() {
		meters.set(timeMeterIndex, new IMeter.NanoMeter());
	}

	/**
	 * configures the benchmark such that it will use the MilliSecond timer for
	 * time measurement.
	 */
	public void useMilliMeter() {
		meters.set(timeMeterIndex, new IMeter.MilliMeter());
	}

	/**
	 * 
	 * @param someMeter
	 *            a meter to register
	 * @return boolean
	 */
	public boolean register(final IMeter someMeter) {
		return meters.add(someMeter);
	}

	/**
	 * Adds a class to the call stack via reflection or annotation. Because
	 * Benchmarkable is deprecated, move to annotations quickly.
	 * 
	 * @param cls
	 *            the class under test.
	 * @throws InstantiationException
	 *             when calling the class instance was not possible
	 * @throws IllegalAccessException
	 *             when the class is not accessible
	 * 
	 */
	public void add(final Object obj) {

		if (obj instanceof Class) {
			throw new IllegalArgumentException(
					"Use a concrete Object, no Class!");
		}
		if (null == obj) {
			appendToLogger(SimpleLog.LOG_LEVEL_INFO, "Null object passed in");
			return;
		}
		children.add(obj);

	}

	@Deprecated
	public void add(final Benchmarkable obj) {
		if (null == obj) {
			appendToLogger(SimpleLog.LOG_LEVEL_INFO, "Null object passed in");
			return;
		}
		benchmarkableChilds.add(obj);
	}

	/**
	 * we're recursing often here, so i implement some indentation utilities in
	 * order to show the result properly.
	 * 
	 * @param indent
	 *            the number of indentations.
	 * 
	 */
	private String toString(final int indent) {

		String ind = "";
		for (int i = 0; i < indent; i++) {
			ind += "\t";
		}

		if (benchmarkableChilds.size() < 1 && children.size() < 1) {
			return ind + "<>";
		}
		String foo = "";
		for (int i = 0, m = benchmarkableChilds.size(); i < m; i++) {
			foo += ind;
			Object obj = benchmarkableChilds.get(i);
			if (obj instanceof Benchmark) {
				foo += ((Benchmark) obj).toString(indent + 1);
			} else {
				foo += obj.toString() + "\n";
			}
		}
		for (int i = 0, m = children.size(); i < m; i++) {
			foo += ind;
			Object obj = children.get(i);
			if (obj instanceof Benchmark) {
				foo += ((Benchmark) obj).toString(indent + 1);
			} else {
				foo += obj.toString() + "\n";
			}
		}
		return foo;
	}

	/**
	 * toString implementation.
	 * 
	 * @return a string.
	 */
	public String toString() {
		return toString(0);
	}

	/**
	 * runs the benchmark with the default number of invocations.
	 * 
	 * @return the result.
	 */
	public Result run() {
		return run(BM_DEFAULT_INVOCATION_COUNT);
	}

	/**
	 * runs all methods with the default invocation count, but still using the
	 * randomizer.
	 * 
	 * @return the result.
	 * @param rand
	 *            the randomizer.
	 */
	public Result run(final IRandomizer rand) {
		return run(BM_DEFAULT_INVOCATION_COUNT, rand);
	}

	/**
	 * runs the benchmark, invoking each method numInvocations times.
	 * 
	 * @param numInvocations
	 *            how many times to run each method.
	 * @return the result.
	 */
	public Result run(final int numInvocations) {
		return run(numInvocations, new IRandomizer.DefaultRandomizer());
	}

	/**
	 * runs a benchmark. give it the number of invocations to perform, and a
	 * benchtimer to calculate the time.
	 * 
	 * @param numInvocations
	 *            the number of runs.
	 * @param rand
	 *            the randomizer to use for the decision making.
	 * @return the result.
	 */
	@SuppressWarnings("unchecked")
	public Result run(final int numInvocations, final IRandomizer rand) {
		if (null == rand) {
			return run(numInvocations);
		}

		ResultContainer myResult = new ResultContainer.BenchmarkResult(this
				.getName());
		for (Benchmarkable it : benchmarkableChilds) {
			myResult.append(doRunObject(it, numInvocations, rand));
		}
		for (Object obj : children) {
			try {
				myResult.append(doRunObject(obj, numInvocations, rand));
			} catch (Exception e) {
				appendToLogger(SimpleLog.LOG_LEVEL_ERROR, "" + e);
				if (shouldThrowException) {
					throw new IllegalStateException(e);
				}
			}
		}
		return myResult;
	}

	/**
	 * overloading our tiny little benchmark to invoke a method. the method has
	 * to be invokable, but since this method is private, there's no problem.
	 * 
	 * @param numInvocations
	 *            the number of invocations.
	 * @param m
	 *            the method to run.
	 * @param rand
	 *            the randomizer to use
	 * @param parent
	 *            the class the method belongs to.
	 * @return Result.
	 */
	private IResult.MethodResult doRunObject(final Method m,
			final int numInvocations, final Object parent,
			final IRandomizer rand) throws Exception {
		assert (parent != null);
		assert (numInvocations >= 0);
		Object[] args = {};
		appendToLogger(SimpleLog.LOG_LEVEL_DEBUG, "Running method "
				+ m.getName() + "(*" + numInvocations + "): ");
		long[] timeElapsed = new long[numInvocations];
		Object[] results = new Object[numInvocations];
		MeterHelper meterHelper = new MeterHelper(numInvocations, meters);
		IMeter timeMeter = meters.get(timeMeterIndex);

		try {

			// TODO can be removed when reflection isn't used any more, the test
			// for
			// annotations is done by the doRunObject-method
			if (checkMethod(m)) {
				appendToLogger(SimpleLog.LOG_LEVEL_INFO,
						"invoking build for method " + m);
				executeBeforeAfter(parent, m, BeforeBenchMethod.class);
				for (int invocationID = 0; invocationID < numInvocations; invocationID++) {
					if (!rand.shouldRun(m)) {
						timeElapsed[invocationID] = LONG_NULLVALUE;
						meterHelper.skip(invocationID);
						continue;
					}
					appendToLogger(SimpleLog.LOG_LEVEL_INFO,
							"invoking setUp for method " + m);
					executeBeforeAfter(parent, m, BeforeBenchRun.class);
					meterHelper.start(invocationID);

					appendToLogger(SimpleLog.LOG_LEVEL_INFO,
							"invoking bench for method " + m);
					long time1 = timeMeter.getValue();
					results[invocationID] = m.invoke(parent, args);
					long time2 = timeMeter.getValue();
					timeElapsed[invocationID] = time2 - time1;

					meterHelper.stop(invocationID);
					appendToLogger(SimpleLog.LOG_LEVEL_INFO,
							"invoking tearDown for method " + m);
					executeBeforeAfter(parent, m, AfterBenchRun.class);
				}

				IResult.SingleResult result = new IResult.SingleResult(m
						.getName(), timeElapsed, results, timeMeter);
				appendToLogger(SimpleLog.LOG_LEVEL_INFO,
						"invoking cleanUp for method " + m);
				executeBeforeAfter(parent, m, AfterBenchMethod.class);

				return meterHelper.createMethodResult(result);
			} else {
				throw new PerfidixMethodException("Method: " + m.getName()
						+ "was invalid because of Params or Returnval.");
			}

		} catch (PerfidixMethodException e) {
			appendToLogger(SimpleLog.LOG_LEVEL_FATAL, "" + e);
			return null;
		}
	}

	private boolean checkMethod(final Method method) {
		if (method == null) {
			return false;
		}
		final Type[] params = method.getGenericParameterTypes();
		final Type returnType = method.getGenericReturnType();

		if (params.length != 0 || returnType != Void.TYPE) {
			return false;
		}
		return true;
	}

	private boolean checkMethodForBench(final Method method) {
		if (!checkMethod(method)) {
			return false;
		}
		if (method.getAnnotation(Bench.class) == null
				&& method.getDeclaringClass().getAnnotation(BenchClass.class) == null) {
			return false;
		}
		if (method.getDeclaringClass().getAnnotation(BenchClass.class) != null) {
			if ((method.getAnnotation(Bench.class) == null)
					&& (method.getAnnotation(BeforeBenchClass.class) != null
							|| method.getAnnotation(BeforeBenchMethod.class) != null
							|| method.getAnnotation(BeforeBenchRun.class) != null
							|| method.getAnnotation(AfterBenchRun.class) != null
							|| method.getAnnotation(AfterBenchMethod.class) != null || method
							.getAnnotation(AfterBenchClass.class) != null)) {
				return false;
			}
		}
		return true;
	}

	private void executeBeforeAfter(final Object objectToBench,
			final Method method, final Class<? extends Annotation> anno)
			throws PerfidixMethodException {
		Method toReturn = null;
		final Class[] setUpParams = {};
		final Object[] methodParams = {};
		try {
			if (objectToBench instanceof Benchmarkable) {
				String methodString = "";
				if (anno.equals(BeforeBenchMethod.class)) {
					methodString = "build";
				}
				if (anno.equals(BeforeBenchRun.class)) {
					methodString = "setUp";
				}
				if (anno.equals(AfterBenchRun.class)) {
					methodString = "tearDown";
				}
				if (anno.equals(AfterBenchMethod.class)) {
					methodString = "cleanUp";
				}
				toReturn = objectToBench.getClass().getDeclaredMethod(
						methodString, setUpParams);
			} else {
				final Bench benchAnno = method.getAnnotation(Bench.class);
				if (benchAnno != null
						&& (anno.equals(BeforeBenchRun.class) && !(benchAnno
								.beforeMethod().equals("")))) {
					toReturn = objectToBench.getClass().getDeclaredMethod(
							benchAnno.beforeMethod(), setUpParams);
				} else if (benchAnno != null
						&& (anno.equals(AfterBenchRun.class) && !(benchAnno
								.afterMethod().equals("")))) {
					toReturn = objectToBench.getClass().getDeclaredMethod(
							benchAnno.afterMethod(), setUpParams);
				} else {
					toReturn = getBeforeAfter(objectToBench, anno);
				}

			}
			if (checkMethod(toReturn)) {
				toReturn.invoke(objectToBench, methodParams);
			} else {
				if (toReturn != null) {
					throw new IllegalStateException("Method: "
							+ toReturn.getName()
							+ " was invalid because of Params or Returnval.");
				}
			}
		} catch (NoSuchMethodException e) {
			appendToLogger(SimpleLog.LOG_LEVEL_INFO, anno.getName()
					+ " method not found or the method has params.");
		} catch (PerfidixMethodException e) {
			throw e;
		} catch (Exception e) {
			appendToLogger(SimpleLog.LOG_LEVEL_ERROR, "" + e);
		}
	}

	private Method getBeforeAfter(final Object obj,
			final Class<? extends Annotation> anno)
			throws PerfidixMethodException {
		Method toReturn = null;
		final Method[] methods = obj.getClass().getMethods();
		boolean found = false;
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getAnnotation(anno) != null) {
				if (!found) {
					toReturn = methods[i];
					found = true;
				} else {
					throw new PerfidixMethodException("Use just one "
							+ anno.getName() + " for every Bench Class!");
				}
			}
		}
		return toReturn;

	}

	/**
	 * runs the object. the internal implementation
	 * 
	 * @param numInvocations
	 *            the number of times one method is to be called.
	 * @param obj
	 *            the object under test.
	 * @return Result
	 * @param timer
	 *            the timer to use
	 * @param rand
	 *            the randomizer to use
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	private Result doRunObject(final Benchmarkable obj,
			final int numInvocations, final IRandomizer rand) {
		
		final ArrayList<Method> invokableMethods = obj.getInvokableMethods();

		final ResultContainer result = new IResult.ClassResult(obj.getName(),
				obj.getClass().getCanonicalName());
		try {
		for (Method it : invokableMethods) {
			final IResult.MethodResult realResult = doRunObject(it,
					numInvocations, obj, rand);
			if (realResult != null) {
				result.append(realResult);
			}
		}
		} catch(Exception e) {
			appendToLogger(SimpleLog.LOG_LEVEL_ERROR, "Error invoking method: " + e);
		}
		return result;
	}

	/**
	 * runs the object. the internal implementation
	 * 
	 * @param numInvocations
	 *            the number of times one method is to be called.
	 * @param obj
	 *            the object under test.
	 * @return Result
	 * @param timer
	 *            the timer to use
	 * @param rand
	 *            the randomizer to use
	 */
	private Result doRunObject(final Object obj, final int numInvocations,
			final IRandomizer rand) throws Exception {

		// getting all methods
		final Object[] params = {};
		final Method[] methods = obj.getClass().getDeclaredMethods();
		final ResultContainer<IResult.MethodResult> result = new IResult.ClassResult(
				obj.getClass().getSimpleName(), obj.getClass()
						.getCanonicalName());

		final Method beforeClass = getBeforeAfter(obj, BeforeBenchClass.class);
		if (beforeClass != null) {
			if (checkMethod(beforeClass)) {
				beforeClass.invoke(obj, params);
			} else {
				throw new IllegalStateException("Method: "
						+ beforeClass.getName()
						+ " was invalid because of Params or Returnval.");
			}
		}

		for (int i = 0; i < methods.length; i++) {
			// checking of method is benchable.
			if (checkMethodForBench(methods[i])) {
				int runs = -1;
				final Bench anno = methods[i].getAnnotation(Bench.class);
				final BenchClass classAnno = methods[i].getDeclaringClass()
						.getAnnotation(BenchClass.class);
				boolean runSet = false;
				if (anno != null) {
					if (anno.runs() != Bench.DEFAULTRUNS) {
						if (anno.runs() < 0) {
							throw new PerfidixMethodException(
									"Runs shall not be negative!");
						}
						runs = anno.runs();
						runSet = true;
					}
				}
				if (classAnno != null && !runSet) {
					if (classAnno.runs() != BenchClass.DEFAULTRUNS) {
						if (classAnno.runs() < 0) {
							throw new PerfidixMethodException(
									"Runs shall not be negative!");
						}
						runs = classAnno.runs();
						runSet = true;
					}
				}
				if (!runSet) {
					runs = numInvocations;
				}
				final IResult.MethodResult realResult = doRunObject(methods[i],
						runs, obj, rand);
				if (realResult != null) {
					result.append(realResult);
				}
			}
		}
		final Method afterClass = getBeforeAfter(obj, AfterBenchClass.class);
		if (afterClass != null) {
			if (checkMethod(afterClass)) {
				afterClass.invoke(obj, params);
			} else {
				throw new IllegalStateException("Method: "
						+ afterClass.getName()
						+ " was invalid because of Params or Returnval.");
			}
		}

		return result;
	}

	private final class MeterHelper {

		private ArrayList<IMeter> meters;

		private boolean metersAvailable = false;

		private long[][] theResults;

		private MeterHelper(final int numInvocations,
				final ArrayList<IMeter> theMeters) {
			meters = theMeters;
			metersAvailable = (meters.size() > 0);
			theResults = new long[meters.size()][numInvocations];
		}

		private void collectResults(final long[][] res, final int invocationID) {
			assert (res.length == meters.size());
			Iterator<IMeter> it = meters.iterator();
			int i = 0;
			while (it.hasNext()) {
				res[i][invocationID] = it.next().getValue()
						- res[i][invocationID];
				i++;
			}
		}

		private void start(final int index) {
			if (!metersAvailable) {
				return;
			}
			collectResults(theResults, index);
		}

		private void stop(final int index) {
			if (!metersAvailable) {
				return;
			}
			collectResults(theResults, index);
		}

		private void skip(final int index) {
			if (!metersAvailable) {
				return;
			}
			int metersSize = meters.size();
			assert (theResults.length == metersSize);
			for (int k = 0; k < metersSize; k++) {
				theResults[k][index] = Benchmark.LONG_NULLVALUE;
			}
		}

		private IResult.MethodResult createMethodResult(
				final IResult.SingleResult timedResult) {
			if (timedResult != null) {
				IResult.MethodResult c = new IResult.MethodResult(timedResult
						.getName());
				append(c);
				return c;
			} else {
				return null;
			}

		}

		private void append(final ResultContainer<IResult.SingleResult> r) {
			Iterator<IMeter> it = meters.iterator();
			int i = 0;
			while (it.hasNext()) {
				IMeter entry = it.next();
				IResult.SingleResult s = new IResult.SingleResult(entry
						.getUnitDescription(), theResults[i], entry);
				r.append(s);
				i++;
			}
		}

	}

}
