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
            "perfidix-3.4-jar-with-dependencies.jar";

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
