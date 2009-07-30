package org.perfidix.Perclipse.util;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClassFile;
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
import org.eclipse.jdt.debug.core.IJavaClassType;

/**
 * The class BenchFinder with its included Annotation class is responisble for
 * the finding of our Perfidix annotations. It searches the given sources
 * depending on their type for existing Perfidix annotations.
 * 
 * @author Graf S.
 */
public final class BenchFinder {

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
     * @param result
     * @param pm
     */
    public static void findBenchsInContainer(
            Object[] elements, Set<IType> result, IProgressMonitor pm) {
        try {
            for (int i = 0; i < elements.length; i++) {
                Object container = BenchSearchEngine.computeScope(elements[i]);
                if (container instanceof IJavaProject) {
                    IJavaProject project = (IJavaProject) container;
                    findBenchsInProject(project, result);
                } else if (container instanceof IPackageFragmentRoot) {
                    IPackageFragmentRoot root =
                            (IPackageFragmentRoot) container;
                    findBenchsInPackageFragmentRoot(root, result);
                } else if (container instanceof IPackageFragment) {
                    IPackageFragment fragment = (IPackageFragment) container;
                    findBenchsInPackageFragment(fragment, result);
                } else if (container instanceof ICompilationUnit) {
                    ICompilationUnit cu = (ICompilationUnit) container;
                    findBenchsInCompilationUnit(cu, result);
                } else if (container instanceof IType) {
                    IType type = (IType) container;
                    findBenchsInType(type, result);
                }
            }
        } catch (JavaModelException e) {
            // do nothing
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
            IJavaProject project, Set<IType> result) throws JavaModelException {
        IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
        for (int i = 0; i < roots.length; i++) {
            IPackageFragmentRoot root = roots[i];
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
            IPackageFragmentRoot root, Set<IType> result)
            throws JavaModelException {
        IJavaElement[] children = root.getChildren();
        for (int j = 0; j < children.length; j++) {
            IPackageFragment fragment = (IPackageFragment) children[j];
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
            IPackageFragment fragment, Set<IType> result)
            throws JavaModelException {
        ICompilationUnit[] compilationUnits = fragment.getCompilationUnits();
        for (int k = 0; k < compilationUnits.length; k++) {
            ICompilationUnit unit = compilationUnits[k];
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
            ICompilationUnit unit, Set<IType> result) throws JavaModelException {
        IType[] types = unit.getAllTypes();
        for (int l = 0; l < types.length; l++) {
            IType type = types[l];
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
    private static void findBenchsInType(IType type, Set<IType> result)
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
    private static boolean isBench(IType type) throws JavaModelException {
        if (!Flags.isAbstract(type.getFlags())
                && (Annotation.Bench.annotatesAtLeastOneMethod(type) || Annotation.BenchClass
                        .annotatesClass(type))) {
            return true;
        }
        return false;
    }

    /**
     * This class contains the two possible Bench Annotation types Bench and
     * BenchClass.
     * 
     * @author lewandow
     */
    private static class Annotation {

        private static final BenchFinder.Annotation Bench =
                new BenchFinder.Annotation(new String[] {
                        "Bench", "org.perfidix.Bench" }); //$NON-NLS-1$ //$NON-NLS-2$

        private static final BenchFinder.Annotation BenchClass =
                new BenchFinder.Annotation(new String[] {
                        "BenchClass", "org.perfidix.BenchClass" });

        private final String[] names;

        /**
         * The constructor. It sets the names array.
         * 
         * @param names
         */
        private Annotation(String[] names) {
            this.names = names;
        }

        /**
         * The foundIn method gets a String as a source for the Perfidix
         * annotation search.
         * 
         * @param source
         * @return
         */
        public boolean foundIn(String source) {
            IScanner scanner =
                    ToolFactory.createScanner(false, true, false, false);
            scanner.setSource(source.toCharArray());
            try {
                int tok;
                do {
                    tok = scanner.getNextToken();
                    if (tok == ITerminalSymbols.TokenNameAT) {
                        String annotationName = ""; //$NON-NLS-1$
                        tok = scanner.getNextToken();
                        while (tok == ITerminalSymbols.TokenNameIdentifier
                                || tok == ITerminalSymbols.TokenNameDOT) {
                            annotationName +=
                                    String.valueOf(scanner
                                            .getCurrentTokenSource());
                            tok = scanner.getNextToken();
                        }
                        for (int i = 0; i < names.length; i++) {
                            String annotation = names[i];
                            if (annotationName.equals(annotation)) {
                                return true;
                            }
                        }
                    }
                } while (tok != ITerminalSymbols.TokenNameEOF);
            } catch (InvalidInputException e) {
                return false;
            }
            return false;
        }

        /**
         * This method gets a source and defines the search range for the
         * foundIn method.
         * 
         * @param member
         * @return
         * @throws JavaModelException
         */
        boolean annotates(IMember member) throws JavaModelException {
            ISourceRange sourceRange = member.getSourceRange();
            ISourceRange nameRange = member.getNameRange();
            int charsToSearch = nameRange.getOffset() - sourceRange.getOffset();
            String source = member.getSource().substring(0, charsToSearch);
            return foundIn(source);
        }

        /**
         * This method is called to check if class type has annotations.
         * 
         * @param type
         * @return
         * @throws JavaModelException
         */
        boolean annotatesClass(IType type) throws JavaModelException {
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
        boolean annotatesAtLeastOneMethod(IType type) throws JavaModelException {
            IMethod[] methods = type.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (annotates(methods[i]))
                    return true;
            }
            return false;
        }
    }

}
