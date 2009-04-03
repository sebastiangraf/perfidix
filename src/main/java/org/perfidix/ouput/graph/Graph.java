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
package org.perfidix.ouput.graph;

import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;

/**
 * Graph class.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Christian Gruen
 * @author Bastian Lemke
 */
public class Graph extends JFrame {
    /** serial version uid. */
    private static final long serialVersionUID = -5057298984019609665L;

    /**
     * Plot a 2D graph with the given data and graph properties.
     * 
     * @param data
     *            the values to plot.
     *            <p>
     *            The map layout is like follows:<br />
     *            <i>MethodGroup</i> -> <i>Method</i> -> <i>Value</i><br />
     *            or in other words:<br />
     *            <i>Line</i> -> <i>x value</i> -> <i>y value</i>
     *            </p>
     *            <p>
     *            If you set "lines" representation, each <i>MethodGroup</i>
     *            will be represented as a single line. Each <i>Method</i> (i.e.
     *            the x value of the line) has exactly one <i>Value</> (i.e. the
     *            y value of the line).
     *            </p>
     *            <p>
     *            The "x value" is not a numeric value but a string.
     *            </p>
     * @param properties
     *            properties for the graph layout.
     */
    public static void createGraph(
            Map<String, Map<String, Double>> data, Properties properties) {
        new Graph(data, properties);
    }

    Graph(Map<String, Map<String, Double>> data, Properties properties) {
        super("Performance Graph - "
                + properties.getProperty("meter_name"));
        getContentPane().add(new GraphFrame(data, properties));
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
