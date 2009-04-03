/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
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
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.ouput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.perfidix.failureHandling.PerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.graph.Graph;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * @author Bastian Lemke
 */
public class GraphOutput extends AbstractOutput {

    /**
     * Constant for methodRename parameter in
     * {@link #visitBenchmark(BenchmarkResult, Map)}. If set as value string for
     * a method, the method will not be included in the graph.
     */
    public static final String SKIP_METHOD = "SKIP";

    // TODO: testcases
    // TODO: include stdv, confidence intervals, ...

    /**
     * Properties for the graph layout.
     * <p>
     * Available properties are shown below in the form <code>key</code> =
     * <i>default value</i> (possible values):
     * <ul>
     * <li><code>title</code> = Perfidix Benchmark (<i>String</i>)</li>
     * <li><code>width</code> = 800 (<i>int</i>)</li>
     * <li><code>height</code> = 400 (<i>int</i>)</li>
     * <li><code>thick</code> = 400 (<i>float</i>)</li>
     * <li><code>calculation_method</code> = mean ([minimum, maximum, mean])</li>
     * <li><code>representation</code> = boxes ([boxes, lines])</li>
     * <li><code>dot</code> = 0 (<i>int</i>)</li>
     * <li><code>colored</code> = true ([true, false])</li>
     * <li><code>legend_size</code> = 22 (<i>int</i>)</li>
     * <li><code>font</code> = Tahoma (<i>available font name</i>)</li>
     * <li><code>font_style</code> = 0 ([0, 1, 2])<br />
     * 0 -> default<br />
     * 1 -> bold<br />
     * 2 -> italic</li>
     * <li><code>oneline</code> = false ([true, false])<br />
     * if true, the legend is printed in a single line</li>
     * <li><code>down</code> = false ([true, false])</li>
     * <li><code>minimum_range</code> = auto ([auto, <i>int</i>])</li>
     * <li><code>maximum_range</code> = auto ([auto, <i>int</i>])<br />
     * the minimum/maximum y position is: 10^<code>minimum_range</code> (10^
     * <code>maximum_range</code>) -> logarithmic scale</li>
     * <li><code>class_names</code> = numbers ([numbers, names])<br />
     * instead of numbers below the diagram, the names of the benchmark classes
     * can be set.<br />
     * If your class names are too long, you can set short names for each of
     * them with a property of the following form (the class in the example is
     * named "CLASS"):<br />
     * <code>setProperty("CLASS_name", "short name")</code></li>
     * </ul>
     * </p>
     */
    public Properties properties;

    private Map<AbstractMeter, Map<String, Map<String, Double>>> data =
            null;
    private int numRuns = -1;

    /**
     * Constructor for creating a GraphOutput with default properties. The
     * properties can be changed via {@link GraphOutput#properties}.
     */
    public GraphOutput() {
        Properties defaults = new Properties();
        defaults.setProperty("title", "Perfidix Benchmark");
        defaults.setProperty("width", "800");
        defaults.setProperty("height", "400");
        defaults.setProperty("thick", "400");
        // minimum, maximum, mean
        defaults.setProperty("calculation_method", "mean");
        // lines or boxes
        defaults.setProperty("representation", "boxes");
        defaults.setProperty("dot", "0");
        defaults.setProperty("colored", "true");
        defaults.setProperty("legend_size", "22");
        defaults.setProperty("font", "Tahoma");
        defaults.setProperty("oneline", "false");
        defaults.setProperty("down", "false");
        // 0 -> default, 1 -> bold, 2 -> italic
        defaults.setProperty("font_style", "0");
        defaults.setProperty("minimum_range", "auto");
        defaults.setProperty("maximum_range", "auto");
        defaults.setProperty("class_names", "numbers");
        properties = new Properties(defaults);
    }

    /**
     * Constructor for creating a GraphOutput with properties from the given
     * properties file.
     * 
     * @param propertiesFile
     *            the file to read the properties from.
     * @throws FileNotFoundException
     *             if the file does not exist.
     * @throws IOException
     *             if an error occurs while reading from the file.
     */
    public GraphOutput(File propertiesFile)
            throws FileNotFoundException, IOException {
        this();
        properties.load(new FileInputStream(propertiesFile));
    }

    /** {@inheritDoc} */
    @Override
    public void listenToException(PerfidixMethodException exec) {
        throw new UnsupportedOperationException("no listener implemented.");

    }

    /** {@inheritDoc} */
    @Override
    public void listenToResultSet(
            Method meth, AbstractMeter meter, double data) {
        throw new UnsupportedOperationException("no listener implemented.");

    }

    /** {@inheritDoc} */
    @Override
    public void visitBenchmark(BenchmarkResult benchRes) {
        addResults(benchRes, new TreeMap<String, String>());
    }

    /**
     * Visiting the {@link BenchmarkResult} and do something with the result.
     * 
     * @param benchRes
     *            the benchmark result.
     * @param methodRename
     *            the methods to rename and the corresponding new names.
     */
    public void visitBenchmark(
            BenchmarkResult benchRes, Map<String, String> methodRename) {
        addResults(benchRes, methodRename);
    }

    /**
     * Save the graph(s) as pdf file.
     */
    @SuppressWarnings("unused")
    private void saveAsPDF() {
        // TODO: implement
    }

    /**
     * Save the benchmark data to a file. The data can later be retrieved and
     * plotted by {@link #load(File, boolean)} and {@link #plot()}.
     * 
     * @param file
     *            the file to write the data to.
     * @throws IOException
     *             if an error occurs while writing the data.
     */
    public void save(File file) throws IOException {
        ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(data);
        oos.writeInt(numRuns);
        oos.close();
    }

    /**
     * Save the benchmark data of a single method to a file. The data can later
     * be retrieved and plotted by {@link #load(File, boolean)} and
     * {@link #plot()}.
     * 
     * @param file
     * @param methodName
     * @throws IOException
     */
    public void saveMethod(File file, String methodName)
            throws IOException {
        if (data == null)
            throw new UnsupportedOperationException("No data available.");
        Map<AbstractMeter, Map<String, Map<String, Double>>> methodData =
                new TreeMap<AbstractMeter, Map<String, Map<String, Double>>>();
        for (AbstractMeter meter : data.keySet()) {
            if (!data.get(meter).containsKey(methodName))
                throw new IllegalArgumentException(
                        "No method with the name \""
                                + methodName
                                + "\" available.");
            Map<String, Map<String, Double>> method =
                    new TreeMap<String, Map<String, Double>>();
            // the data to store for the method
            Map<String, Double> md = new TreeMap<String, Double>();
            md = data.get(meter).get(methodName);
            method.put(methodName, md);
            methodData.put(meter, method);
        }
        ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(methodData);
        oos.writeInt(numRuns);
        oos.close();
    }

    /**
     * Reads previously saved data from a file. The data can be plotted by
     * {@link #plot()}.
     * 
     * @param file
     *            the file to read the data from.
     * @param replaceExisting
     *            if true, all benchmark data that was added by
     *            {@link #visitBenchmark(BenchmarkResult)},
     *            {@link #visitBenchmark(BenchmarkResult, Map)} or
     *            {@link #load(File, boolean)} is deleted. If false, the "old"
     *            data is kept and the new data is appended.
     * @throws FileNotFoundException
     *             if the file was not found.
     * @throws IOException
     *             if an error occurs while reading the data.
     * @throws ClassNotFoundException
     *             if the file contains wrong data.
     */
    @SuppressWarnings("unchecked")
    public void load(File file, boolean replaceExisting)
            throws FileNotFoundException, IOException,
            ClassNotFoundException {
        Map<AbstractMeter, Map<String, Map<String, Double>>> newData;
        ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(file));
        newData =
                (Map<AbstractMeter, Map<String, Map<String, Double>>>) ois
                        .readObject();
        if (data == null || replaceExisting) {
            numRuns = ois.readInt();
            data = newData;
        } else {
            int newNumRuns = ois.readInt();
            if (newNumRuns != numRuns)
                System.err
                        .println("WARNING: number of RUNS should be the "
                                + "same for all methods.");
            for (AbstractMeter meter : newData.keySet()) {
                if (!data.containsKey(meter)) {
                    data.put(meter, newData.get(meter));
                } else {
                    Map<String, Map<String, Double>> methods =
                            data.get(meter);
                    for (String newMethod : newData.get(meter).keySet()) {
                        if (!methods.containsKey(newMethod)) {
                            methods.put(newMethod, newData.get(meter).get(
                                    newMethod));
                        } else {
                            System.err.println("WARNING: method \""
                                    + newMethod
                                    + "\" ignored. A method with the "
                                    + "same name already exists.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the names of all methods for the given {@link AbstractMeter}.
     * 
     * @param meter
     *            the meter to get the method names for.
     * @return array with the names of all methods.
     */
    public String[] getMethodNames(AbstractMeter meter) {
        return data.get(meter).keySet().toArray(new String[0]);
    }

    /**
     * Renames a single method if the method exists or does nothing otherwise.
     * The method is renamed for all meters.
     * 
     * @param oldName
     *            the old method name to be changed.
     * @param newName
     *            the new method name.
     */
    public void renameMethod(String oldName, String newName) {
        for (AbstractMeter meter : data.keySet()) {
            Map<String, Map<String, Double>> meterData = data.get(meter);
            Map<String, Double> methodData = meterData.get(oldName);
            if (methodData != null) {
                meterData.remove(oldName);
                meterData.put(newName, methodData);
            }
        }
    }

    /**
     * <p>
     * Plots the benchmark results.
     * </p>
     * <p>
     * Before plotting you have to either run a perfidix benchmark and call
     * {@link #visitBenchmark(BenchmarkResult)} /
     * {@link #visitBenchmark(BenchmarkResult, Map)} or load a file with
     * previously saved data ({@link #load(File, boolean)}).
     * </p>
     * <p>
     * For each of the registered meters an independent graph is plotted.
     * </p>
     */
    public void plot() {
        for (AbstractMeter meter : data.keySet()) {
            properties.setProperty("meter_name", meter.getName());
            properties.setProperty("num_runs", String.valueOf(numRuns));
            properties.setProperty("unit", meter.getUnit());
            properties
                    .setProperty("unit_desc", meter.getUnitDescription());
            Graph.createGraph(data.get(meter), properties);
        }
    }

    /**
     * <p>
     * Read all results from the <code>benchRes</code> object and convert them
     * to the {@link GraphOutput} internal format. The converted data is
     * appended to possibly existing data that was added before or loaded from a
     * file.
     * </p>
     * <p>
     * Duplicate method names are not supported. If a method is to be added and
     * a method with the same name already exists, it is ignored and a warning
     * message is printed to stderr.
     * </p>
     * <p>
     * Optionally, a string map for renaming benchmark methods can be set (can
     * be empty or <code>null</code> if no renaming has to be done).
     * </p>
     * 
     * @param benchRes
     *            the benchmark result object to read the data from.
     * @param methodRename
     *            a string mapping (<i>old method name</i> -> <i>new method
     *            name</i>) to rename methods from the {@link BenchmarkResult}
     *            object.
     */
    private void addResults(
            BenchmarkResult benchRes, Map<String, String> methodRename) {

        // initialize the global GraphOutput data storage
        if (data == null)
            data =
                    new TreeMap<AbstractMeter, Map<String, Map<String, Double>>>();
        if (methodRename == null)
            methodRename = new TreeMap<String, String>();

        // layout of the GraphOutput data storage:
        // meter -> method name -> class name -> value

        /*
         * The benchmark data in the BenchmarkResult object is stored in a
         * different order (meter -> class -> method -> value) but in the
         * current GraphOutput implementation, the classes are "contained" in
         * the methods. The reason for this is the graph representation: _______
         * _____________________________________________________________________
         * A class represents a fixed benchmark environment that is equal for
         * all methods. In the graph output each class describes one "x value"
         * of the graph. _______________________________________________________
         * Methods can be used to test e.g. different configurations of an
         * implementation or different implementations against each other (with
         * the same benchmark environment -> the same classes). With lines
         * representation, each method describes one line in the graph. Each
         * method "contains" a number of classes (all methods have to contain
         * the same classes) which have assigned a "y value".
         */

        // loop over the meters. each meter gets an independent graph
        for (AbstractMeter meter : benchRes.getRegisteredMeters()) {

            // count the number of classes for checking if all classes contains
            // the same number of methods.
            int numClasses = 0;
            if (!data.containsKey(meter))
                data.put( //
                        meter, new TreeMap<String, Map<String, Double>>());

            // one "line" in the graph (with lines representation)
            Map<String, Map<String, Double>> methodResults =
                    data.get(meter);
            for (ClassResult classRes : benchRes.getIncludedResults()) {
                numClasses++;
                for (MethodResult methRes : classRes.getIncludedResults()) {
                    String name = methRes.getElementName();
                    if (methodRename.containsKey(name))
                        name = methodRename.get(name);
                    if (name == SKIP_METHOD)
                        continue;
                    Map<String, Double> method;
                    if (!methodResults.containsKey(name)) {
                        method = new TreeMap<String, Double>();
                        methodResults.put(name, method);
                    } else
                        method = methodResults.get(name);

                    // check if all methods have the same number of runs
                    if (numRuns == -1) {
                        numRuns = methRes.getNumberOfResult(meter);
                    } else if (numRuns != methRes.getNumberOfResult(meter)) {
                        System.err
                                .println("WARNING: number of RUNS should be the "
                                        + "same for all methods.");
                    }

                    Double result;
                    String calcMethod =
                            properties.getProperty("calculation_method");
                    if (calcMethod.equals("minimum")) {
                        result = methRes.min(meter);
                    } else if (calcMethod.equals("maximum")) {
                        result = methRes.max(meter);
                    } else if (calcMethod.equals("mean")) {
                        result = methRes.mean(meter);
                    } else {
                        throw new IllegalArgumentException("value \""
                                + calcMethod
                                + "\" not allowed for property "
                                + "\"value_calculation\". "
                                + "Possible values are: \"minimum\", "
                                + "\"maximum\" and \"mean\".");
                    }
                    method.put(classRes.getElementName(), result);
                }
            }

            // iterate over all methods and check the number of classes
            int numLast = -1;
            for (String method : methodResults.keySet()) {
                if (numLast == -1)
                    numLast = methodResults.get(method).size();
                else if (numLast != methodResults.get(method).size())
                    throw new IllegalArgumentException(
                            "Unsupported BenchResult structure. All benchmark "
                                    + "classes have to contain exactly the same "
                                    + "methods!");
            }
        }
    }
}
