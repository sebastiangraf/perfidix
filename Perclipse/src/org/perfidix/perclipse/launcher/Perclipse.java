package org.perfidix.perclipse.launcher;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Perclipse extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "Perclipse";

  // The shared instance
  private static Perclipse plugin;
  
  /**
   * The constructor
   */
  public Perclipse() {
    plugin = this;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
  public static Perclipse getDefault() {
    return plugin;
  }

  public static String getPluginId() {
    return PLUGIN_ID;
  }

  public static void logInfo(String message) {
	  log(new Status(IStatus.INFO,
				Perclipse.getPluginId(),
				IStatus.INFO,
				message,
				null));
  }
  
  public static void log(Throwable e) {
    log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, "Error", e));
  }

  public static void log(IStatus status) {
    getDefault().getLog().log(status);
  }
  
  /**
   * Returns an image descriptor for the image file at the given
   * plug-in relative path
   *
   * @param path the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String path) {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }
}
