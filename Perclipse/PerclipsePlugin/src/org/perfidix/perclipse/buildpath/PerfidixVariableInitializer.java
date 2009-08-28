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
