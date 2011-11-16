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
package org.perfidix.perclipse.launcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
import org.perfidix.perclipse.util.BenchSearchEngine;

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
        private transient final Throwable exce;

        /**
         * The constructor.
         * 
         * @param paramExec
         *            The throwable.
         */
        public LaunchCancelledByUserException(final Throwable paramExec) {
            super();
            exce = paramExec;
        }

        /**
         * Returns occurred exception.
         * 
         * @return Occurred exception.
         */
        public Throwable getException() {
            return exce;
        }
    }

    /** {@inheritDoc} */
    public void launch(final ISelection selection, final String mode) {

        if (selection instanceof IStructuredSelection) {
            searchAndLaunch(((IStructuredSelection) selection).toArray(), mode);

        }

    }

    /** {@inheritDoc} */
    public void launch(final IEditorPart editor, final String mode) {
        IJavaElement element = null;
        final IEditorInput input = editor.getEditorInput();
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
    private void searchAndLaunch(final Object[] search, final String mode) {
        if (search != null
                && search.length != 0
                && search[0] instanceof IJavaElement) {

            final IJavaElement element = (IJavaElement) search[0];

            // the IJavaElement is a model/container/fragment/package
            if (element.getElementType() < IJavaElement.COMPILATION_UNIT) {
                try {
                    launch(mode, describeContainerLaunch(element));
                } catch (LaunchCancelledByUserException e) {
                    PerclipseActivator.log(e);
                }
                // the IJavaElement is a compilation unit/type
            } else if (element.getElementType() == IJavaElement.COMPILATION_UNIT) {
                launchType(element, mode);
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
    private void launchType(final IJavaElement search, final String mode) {
        IType[] types = null;
        try {
            types = BenchSearchEngine.findBenchs(new Object[] { search });
            IType type = null;

            if (types.length != 0) {
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
        } catch (InterruptedException e) {
            PerclipseActivator.log(e);
        } catch (InvocationTargetException e) {
            PerclipseActivator.log(e);
        }

    }

    /**
     * Create the description for benching a project/package.
     * 
     * @param container
     *            the container to bench
     * @return the perfidix launch configuration.
     */
    public PerfidixLaunchDescription describeContainerLaunch(
            final IJavaElement container) {
        final PerfidixLaunchDescription description =
                new PerfidixLaunchDescription(
                        container, getContainerLabel(container));
        description.setContainer(container.getHandleIdentifier());

        return description;
    }

    /**
     * Create the description for benching a type (class).
     * 
     * @param type
     *            The bench type.
     * @return The launch description for a given type.
     */
    public PerfidixLaunchDescription describeTypeLaunch(final IType type) {
        final PerfidixLaunchDescription description =
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
    private void launch(
            final String mode, final PerfidixLaunchDescription description)
            throws LaunchCancelledByUserException {
        final ILaunchConfiguration config =
                findOrCreateLaunchConfiguration(mode, this, description);
        if (config != null) {
            DebugUITools.launch(config, mode);

        }

    }

    /**
     * Check to see if a configuration for this type already exists. If not,
     * create a new one.
     * 
     * @param mode
     *            The run mode.
     * @param registry
     *            The launch shortcut type.
     * @param description
     *            The perfidix launch description.
     * @return The launch configuration.
     * @throws LaunchCancelledByUserException
     *             The cancel exception as an event of user interaction.
     */
    public ILaunchConfiguration findOrCreateLaunchConfiguration(
            final String mode, final PerfidixLaunchShortcut registry,
            final PerfidixLaunchDescription description)
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
     * @param mode
     *            The launch mode.
     * @param description
     *            The perfidix launch description.
     * @return The launch configuration.
     * @throws LaunchCancelledByUserException
     *             The exception occurred when an user canceled the run.
     */
    public ILaunchConfiguration findLaunchConfiguration(
            final String mode, final PerfidixLaunchDescription description)
            throws LaunchCancelledByUserException {
        ILaunchConfiguration retConfig = null;

        final ILaunchConfigurationType configType =
                getPerfidixLaunchConfigType();
        List<ILaunchConfiguration> candidateConfigs =
                new ArrayList<ILaunchConfiguration>(0);

        try {
            final ILaunchConfiguration[] configs =
                    getLaunchManager().getLaunchConfigurations(configType);
            candidateConfigs =
                    new ArrayList<ILaunchConfiguration>(configs.length);

            for (int i = 0; i < configs.length; i++) {
                final ILaunchConfiguration config = configs[i];
                if (description.attributesMatch(config)) {
                    candidateConfigs.add(config);
                }
            }
        } catch (CoreException e) {
            PerclipseActivator.log(e);
        }

        final int candidateCount = candidateConfigs.size();

        // return null if no matching configuration was found
        if (candidateCount >= 1) {
            retConfig = candidateConfigs.get(0);
        }
        return retConfig;
    }

    /**
     * Create a configuration from the the perfidix description.
     * 
     * @param description
     *            The perfidix launch description.
     * @return The launch configuration.
     */
    public ILaunchConfiguration createConfiguration(
            final PerfidixLaunchDescription description) {
        final String mainType =
                description
                        .getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME);
        final String benchName =
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
     * @param project
     *            The java project for a launch.
     * @param name
     *            The config name.
     * @param mainType
     *            The main type.
     * @param container
     *            The container attribute.
     * @param benchName
     *            The bench name.
     * @return The launch configuration.
     */
    protected ILaunchConfiguration createConfiguration(
            final IJavaProject project, final String name,
            final String mainType, final String container,
            final String benchName) {
        ILaunchConfiguration config = null;

        try {
            final ILaunchConfigurationWorkingCopy workCop =
                    newWorkingCopy(name);
            workCop.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    mainType);
            workCop.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    project.getElementName());
            workCop.setAttribute(
                    PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, container);
            if (benchName.length() > 0) {
                workCop.setAttribute(
                        PerfidixLaunchConfiguration.BENCH_NAME_ATTR, benchName);
            }
            config = workCop.doSave();
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
     * @param name
     *            The configuration name.
     * @return The launch configuration working copy.
     * @throws CoreException
     *             The core exception.
     */
    protected ILaunchConfigurationWorkingCopy newWorkingCopy(final String name)
            throws CoreException {
        final ILaunchConfigurationType configType =
                getPerfidixLaunchConfigType();
        return configType.newInstance(null, getLaunchManager()
                .generateUniqueLaunchConfigurationNameFrom(name));
    }

    /**
     * Returns the local java launch configuration type.
     * 
     * @return ILaunchConfigurationType
     */
    protected ILaunchConfigurationType getPerfidixLaunchConfigType() {
        final ILaunchManager launchMan = getLaunchManager();

        return launchMan
                .getLaunchConfigurationType(PerfidixLaunchConfiguration.ID_PERFIDIX_APP);
    }

    /**
     * Returns the container label of a given {@link IJavaElement}.
     * 
     * @param container
     *            The java element.
     * @return The label name for a java element.
     */
    protected String getContainerLabel(final IJavaElement container) {
        final String name =
                JavaElementLabels.getTextLabel(
                        container, JavaElementLabels.ALL_FULLY_QUALIFIED);
        return name.substring(name.lastIndexOf(IPath.SEPARATOR) + 1);
    }

}
