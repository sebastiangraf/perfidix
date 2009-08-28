package org.perfidix.perclipse.views;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;

/**
 * The PerclipseTabGroup class represent the our tabs in the launch
 * configuration of the Perfidix Bench Plugin alias Perclipse
 * 
 * @author Graf S., Lewandowski L.
 */
public class PerclipseTabGroup extends AbstractLaunchConfigurationTabGroup {

    /** {@inheritDoc} */
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        ILaunchConfigurationTab[] tabs =
                new ILaunchConfigurationTab[] {
                        new PerclipseMainTab(), new JavaArgumentsTab(),
                        new JavaClasspathTab(), new JavaJRETab(),
                        new EnvironmentTab(), new CommonTab() };
        setTabs(tabs);
    }

}
