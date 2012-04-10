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
package org.perfidix.perclipse.launcher;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.perfidix.perclipse.model.BenchModel;
import org.perfidix.perclipse.views.BenchView;

/**
 * The activator class controls the plug-in life cycle
 */
public class PerclipseActivator extends AbstractUIPlugin {

    // The plug-in ID
    /**
     * The plugin-id
     */
    public static final String PLUGIN_ID = "org.perfidix.perclipse";

    // The shared instance
    private static transient PerclipseActivator plugin;

    private static final BenchModel BENCH_MODEL = new BenchModel();

    /**
     * The Perfidix home variable for building purposes
     */
    public static final String PERFIDIX_HOME = "PERFIDIX_HOME";

    /**
     * The Perfidix source home variable for building purposes
     */
    public static final String PERFIDIX_SRC_HOME = "PERFIDIX_SRC_HOME";

    private transient BenchView view;

    private transient BundleContext bundleContext;

    /**
     * The constructor
     */
    public PerclipseActivator() {
        super();
        setSharedInstance(this);

    }

    /** {@inheritDoc} */
    @Override
    public void start(final BundleContext context) {
        plugin = this;
        try {
            super.start(context);
            bundleContext = context;
            BENCH_MODEL.start();
        } catch (Exception e) {
            log(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop(final BundleContext context) {
        try {
            BENCH_MODEL.stop();
        } finally {
            try {
                super.stop(context);
            } catch (Exception e) {
                log(e);
            }
        }
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static PerclipseActivator getDefault() {
        return plugin;
    }

    /**
     * The getPluginID method returns, oh wonder, the PluginID.
     * 
     * @return The String plug-in id.
     */
    public static String getPluginId() {
        return PLUGIN_ID;
    }

    /**
     * The logInfo method expect a String message that will be saved in the log
     * to document the status.
     * 
     * @param message
     *            The message which has to be stored in the log.
     */
    public static void logInfo(final String message) {
        log(new Status(IStatus.INFO, getPluginId(), IStatus.INFO, message, null));
    }

    /**
     * The log method gets a Throwable error message and saves its status in the
     * log.
     * 
     * @param exce
     *            The Exception occurred and has to be stored in the log.
     */
    public static void log(final Throwable exce) {
        log(new Status(
                IStatus.ERROR, getPluginId(), IStatus.ERROR, "Error", exce));
    }

    /**
     * The log method gets a Throwable error message and saves its status in the
     * log.
     * 
     * @param exce
     *            The Exception occurred and has to be stored in the log.
     * @param text
     *            The error title.
     */
    public static void log(final Throwable exce, final String text) {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, text, exce));
    }

    /**
     * The log method expect a parameter, the status of type IStatus and
     * afterwards logs the status.
     * 
     * @param status
     *            The status that has to be logged.
     */
    public static void log(final IStatus status) {
        getDefault().getLog().log(status);
    }

//    /**
//     * Returns an image descriptor for the image file at the given plug-in
//     * relative path
//     * 
//     * @param path
//     *            the path
//     * @return the image descriptor
//     */
//    public static ImageDescriptor getImageDescriptor(final String path) {
//        ImageDescriptor imageDescriptor = null;
//        if (path != null) {
//            imageDescriptor = imageDescriptorFromPlugin(PLUGIN_ID, path);
//        }
//        return imageDescriptor;
//    }

    /**
     * @return This method returns the active workbench window of this plug-in.
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        IWorkbenchWindow window = null;
        if (plugin != null) {
            final IWorkbench workBench = plugin.getWorkbench();
            if (workBench != null) {
                window = workBench.getActiveWorkbenchWindow();
            }

        }
        return window;
    }

    /**
     * @return This Method returns the active workbench page of this plug-in.
     */
    public static IWorkbenchPage getActivePage() {
        IWorkbenchPage page = null;
        final IWorkbenchWindow activeWorkbWindow = getActiveWorkbenchWindow();
        if (activeWorkbWindow != null) {
            page = activeWorkbWindow.getActivePage();
        }
        return page;
    }

    /**
     * @return This method returns the instance of the BenchModel class.
     */
    public static BenchModel getModel() {
        return BENCH_MODEL;
    }

    /**
     * This method sets the created BenchView.
     * 
     * @param view
     *            The BenchView.
     */
    public void setBenchView(final BenchView view) {
        this.view = view;

    }

    /**
     * @return This method returns the created BenchView.
     */
    public BenchView getBenchView() {
        return view;
    }

    /**
     * @return This method returns the plug-in workspace.
     */
    public IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * @return This method returns the bundle context or null.
     */
    public BundleContext getBundleContext() {
        return bundleContext;
    }

    /**
     * Set shared instance-
     * 
     * @param instance
     *            This instance.
     */
    private void setSharedInstance(final PerclipseActivator instance) {
        plugin = instance;
    }

}
