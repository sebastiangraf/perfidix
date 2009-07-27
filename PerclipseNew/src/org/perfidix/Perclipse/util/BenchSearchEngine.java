package org.perfidix.Perclipse.util;

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

/**
 * This class is responsible for the bench search options of our Perfidix
 * plugin. It uses the class {@link BenchFinder} to find the in the source
 * contained benchs.
 * 
 * @author lewandow
 */
public class BenchSearchEngine {

    /**
     * This method expects a runnable context and an object array to find Benchs
     * in the given source.
     * 
     * @param context
     * @param elements
     * @return
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    public static IType[] findBenchs(
            IRunnableContext context, final Object[] elements)
            throws InvocationTargetException, InterruptedException {
        final Set<IType> result = new HashSet<IType>();

        if (elements.length > 0) {
            IRunnableWithProgress runnable = new IRunnableWithProgress() {
                public void run(IProgressMonitor pm)
                        throws InterruptedException {
                    BenchFinder.findBenchsInContainer(elements, result, pm);
                }
            };
            context.run(true, true, runnable);
        }
        return (IType[]) result.toArray(new IType[result.size()]);
    }

    /**
     * This method checks the object element for its type and returns if
     * afterwards.
     * 
     * @param element
     * @return
     * @throws JavaModelException
     */
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

    /**
     * This method gets an array of java elements and delegates the bench search
     * to the class {@link BenchFinder}.
     * 
     * @param elements
     * @return an IType array
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    public static IType[] findBenchs(final Object[] elements)
            throws InvocationTargetException, InterruptedException {
        final Set<IType> result = new HashSet<IType>();

        if (elements.length > 0) {
            BenchFinder.findBenchsInContainer(elements, result, null);
        }
        return (IType[]) result.toArray(new IType[result.size()]);
    }

    /**
     * This method gets a java project and evaluates if it has a bench type or
     * not.
     * 
     * @param javaProject
     * @return
     */
    public static boolean hasBenchType(IJavaProject javaProject) {
        return benchType(javaProject) != null;
    }

    /**
     * This method return the first type found with the given fully qualified
     * name. In our case a type with a bench.
     * 
     * @param javaProject
     * @return
     */
    private static IType benchType(IJavaProject javaProject) {
        try {
            return javaProject.findType("org.perfidix.Benchmark"); //$NON-NLS-1$
        } catch (JavaModelException e) {
            return null;
        }
    }

}
