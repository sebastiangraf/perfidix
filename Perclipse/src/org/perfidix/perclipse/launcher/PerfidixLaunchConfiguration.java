package org.perfidix.perclipse.launcher;

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
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.perfidix.perclipse.util.BenchSearchEngine;

public class PerfidixLaunchConfiguration extends
		AbstractJavaLaunchConfigurationDelegate {

	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		IVMInstall vm = verifyVMInstall(configuration);
		IVMRunner runner = vm.getVMRunner(mode);
		try {
			BenchSearchResult searchResult = findTestTypes(configuration);
			VMRunnerConfiguration runConfig = launchTypes(configuration, mode,
					searchResult);
			runner.run(runConfig, launch, monitor); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param configuration
	 * @param pm
	 * @return The types representing tests to be launched for this
	 *         configuration
	 * @throws CoreException
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	private BenchSearchResult findTestTypes(ILaunchConfiguration configuration)
			throws CoreException, InvocationTargetException,
			InterruptedException {
		IJavaProject javaProject = getJavaProject(configuration);
		IType[] types = null;

		types = BenchSearchEngine.findBenchs(new Object[] { javaProject });

		BenchSearchResult result = new BenchSearchResult(types);
		return result;
	}

	private VMRunnerConfiguration launchTypes(
			ILaunchConfiguration configuration, String mode,
			BenchSearchResult tests) throws CoreException {
		File workingDir = verifyWorkingDirectory(configuration);
		String workingDirName = null;
		if (workingDir != null)
			workingDirName = workingDir.getAbsolutePath();

		// Program & VM args
		String vmArgs = getVMArguments(configuration);
		ExecutionArguments execArgs = new ExecutionArguments(vmArgs, ""); //$NON-NLS-1$
		String[] envp = getEnvironment(configuration);

		VMRunnerConfiguration runConfig = createVMRunner(configuration, tests,
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

	protected VMRunnerConfiguration createVMRunner(
			ILaunchConfiguration configuration, BenchSearchResult testTypes,
			 String runMode) throws CoreException {
		// String[] classPath = createClassPath(configuration,
		// testTypes.getTestKind());
		String[] classPath = getClasspath(configuration);

		VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(
				"org.perfidix.Perfidix", classPath); //$NON-NLS-1$
		Vector argv = getVMArgs(configuration, testTypes, runMode);
		String[] args = new String[argv.size()];
		argv.copyInto(args);
		vmConfig.setProgramArguments(args);
		return vmConfig;
	}

	public Vector getVMArgs(ILaunchConfiguration configuration,
			BenchSearchResult result, String runMode)
			throws CoreException {
		String progArgs = getProgramArguments(configuration);

		// insert the program arguments
		Vector<String> argv = new Vector<String>(10);
		ExecutionArguments execArgs = new ExecutionArguments("", progArgs); //$NON-NLS-1$
		String[] pa = execArgs.getProgramArgumentsArray();

		for (int i = 0; i < pa.length; i++) {
			argv.add(pa[i]);
		}

		IType[] testTypes = result.getTypes();

		for (int i = 0; i < testTypes.length; i++) {
			argv.add(testTypes[i].getFullyQualifiedName());
		}

		return argv;
	}

}
