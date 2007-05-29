package org.perfidix.perclipse.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IFileEditorInput;
public class BenchSearchEngine {

	public static IType[] findBenchs(IRunnableContext context, final Object[] elements) throws InvocationTargetException, InterruptedException {
		final Set result= new HashSet();

		if (elements.length > 0) {
			IRunnableWithProgress runnable= new IRunnableWithProgress() {
				public void run(IProgressMonitor pm) throws InterruptedException {
					BenchFinder.findBenchsInContainer(elements, result, pm);
				}
			};
			context.run(true, true, runnable);
		}
		return (IType[]) result.toArray(new IType[result.size()]);
	}
	
	public static Object computeScope(Object element) throws JavaModelException {
		if (element instanceof IFileEditorInput)
			element = ((IFileEditorInput) element).getFile();
		if (element instanceof IResource)
			element = JavaCore.create((IResource) element);
		if (element instanceof IClassFile) {
			IClassFile cf = (IClassFile) element;
			element = cf.getType();
		}
		return element;
	}

	public static IType[] findBenchs(final Object[] elements)
			throws InvocationTargetException, InterruptedException {
		final Set<IType> result = new HashSet<IType>();

		if (elements.length > 0) {
			BenchFinder.findBenchsInContainer(elements, result, null);
		}
		return (IType[]) result.toArray(new IType[result.size()]);
	}

	public static boolean hasBenchType(IJavaProject javaProject) {
		return benchType(javaProject) != null;
	}
	
	private static IType benchType(IJavaProject javaProject) {
		try {
			return javaProject.findType("org.perfidix.Bench"); //$NON-NLS-1$
		} catch (JavaModelException e) {
			return null;
		}
	}
	
}
