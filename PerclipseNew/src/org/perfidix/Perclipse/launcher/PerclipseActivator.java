package org.perfidix.Perclipse.launcher;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.perfidix.Perclipse.model.BenchModel;
import org.perfidix.Perclipse.views.BenchView;

/**
 * The activator class controls the plug-in life cycle
 */
public class PerclipseActivator extends AbstractUIPlugin {

    // The plug-in ID
    /**
     * The plugin-id
     */
    public static final String PLUGIN_ID = "org.perfidix.Perclipse";

    // The shared instance
    private static PerclipseActivator plugin;

    private static final BenchModel BENCH_MODEL = new BenchModel();

    /**
     * The Perfidix home variable for building purposes
     */
    public static final String PERFIDIX_HOME = "PERFIDIX_HOME";

    /**
     * The Perfidix source home variable for building purposes
     */
    public static final String PERFIDIX_SRC_HOME = "PERFIDIX_SRC_HOME";

    private BenchView view;

    private BundleContext bundleContext;

    /**
     * The constructor
     */
    public PerclipseActivator() {
        plugin = this;

    }

    /** {@inheritDoc} */
    public void start(BundleContext context) throws Exception {
        plugin = this;
        super.start(context);
        bundleContext = context;

        BENCH_MODEL.start();
    }

    /** {@inheritDoc} */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        try {
            BENCH_MODEL.stop();
        } finally {
            super.stop(context);
        }
        bundleContext = null;
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
    public static void logInfo(String message) {
        log(new Status(IStatus.INFO, getPluginId(), IStatus.INFO, message, null));
    }

    /**
     * The log method gets a Throwable error message and saves its status in the
     * log.
     * 
     * @param e
     *            The Exception occurred and has to be stored in the log.
     */
    public static void log(Throwable e) {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, "Error", e));
    }

    /**
     * The log method gets a Throwable error message and saves its status in the
     * log.
     * 
     * @param e
     *            The Exception occurred and has to be stored in the log.
     * @param text
     *            The error title.
     */
    public static void log(Throwable e, String text) {
        log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, text, e));
    }

    /**
     * The log method expect a parameter, the status of type IStatus and
     * afterwards logs the status.
     * 
     * @param status
     *            The status that has to be logged.
     */
    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        if (path != null) {
            return imageDescriptorFromPlugin(PLUGIN_ID, path);
        }
        return null;
    }

    /**
     * @return This method returns the active workbench window of this plug-in.
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        if (plugin == null) {
            return null;
        }
        IWorkbench workBench = plugin.getWorkbench();
        if (workBench == null) {
            return null;
        }
        return workBench.getActiveWorkbenchWindow();
    }

    /**
     * @return This Method returns the active workbench page of this plug-in.
     */
    public static IWorkbenchPage getActivePage() {
        IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return null;
        }
        return activeWorkbenchWindow.getActivePage();
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
    public void setBenchView(BenchView view) {
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
        return plugin.getWorkspace();
    }

    /**
     * @return This method returns the bundle context or null.
     */
    public BundleContext getBundleContext() {
        return bundleContext;
    }

}
