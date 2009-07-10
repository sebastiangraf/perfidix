package org.perfidix.Perclipse.launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Vector;

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
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.perfidix.Perclipse.model.SimulatedWorkClass;
import org.perfidix.Perclipse.util.BenchRunSession;
import org.perfidix.Perclipse.util.BenchSearchEngine;

/**
 * The PerfidixLaunchConfiguration class is a subclass of the
 * AbstractJavaLaunchConfigurationDelegate and so our implementation of
 * LaunchConfigurationDelegate. It contains the necessary configurations for our
 * plugin.
 * 
 * @author lewandow
 * 
 */
public class PerfidixLaunchConfiguration extends
		AbstractJavaLaunchConfigurationDelegate {

	public static final String LAUNCH_CONTAINER_ATTR = PerclipseActivator.PLUGIN_ID
			+ ".CONTAINER";

	public static final String BENCH_NAME_ATTR = PerclipseActivator.PLUGIN_ID
			+ ".BENCHNAME";

	// public static final String LAUNCH_CONTAINER_ATT =
	// PerclipseActivator.PLUGIN_ID + ".CONTAINER";

	public static final String ID_PERFIDIX_APPLICATION = "org.perfidix.configureBench";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.
	 * eclipse.debug.core.ILaunchConfiguration, java.lang.String,
	 * org.eclipse.debug.core.ILaunch,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		IVMInstall vm = verifyVMInstall(configuration);
		IVMRunner runner = vm.getVMRunner(mode);
		try {
			BenchSearchResult searchResult = findBenchTypes(configuration);
			VMRunnerConfiguration runConfig = launchTypes(configuration, mode,
					searchResult);
			runner.run(runConfig, launch, monitor);
			new SimulatedWorkClass();
			


			
			
		} catch (Exception e) {
			// TODO message in the application that file has been removed and
			// configuration isn't valid any more
			// e.printStackTrace();
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
	private BenchSearchResult findBenchTypes(ILaunchConfiguration configuration)
			throws CoreException, InvocationTargetException,
			InterruptedException {
		IJavaProject javaProject = getJavaProject(configuration);
		IType[] types = null;

		/*
		 * Check LAUNCH_CONTAINER_ATTR to see if we are benching an entire
		 * project/package or just a single class.
		 */

		String containerHandle = configuration.getAttribute(
				LAUNCH_CONTAINER_ATTR, ""); //$NON-NLS-1$

		if (containerHandle.length() != 0) {
			// benching an entire project/package
			types = BenchSearchEngine.findBenchs(new Object[] { javaProject });
			System.out.println("Benchin entire project/package: " + javaProject.getElementName());
			PerclipseActivator.logInfo("Benching an entire project/package");
		} else {
			// benching a single class
			String benchTypeName = configuration.getAttribute(
					IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
					(String) null);

			types = new IType[] { javaProject.findType(benchTypeName) };
			System.out.println("Benching class: " + benchTypeName);
			PerclipseActivator.logInfo("Benching class " + benchTypeName);
		}
		
		

		BenchSearchResult result = new BenchSearchResult(types);
		return result;
	}

	/**
	 * 
	 * 
	 * @param configuration
	 * @param mode
	 * @param benchs
	 * @return
	 * @throws CoreException
	 */
	private VMRunnerConfiguration launchTypes(
			ILaunchConfiguration configuration, String mode,
			BenchSearchResult benchs) throws CoreException {
		File workingDir = verifyWorkingDirectory(configuration);
		String workingDirName = null;
		if (workingDir != null)
			workingDirName = workingDir.getAbsolutePath();

		// Program & VM args
		String vmArgs = getVMArguments(configuration);
		ExecutionArguments execArgs = new ExecutionArguments(vmArgs, ""); //$NON-NLS-1$
		String[] envp = getEnvironment(configuration);

		VMRunnerConfiguration runConfig = createVMRunner(configuration, benchs,
				mode);
		runConfig.setVMArguments(execArgs.getVMArgumentsArray());
		runConfig.setWorkingDirectory(workingDirName);
		runConfig.setEnvironment(envp);

		Map vmAttributesMap = getVMSpecificAttributesMap(configuration);
		runConfig.setVMSpecificAttributesMap(vmAttributesMap);

		String[] bootpath = getBootpath(configuration);
		runConfig.setBootClassPath(bootpath);

		return runConfig;
	}

	/**
	 * @param configuration
	 * @param benchTypes
	 * @param runMode
	 * @return
	 * @throws CoreException
	 */
	protected VMRunnerConfiguration createVMRunner(
			ILaunchConfiguration configuration, BenchSearchResult benchTypes,
			String runMode) throws CoreException {
		// String[] classPath = createClassPath(configuration,
		// benchTypes.getTestKind());
		String[] classPath = getClasspath(configuration);

		VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(
				"org.perfidix.Perfidix", classPath); //$NON-NLS-1$
		Vector argv = getVMArgs(configuration, benchTypes, runMode);
		BenchRunSession benchRunSession = BenchRunSession.getInstance();
		benchRunSession.setBenchedClasses(argv);
		String[] args = new String[argv.size()];
		argv.copyInto(args);
		vmConfig.setProgramArguments(args);
		return vmConfig;
	}

	/**
	 * @param configuration
	 * @param result
	 * @param runMode
	 * @return
	 * @throws CoreException
	 */
	public Vector getVMArgs(ILaunchConfiguration configuration,
			BenchSearchResult result, String runMode) throws CoreException {
		String progArgs = getProgramArguments(configuration);

		// insert the program arguments
		Vector<String> argv = new Vector<String>(10);
		ExecutionArguments execArgs = new ExecutionArguments("", progArgs); //$NON-NLS-1$
		String[] pa = execArgs.getProgramArgumentsArray();

		for (int i = 0; i < pa.length; i++) {
			argv.add(pa[i]);

		}

		IType[] benchTypes = result.getTypes();



		for (int i = 0; i < benchTypes.length; i++) {
			argv.add(benchTypes[i].getFullyQualifiedName());

		}

		return argv;
	}

}