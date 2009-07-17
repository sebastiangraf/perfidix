package org.perfidix.Perclipse.buildpath;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.internal.PartPluginAction;
import org.osgi.framework.Bundle;
import org.perfidix.Perclipse.launcher.PerclipseActivator;

public class PerfidixVariableInitializer extends ClasspathVariableInitializer {

	public final static String PERFIDIX_VARIABLE_INIT = "org.perfidix.Perclipse.PERFIDIX_VAR_INIT";

	@Override
	public void initialize(String variable) {
		Bundle bundle = PerclipseActivator.getDefault().getBundle(); //$NON-NLS-1$
		if (bundle == null) {
			JavaCore.removeClasspathVariable(PerclipseActivator.PERFIDIX_HOME, null);
			return;
		}
		URL installLocation = bundle.getEntry("/lib/perfidix-3.3-jar-with-dependencies.jar"); //$NON-NLS-1$
//		URL local=null;
//		try {
//			local = Platform.asLocalURL(installLocation);
//			System.out.println(local);
//		} catch (IOException e) {
//			JavaCore.removeClasspathVariable(PerclipseActivator.PERFIDIX_HOME, null);
//			System.out.println("Fehler");;
//		}
		try {
			System.out.println("instlallLocation url: "+installLocation);
			String fullPath = new File(installLocation.getPath()).getAbsolutePath();
			System.out.println("Fullpath string :"+fullPath);
			IPath path= Path.fromOSString(fullPath);
			System.out.println(path);
			JavaCore.setClasspathVariable(PerclipseActivator.PERFIDIX_HOME, path, null);
		
			
		} catch (JavaModelException e1) {
			JavaCore.removeClasspathVariable(PerclipseActivator.PERFIDIX_HOME, null);
		}
	}

}
