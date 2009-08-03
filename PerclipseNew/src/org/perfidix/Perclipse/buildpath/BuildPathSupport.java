package org.perfidix.Perclipse.buildpath;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;
import org.perfidix.Perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for finding the provided perfidix jar and help to
 * load it to the build path of the plugin.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BuildPathSupport {
    /**
     * This field specifies the name of the existing jar that will be added to
     * the plug-ins build path.
     */
    public final static String JAR_NAME =
            "perfidix-3.3-jar-with-dependencies.jar";

    /**
     * This method returns the path of the plug-ins bundle location.
     * 
     * @return Returns the bundle location.
     */
    public static IPath getBundleLocation() {
        Bundle bundle = PerclipseActivator.getDefault().getBundle();
        if (bundle == null) {
            return null;
        }

        URL local = null;
        try {
            local = FileLocator.toFileURL(bundle.getEntry("/"));

        } catch (IOException e) {
            PerclipseActivator.log(e);
            e.printStackTrace();
            return null;
        }
        String fullPath = new File(local.getPath()).getAbsolutePath();
        return Path.fromOSString(fullPath);
    }

    /**
     * This method returns the Perfidix classpath entry.
     * 
     * @return Returns classpath entry.
     */
    public static IClasspathEntry getPerfidixClasspathEntry() {
        return JavaCore
                .newContainerEntry(PerfidixContainerInitializer.PERFIDIX_PATH);
    }

    /**
     * This method returns the Perfidix library classpath entry.
     * 
     * @return Returns the Perfidix library classpath entry.
     */
    public static IClasspathEntry getPerfidixLibraryEntry() {
        IPath bundleBase = getBundleLocation();
        if (bundleBase != null) {
            IPath jarLocation =
                    bundleBase
                            .append("/lib/".concat(BuildPathSupport.JAR_NAME));

            IAccessRule[] accessRule = {};
            String javadocLocation =
                    PerclipseActivator
                            .getDefault().getPreferenceStore().getString(
                                    "This is the Perfidix JavaDoc");
            IClasspathAttribute[] attributes =
                    { JavaCore
                            .newClasspathAttribute(
                                    IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME,
                                    javadocLocation) };
            return JavaCore.newLibraryEntry(
                    jarLocation, null, null, accessRule, attributes, false);

        }
        return null;

    }

}
