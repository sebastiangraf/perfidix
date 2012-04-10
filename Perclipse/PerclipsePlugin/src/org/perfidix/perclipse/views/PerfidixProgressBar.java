/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

    private transient int currentTickCount = 0;
    private transient int maxTickCount = 0;
    private transient int colorBarWidth = 0;
    final private transient Color okColor;
    final private transient Color errorColor;
    final private transient Color stoppedColor;
    private transient boolean error;
    private transient boolean hasStopped = false;

    /**
     * The constructor initializes the listeners and the responsible
     * {@link Display} of the parent composite (BenchView). Furthermore it
     * initializes the possible colors for the progress bar.
     * 
     * @param parent
     *            The composite of the parent.
     */
    public PerfidixProgressBar(final Composite parent) {
        super(parent, SWT.NONE);

        addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(final ControlEvent event) {
                colorBarWidth = scale(currentTickCount);
                redraw();
            }
        });
        addPaintListener(new PaintListener() {
            public void paintControl(final PaintEvent event) {
                paint(event);
            }
        });
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(final DisposeEvent event) {
                errorColor.dispose();
                okColor.dispose();
                stoppedColor.dispose();
            }
        });
        final Display display = parent.getDisplay();
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
    public void setMaximum(final int max) {
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
            final boolean hasErrors, final boolean stopped,
            final int ticksDone, final int maximum) {
        final boolean noChange =
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
    private void paintStep(final int startX, final int endX) {
        int starterX = startX;
        final GC theGC = new GC(this);
        setStatusColor(theGC);
        final Rectangle rect = getClientArea();
        starterX = Math.max(1, starterX);
        theGC.fillRectangle(starterX, 1, endX - starterX, rect.height - 2);
        theGC.dispose();
    }

    /**
     * This method sets the status color of the progress bar, depending on if it
     * has stopped, an error occurred or if anything happened right way.
     * 
     * @param theGC
     *            The responsible graphics class for drawing within this class.
     */
    private void setStatusColor(final GC theGC) {
        if (hasStopped) {
            theGC.setBackground(stoppedColor);
        } else if (error) {
            theGC.setBackground(errorColor);
        } else {
            theGC.setBackground(okColor);
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
    private int scale(final int value) {
        int retScale = value;
        if (maxTickCount > 0) {
            final Rectangle rect = getClientArea();
            if (rect.width != 0) {
                retScale = Math.max(0, value * (rect.width - 2) / maxTickCount);
            }
        }
        return retScale;
    }

    /**
     * This method is responsible for drawing the rectangle and its lines for
     * the progress bar.
     * 
     * @param theGC
     * @param xVar
     * @param yVar
     * @param wVar
     * @param hVar
     * @param topleft
     * @param bottomright
     */
    private void drawBevelRect(
            final GC theGC, final int xVar, final int yVar, final int wVar,
            final int hVar, final Color topleft, final Color bottomright) {
        theGC.setForeground(topleft);
        theGC.drawLine(xVar, yVar, xVar + wVar - 1, yVar);
        theGC.drawLine(xVar, yVar, xVar, yVar + hVar - 1);

        theGC.setForeground(bottomright);
        theGC.drawLine(xVar + wVar, yVar, xVar + wVar, yVar + hVar);
        theGC.drawLine(xVar, yVar + hVar, xVar + wVar, yVar + hVar);
    }

    /**
     * This method is responsible for painting the whole stuff.
     * 
     * @param event
     *            Represents an paint event that occurred.
     */
    private void paint(final PaintEvent event) {
        final GC theGC = event.gc;
        final Display disp = getDisplay();

        final Rectangle rect = getClientArea();
        theGC.fillRectangle(rect);
        drawBevelRect(
                theGC, rect.x, rect.y, rect.width - 1, rect.height - 1, disp
                        .getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), disp
                        .getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

        setStatusColor(theGC);
        colorBarWidth = Math.min(rect.width - 2, colorBarWidth);
        theGC.fillRectangle(1, 1, colorBarWidth, rect.height - 2);
    }

    /** {@inheritDoc} */
    @Override
    public Point computeSize(
            final int wHint, final int hHint, final boolean changed) {
        checkWidget();
        final Point size = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
    public void step(final int failures) {
        currentTickCount++;
        int xVal = colorBarWidth;

        colorBarWidth = scale(currentTickCount);

        if (!error && failures > 0) {
            error = true;
            xVal = 1;
        }
        if (currentTickCount == maxTickCount) {
            colorBarWidth = getClientArea().width - 1;
        }
        paintStep(xVal, colorBarWidth);
    }

    /**
     * This method refreshes the progress bar. So it sets the error value and
     * redraw the progress bar.
     * 
     * @param hasErrors
     *            This param sets the value of occurred errors.
     */
    public void refresh(final boolean hasErrors) {
        error = hasErrors;
        redraw();
    }

}
