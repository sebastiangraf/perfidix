package org.perfidix.Perclipse.launcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.perfidix.Perclipse.util.BenchSearchEngine;
import org.perfidix.Perclipse.views.BenchView;

/**
 * The class is responsible for the settings of our Perfidix LaunchShortcut
 * implementation. It specifies the launch settings with a given launch
 * configuration.
 * 
 * @author Graf S.
 */
public class PerfidixLaunchShortcut implements ILaunchShortcut {

    /**
     * Contains the final set serialVersionUID.
     * 
     * @author Graf S.
     */
    public class LaunchCancelledByUserException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers
     * .ISelection, java.lang.String)
     */
    public void launch(ISelection selection, String mode) {

        if (selection instanceof IStructuredSelection) {
            searchAndLaunch(((IStructuredSelection) selection).toArray(), mode);

        }

    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart,
     * java.lang.String)
     */
    public void launch(IEditorPart editor, String mode) {
        IJavaElement element = null;
        IEditorInput input = editor.getEditorInput();
        element = (IJavaElement) input.getAdapter(IJavaElement.class);

        if (element != null) {
            searchAndLaunch(new Object[] { element }, mode);
        }
    }

    /**
     * The searchAndLaunch method gets an object array and a mode and checks if
     * the object array item is a java element to launch either in container
     * mode or a single compilation unit mode.
     * 
     * @param search
     * @param mode
     */
    private void searchAndLaunch(Object[] search, String mode) {
        if (search != null) {
            if (search.length == 0) {
                System.out.println("No bench found!");
                // TODO Do something real fancy here if no bench is found..
                return;
            }

            if (search[0] instanceof IJavaElement) {
                IJavaElement element = (IJavaElement) search[0];

                // the IJavaElement is a model/container/fragment/package
                if (element.getElementType() < IJavaElement.COMPILATION_UNIT) {
                    try {
                        launch(mode, describeContainerLaunch(element));
                    } catch (LaunchCancelledByUserException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return;

                    // the IJavaElement is a compilation unit/type
                } else if (element.getElementType() == IJavaElement.COMPILATION_UNIT) {
                    launchType(element, mode);
                }
            }
        }
    }

    /**
     * The launchType method gets an IJavaElement and a mode and searches after
     * included benchs in the given element and afterwards launch the
     * compilation unit.
     * 
     * @param search
     * @param mode
     */
    private void launchType(IJavaElement search, String mode) {
        IType[] types = null;
        try {
            types = BenchSearchEngine.findBenchs(new Object[] { search });
            for (int i = 0; i < types.length; i++) {

            }
        } catch (InterruptedException e) {
            PerclipseActivator.log(e);
            // TODO Do something real fancy here if exceptions were thrown
            return;
        } catch (InvocationTargetException e) {
            PerclipseActivator.log(e);
            // TODO Do something real fancy here if exceptions were thrown
            return;
        }

        IType type = null;

        if (types.length == 0) {
            // TODO Do something real fancy here if no bench is found..
            System.out.println("No bench here");
        } else {
            type = types[0];

        }

        if (type != null) {
            try {
                launch(mode, describeTypeLaunch(type));
            } catch (LaunchCancelledByUserException e) {
                PerclipseActivator.log(e);
                // OK, silently move on
            }
        }
    }

    /**
     * Create the description for benching a project/package.
     * 
     * @param container
     *            the container to bench
     * @return
     */
    public PerfidixLaunchDescription describeContainerLaunch(
            IJavaElement container) {
        PerfidixLaunchDescription description =
                new PerfidixLaunchDescription(
                        container, getContainerLabel(container));
        description.setContainer(container.getHandleIdentifier());

        return description;
    }

    /**
     * Create the description for benching a type (class).
     * 
     * @param type The bench type.
     * @return The launch description for a given type.
     */
    public PerfidixLaunchDescription describeTypeLaunch(IType type) {
        PerfidixLaunchDescription description =
                new PerfidixLaunchDescription(type, type
                        .getFullyQualifiedName());
        description.setMainType(type);

        return description;
    }

    /**
     * Looks up if config already exists and take it to call the DebugUITools
     * launch method. If the launch configuration (config) does not exist, it
     * creates a new one.
     * 
     * @param mode
     * @param description
     * @throws LaunchCancelledByUserException
     */
    private void launch(String mode, PerfidixLaunchDescription description)
            throws LaunchCancelledByUserException {
        ILaunchConfiguration config =
                findOrCreateLaunchConfiguration(mode, this, description);
        if (config != null) {
            DebugUITools.launch(config, mode);

        }

    }

    /**
     * Check to see if a configuration for this type already exists. If not,
     * create a new one.
     * 
     * @param mode The run mode.
     * @param registry The launch shortcut type.
     * @param description The perfidix launch description.
     * @return The launch configuration.
     * @throws LaunchCancelledByUserException The cancel exception as an event of user interaction.
     */
    public ILaunchConfiguration findOrCreateLaunchConfiguration(
            String mode, PerfidixLaunchShortcut registry,
            PerfidixLaunchDescription description)
            throws LaunchCancelledByUserException {

        ILaunchConfiguration config =
                registry.findLaunchConfiguration(mode, description);

        if (config == null) {
            config = registry.createConfiguration(description);

        }
        return config;
    }

    /**
     * Search for an existing configuration.
     * 
     * @param mode The launch mode.
     * @param description The perfidix launch description.
     * @return The launch configuration.
     * @throws LaunchCancelledByUserException The exception occurred when an user canceled the run.
     */
    public ILaunchConfiguration findLaunchConfiguration(
            String mode, PerfidixLaunchDescription description)
            throws LaunchCancelledByUserException {

        ILaunchConfigurationType configType = getPerfidixLaunchConfigType();
        List<ILaunchConfiguration> candidateConfigs =
                new Vector<ILaunchConfiguration>(0);

        try {
            ILaunchConfiguration[] configs =
                    getLaunchManager().getLaunchConfigurations(configType);
            candidateConfigs =
                    new ArrayList<ILaunchConfiguration>(configs.length);

            for (int i = 0; i < configs.length; i++) {
                ILaunchConfiguration config = configs[i];
                if (description.attributesMatch(config)) {
                    candidateConfigs.add(config);
                }
            }
        } catch (CoreException e) {
            PerclipseActivator.log(e);
        }

        int candidateCount = candidateConfigs.size();

        // return null if no matching configuration was found
        if (candidateCount < 1) {
            return null;
            // TODO: what if more than one matching configuration
        } else {
            return (ILaunchConfiguration) candidateConfigs.get(0);
        }
    }

    /**
     * Create a configuration from the the perfidix description.
     * 
     * @param description The perfidix launch description.
     * @return The launch configuration.
     */
    public ILaunchConfiguration createConfiguration(
            PerfidixLaunchDescription description) {
        String mainType =
                description
                        .getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME);
        String benchName =
                description
                        .getAttribute(PerfidixLaunchConfiguration.BENCH_NAME_ATTR);
        // create the configuration
        return createConfiguration(description.getProject(), description
                .getName(), mainType, description.getContainer(), benchName);
    }

    /**
     * The following method creates a launch configuration out of a working copy
     * of launch configuration.
     * 
     * @param project The  java project for a launch.
     * @param name The config name.
     * @param mainType The main type.
     * @param container The container attribute.
     * @param benchName The bench name.
     * @return The launch configuration.
     */
    protected ILaunchConfiguration createConfiguration(
            IJavaProject project, String name, String mainType,
            String container, String benchName) {
        ILaunchConfiguration config = null;

        try {
            ILaunchConfigurationWorkingCopy wc = newWorkingCopy(name);
            wc.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    mainType);
            wc.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    project.getElementName());
            wc.setAttribute(
                    PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR,
                    container);
            if (benchName.length() > 0)
                wc.setAttribute(
                        PerfidixLaunchConfiguration.BENCH_NAME_ATTR, benchName);
            config = wc.doSave();
        } catch (CoreException ce) {
            PerclipseActivator.log(ce);
        }

        return config;
    }

    /**
     * Returns the existing launch manager of type {@link ILaunchManager}.
     * 
     * @return
     */
    private ILaunchManager getLaunchManager() {
        return DebugPlugin.getDefault().getLaunchManager();
    }

    /**
     * Returns a new instance of ILaunchConfigurationWorkingCopy with the config
     * setting of the perfidix launch configuration.
     * 
     * @param name The configuration name.
     * @return The launch configuration working copy.
     * @throws CoreException The core exception.
     */
    protected ILaunchConfigurationWorkingCopy newWorkingCopy(String name)
            throws CoreException {
        ILaunchConfigurationType configType = getPerfidixLaunchConfigType();
        return configType.newInstance(null, getLaunchManager()
                .generateUniqueLaunchConfigurationNameFrom(name));
    }

    /**
     * Returns the local java launch configuration type.
     * 
     * @return ILaunchConfigurationType
     */
    protected ILaunchConfigurationType getPerfidixLaunchConfigType() {
        ILaunchManager lm = getLaunchManager();

        return lm
                .getLaunchConfigurationType(PerfidixLaunchConfiguration.ID_PERFIDIX_APPLICATION);
    }

    /**
     * Returns the container label of a given {@link IJavaElement}.
     * 
     * @param container The java element. 
     * @return The label name for a java element.
     */
    protected String getContainerLabel(IJavaElement container) {
        String name =
                JavaElementLabels.getTextLabel(
                        container, JavaElementLabels.ALL_FULLY_QUALIFIED);
        return name.substring(name.lastIndexOf(IPath.SEPARATOR) + 1);
    }

}
