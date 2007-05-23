package org.perfidix.perclipse.launcher;


import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.perfidix.perclipse.util.BenchSearchEngine;

public class PerfidixLaunchShortcut implements ILaunchShortcut {

	public void launch(ISelection selection, String mode) {
    
    System.out.println("TestSelection " + selection.toString()
				+ " and arg1 " + mode);
		if (selection instanceof IStructuredSelection) {
			searchAndLaunch(((IStructuredSelection) selection).toArray(), mode);
		}

	}

	public void launch(IEditorPart editor, String mode) {
		System.out.println("TestEditorPart " + editor.toString() + " and arg1 "
				+ mode);
		IJavaElement element = null;
		IEditorInput input = editor.getEditorInput();
		element = (IJavaElement) input.getAdapter(IJavaElement.class);

		if (element != null) {
			searchAndLaunch(new Object[] { element }, mode);
		}
	}

	private void searchAndLaunch(Object[] search, String mode) {
		if (search != null) {
			if (search.length == 0) {
				// TODO Do something real fancy here if no bench is found..
				return;
			}
			if (search[0] instanceof IJavaElement) {
				IJavaElement element = (IJavaElement) search[0];
				// launch a CU or type
				launchType(element, mode);
			}
		}
	}

	private void launchType(IJavaElement search, String mode) {
		IType[] types = null;
		try {
			types = BenchSearchEngine.findBenchs(new Object[] { search });
		} catch (InterruptedException e) {
			// TODO Do something real fancy here if exceptions were thrown
			return;
		} catch (InvocationTargetException e) {
			// TODO Do something real fancy here if exceptions were thrown
			return;
		}
		IType type = null;
		if (types.length == 0 || types.length > 1) {
			// TODO Do something real fancy here if no bench is found..
		} else {
			type = types[0];
		}
		if (type != null) {
			launch(mode, type);
		}
	}


	private void launch(String mode, IType type) {
		ILaunchConfiguration config = createConfiguration(type);
    

    
		if (config != null) {
			DebugUITools.launch(config, mode);
		}
	}

	private ILaunchConfiguration createConfiguration(IType type) {
		ILaunchConfiguration config = null;
		try {
			ILaunchConfigurationType configType = getPerfidixLaunchConfigType();
			ILaunchConfigurationWorkingCopy wc = newWorkingCopy(configType, type.getElementName());
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, type.getFullyQualifiedName());
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, type.getJavaProject().getElementName());
			config = wc.doSave();
		} catch (CoreException ce) {
		  ce.printStackTrace();
    }
		return config;
	}

	protected ILaunchConfigurationWorkingCopy newWorkingCopy(ILaunchConfigurationType configType, String name)
			throws CoreException {

		return configType.newInstance(null, getLaunchManager()
				.generateUniqueLaunchConfigurationNameFrom(name));
	}

	private ILaunchConfigurationType getPerfidixLaunchConfigType() {
		ILaunchManager lm = getLaunchManager();
		return lm.getLaunchConfigurationType("org.perfidix.configureBench");
	}
	
	private ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

}
