package org.perfidix.perclipse.launcher;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class PerfidixLaunchDescription {

	private Map<String,Object> fAttributes= new HashMap<String,Object>();

	private final IJavaElement fElement;

	private final String fName;
	
	public PerfidixLaunchDescription(IJavaElement element, String name) {
		fElement= element;
		fName= name;
		setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, getProjectName());
	}
	
	public String getProjectName() {
		IJavaProject project = getProject();
		return project == null ? null : project.getElementName();
	}
	
  public String getName() {
    return fName;
  }

  private IJavaProject getProject() {
		return fElement == null ? null : fElement.getJavaProject();
	}
	
	private PerfidixLaunchDescription setAttribute(String attr, Object value) {
		fAttributes.put(attr, value);
		return this;
	}
	
	public PerfidixLaunchDescription setMainType(IType mainType) {
		return setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, mainType);
	}
}
