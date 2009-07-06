package org.perfidix.Perclipse.launcher;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class PerclipseActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.perfidix.Perclipse";

	// The shared instance
	private static PerclipseActivator plugin;

	/**
	 * The constructor
	 */
	public PerclipseActivator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		
		super.start(context);


		// plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
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
	 * @return
	 */
	public static String getPluginId() {
		return PLUGIN_ID;
	}

	/**
	 * The logInfo method expect a String message that will be saved in the log
	 * to document the status.
	 * 
	 * @param message
	 */
	public static void logInfo(String message) {
		log(new Status(IStatus.INFO, getPluginId(), IStatus.INFO, message, null));
	}

	/**
	 * The log method gets a Throwable error message and saves its status in the
	 * log.
	 * 
	 * @param e
	 */
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, "Error", e));
	}

	/**
	 * The log method expect a parameter, the status of type IStatus and
	 * afterwards logs the status.
	 * 
	 * @param status
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
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		         if (plugin == null)
		             return null;
		         IWorkbench workBench= plugin.getWorkbench();
		         if (workBench == null)
		             return null;
		         return workBench.getActiveWorkbenchWindow();
		     }
		
		     public static IWorkbenchPage getActivePage() {
		         IWorkbenchWindow activeWorkbenchWindow= getActiveWorkbenchWindow();
		         if (activeWorkbenchWindow == null)
		             return null;
		         return activeWorkbenchWindow.getActivePage();
		     }


}
