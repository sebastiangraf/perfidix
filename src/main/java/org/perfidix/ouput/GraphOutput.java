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
     * <li><code>class_names</code> = numbers ([numbers,
     * {<i>String1</i>,<i>String2</i>,<i>String3</i>,...}])<br />
     * instead of numbers below the diagram, you can set string values (as comma
     * separated list).</li>
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

    public void saveAsPDF() {
        // TODO: implement
    }

    /**
     * Save the benchmark data to a file. The data can later be retrieved and
     * plotted by {@link #load(File)} and {@link #plot()}.
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
     * Reads previously saved data from a file. The data can be plotted by
     * {@link #plot()}.
     * 
     * @param file
     *            the file to read the data from.
     * @throws FileNotFoundException
     *             if the file was not found.
     * @throws IOException
     *             if an error occurs while reading the data.
     * @throws ClassNotFoundException
     *             if the file contains wrong data.
     */
    @SuppressWarnings("unchecked")
    public void load(File file)
            throws FileNotFoundException, IOException,
            ClassNotFoundException {
        if (data != null)
            throw new UnsupportedOperationException(
                    "GraphOutput already contains data.");
        ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(file));
        data =
                (Map<AbstractMeter, Map<String, Map<String, Double>>>) ois
                        .readObject();
        numRuns = ois.readInt();
    }

    /**
     * Plots the benchmark results. Before plotting you have to either run a
     * perfidix benchmark and call {@link #visitBenchmark(BenchmarkResult)} or
     * load a file with previously saved data ({@link #load(File)}).
     */
    public void plot() {
        for (AbstractMeter meter : data.keySet()) {
            properties.setProperty("num_runs", String.valueOf(numRuns));
            properties.setProperty("unit", meter.getUnit());
            properties
                    .setProperty("unit_desc", meter.getUnitDescription());
            Graph.createGraph(data.get(meter), properties);
        }
    }

    // private Map<String, Map<String, Double>> addResults(
    // BenchmarkResult result, String[] methodNames) {
    // //
    // // Map<String, Map<String, Double>> methodResults =
    // // new TreeMap<String, Map<String, Double>>();
    //
    // for (AbstractMeter meter : result.getRegisteredMeters()) {
    // Map<String, Map<String, Double>> oldMethods, newMethods;
    // newMethods = getData(result, meter);
    // if (!data.containsKey(meter)) {
    // data.put(meter, newMethods);
    // } else {
    // oldMethods = data.get(meter);
    // for (String method : newMethods.keySet()) {
    // if (!)
    // }
    // }
    // }
    // return null;
    // }

    @Deprecated
    private Map<String, Map<String, Double>> getData(
            BenchmarkResult benchRes, AbstractMeter meter) {

        // method -> class -> value
        Map<String, Map<String, Double>> methodResults =
                new TreeMap<String, Map<String, Double>>();

        int numClasses = 0;
        for (ClassResult classRes : benchRes.getIncludedResults()) {
            numClasses++;
            for (MethodResult methRes : classRes.getIncludedResults()) {
                String name = methRes.getElementName();
                if (!methodResults.containsKey(name)) {
                    methodResults.put(name, new TreeMap<String, Double>());
                }
                if (numRuns == -1) {
                    numRuns = methRes.getNumberOfResult(meter);
                } else if (numRuns != methRes.getNumberOfResult(meter)) {
                    System.err
                            .println("WARNING: number of RUNS should be the "
                                    + "same for all methods.");
                }
                Map<String, Double> method = methodResults.get(name);
                Double result;
                String valCalc =
                        properties.getProperty("calculation_method");
                if (valCalc.equals("minimum")) {
                    result = methRes.min(meter);
                } else if (valCalc.equals("maximum")) {
                    result = methRes.max(meter);
                } else if (valCalc.equals("mean")) {
                    result = methRes.mean(meter);
                } else {
                    throw new IllegalArgumentException("value \""
                            + valCalc
                            + "\" not allowed for property "
                            + "\"value_calculation\". "
                            + "Possible values are: \"minimum\", "
                            + "\"maximum\" and \"mean\".");
                }
                method.put(classRes.getElementName(), result);
            }
        }
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
        properties.setProperty("num_runs", String.valueOf(numRuns));
        return methodResults;
    }

    private void addResults(
            BenchmarkResult benchRes, Map<String, String> methodRename) {

        if (data == null) {
            data =
                    new TreeMap<AbstractMeter, Map<String, Map<String, Double>>>();
        }

        for (AbstractMeter meter : benchRes.getRegisteredMeters()) {
            int numClasses = 0;
            if (!data.containsKey(meter))
                data.put( //
                        meter, new TreeMap<String, Map<String, Double>>());
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
                    if (!methodResults.containsKey(name)) {
                        methodResults.put(
                                name, new TreeMap<String, Double>());
                    }
                    if (numRuns == -1) {
                        numRuns = methRes.getNumberOfResult(meter);
                    } else if (numRuns != methRes.getNumberOfResult(meter)) {
                        System.err
                                .println("WARNING: number of RUNS should be the "
                                        + "same for all methods.");
                    }
                    Map<String, Double> method = methodResults.get(name);
                    Double result;
                    String valCalc =
                            properties.getProperty("calculation_method");
                    if (valCalc.equals("minimum")) {
                        result = methRes.min(meter);
                    } else if (valCalc.equals("maximum")) {
                        result = methRes.max(meter);
                    } else if (valCalc.equals("mean")) {
                        result = methRes.mean(meter);
                    } else {
                        throw new IllegalArgumentException("value \""
                                + valCalc
                                + "\" not allowed for property "
                                + "\"value_calculation\". "
                                + "Possible values are: \"minimum\", "
                                + "\"maximum\" and \"mean\".");
                    }
                    method.put(classRes.getElementName(), result);
                }
            }
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
