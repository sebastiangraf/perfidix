package org.perfidix.perclipse.runner;

import java.util.Vector;

public class BenchRunner {

  /** The name of the classes to be benched. */
  private String[] classNames;

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
      }


      if (classNames == null || classNames.length == 0){
        throw new IllegalArgumentException("BenchRunner.error.classnamemissing");
      }

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
