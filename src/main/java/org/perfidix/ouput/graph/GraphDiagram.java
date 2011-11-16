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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Graph class.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Christian Gruen
 * @author Bastian Lemke
 */
abstract class GraphDiagram extends JPanel {
    /** serial version uid. */
    private static final long serialVersionUID = 6960169764397899414L;

    GraphFrame frame;
    Color[] COLORS;

    int off = 1;
    double min = Double.MAX_VALUE, max = 0.0000001;

    void init(GraphFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(frame.graphWidth, frame.graphHeight));
        setBackground(Color.white);
        setColors();
    }

    int getFactor(double value, boolean max) {
        int factor = 0;
        if (value > 1) {
            factor = -1;
            while (value > 1) {
                value /= 10;
                factor++;
            }
        } else if (value < 1) {
            while (value < 1) {
                value *= 10;
                factor--;
            }
        }
        return max ? factor + 1 : factor;
    }

    String getLabel(double d) {
        return Double.toString(d).replaceAll("(\\.0+|0+)$", "");
    }

    void setColors() {
        Color[] COL =
                new Color[] {
                        // new Color(0, 0, 192), new Color(192, 0, 0),
                        // new Color(192, 0, 192), new Color(0, 160, 160),
                        new Color(0, 0, 192), new Color(0, 192, 0),
                        new Color(192, 0, 0), new Color(0, 192, 192),
                        new Color(192, 0, 192), new Color(192, 96, 0),
                        new Color(64, 64, 255), new Color(64, 255, 64),
                        new Color(255, 64, 64), new Color(0, 255, 255),
                        new Color(255, 64, 255), new Color(255, 160, 0),
                        new Color(192, 192, 255),
                        new Color(192, 255, 192),
                        new Color(255, 192, 192),
                        new Color(128, 255, 255),
                        new Color(255, 192, 255),
                        new Color(255, 224, 128), };

        COLORS = new Color[frame.nrValues];
        if (frame.graphColored) {
            COLORS = COL;
        } else {
            int max = Math.min(160, frame.nrFiles * 40);
            for (int i = 0; i < frame.nrFiles; i++) {
                int c = 64 + max * i / Math.max(1, frame.nrFiles - 1);
                COLORS[i] = new Color(c, c, c);
                // COLORS[nrFiles - i - 1] = new Color(c, c, c);
            }
        }
    }

    void drawString(Graphics g, String str, int x, int y) {
        Color c = g.getColor();
        g.setColor(getBackground());
        for (int xo = -off; xo < off; xo++) {
            for (int yo = -off; yo < off; yo++) {
                g.drawString(str, x - xo, y - yo);
            }
        }
        g.setColor(c);
        g.drawString(str, x, y);
    }

    void fillOval(Graphics g, int x, int y, int w, int h) {
        Color c = g.getColor();
        g.setColor(getBackground());
        for (int xo = -off; xo < off; xo++) {
            for (int yo = -off; yo < off; yo++) {
                g.fillOval(x - 1, y - 1, w, h);
            }
        }
        g.setColor(c);
        g.fillOval(x, y, w, h);
    }

    void fillRect(Graphics g, int x, int y, int w, int h) {
        Color c = g.getColor();
        g.setColor(getBackground());
        for (int xo = -off; xo < off; xo++) {
            for (int yo = -off; yo < off; yo++) {
                g.fillRect(x - 1, y - 1, w, h);
            }
        }
        g.setColor(c);
        g.fillRect(x, y, w, h);
    }

    void drawLine(Graphics g, int x, int y, int xe, int ye) {
        Color c = g.getColor();
        g.setColor(getBackground());
        for (int xo = -off; xo < off; xo++) {
            for (int yo = -off; yo < off; yo++) {
                g.drawLine(x - xo, y - yo, xe - xo, ye - yo);
            }
        }
        g.setColor(c);
        g.drawLine(x, y, xe, ye);
    }

    /**
     * Enables/Disables anti-aliasing.
     * 
     * @param g
     *            graphics reference
     */
    public static void antiAlias(final Graphics g) {
        final Graphics2D gg = (Graphics2D) g;
        gg.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }
}
