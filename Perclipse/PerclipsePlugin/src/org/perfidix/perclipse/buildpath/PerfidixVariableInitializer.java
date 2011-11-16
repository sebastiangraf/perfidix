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
package org.perfidix.perclipse.buildpath;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.framework.Bundle;
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for the classpath variable initialization within
 * eclipse - java - build path - classpath variable.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerfidixVariableInitializer extends ClasspathVariableInitializer {

    /**
     * This field specifies the variable id, needed by the corresponding
     * extension point.
     */
    public static final String PERFIDIX_VAR_INIT =
            "org.perfidix.perclipse.PERFIDIX_VAR_INIT";

    /**
     * This method is responisble for initialization of the classpath variable
     * within eclipse for our perfidix libs.
     * 
     * @param variable
     *            The variable for initialization.
     * @see org.eclipse.jdt.core.ClasspathVariableInitializer#initialize(java.lang.String)
     */
    @Override
    public void initialize(final String variable) {
        final Bundle bundle = PerclipseActivator.getDefault().getBundle();
        if (bundle == null) {
            JavaCore.removeClasspathVariable(
                    PerclipseActivator.PERFIDIX_HOME, null);
            return;
        }
        final URL installLocation =
                bundle.getEntry("/lib/".concat(BuildPathSupport.JAR_NAME)); //$NON-NLS-1$

        try {
            final String fullPath =
                    new File(installLocation.getPath()).getAbsolutePath();
            final IPath path = Path.fromOSString(fullPath);
            JavaCore.setClasspathVariable(
                    PerclipseActivator.PERFIDIX_HOME, path, null);

        } catch (JavaModelException e1) {
            PerclipseActivator.log(e1);
            JavaCore.removeClasspathVariable(
                    PerclipseActivator.PERFIDIX_HOME, null);
        }
    }

}
