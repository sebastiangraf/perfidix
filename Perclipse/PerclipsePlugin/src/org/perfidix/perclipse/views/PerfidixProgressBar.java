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
package org.perfidix.perclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * The PerfidixProgressBar represents the progress bar in the eclipse view
 * {@link BenchView}. The progress bar will be updated by the perfidix project.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz.
 */
public class PerfidixProgressBar extends Canvas {
    private static final int DEFAULT_WIDTH = 160;
    private static final int DEFAULT_HEIGHT = 18;

    private int currentTickCount = 0;
    private int maxTickCount = 0;
    private int colorBarWidth = 0;
    private Color okColor;
    private Color errorColor;
    private Color stoppedColor;
    private boolean error;
    private boolean hasStopped = false;

    /**
     * The constructor initializes the listeners and the responsible
     * {@link Display} of the parent composite (BenchView). Furthermore it
     * initializes the possible colors for the progress bar.
     * 
     * @param parent
     *            The composite of the parent.
     */
    public PerfidixProgressBar(Composite parent) {
        super(parent, SWT.NONE);

        addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                colorBarWidth = scale(currentTickCount);
                redraw();
            }
        });
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                paint(e);
            }
        });
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                errorColor.dispose();
                okColor.dispose();
                stoppedColor.dispose();
            }
        });
        Display display = parent.getDisplay();
        errorColor = new Color(display, 159, 63, 63);
        okColor = new Color(display, 30, 144, 255);
        stoppedColor = new Color(display, 120, 120, 120);

    }

    /**
     * This method sets the maxTickCount.
     * 
     * @param max
     *            This param sets the maxTickCount value.
     */
    public void setMaximum(int max) {
        maxTickCount = max;
    }

    /**
     * This method resets the values of the progress bar.
     */
    public void reset() {
        error = false;
        hasStopped = false;
        currentTickCount = 0;
        maxTickCount = 0;
        colorBarWidth = 0;
        redraw();
    }

    /**
     * This method resets the origin values with the values of the params.
     * 
     * @param hasErrors
     *            This boolean value shows that in the process has occurred an
     *            error in a given java element that should be benched.
     * @param stopped
     *            This boolean value shows that the process has stopped and the
     *            progress bar has to be stopped too.
     * @param ticksDone
     *            This value sets the current tick/ bench run.
     * @param maximum
     *            This value represents the total count for the bench runs.
     */
    public void reset(
            boolean hasErrors, boolean stopped, int ticksDone, int maximum) {
        boolean noChange =
                error == hasErrors
                        && hasStopped == stopped
                        && currentTickCount == ticksDone
                        && maxTickCount == maximum;
        error = hasErrors;
        hasStopped = stopped;
        currentTickCount = ticksDone;
        maxTickCount = maximum;
        colorBarWidth = scale(ticksDone);
        if (!noChange) {
            redraw();
        }
    }

    /**
     * This private method sets every step in the progress view and paints it.
     * 
     * @param startX
     *            This value represents the start of the paint step.
     * @param endX
     *            This value sets the end position of the painting progress.
     */
    private void paintStep(int startX, int endX) {
        GC gc = new GC(this);
        setStatusColor(gc);
        Rectangle rect = getClientArea();
        startX = Math.max(1, startX);
        gc.fillRectangle(startX, 1, endX - startX, rect.height - 2);
        gc.dispose();
    }

    /**
     * This method sets the status color of the progress bar, depending on if it
     * has stopped, an error occurred or if anything happened right way.
     * 
     * @param gc
     *            The responsible graphics class for drawing within this class.
     */
    private void setStatusColor(GC gc) {
        if (hasStopped) {
            gc.setBackground(stoppedColor);
        } else if (error) {
            gc.setBackground(errorColor);
        } else {
            gc.setBackground(okColor);
        }
    }

    /**
     * This method stops the progress bar.
     */
    public void stopped() {
        hasStopped = true;
        redraw();
    }

    /**
     * This method is responsible for scaling purposes of the progress bar.
     * 
     * @param value
     *            This value represents the ticks done value.
     * @return returns the new value of scale.
     */
    private int scale(int value) {
        if (maxTickCount > 0) {
            Rectangle r = getClientArea();
            if (r.width != 0) {
                return Math.max(0, value * (r.width - 2) / maxTickCount);
            }
        }
        return value;
    }

    /**
     * This method is responsible for drawing the rectangle and its lines for
     * the progress bar.
     * 
     * @param gc
     * @param x
     * @param y
     * @param w
     * @param h
     * @param topleft
     * @param bottomright
     */
    private void drawBevelRect(
            GC gc, int x, int y, int w, int h, Color topleft, Color bottomright) {
        gc.setForeground(topleft);
        gc.drawLine(x, y, x + w - 1, y);
        gc.drawLine(x, y, x, y + h - 1);

        gc.setForeground(bottomright);
        gc.drawLine(x + w, y, x + w, y + h);
        gc.drawLine(x, y + h, x + w, y + h);
    }

    /**
     * This method is responsible for painting the whole stuff.
     * 
     * @param event
     *            Represents an paint event that occurred.
     */
    private void paint(PaintEvent event) {
        GC gc = event.gc;
        Display disp = getDisplay();

        Rectangle rect = getClientArea();
        gc.fillRectangle(rect);
        drawBevelRect(gc, rect.x, rect.y, rect.width - 1, rect.height - 1, disp
                .getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), disp
                .getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

        setStatusColor(gc);
        colorBarWidth = Math.min(rect.width - 2, colorBarWidth);
        gc.fillRectangle(1, 1, colorBarWidth, rect.height - 2);
    }

    /** {@inheritDoc} */
    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        checkWidget();
        Point size = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        if (wHint != SWT.DEFAULT) {
            size.x = wHint;
        }
        if (hHint != SWT.DEFAULT) {
            size.y = hHint;
        }
        return size;
    }

    /**
     * This method informs about incoming failures. Currently its not used.
     * 
     * @param failures
     *            This param represents the {@link Integer} value of occurred
     *            failures.
     */
    public void step(int failures) {
        currentTickCount++;
        int x = colorBarWidth;

        colorBarWidth = scale(currentTickCount);

        if (!error && failures > 0) {
            error = true;
            x = 1;
        }
        if (currentTickCount == maxTickCount) {
            colorBarWidth = getClientArea().width - 1;
        }
        paintStep(x, colorBarWidth);
    }

    /**
     * This method refreshes the progress bar. So it sets the error value and
     * redraw the progress bar.
     * 
     * @param hasErrors
     *            This param sets the value of occurred errors.
     */
    public void refresh(boolean hasErrors) {
        error = hasErrors;
        redraw();
    }

}
