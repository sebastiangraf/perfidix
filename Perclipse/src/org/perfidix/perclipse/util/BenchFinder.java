package org.perfidix.perclipse.util;

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

public final class BenchFinder {

	private BenchFinder(){}

	public static void findBenchsInContainer(Object[] elements, Set<IType> result, IProgressMonitor pm) {
		try {
			for (int i= 0; i < elements.length; i++) {
				Object container= BenchSearchEngine.computeScope(elements[i]);
				if (container instanceof IJavaProject) {
					IJavaProject project= (IJavaProject) container;
					findBenchsInProject(project, result);
				} else if (container instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot root= (IPackageFragmentRoot) container;
					findBenchsInPackageFragmentRoot(root, result);
				} else if (container instanceof IPackageFragment) {
					IPackageFragment fragment= (IPackageFragment) container;
					findBenchsInPackageFragment(fragment, result);
				} else if (container instanceof ICompilationUnit) {
					ICompilationUnit cu= (ICompilationUnit) container;
					findBenchsInCompilationUnit(cu, result);
				} else if (container instanceof IType) {
					IType type= (IType) container;
					findBenchsInType(type, result);
				}
			}			
		} catch (JavaModelException e) {
			// do nothing
		}
	}

	private static void findBenchsInProject(IJavaProject project, Set<IType> result) throws JavaModelException {
		IPackageFragmentRoot[] roots= project.getPackageFragmentRoots();
		for (int i= 0; i < roots.length; i++) {
			IPackageFragmentRoot root= roots[i];
			findBenchsInPackageFragmentRoot(root, result);
		}
	}

	private static void findBenchsInPackageFragmentRoot(IPackageFragmentRoot root, Set<IType> result) throws JavaModelException {
		IJavaElement[] children= root.getChildren();
		for (int j= 0; j < children.length; j++) {
			IPackageFragment fragment= (IPackageFragment) children[j];
			findBenchsInPackageFragment(fragment, result);
		}
	}

	private static void findBenchsInPackageFragment(IPackageFragment fragment, Set<IType> result) throws JavaModelException {
		ICompilationUnit[] compilationUnits= fragment.getCompilationUnits();
		for (int k= 0; k < compilationUnits.length; k++) {
			ICompilationUnit unit= compilationUnits[k];
			findBenchsInCompilationUnit(unit, result);
		}
	}

	private static void findBenchsInCompilationUnit(ICompilationUnit unit, Set<IType> result) throws JavaModelException {
		IType[] types= unit.getAllTypes();
		for (int l= 0; l < types.length; l++) {
			IType type= types[l];
			findBenchsInType(type, result);
		}
	}

	private static void findBenchsInType(IType type, Set<IType> result) throws JavaModelException {
		if (isBench(type))
			result.add(type);
	}

	private static boolean isBench(IType type) throws JavaModelException {
		if (!Flags.isAbstract(type.getFlags()) && Annotation.Bench.annotatesAtLeastOneMethod(type)) {
			return true;
		}
		return false;
	}
	
	private static class Annotation {

		private static final BenchFinder.Annotation Bench= new BenchFinder.Annotation(new String[] { "Bench", "org.perfidix.Bench" }); //$NON-NLS-1$ //$NON-NLS-2$

		private final String[] names;

		private Annotation(String[] names) {
			this.names= names;
		}

		public boolean foundIn(String source) {
			IScanner scanner= ToolFactory.createScanner(false, true, false, false);
			scanner.setSource(source.toCharArray());
			try {
				int tok;
				do {
					tok= scanner.getNextToken();
					if (tok == ITerminalSymbols.TokenNameAT) {
						String annotationName= ""; //$NON-NLS-1$
						tok= scanner.getNextToken();
						while (tok == ITerminalSymbols.TokenNameIdentifier || tok == ITerminalSymbols.TokenNameDOT) {
							annotationName+= String.valueOf(scanner.getCurrentTokenSource());
							tok= scanner.getNextToken();
						}
						for (int i= 0; i < names.length; i++) {
							String annotation= names[i];
							if (annotationName.equals(annotation))
								return true;
						}
					}
				} while (tok != ITerminalSymbols.TokenNameEOF);
			} catch (InvalidInputException e) {
				return false;
			}
			return false;
		}

		boolean annotates(IMember member) throws JavaModelException {
			ISourceRange sourceRange= member.getSourceRange();
			ISourceRange nameRange= member.getNameRange();
			int charsToSearch= nameRange.getOffset() - sourceRange.getOffset();
			String source= member.getSource().substring(0, charsToSearch);
			return foundIn(source);
		}

		boolean annotatesAtLeastOneMethod(IType type) throws JavaModelException {
			IMethod[] methods= type.getMethods();
			for (int i= 0; i < methods.length; i++) {
				if (annotates(methods[i]))
					return true;
			}
			return false;
		}
	}
	
}
