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
package org.perfidix.perclipse.buildpath;

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
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for finding the provided perfidix jar and help to
 * load it to the build path of the plugin.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public final class BuildPathSupport {
    /**
     * This field specifies the name of the existing jar that will be added to
     * the plug-ins build path.
     */
    public static final String JAR_NAME =
            "perfidix-3.5-jar-with-dependencies.jar";

    /**
     * The constructor.
     */
    private BuildPathSupport() {
    }

    /**
     * This method returns the path of the plug-ins bundle location.
     * 
     * @return Returns the bundle location.
     */
    public static IPath getBundleLocation() {
        IPath returnPath = null;
        final Bundle bundle = PerclipseActivator.getDefault().getBundle();
        if (bundle != null) {
            URL local = null;
            try {
                local = FileLocator.toFileURL(bundle.getEntry("/"));
                final String fullPath =
                        new File(local.getPath()).getAbsolutePath();
                returnPath = Path.fromOSString(fullPath);

            } catch (IOException e) {
                PerclipseActivator.log(e);
            }
        }
        return returnPath;
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
        IClasspathEntry returnEntry = null;
        final IPath bundleBase = getBundleLocation();
        if (bundleBase != null) {
            final IPath jarLocation =
                    bundleBase
                            .append("/lib/".concat(BuildPathSupport.JAR_NAME));

            final IAccessRule[] accessRule = {};
            final String javadocLocation =
                    PerclipseActivator
                            .getDefault().getPreferenceStore().getString(
                                    "This is the Perfidix JavaDoc");
            final IClasspathAttribute[] attributes =
                    { JavaCore
                            .newClasspathAttribute(
                                    IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME,
                                    javadocLocation) };
            returnEntry =
                    JavaCore.newLibraryEntry(
                            jarLocation, null, null, accessRule, attributes,
                            false);

        }
        return returnEntry;

    }

}
