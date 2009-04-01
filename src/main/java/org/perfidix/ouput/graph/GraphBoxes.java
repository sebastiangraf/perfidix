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
class GraphBoxes extends GraphDiagram {

    /** serial version uid. */
    private static final long serialVersionUID = 4470160343073572617L;

    GraphBoxes(GraphFrame frame) {
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
        offX =
                Math.max(offX, fm.stringWidth(getLabel(Math.pow(10, mn)))) + 12;
        int offY = fh / 2;
        w -= offX;
        h -= fh * 2;

        // draw distance values & horizontal lines
        double ww = w / (double) frame.nrValues;
        // int wwh = (int) Math.round(ww / 2);

        for (int i = 0, mo = mn; i <= s; i++, mo++) {
            String str = getLabel(Math.pow(10, mo));
            int fw = fm.stringWidth(str);
            g.setColor(Color.black);
            int yy = offY + h - i * h / s - 1;
            g.drawString(str, offX - fw - 12, yy + fh / 4);
            g.setColor(GraphFrame.COLORLGRAY);

            for (int j = 1; j <= 10; j++) {
                double yyy = Math.log(j) * h / s / Math.log(10);
                yyy = yy - yyy;
                g.drawLine(offX - 5, (int) yyy, offX + w, (int) yyy);
            }
            g.setColor(GraphFrame.COLORGRAY);
            g.drawLine(offX, yy, offX + w, yy);
        }

        // draw diagram values
        g.setColor(Color.black);
        for (int i = 0; i < frame.nrValues; i++) {
            String str =
                    frame.graphDrawNames ? frame.graphNames[i] : ""
                            + (i + 1);
            int fw = fm.stringWidth(str);
            g.drawString(str, offX + (int) (i * ww + (ww - fw) / 2), offY
                    + h
                    + fh);
        }

        // draw vertical lines
        g.setColor(GraphFrame.COLORGRAY);
        for (int i = 0; i <= frame.nrValues; i++) {
            int xx = (int) (offX + i * ww);
            if (i == frame.nrValues)
                xx--;
            g.drawLine(xx, offY, xx, offY + h + 20);
        }

        // draw rectangles
        double mf = Math.pow(10, mn);
        int bo = (int) ww / 10;
        double bw = (ww - bo * 2) / frame.nrFiles;
        for (int i = 0; i < frame.nrValues; i++) {
            int xx = (int) (offX + i * ww);
            for (int j = 0; j < frame.nrFiles; j++) {
                // int cc = 192 - i * 192 / nrValues;
                double hmn = frame.data[j][i] / mf;
                double hhh = (Math.log(hmn) / Math.log(10)) * h / s;
                if (hhh < 1)
                    hhh = 1;
                int xxx = xx + bo + (int) (bw * j);
                g.setColor(COLORS[j]);
                g.fillRect(
                        xxx, (int) (offY + h - hhh) + 1, (int) bw - 2,
                        (int) hhh);
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

        int xb = offX + offB;
        int yb = offY + offB;

        if (frame.graphTitle != null) {
            g.setColor(Color.black);
            drawString(g, frame.graphTitle, xb, yb
                    + boxw
                    - fm.getDescent()
                    / 2);
        }

        if (frame.graphOneline) {
            xb += fm.stringWidth(frame.graphTitle) + boxw;
        }

        for (int i = 0; i < frame.nrFiles; i++) {
            g.setColor(COLORS[i]);

            if (!frame.graphOneline) {
                yb =
                        offY
                                + boxh
                                * ((frame.graphTitle == null ? 0 : 1) + (frame.nrFiles - 1 - i))
                                + offB;
            }
            if (!frame.graphColored) {
                g.fillRect(xb, yb, boxw, boxw);
                g.setColor(Color.black);
            }

            if (frame.graphOneline) {
                drawString(g, frame.desc[i], xb
                        + (frame.graphColored ? 0 : boxh), yb
                        + boxw
                        - fm.getDescent()
                        / 2);
                xb +=
                        fm.stringWidth(frame.desc[i])
                                + (int) (boxw * (frame.graphColored
                                        ? 1 : 2.5));
            } else {
                yb =
                        offY
                                + boxh
                                * ((frame.graphTitle == null ? 0 : 1) + (frame.nrFiles - 1 - i))
                                + offB;
                drawString(g, frame.desc[i], xb
                        + (frame.graphColored ? 0 : boxh), yb
                        + offB
                        + boxw
                        - fm.getDescent()
                        / 3);
            }
        }
    }
}
