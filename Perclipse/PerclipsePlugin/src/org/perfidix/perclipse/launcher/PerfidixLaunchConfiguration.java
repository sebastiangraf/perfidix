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
package org.perfidix.perclipse.launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.SocketUtil;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.perfidix.perclipse.model.PerclipseViewSkeleton;
import org.perfidix.perclipse.util.BenchSearchEngine;

/**
 * The PerfidixLaunchConfiguration class is a subclass of the
 * AbstractJavaLaunchConfigurationDelegate and so our implementation of
 * LaunchConfigurationDelegate. It contains the necessary configurations for
 * starting perfidix within our plugin.
 * 
 * @author Graf S., Lewandowski L., DiSy, University of Konstanz
 */
public class PerfidixLaunchConfiguration
        extends AbstractJavaLaunchConfigurationDelegate {

    /**
     * The launch container attribute.
     */
    public static final String LAUNCH_CONT_ATTR =
            PerclipseActivator.PLUGIN_ID + ".CONTAINER";

    /**
     * The bench name attribute.
     */
    public static final String BENCH_NAME_ATTR =
            PerclipseActivator.PLUGIN_ID + ".BENCHNAME";

    /**
     * The id of perfidix application.
     */
    public static final String ID_PERFIDIX_APP = "org.perfidix.configureBench";

    /** {@inheritDoc} */
    public void launch(
            final ILaunchConfiguration configuration, final String mode,
            final ILaunch launch, final IProgressMonitor monitor)
            throws CoreException {

        final IVMInstall vmInst = verifyVMInstall(configuration);
        final IVMRunner runner = vmInst.getVMRunner(mode);
        try {
            final BenchSearchResult searchResult =
                    findBenchTypes(configuration);
            final int port = SocketUtil.findFreePort();
            final PerclipseViewSkeleton skeleton =
                    new PerclipseViewSkeleton(port);
            skeleton.start();
            final VMRunnerConfiguration runConfig =
                    launchTypes(configuration, mode, searchResult, port);
            runner.run(runConfig, launch, monitor);

        } catch (Exception e) {
            PerclipseActivator.log(e);
        }
    }

    /**
     * This method checks the configuration if you try to bench a
     * project/package or just a single class and gives as result the
     * BenchSearchResult object.
     * 
     * @param configuration
     * @return The types representing benchs to be launched for this
     *         configuration
     * @throws CoreException
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    private BenchSearchResult findBenchTypes(
            final ILaunchConfiguration configuration)
            throws CoreException, InvocationTargetException,
            InterruptedException {
        final IJavaProject javaProject = getJavaProject(configuration);
        IType[] types = null;

        /*
         * Check LAUNCH_CONTAINER_ATTR to see if we are benching an entire
         * project/package or just a single class.
         */

        final String containerHandle =
                configuration.getAttribute(LAUNCH_CONT_ATTR, ""); //$NON-NLS-1$

        if (containerHandle.length() == 0) {
            // benching a single class
            final String benchTypeName =
                    configuration
                            .getAttribute(
                                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                                    (String) null);

            types = new IType[] { javaProject.findType(benchTypeName) };
            PerclipseActivator.logInfo("Benching a single class "
                    + benchTypeName);
        } else {
            // benching an entire project/package
            types = BenchSearchEngine.findBenchs(new Object[] { javaProject });
            PerclipseActivator.logInfo("Benching an entire project/package");
        }

        return new BenchSearchResult(types);
    }

    /**
     * @param configuration
     * @param mode
     * @param benchs
     * @param port
     * @return
     * @throws CoreException
     */
    private VMRunnerConfiguration launchTypes(
            final ILaunchConfiguration configuration, final String mode,
            final BenchSearchResult benchs, final int port)
            throws CoreException {
        final File workingDir = verifyWorkingDirectory(configuration);
        String workingDirName = null;
        if (workingDir != null) {
            workingDirName = workingDir.getAbsolutePath();
        }

        // Program & VM args
        final String vmArgs = getVMArguments(configuration);
        final ExecutionArguments execArgs = new ExecutionArguments(vmArgs, ""); //$NON-NLS-1$
        final String[] envp = getEnvironment(configuration);

        final VMRunnerConfiguration runConfig =
                createVMRunner(configuration, benchs, mode, port);
        runConfig.setVMArguments(execArgs.getVMArgumentsArray());
        runConfig.setWorkingDirectory(workingDirName);
        runConfig.setEnvironment(envp);

        final Map<?, ?> vmAttributesMap =
                getVMSpecificAttributesMap(configuration);
        runConfig.setVMSpecificAttributesMap(vmAttributesMap);

        final String[] bootpath = getBootpath(configuration);
        runConfig.setBootClassPath(bootpath);

        return runConfig;
    }

    /**
     * This method creates a VMRunner for our perfidix project.
     * 
     * @param configuration
     *            The created launch configuration for the project which has to
     *            be benched.
     * @param benchTypes
     *            The result of the bench type search.
     * @param runMode
     *            The launch mode run/debug.
     * @param port
     *            The port where the
     *            {@link org.perfidix.perclipse.model.PerclipseViewSkeleton} is
     *            listening.
     * @return The runner configuration.
     * @throws CoreException
     *             The exception.
     */
    protected VMRunnerConfiguration createVMRunner(
            final ILaunchConfiguration configuration,
            final BenchSearchResult benchTypes, final String runMode,
            final int port) throws CoreException {
        // String[] classPath = createClassPath(configuration,
        // benchTypes.getTestKind());
        final String[] classPath = getClasspath(configuration);

        final VMRunnerConfiguration vmConfig =
                new VMRunnerConfiguration(
                        "org.perfidix.socketadapter.SocketAdapter", classPath); //$NON-NLS-1$
        final List<String> argv =
                getVMArgs(configuration, benchTypes, runMode, port);
        String[] args = new String[argv.size()];
        args = (String[]) argv.toArray(new String[argv.size()]);
        vmConfig.setProgramArguments(args);
        return vmConfig;
    }

    /**
     * This method returns the VM arguments in a Vector List for given launch
     * configuration, bench search result, the launch mode and the port for the
     * skeleton.
     * 
     * @param configuration
     *            The launch configuration.
     * @param result
     *            The result of the search for bench elements within the
     *            launching project.
     * @param runMode
     *            The launch mode - run/debug.
     * @param port
     *            The port where the skeleton is listening.
     * @return A Vector List containing the VM arguments for invoking perfidix's
     *         main.
     * @throws CoreException
     *             The exception.
     */
    public List<String> getVMArgs(
            final ILaunchConfiguration configuration,
            final BenchSearchResult result, final String runMode, final int port)
            throws CoreException {
        final String progArgs = getProgramArguments(configuration);

        // insert the program arguments
        final List<String> argv = new ArrayList<String>(10);
        final ExecutionArguments execArgs =
                new ExecutionArguments("", progArgs); //$NON-NLS-1$
        final String[] progArg = execArgs.getProgramArgumentsArray();

        for (String string : progArg) {
            argv.add(string);
        }

        final IType[] benchTypes = result.getTypes();

        for (int i = 0; i < benchTypes.length; i++) {
            argv.add(benchTypes[i].getFullyQualifiedName());
        }

        argv.add("-Port");
        final String stringPort = Integer.toString(port);
        argv.add(stringPort);

        return argv;
    }

}