package org.perfidix.perclipse.launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.SocketUtil;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.perfidix.perclipse.util.BenchSearchEngine;

/**
 * Launch configuration delegate for a plain JUnit test.
 */
public class PerfidixLaunchConfiguration extends AbstractJavaLaunchConfigurationDelegate  {

  public static final String RUN_QUIETLY_MODE = "runQuietly"; //$NON-NLS-1$

  public static final String PLUGIN_ID= "org.perfidix";

  public static final String PORT_ATTR= PLUGIN_ID+".PORT";

  public static final String TESTTYPE_ATTR= PLUGIN_ID+".TESTTYPE";

  public static final String ATTR_KEEPRUNNING = PLUGIN_ID + ".KEEPRUNNING_ATTR"; //$NON-NLS-1$

  
  public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
    mode = ILaunchManager.RUN_MODE;
    try {
      BenchSearchResult testTypes;
      testTypes = findTestTypes(configuration, monitor);


      IVMInstall install= getVMInstall(configuration);
      IVMRunner runner = install.getVMRunner(mode);

      if (runner == null) {
      }

      int port= SocketUtil.findFreePort();
      VMRunnerConfiguration runConfig= launchTypes(configuration, mode, testTypes, port);
      setDefaultSourceLocator(launch, configuration);

      launch.setAttribute(PORT_ATTR, Integer.toString(port));
      launch.setAttribute(TESTTYPE_ATTR, testTypes.getTypes()[0].getHandleIdentifier());
      runner.run(runConfig, launch, monitor);    

    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
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
  protected BenchSearchResult findTestTypes(ILaunchConfiguration configuration,
      IProgressMonitor pm) throws CoreException,
      InvocationTargetException,
      InterruptedException {
    IJavaProject javaProject= getJavaProject(configuration);
    IType[] types = null;

    types = BenchSearchEngine.findBenchs(new Object[] {javaProject});

    BenchSearchResult result = new BenchSearchResult(types);
    return result;
  }

  
  /**
   * 
   * @param configuration
   * @param mode
   * @param tests
   * @param port
   * @return
   * @throws CoreException
   */
  protected VMRunnerConfiguration launchTypes(ILaunchConfiguration configuration, 
      String mode, BenchSearchResult tests, int port) throws CoreException {
    File workingDir = verifyWorkingDirectory(configuration);
    String workingDirName = null;
    if (workingDir != null) 
      workingDirName = workingDir.getAbsolutePath();

//  Program & VM args
    String vmArgs= getVMArguments(configuration);
    ExecutionArguments execArgs = new ExecutionArguments(vmArgs, ""); //$NON-NLS-1$
    String[] envp= getEnvironment(configuration);

    VMRunnerConfiguration runConfig= createVMRunner(configuration, tests, port, mode);
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
   * Add a VMRunner with a class path that includes org.eclipse.jdt.junit plugin.
   * In addition it adds the port for the RemoteTestRunner as an argument
   */
  protected VMRunnerConfiguration createVMRunner(ILaunchConfiguration configuration, BenchSearchResult testTypes, int port, String runMode) throws CoreException {
    //String[] classPath = createClassPath(configuration, testTypes.getTestKind());
    String[] classPath= getClasspath(configuration);
    
    VMRunnerConfiguration vmConfig= new VMRunnerConfiguration("org.perfidix.perclipse.runner.BenchRunner", classPath); //$NON-NLS-1$
    Vector argv= getVMArgs(configuration, testTypes, port, runMode);
    String[] args= new String[argv.size()];
    argv.copyInto(args);
    vmConfig.setProgramArguments(args);
    return vmConfig;
  }


  /**
   * Create the program arguments to pass to the BenchRunner.
   * 
   * @param configuration
   * @param result
   * @param port
   * @param runMode
   * @return
   * @throws CoreException
   */
  public Vector getVMArgs(ILaunchConfiguration configuration, 
                            BenchSearchResult result,
                            int port, String runMode) 
                                                         throws CoreException {
    String progArgs= getProgramArguments(configuration);
    
    // insert the program arguments
    Vector<String> argv= new Vector<String>(10);
    ExecutionArguments execArgs = new ExecutionArguments("", progArgs); //$NON-NLS-1$
    String[] pa= execArgs.getProgramArgumentsArray();
    
    for (int i= 0; i < pa.length; i++) {
      argv.add(pa[i]);
    }

    argv.addAll(getBasicArguments(configuration, port, runMode, result));

    IType[] testTypes = result.getTypes();

    argv.add("-classNames"); //$NON-NLS-1$
    for (int i= 0; i < testTypes.length; i++) {
      argv.add(testTypes[i].getFullyQualifiedName());
    }
    
    return argv;
  }

  
  /**
   * Adds some basic arguments.
   * 
   * @param configuration
   * @param port
   * @param runMode
   * @param result
   * @return
   * @throws CoreException
   */
  public List<String> getBasicArguments(ILaunchConfiguration configuration, int port, String runMode, BenchSearchResult result) throws CoreException {
    ArrayList<String> argv = new ArrayList<String>();
    argv.add("-version"); //$NON-NLS-1$
    argv.add("3"); //$NON-NLS-1$

    argv.add("-port"); //$NON-NLS-1$
    argv.add(Integer.toString(port));

    if (keepAlive(configuration) && runMode.equals(ILaunchManager.DEBUG_MODE))
      argv.add(0, "-keepalive"); //$NON-NLS-1$

    return argv;
  }

 
  protected boolean keepAlive(ILaunchConfiguration config) {
    try {
      return config.getAttribute(ATTR_KEEPRUNNING, false);
    } catch(CoreException e) {
    }
    return false;
  }

  
  /* (non-Javadoc)
   * @see org.eclipse.jdt.internal.junit.launcher.ITestFindingAbortHandler#abort(java.lang.String, java.lang.Throwable, int)
   */
  protected void abort(String message, Throwable exception, int code) throws CoreException {
    throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, code, message, exception));
  }
}

