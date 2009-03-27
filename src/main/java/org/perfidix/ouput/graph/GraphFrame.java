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

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Map;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Graph class.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Christian Gruen
 * @author Bastian Lemke
 */
class GraphFrame extends JPanel {
    /** serial version uid. */
    private static final long serialVersionUID = -6327542631159381446L;

    static final Color COLORGRAY = new Color(192, 192, 192);
    static final Color COLORLGRAY = new Color(240, 240, 240);

    private final Properties properties;
    final String graphFont;
    final int graphFontStyle;
    final String graphTitle;
    final int graphWidth;
    final int graphHeight;
    final float graphThick;
    final String graphValue;
    final String graphRepresentation;
    final int graphDot;
    final boolean graphColored;
    final int graphLegendSize;
    final boolean graphOneline;
    final boolean graphDown;
    final int graphMinRange;
    final int graphMaxRange;

    final int nrFiles;
    final int nrValues;

    final double[][] data;
    final String[] desc;

    GraphFrame(Map<String, Map<String, Double>> data, Properties properties) {
        this.desc = data.keySet().toArray(new String[0]);
        this.data = new double[desc.length][];
        int i = 0;
        int numVal = 0;
        double max = 0; // max of all values, used for determine the max range
        double min = Double.MAX_VALUE; // min of all values
        for (Map<String, Double> clazz : data.values()) {
            this.data[i] = new double[clazz.values().size()];
            int j = 0;
            for (Double val : clazz.values()) {
                double v = val.doubleValue();
                if (v < min)
                    min = v;
                if (v > max)
                    max = v;
                this.data[i][j++] = v;
                if (j > numVal)
                    numVal = j;
            }
            i++;
        }
        this.nrValues = numVal;
        this.nrFiles = this.desc.length;
        this.properties = properties;

        // parse properties
        graphWidth = propInt("width");
        graphHeight = propInt("height");
        graphThick = propFloat("thick");
        graphValue = propStr("value_calculation");
        graphRepresentation = propStr("representation");
        graphDot = propInt("dot");
        graphColored = propBool("colored");
        graphLegendSize = propInt("legend_size");
        graphFont = propStr("font");
        graphFontStyle = propInt("font_style");
        graphOneline = propBool("oneline");
        graphDown = propBool("down");
        if (propStr("minimum_range").equals("auto")) {
            graphMinRange = (int) Math.floor(Math.log10(min));
        } else {
            graphMinRange = propInt("minimum_range");
        }
        if (propStr("maximum_range").equals("auto")) {
            graphMaxRange = (int) Math.ceil(Math.log10(max)) + 1;
        } else {
            graphMaxRange = propInt("maximum_range");
        }

        StringBuilder str = new StringBuilder();
        str.append(propStr("title"));
        str.append(" (");
        str.append(propStr("num_runs"));
        // compact title, if it's printed in one line
        if (!graphOneline) {
            str.append(" runs, method: ");
            str.append(propStr("calculation_method"));
            str.append(", unit: ");
            str.append(propStr("unit"));
        } else {
            str.append(", ");
            str.append(propStr("calculation_method"));
            str.append(", [");
            str.append(propStr("unit"));
            str.append("]");
        }
        str.append(")");
        graphTitle = str.toString();

        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout());
        setBackground(Color.white);

        if (graphRepresentation.equals("lines")) {
            add(new GraphLines(this), BorderLayout.CENTER);
        } else if (graphRepresentation.equals("boxes")) {
            add(new GraphBoxes(this), BorderLayout.CENTER);
        }
    }

    // -------------------------------------------------------------------------

    private String propStr(String key) {
        return properties.getProperty(key);
    }

    private int propInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    private float propFloat(String key) {
        return Float.parseFloat(properties.getProperty(key));
    }

    private boolean propBool(String key) {
        return properties.getProperty(key).equals("true");
    }
}
