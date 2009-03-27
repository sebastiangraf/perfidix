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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Graph class.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Christian Gruen
 */
class GraphLines extends GraphDiagram {

    /** serial version uid. */
    private static final long serialVersionUID = -3453296008413558882L;

    GraphLines(GraphFrame frame) {
        init(frame);
    }

    @Override
    public void paintComponent(Graphics g) {
        paint(g, getWidth(), getHeight());
    }

    public void paint(Graphics gg, int w, int h) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        antiAlias(g);

        Font FONTSMALL =
                new Font(frame.graphFont, frame.graphFontStyle, h / 20);
        g.setFont(FONTSMALL);
        FontMetrics fm = g.getFontMetrics();

        // calculate vertical line distances
        int mn = frame.graphMinRange;
        int mx = frame.graphMaxRange;
        if (mn == Integer.MAX_VALUE)
            mn = getFactor(min, false);
        if (mx == Integer.MAX_VALUE)
            mx = getFactor(max, true);
        int s = mx - mn;

        // calculate diagram offsets
        int fh = fm.getHeight();
        int offX;
        if (mx > 6 && mn < 7) {
            // above 10^6, the values are written as 1.0E7, ...
            // and need less space than 1000000
            offX = fm.stringWidth(getLabel(Math.pow(10, 6)));
        } else {
            offX = fm.stringWidth(getLabel(Math.pow(10, mx)));
        }
        offX = Math.max(offX, fm.stringWidth(getLabel(Math.pow(10, mn))));
        int offY = fh / 2;
        w -= offX;
        h -= fh * 2;

        // draw distance values & horizontal lines
        double ww = w / (double) frame.nrValues;
        int wwh = (int) Math.round(ww / 2);

        for (int i = 0, mo = mn; i <= s; i++, mo++) {
            String str = getLabel(Math.pow(10, mo));
            int fw = fm.stringWidth(str);
            g.setColor(Color.black);
            int yy = offY + h - i * h / s - 1;
            g.drawString(str, offX - fw + 5, yy + fh / 4);
            g.setColor(GraphFrame.COLORLGRAY);
            if (i == s)
                continue;
            for (int j = 1; j <= 10; j++) {
                double yyy = Math.log(j) * h / s / Math.log(10);
                yyy = yy - yyy;
                g.drawLine(
                        offX + wwh - 5, (int) yyy, offX + w - wwh,
                        (int) yyy);
            }
            g.setColor(GraphFrame.COLORGRAY);
            g.drawLine(offX + wwh, yy, offX + w - wwh, yy);
        }

        // draw diagram values
        g.setColor(Color.black);
        for (int i = 0; i < frame.nrValues; i++) {
            String str = "" + (i + 1);
            int fw = fm.stringWidth(str);
            g.drawString(str, offX + (int) (i * ww + (ww - fw) / 2), offY
                    + h
                    + fh);
        }

        // draw vertical lines
        g.setColor(GraphFrame.COLORGRAY);
        for (int i = 0; i < frame.nrValues; i++) {
            int xx = (int) (offX + i * ww + wwh);
            g.drawLine(xx, offY, xx, offY + h);
        }

        // draw lines
        float factor = h / 1.0f / frame.graphThick;
        float[] dash1 = { 13.0f * factor, 5.0f * factor };
        float[] dash2 = { 10.0f * factor };
        float[] dash3 = { 5.0f * factor };
        float sw0 = factor * 3;
        float sw1 = factor * 2;
        float sw2 = factor;
        BasicStroke stroke0 = new BasicStroke(sw0);
        BasicStroke stroke1 = new BasicStroke(sw1);
        BasicStroke stroke2 = new BasicStroke(sw2);
        BasicStroke[] dashed =
                {
                        stroke1,
                        new BasicStroke(
                                sw1, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f),
                        new BasicStroke(
                                sw1, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f),
                        new BasicStroke(
                                sw1, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash3, 0.0f),
                        stroke2,
                        new BasicStroke(
                                sw2, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f),
                        new BasicStroke(
                                sw2, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f),
                        new BasicStroke(
                                sw2, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash3, 0.0f),
                        stroke0,
                        new BasicStroke(
                                sw0, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f),
                        new BasicStroke(
                                sw0, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f),
                        new BasicStroke(
                                sw0, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 10.0f, dash3, 0.0f), };
        if (frame.nrFiles == 4) {
            dashed =
                    new BasicStroke[] {
                            stroke1,
                            new BasicStroke(
                                    sw1, BasicStroke.CAP_BUTT,
                                    BasicStroke.JOIN_MITER, 10.0f, dash3,
                                    0.0f),
                            stroke2,
                            new BasicStroke(
                                    sw2, BasicStroke.CAP_BUTT,
                                    BasicStroke.JOIN_MITER, 10.0f, dash3,
                                    0.0f), };
        }
        if (frame.nrFiles == 3) {
            dashed =
                    new BasicStroke[] {
                            stroke1,
                            new BasicStroke(
                                    sw1, BasicStroke.CAP_BUTT,
                                    BasicStroke.JOIN_MITER, 10.0f, dash1,
                                    0.0f),
                            new BasicStroke(
                                    sw1, BasicStroke.CAP_BUTT,
                                    BasicStroke.JOIN_MITER, 10.0f, dash3,
                                    0.0f), };
        }

        int dot = frame.graphDot > 0 ? h / frame.graphDot : 0;
        double mf = Math.pow(10, mn);
        for (int i = 0; i < frame.nrValues; i++) {
            int xx = (int) (offX + i * ww);
            for (int j = 0; j < frame.nrFiles; j++) {
                g.setStroke(frame.graphColored ? stroke1 : dashed[j]);
                g.setColor(frame.graphColored ? COLORS[j] : Color.black);

                // int cc = 192 - i * 192 / nrValues;
                double hhh =
                        (Math.log(frame.data[j][i] / mf) / Math.log(10))
                                * h
                                / s;
                if (hhh < 1)
                    hhh = 1;

                int xxx = xx + wwh;
                if (i + 1 < frame.nrValues) {
                    if (frame.data[j][i + 1] != 0) {
                        double hh2 =
                                (Math.log(frame.data[j][i + 1] / mf) / Math
                                        .log(10))
                                        * h
                                        / s;
                        if (hh2 < 1)
                            hh2 = 1;
                        g.drawLine(xxx, (int) (offY + h - hhh), xxx
                                + (int) (ww), (int) (offY + h - hh2));
                    }
                }

                if (dot > 0 && frame.data[j][i] != 0) {
                    if (j % 2 == 0) {
                        fillOval(
                                g, xxx - dot / 2,
                                (int) (offY + h - hhh - dot / 2), dot, dot);
                    } else {
                        fillRect(
                                g, xxx - dot / 2,
                                (int) (offY + h - hhh - dot / 2), dot, dot);
                    }
                }
            }
        }

        // draw legend
        int boxw = frame.graphLegendSize * h / 500;
        int boxh = (int) (1.4 * boxw);
        int offB = h / 40;
        off = h / 100;
        Font FONTBIG =
                new Font(frame.graphFont, frame.graphFontStyle, boxw + 3);
        g.setFont(FONTBIG);
        fm = g.getFontMetrics();
        // boolean nodot = frame.COLORED && dot == 0;

        int xb = offX + offB + wwh;
        int yb = offY + offB;
        if (frame.graphDown) {
            yb = offY + h - fm.getHeight();

            int fw =
                    frame.graphTitle != null ? fm
                            .stringWidth(frame.graphTitle)
                            + boxw
                            / 2 : 0;
            for (int i = 0; i < frame.nrFiles; i++) {
                fw += fm.stringWidth(frame.desc[i]) + boxw * 2;
            }
            xb = (int) (offX + wwh + w - ww - fw);
        }

        if (frame.graphTitle != null) {
            g.setColor(Color.black);
            drawString(g, frame.graphTitle, xb, yb
                    + boxw
                    - fm.getDescent()
                    / 2);
        }

        if (frame.graphOneline) {
            xb += fm.stringWidth(frame.graphTitle);
            if (!frame.graphColored)
                xb += boxw / 2;
        }

        for (int i = 0; i < frame.nrFiles; i++) {
            g.setColor(frame.graphColored ? COLORS[i] : Color.black);

            if (frame.graphOneline) {
                drawString(g, frame.desc[i], xb + (int) (1.2 * boxw), yb
                        + boxw
                        - fm.getDescent()
                        / 2);
            } else {
                yb =
                        offY
                                + boxh
                                * ((frame.graphTitle == null ? 0 : 1) + (frame.nrFiles - 1 - i))
                                + offB;
                if (frame.graphColored)
                    drawString(g, frame.desc[i], xb, yb
                            + boxw
                            - fm.getDescent()
                            / 3);
                else
                    drawString(g, frame.desc[i], xb + boxh, yb
                            + boxw
                            - fm.getDescent()
                            / 3);
            }

            if (frame.graphColored) {
                g.setStroke(stroke1);
            } else {
                g.setStroke(dashed[i]);
            }
            if (!frame.graphColored)
                drawLine(g, xb, yb, xb + boxw, yb + boxw);

            if (dot > 0) {
                int boxc = (boxw - dot) / 2;
                if (i % 2 == 0) {
                    fillOval(g, xb + boxc, yb + boxc, dot, dot);
                } else {
                    fillRect(g, xb + boxc, yb + boxc, dot, dot);
                }
            }
            if (frame.graphOneline) {
                xb += fm.stringWidth(frame.desc[i]) + boxw / 2;
                if (!frame.graphColored)
                    xb += boxw * 3 / 2;
            }
        }
    }
}
