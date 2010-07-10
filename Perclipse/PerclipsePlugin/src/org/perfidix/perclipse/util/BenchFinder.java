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
package org.perfidix.perclipse.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * The class BenchFinder with its included Annotation class is responisble for
 * the finding of our Perfidix annotations. It searches the given sources
 * depending on their type for existing Perfidix annotations.
 * 
 * @author Graf S., Lewandowski L., DiSy, University of Konstanz
 */
public final class BenchFinder {
    
    private static transient boolean subpackages=true;

    /**
     * The constructor.
     */
    private BenchFinder() {
    }

    /**
     * This method gets an object array with java elements, looks up its exact
     * type and delegate the find bench option to the responsible method.
     * 
     * @param elements
     *            An array of objects which eventually contain bench types.
     * @param result
     *            The result set.
     * @param pMonitor
     *            The progress monitor.
     */
    public static void findBenchsInContainer(
            final Object[] elements, final Set<IType> result,
            final IProgressMonitor pMonitor) {
        try {
            for (int i = 0; i < elements.length; i++) {
                final Object container =
                        BenchSearchEngine.computeScope(elements[i]);
                if (container instanceof IJavaProject) {
                    final IJavaProject project = (IJavaProject) container;
                    findBenchsInProject(project, result);
                } else if (container instanceof IPackageFragmentRoot) {
                    final IPackageFragmentRoot root =
                            (IPackageFragmentRoot) container;
                    findBenchsInPackageFragmentRoot(root, result);
                } else if (container instanceof IPackageFragment) {
                    final IPackageFragment fragment =
                            (IPackageFragment) container;
                    if(subpackages){
                        subpackages=false;
                        final List<IPackageFragment> theList= containSubPackages(fragment);
                        if(!theList.isEmpty()){
                            findBenchsInContainer(theList.toArray(), result, pMonitor);
                        }                        
                    }
                    findBenchsInPackageFragment(fragment, result);
                } else if (container instanceof ICompilationUnit) {
                    final ICompilationUnit comUnit =
                            (ICompilationUnit) container;
                    findBenchsInCompilationUnit(comUnit, result);
                } else if (container instanceof IType) {
                    final IType type = (IType) container;
                    findBenchsInType(type, result);
                }
            }
            subpackages=true;
        } catch (JavaModelException e) {
            PerclipseActivator.log(e);
        }
    }

    /**
     * This method expect a java project source and checks for packages and
     * calls afterwards the package responsible method for the find bench stuff.
     * 
     * @param project
     * @param result
     * @throws JavaModelException
     */
    private static void findBenchsInProject(
            final IJavaProject project, final Set<IType> result)
            throws JavaModelException {
        final IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
        for (int i = 0; i < roots.length; i++) {
            final IPackageFragmentRoot root = roots[i];
            findBenchsInPackageFragmentRoot(root, result);
        }
    }

    /**
     * This method is called when a package root was found and checks the
     * children for further sources for the next find bench delegates.
     * 
     * @param root
     * @param result
     * @throws JavaModelException
     */
    private static void findBenchsInPackageFragmentRoot(
            final IPackageFragmentRoot root, final Set<IType> result)
            throws JavaModelException {
        final IJavaElement[] children = root.getChildren();
        for (int j = 0; j < children.length; j++) {
            final IPackageFragment fragment = (IPackageFragment) children[j];
            findBenchsInPackageFragment(fragment, result);
        }
    }

    /**
     * This method is called when the package root has children that could
     * contain benchs.
     * 
     * @param fragment
     * @param result
     * @throws JavaModelException
     */
    private static void findBenchsInPackageFragment(
            final IPackageFragment fragment, final Set<IType> result)
            throws JavaModelException {
        final ICompilationUnit[] compilationUnits =
                fragment.getCompilationUnits();
        for (int k = 0; k < compilationUnits.length; k++) {
            final ICompilationUnit unit = compilationUnits[k];
            findBenchsInCompilationUnit(unit, result);
        }
    }

    /**
     * This method is called when a compilation unit was found and delegates
     * each unit type to the next method.
     * 
     * @param unit
     * @param result
     * @throws JavaModelException
     */
    private static void findBenchsInCompilationUnit(
            final ICompilationUnit unit, final Set<IType> result)
            throws JavaModelException {
        final IType[] types = unit.getAllTypes();
        for (int l = 0; l < types.length; l++) {
            final IType type = types[l];
            findBenchsInType(type, result);
        }
    }

    /**
     * This method gets the compilation unit type and sends a request to the
     * isBench method to evaluate if a bench is contained in that source.
     * Afterwards the result set adds the successful found bench type.
     * 
     * @param type
     * @param result
     * @throws JavaModelException
     */
    private static void findBenchsInType(
            final IType type, final Set<IType> result)
            throws JavaModelException {
        if (isBench(type)) {
            result.add(type);

        }
    }

    /**
     * This method evaluates if the given type contains a bench annotation and
     * return afterwards the result as a boolean value.
     * 
     * @param type
     * @return
     * @throws JavaModelException
     */
    private static boolean isBench(final IType type) throws JavaModelException {
        boolean returnIsBench = false;
        if (!Flags.isAbstract(type.getFlags())
                && (Annotation.BENCH.annotatesAtLeastOneMethod(type)
                        || Annotation.BENCH_CLASS.annotatesClass(type) || Annotation.BENCH_CONFIG
                        .annotatesClass(type))) {
            returnIsBench = true;
        }
        return returnIsBench;
    }

    /**
     * This class contains the two possible Bench Annotation types Bench and
     * BenchClass.
     * 
     * @author lewandow
     */
    private static final class Annotation {

        public static final BenchFinder.Annotation BENCH =
                new BenchFinder.Annotation(new String[] {
                        "Bench", "org.perfidix.Bench" }); //$NON-NLS-1$ //$NON-NLS-2$

        public static final BenchFinder.Annotation BENCH_CLASS =
                new BenchFinder.Annotation(new String[] {
                        "BenchClass", "org.perfidix.BenchClass" });

        public static final BenchFinder.Annotation BENCH_CONFIG =
                new BenchFinder.Annotation(new String[] {
                        "BenchmarkConfig", "org.perfidix.BenchmarkConfig" });

        private final transient String[] names;

        /**
         * The constructor. It sets the names array.
         * 
         * @param names
         */
        private Annotation(final String[] names) {
            this.names = names.clone();
        }

        /**
         * The foundIn method gets a String as a source for the Perfidix
         * annotation search.
         * 
         * @param source
         * @return
         */
        public boolean foundIn(final String source) {
            boolean returnValue = false;
            final IScanner scanner =
                    ToolFactory.createScanner(false, true, false, false);
            scanner.setSource(source.toCharArray());
            try {
                int tok;
                do {
                    tok = scanner.getNextToken();
                    if (tok == ITerminalSymbols.TokenNameAT) {
                        final StringBuffer stringBuffer = new StringBuffer("");
                        tok = scanner.getNextToken();
                        while (tok == ITerminalSymbols.TokenNameIdentifier
                                || tok == ITerminalSymbols.TokenNameDOT) {
                            stringBuffer.append(String.valueOf(scanner
                                    .getCurrentTokenSource()));

                            tok = scanner.getNextToken();
                        }
                        for (int i = 0; i < names.length; i++) {
                            final String annotation = names[i];
                            if (stringBuffer.toString().equals(annotation)) {
                                returnValue = true;
                            }
                        }
                    }
                } while (tok != ITerminalSymbols.TokenNameEOF);
            } catch (InvalidInputException e) {
                PerclipseActivator.log(e);
            }
            return returnValue;
        }

        /**
         * This method gets a source and defines the search range for the
         * foundIn method.
         * 
         * @param member
         * @return
         * @throws JavaModelException
         */
        private boolean annotates(final IMember member)
                throws JavaModelException {
            final ISourceRange sourceRange = member.getSourceRange();
            final ISourceRange nameRange = member.getNameRange();
            final int charsToSearch =
                    nameRange.getOffset() - sourceRange.getOffset();
            final String source =
                    member.getSource().substring(0, charsToSearch);
            return foundIn(source);
        }

        /**
         * This method is called to check if class type has annotations.
         * 
         * @param type
         * @return
         * @throws JavaModelException
         */
        private boolean annotatesClass(final IType type)
                throws JavaModelException {
            return annotates(type);
        }

        /**
         * This method gets a type and searches within the type for contained
         * methods and calls annotates if it will find anyone.
         * 
         * @param type
         * @return
         * @throws JavaModelException
         */
        private boolean annotatesAtLeastOneMethod(final IType type)
                throws JavaModelException {
            boolean retValue = false;
            final IMethod[] methods = type.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (annotates(methods[i])) {
                    retValue = true;
                }
            }
            return retValue;
        }
        
    }
    /**
     * This method checks if the given packages has sub packages and adds them to a package list.
     * 
     * @param packageFragment The package that may contain sub packages.
     * @return The list with all sub packages containing the head package.
     * @throws JavaModelException The thrown exception.
     */
    private static List<IPackageFragment> containSubPackages(final IPackageFragment packageFragment) throws JavaModelException{
        
        final List<IPackageFragment> subPackages = new ArrayList<IPackageFragment>();
        final IJavaElement[] allPackages = ((IPackageFragmentRoot) packageFragment
            .getParent()).getChildren();
        for (IJavaElement javaElement : allPackages) {
          final IPackageFragment pakage = (IPackageFragment) javaElement;
          final String startPackagenName = packageFragment.getElementName() + "."; //$NON-NLS-1$
          if (packageFragment.isDefaultPackage()
              || pakage.getElementName().startsWith(startPackagenName)) {
            subPackages.add(pakage);
          }
        }
        return subPackages;
    }

}
