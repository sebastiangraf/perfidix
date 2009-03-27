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
import java.io.IOException;
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
     * <li><code>minimum_range</code> = 0 (<i>int</i>)</li>
     * <li><code>maximum_range</code> = 4 (<i>int</i>)</li>
     * </ul>
     * </p>
     */
    public Properties properties;

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
        defaults.setProperty("minimum_range", "0");
        defaults.setProperty("maximum_range", "4");
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
        for (AbstractMeter meter : benchRes.getRegisteredMeters()) {
            properties.setProperty("unit", meter.getUnit());
            Graph.createGraph(getData(benchRes, meter), properties);
        }
    }

    private Map<String, Map<String, Double>> getData(
            BenchmarkResult benchRes, AbstractMeter meter) {

        Map<String, Map<String, Double>> methodResults =
                new TreeMap<String, Map<String, Double>>();

        int numClasses = 0;
        for (ClassResult classRes : benchRes.getIncludedResults()) {
            numClasses++;
            for (MethodResult methRes : classRes.getIncludedResults()) {
                String name = methRes.getElementName();
                Map<String, Double> method;
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
                method = methodResults.get(name);
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
        properties.setProperty("num_runs", String.valueOf(numRuns));
        return methodResults;
    }
}
