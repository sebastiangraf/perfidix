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
import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.AfterEachBenchRun;
import org.perfidix.annotation.AfterLastBenchRun;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.BeforeEachBenchRun;
import org.perfidix.annotation.BeforeFirstBenchRun;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.BenchClass;
import org.perfidix.annotation.SkipBench;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.MemMeter;
import org.perfidix.meter.MilliMeter;
import org.perfidix.meter.NanoMeter;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;
import org.perfidix.result.ResultContainer;
import org.perfidix.result.SingleResult;

/**
 * This is the generic benchmark container.
 * 
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 */
public class Benchmark {

    // ////////////////////////////////////////////
    // Static variables
    // ////////////////////////////////////////////

    /**
     * Logger instance.
     */
    private static final Log LOGGER = LogFactory.getLog("Benchmark");

    /**
     * Defines how often the methods will be invoked when no invocation count is
     * given.
     */
    public static final int BM_DEFAULT_INVOCATION_COUNT = 1;

    /**
     * the null value of a long.
     */
    public static final long LONG_NULLVALUE = -1;

    // ////////////////////////////////////////////
    // Normal variables
    // ////////////////////////////////////////////

    /**
     * List with all objects to bench.
     */
    private final ArrayList<Object> children;

    /**
     * Name of this bench.
     */
    private String name = "";

    /**
     * The array list of the meters. the first one is always the time meter.
     */
    private ArrayList<AbstractMeter> meters = new ArrayList<AbstractMeter>();

    /**
     * Index of the timer in the IMeter-arraylist.
     */
    private final int timeMeterIndex = 0;

    /**
     * Boolean if the exceptions should be logged.
     */
    private boolean logger = true;

    /**
     * Boolean if any exception is thrown by the bench.
     */
    private boolean exceptionsThrown = false;

    /**
     * Boolean if exceptions of any kind should be thrown to the benched class.
     */
    private boolean shouldThrowException = true;

    // ////////////////////////////////////////////
    // Constructors
    // ////////////////////////////////////////////

    /**
     * Constructor, without name or mem-benchmarking.
     */
    public Benchmark() {
        this(false);
    }

    /**
     * Constructor, with name but without mem-benchmarking.
     * 
     * @param paramName
     *            name of benchmark
     */
    public Benchmark(final String paramName) {
        this(paramName, false);
    }

    /**
     * Constructor, without name but with mem-benchmarking.
     * 
     * @param useMemMeter
     *            should the mem be benchmarked
     */
    public Benchmark(final boolean useMemMeter) {
        this(Benchmark.class.toString(), useMemMeter);
    }

    /**
     * Constructor, without name but with mem-benchmarking.
     * 
     * @param useMemMeter
     *            should the mem be benchmarked
     * @param theName
     *            name of benchmark
     */
    public Benchmark(final String theName, final boolean useMemMeter) {
        this.name = theName;
        children = new ArrayList<Object>();
        meters.add(timeMeterIndex, new MilliMeter());
        if (useMemMeter) {
            meters.add(new MemMeter());
        }
    }

    /**
     * Adds an object to the call stack via annotation.
     * 
     * @param obj
     *            the obj under bench.
     */
    public final void add(final Object obj) {

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

    /**
     * Adds an object with a given name to the call stack via anotation.
     * 
     * @param obj
     *            the obj under bench
     * @param paramName
     *            name of the bench
     */
    public final void add(final Object obj, final String paramName) {

        if (obj instanceof Class) {
            throw new IllegalArgumentException(
                    "Use a concrete Object, no Class!");
        }
        if (null == obj) {
            appendToLogger(SimpleLog.LOG_LEVEL_INFO, "Null object passed in");
            return;
        }
        final Object[] newObject = { obj, paramName };
        children.add(newObject);

    }

    /**
     * Runs the benchmark with the default number of invocations.
     * 
     * @return the result.
     */
    public final AbstractResult run() {
        return run(BM_DEFAULT_INVOCATION_COUNT);
    }

    /**
     * runs a benchmark. give it the number of invocations to perform, and a
     * benchtimer to calculate the time.
     * 
     * @param numInvocations
     *            the number of runs.
     * @return the result.
     */
    public final AbstractResult run(final int numInvocations) {

        ResultContainer myResult = new BenchmarkResult(this.getName());
        for (Object obj : children) {
            try {
                myResult.append(doRunObject(obj, numInvocations));
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
     * Overloading our tiny little benchmark to invoke a method. the method has
     * to be invokable, but since this method is private, there's no problem.
     * 
     * @param numInvocations
     *            the number of invocations.
     * @param m
     *            the method to run.
     * @param parent
     *            the class the method belongs to.
     * @return Result.
     * @throws Exception
     *             of any kind
     */
    private MethodResult doRunObject(
            final Method m, final int numInvocations, final Object parent)
            throws Exception {
        assert (parent != null);
        assert (numInvocations >= 0);
        final Object[] args = {};
        appendToLogger(SimpleLog.LOG_LEVEL_DEBUG, "Running method "
                + m.getName()
                + "(*"
                + numInvocations
                + "): ");
        final long[] timeElapsed = new long[numInvocations];
        final Object[] results = new Object[numInvocations];
        final MeterHelper meterHelper = new MeterHelper(numInvocations, meters);
        final AbstractMeter timeMeter = meters.get(timeMeterIndex);

        try {

            appendToLogger(
                    SimpleLog.LOG_LEVEL_INFO, "invoking build for method " + m);
            executeBeforeAfter(parent, m, BeforeFirstBenchRun.class);
            for (int invocationID = 0; invocationID < numInvocations; invocationID++) {
                appendToLogger(
                        SimpleLog.LOG_LEVEL_INFO, "invoking setUp for method "
                                + m);
                executeBeforeAfter(parent, m, BeforeEachBenchRun.class);
                meterHelper.start(invocationID);

                appendToLogger(
                        SimpleLog.LOG_LEVEL_INFO, "invoking bench for method "
                                + m);
                long time1 = timeMeter.getValue();
                results[invocationID] = m.invoke(parent, args);
                long time2 = timeMeter.getValue();
                timeElapsed[invocationID] = time2 - time1;

                meterHelper.stop(invocationID);
                appendToLogger(
                        SimpleLog.LOG_LEVEL_INFO,
                        "invoking tearDown for method " + m);
                executeBeforeAfter(parent, m, AfterEachBenchRun.class);
            }

            final SingleResult result =
                    new SingleResult(
                            m.getName(), timeElapsed, results, timeMeter);
            appendToLogger(
                    SimpleLog.LOG_LEVEL_INFO, "invoking cleanUp for method "
                            + m);
            executeBeforeAfter(parent, m, AfterLastBenchRun.class);

            return meterHelper.createMethodResult(result);

        } catch (Exception e) {
            appendToLogger(SimpleLog.LOG_LEVEL_FATAL, "" + e);
            if (exceptionsThrown) {
                throw new IllegalStateException(e);
            } else {
                return null;
            }
        }
    }

    /**
     * Method to execute before/after sourcecode.
     * 
     * @param objectToBench
     *            object to bench
     * @param method
     *            method to be benched
     * @param anno
     *            annotation be checked
     */
    private void executeBeforeAfter(
            final Object objectToBench, final Method method,
            final Class<? extends Annotation> anno) {
        Method toReturn = null;
        final Class<?>[] setUpParams = {};
        final Object[] methodParams = {};
        try {
            final Bench benchAnno = method.getAnnotation(Bench.class);
            if (benchAnno != null
                    && (anno.equals(BeforeFirstBenchRun.class) && !(benchAnno
                            .beforeFirstBenchRun().equals("")))) {
                toReturn =
                        objectToBench.getClass().getDeclaredMethod(
                                benchAnno.beforeFirstBenchRun(), setUpParams);
            } else if (benchAnno != null
                    && (anno.equals(BeforeEachBenchRun.class) && !(benchAnno
                            .beforeEveryBenchRun().equals("")))) {
                toReturn =
                        objectToBench.getClass().getDeclaredMethod(
                                benchAnno.beforeEveryBenchRun(), setUpParams);
            } else if (benchAnno != null
                    && (anno.equals(AfterEachBenchRun.class) && !(benchAnno
                            .afterEveryBenchRun().equals("")))) {
                toReturn =
                        objectToBench.getClass().getDeclaredMethod(
                                benchAnno.afterEveryBenchRun(), setUpParams);
            } else if (benchAnno != null
                    && (anno.equals(AfterLastBenchRun.class) && !(benchAnno
                            .afterLastBenchRun().equals("")))) {
                toReturn =
                        objectToBench.getClass().getDeclaredMethod(
                                benchAnno.afterLastBenchRun(), setUpParams);
            } else {
                toReturn = getBeforeAfter(objectToBench, anno);
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
            if (exceptionsThrown) {
                throw new IllegalStateException(e);
            }
        } catch (Exception e) {
            appendToLogger(SimpleLog.LOG_LEVEL_ERROR, "" + e);
            if (exceptionsThrown) {
                throw new IllegalStateException(e);
            }
        }
    }

    private Method getBeforeAfter(
            final Object obj, final Class<? extends Annotation> anno) {
        Method toReturn = null;
        final Method[] methods = obj.getClass().getMethods();
        boolean found = false;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getAnnotation(anno) != null) {
                if (!found) {
                    toReturn = methods[i];
                    found = true;
                } else {
                    throw new IllegalArgumentException("Use just one "
                            + anno.getName()
                            + " for every Bench Class!");
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
     * @return IResult to be returned
     * @throws Exception
     *             of any kind
     */
    private AbstractResult doRunObject(
            final Object obj, final int numInvocations) throws Exception {

        // getting all methods
        final Object[] params = {};
        ResultContainer<MethodResult> result;
        Object toExecute;
        if (obj.getClass().isArray()) {
            result =
                    new ClassResult(
                            ((Object[]) obj)[1].toString(), ((Object[]) obj)[1]
                                    .toString());
            toExecute = ((Object[]) obj)[0];
        } else {
            result =
                    new ClassResult(obj.getClass().getSimpleName(), obj
                            .getClass().getCanonicalName());
            toExecute = obj;
        }
        final Method[] methods = toExecute.getClass().getDeclaredMethods();
        final Method beforeClass =
                getBeforeAfter(toExecute, BeforeBenchClass.class);
        if (beforeClass != null) {
            if (checkMethod(beforeClass)) {
                beforeClass.invoke(toExecute, params);
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
                final BenchClass classAnno =
                        methods[i].getDeclaringClass().getAnnotation(
                                BenchClass.class);
                boolean runSet = false;
                if (anno != null) {
                    if (anno.runs() != -1) {
                        if (anno.runs() < 0) {
                            throw new IllegalArgumentException(
                                    "Runs shall not be negative!");
                        }
                        runs = anno.runs();
                        runSet = true;
                    }
                }
                if (classAnno != null && !runSet) {
                    if (classAnno.runs() != -1) {
                        if (classAnno.runs() < 0) {
                            throw new IllegalArgumentException(
                                    "Runs shall not be negative!");
                        }
                        runs = classAnno.runs();
                        runSet = true;
                    }
                }
                if (!runSet) {
                    runs = numInvocations;
                }
                final MethodResult realResult =
                        doRunObject(methods[i], runs, toExecute);
                if (realResult != null) {
                    result.append(realResult);
                }
            }
        }
        final Method afterClass =
                getBeforeAfter(toExecute, AfterBenchClass.class);
        if (afterClass != null) {
            if (checkMethod(afterClass)) {
                afterClass.invoke(toExecute, params);
            } else {
                throw new IllegalStateException("Method: "
                        + afterClass.getName()
                        + " was invalid because of Params or Returnval.");
            }
        }

        return result;
    }

    // ////////////////////////////////////////////
    // checkMethods
    // ////////////////////////////////////////////

    /**
     * Check if method is valid for beforeBench,afterBench and bench, that means
     * void and param-free.
     * 
     * @param method
     *            to be checked
     * @return boolean if check was successful or not.
     */
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

    /**
     * Check if method is valid for bench.
     * 
     * @param method
     *            to check
     * @return booelean if check was successful or not
     */
    private boolean checkMethodForBench(final Method method) {
        if (!checkMethod(method)) {
            return false;
        }
        if ((method.getAnnotation(Bench.class) == null && method
                .getDeclaringClass().getAnnotation(BenchClass.class) == null)
                || method.getAnnotation(SkipBench.class) != null) {
            return false;
        }
        if (method.getDeclaringClass().getAnnotation(BenchClass.class) != null) {
            if ((method.getAnnotation(Bench.class) == null)
                    && (method.getAnnotation(BeforeBenchClass.class) != null
                            || method.getAnnotation(BeforeFirstBenchRun.class) != null
                            || method.getAnnotation(BeforeEachBenchRun.class) != null
                            || method.getAnnotation(AfterEachBenchRun.class) != null
                            || method.getAnnotation(AfterLastBenchRun.class) != null || method
                            .getAnnotation(AfterBenchClass.class) != null)) {
                return false;
            }
        }
        return true;
    }

    // ////////////////////////////////////////////
    // simple access methods
    // ////////////////////////////////////////////
    /**
     * toString implementation.
     * 
     * @return a string.
     */
    @Override
    public final String toString() {
        return toString(0);
    }

    /**
     * Setter if you want to get all your thrown Exception to the benched class.
     * 
     * @param value
     */
    public final void shouldThrowException(final boolean value) {
        shouldThrowException = value;
    }

    /**
     * getting the name
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
     * @param boolean if logger should be set.
     */
    public void setLogger(final boolean logger) {
        this.logger = logger;
    }

    /**
     * configures the benchmark to use the NanoTimer for time measurement.
     */
    public void useNanoMeter() {
        meters.set(timeMeterIndex, new NanoMeter());
    }

    /**
     * configures the benchmark such that it will use the MilliSecond timer for
     * time measurement.
     */
    public void useMilliMeter() {
        meters.set(timeMeterIndex, new MilliMeter());
    }

    /**
     * registers some meter
     * 
     * @param someMeter
     *            a meter to register
     * @return boolean
     */
    public boolean register(final AbstractMeter someMeter) {
        return meters.add(someMeter);
    }

    // ////////////////////////////////////////////
    // helper stuff
    // ////////////////////////////////////////////
    /**
     * we're recursing often here, so i implement some indentation utilities in
     * order to show the result properly.
     * 
     * @param indent
     *            the number of indentations.
     */
    private String toString(final int indent) {

        String ind = "";
        for (int i = 0; i < indent; i++) {
            ind += "\t";
        }

        if (children.size() < 1) {
            return ind + "<>";
        }
        String foo = "";
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

    private final class MeterHelper {

        private ArrayList<AbstractMeter> meters;

        private boolean metersAvailable = false;

        private long[][] theResults;

        private MeterHelper(
                final int numInvocations,
                final ArrayList<AbstractMeter> theMeters) {
            // check for arraymeter and set sizes correct.
            // if Arraymeter present,
            meters = theMeters;
            int l = meters.size();
            metersAvailable = (l > 0);
            theResults = new long[l][numInvocations];
        }

        // private void collectResults(final long[][] res, final int
        // invocationID) {
        private void collectResults(final int invocationID) {
            assert (theResults.length == meters.size());

            int i = 0;
            for (final AbstractMeter meter : meters) {
                // if (meter instanceof MemMeter) {
                // theResults[i][invocationID] = meter.getValue();
                // } else {
                theResults[i][invocationID] =
                        meter.getValue() - theResults[i][invocationID];
                // }

                i++;
            }
        }

        private void start(final int index) {
            if (!metersAvailable) {
                return;
            }
            // collectResults(theResults, index);
            collectResults(index);
        }

        private void stop(final int index) {
            if (!metersAvailable) {
                return;
            }
            // collectResults(theResults, index);
            collectResults(index);
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

        private MethodResult createMethodResult(final SingleResult timedResult) {
            if (timedResult != null) {
                MethodResult c = new MethodResult(timedResult.getName());
                append(c);
                return c;
            } else {
                return null;
            }

        }

        private void append(final ResultContainer<SingleResult> r) {
            Iterator<AbstractMeter> it = meters.iterator();
            int i = 0;
            while (it.hasNext()) {
                AbstractMeter entry = it.next();
                SingleResult s =
                        new SingleResult(
                                entry.getUnitDescription(), theResults[i],
                                entry);
                r.append(s);
                i++;
            }
            System.out.println(r.toString());
        }

    }

}
