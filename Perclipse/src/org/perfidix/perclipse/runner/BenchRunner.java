package org.perfidix.perclipse.runner;

import java.util.Vector;

public class BenchRunner {

  /** The name of the classes to be benched. */
  private String[] classNames;

  /** Host to connect to, default is the localhost. */
  private String host = ""; //$NON-NLS-1$

  /** Port to connect to. */
  private int port = -1;

  /** Bench Loader. */
  private PerfidixBenchLoader loader;
  
  /**
   * Parse command line arguments. Hook for subclasses to process
   * additional arguments.
   */
  protected void init(String[] args) {
    defaultInit(args);
  }

  
  /**
   * The class loader to be used for loading tests.
   * Subclasses may override to use another class loader.
   */
  protected ClassLoader getTestClassLoader() {
    return getClass().getClassLoader();
  }

  
  /**
   * Process the default arguments.
   */
  protected final void defaultInit(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].toLowerCase().equals("-classnames") 
                            || args[i].toLowerCase().equals("-classname")) {
        
        Vector<String> list = new Vector<String>();
        
        for (int j = i + 1; j < args.length; j++) {
          if (args[j].startsWith("-")) //$NON-NLS-1$
            break;
          list.add(args[j]);
        }
        
        classNames = (String[]) list.toArray(new String[list.size()]);
        
      } else if (args[i].toLowerCase().equals("-port")) { //$NON-NLS-1$
        port = Integer.parseInt(args[i + 1]);
        i++;
      } else if (args[i].toLowerCase().equals("-host")) { //$NON-NLS-1$
        host = args[i + 1];
        i++;
      }

      initDefaultLoader();

      if (classNames == null || classNames.length == 0){
        throw new IllegalArgumentException("BenchRunner.error.classnamemissing");
      }

      if (port == -1){
        throw new IllegalArgumentException("BenchRunner.error.portmissing");
      }
    } 
  }

  
  /**
   * Initiates the default loader.
   *
   */
  public void initDefaultLoader() {
    createLoader(PerfidixBenchLoader.class.getName());
  }

  /**
   * Creates a loader with of the given class name.
   * 
   * @param className
   */
  public void createLoader(String className) {
    loader = createRawTestLoader(className);
  }

  
  /**
   * Creates a new instance of the loader.
   * 
   * @param className
   * @return
   */
  protected PerfidixBenchLoader createRawTestLoader(String className) {
    try {
      return (PerfidixBenchLoader) loadTestLoaderClass(className).newInstance();
    } catch (Exception e) {
      throw new IllegalArgumentException("benchRunner.error.invalidloader");
    }
  }

  
  /**
   * Stupid useless method.
   * 
   * @param className
   * @return
   * @throws ClassNotFoundException
   */
  protected Class loadTestLoaderClass(String className) throws ClassNotFoundException {
    return Class.forName(className);
  }
  
  
  /** 
   * The main entry point.
   * Parameters<pre>
   * -classnames: the name of the test suite class
   * -testfilename: the name of a file containing classnames of test suites
   * -test: the test method name (format classname testname) 
   * -host: the host to connect to default local host 
   * -port: the port to connect to, mandatory argument 
   * -keepalive: keep the process alive after a test run
   * </pre>
   */
  public static void main(String[] args) {
    try {
      BenchRunner testRunServer = new BenchRunner();
      testRunServer.init(args);
      
      System.out.println("RUN");
      
    } catch (Throwable e) {
      e.printStackTrace(); // don't allow System.exit(0) to swallow exceptions
    } finally {
      // fix for 14434
      System.exit(0);
    }
  }
}
