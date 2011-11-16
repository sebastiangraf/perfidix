/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
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

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for the counter panel in the eclipse view of
 * Perclipse. It updates the counter (runs, errors) which are sent by the
 * perfidix benching process.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewCounterPanel extends Composite {

    private transient final Text benchRuns;
    private transient final Text benchErrors;
    private transient int totalRuns;
    private transient final Image benchRunImage =createImageDescriptor(
            PerclipseActivator.getDefault().getBundle(), new Path("icons/time.png"), true)
            .createImage();
    private transient final IPath iconPath=new Path("icons/error.png");
    private transient final Image benchErrorImage =createImageDescriptor(
            PerclipseActivator.getDefault().getBundle(), iconPath, true)
            .createImage();

    /**
     * The constructor creates the counter panel for the BenchView.
     * 
     * @param parent
     *            This param is the composite in which the panel has to be
     *            created. In our case it is the BenchView.
     */
    public BenchViewCounterPanel(final Composite parent) {
        super(parent, SWT.WRAP);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 9;
        gridLayout.makeColumnsEqualWidth = false;
        gridLayout.marginWidth = 0;
        setLayout(gridLayout);

        benchRuns = createLabel("Runs: ", benchRunImage, " 0/0 ");

        benchErrors = createLabel("Errors: ", benchErrorImage, " 0 ");

        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(final DisposeEvent event) {
                disposeIcons();
            }
        });

    }

    /**
     * This method is responsible for creating a label.
     * 
     * @param labelName
     * @param image
     * @param initValue
     * @return This method returns the Text widget containing the labeling and
     *         additional stuff.
     */
    private Text createLabel(
            final String labelName, final Image image, final String initValue) {
        Text retText = null;
        if (labelName != null && initValue != null) {
            Label label = new Label(this, SWT.NONE);
            if (image != null) {
                image.setBackground(label.getBackground());
                label.setImage(image);
            }

            label.setLayoutData(new GridData(
                    GridData.HORIZONTAL_ALIGN_BEGINNING));

            label = new Label(this, SWT.NONE);
            label.setText(labelName);
            label.setLayoutData(new GridData(
                    GridData.HORIZONTAL_ALIGN_BEGINNING));

            final Text text = new Text(this, SWT.READ_ONLY);
            text.setText(initValue);
            text.setBackground(getDisplay().getSystemColor(
                    SWT.COLOR_WIDGET_BACKGROUND));
            text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                    | GridData.HORIZONTAL_ALIGN_BEGINNING));

            retText = text;
        }
        return retText;
    }

    /**
     * This method resets the values of the counter labels.
     */
    public void resetRuns() {
        setBenchRuns(0);
        setBenchErrors(0);
        setTotalRuns(0);

    }

    /**
     * This method returns the totalRuns value.
     * 
     * @return the total runs.
     */
    public int getTotalRuns() {
        return totalRuns;
    }

    /**
     * Setter of total runs for the label in the view.
     * 
     * @param totalRuns
     *            The total runs value.
     */
    public void setTotalRuns(final int totalRuns) {
        this.totalRuns = totalRuns;

    }

    /**
     * Setter of the current run value of the label in the view.
     * 
     * @param benchRuns
     *            This param contains the value of the current bench run.
     */
    public void setBenchRuns(final int benchRuns) {

        this.benchRuns.setText(benchRuns + "/" + totalRuns);

        this.benchRuns.redraw();

    }

    /**
     * Setter of occurred errors.
     * 
     * @param benchErrors
     *            The int value for upcoming bench runs.
     */
    public void setBenchErrors(final int benchErrors) {
        this.benchErrors.setText(Integer.toString(benchErrors));
        redraw();
    }
    
    /**
     * This method is responsible for creation an image within the view.
     * 
     * @param bundle The Perclipse bundle.
     * @param path The String name/path of the image.
     * @param useMisImageDesc  The image descripot
     * @return It retruns the created image.
     */
    private static ImageDescriptor createImageDescriptor(final 
            Bundle bundle, final IPath path, final boolean useMisImageDesc) {
        ImageDescriptor retDescriptor=null;
        final URL url = FileLocator.find(bundle, path, null);
        if (url == null) {
            retDescriptor= ImageDescriptor.getMissingImageDescriptor();
        }
        else if (useMisImageDesc) {
            retDescriptor= ImageDescriptor.createFromURL(url);
        }
        return retDescriptor;
    }

    /**
     * This method is responsible for disposing of used icons.
     */
    private void disposeIcons() {
        benchErrorImage.dispose();
        benchRunImage.dispose();

    }
}
