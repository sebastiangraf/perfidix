package org.perfidix.perclipse.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IFileEditorInput;

public class BenchSearchEngine {

	
	
	public static Object computeScope(Object element) throws JavaModelException {
		if (element instanceof IFileEditorInput)
			element= ((IFileEditorInput) element).getFile();
		if (element instanceof IResource)
			element= JavaCore.create((IResource) element);
		if (element instanceof IClassFile) {
			IClassFile cf= (IClassFile) element;
			element= cf.getType();
		}
		return element;
	}
	
	public static IType[] findBenchs(final Object[] elements) throws InvocationTargetException, InterruptedException {
		final Set result= new HashSet();

//		if (elements.length > 0) {
//			IRunnableWithProgress runnable= new IRunnableWithProgress() {
//				public void run(IProgressMonitor pm) throws InterruptedException {
//					ArrayList/*<TestKind>*/ kinds= TestKindRegistry.getDefault().getAllKinds();
//					int kindCount= kinds.size();
//					pm.beginTask("", kindCount); //$NON-NLS-1$
//					for (int i= 0; i < kindCount; i++) {
//						TestKind kind= (TestKind) kinds.get(i);
//						kind.createFinder().findTestsInContainer(elements, result, new SubProgressMonitor(pm, 1));
//						if (! result.isEmpty()) {
//							break;
//						}
//					}
//					pm.done();
//				}
//			};
//			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
//		}
		return (IType[]) result.toArray(new IType[result.size()]);
	}
	
}
