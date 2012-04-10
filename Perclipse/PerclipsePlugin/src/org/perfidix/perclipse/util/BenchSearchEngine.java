/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for the bench search options of our Perfidix
 * plugin. It uses the class {@link BenchFinder} to find the in the source
 * contained benchs.
 * 
 * @author Graf S.
 */
public final class BenchSearchEngine {

    /**
     * The constructor.
     */
    private BenchSearchEngine() {
    }

    /**
     * This method expects a runnable context and an object array to find Benchs
     * in the given source.
     * 
     * @param context
     *            The runnable context.
     * @param elements
     *            The array of elements.
     * @return A type array.
     * @throws InvocationTargetException
     *             The upcoming exception.
     * @throws InterruptedException
     *             The upcoming exceptions.
     */
    public static IType[] findBenchs(
            final IRunnableContext context, final Object[] elements)
            throws InvocationTargetException, InterruptedException {
        final Set<IType> result = new HashSet<IType>();

        if (elements.length > 0) {
            final IRunnableWithProgress runnable = new IRunnableWithProgress() {
                public void run(final IProgressMonitor progMonitor)
                        throws InterruptedException {
                    BenchFinder.findBenchsInContainer(
                            elements, result, progMonitor);
                }
            };
            context.run(true, true, runnable);
        }
        return result.toArray(new IType[result.size()]);
    }

    /**
     * This method checks the object element for its type and returns if
     * afterwards.
     * 
     * @param element
     *            The element.
     * @return The object.
     * @throws JavaModelException
     *             The upcoming exception.
     */
    public static Object computeScope(final Object element)
            throws JavaModelException {
        Object returnElement = element;
        if (element instanceof IFileEditorInput) {
            returnElement = ((IFileEditorInput) element).getFile();
        }
        if (element instanceof IResource) {
            returnElement = JavaCore.create((IResource) element);
        }
        if (element instanceof IClassFile) {
            final IClassFile clazzFile = (IClassFile) element;
            returnElement = clazzFile.getType();

        }
        return returnElement;
    }

    /**
     * This method gets an array of java elements and delegates the bench search
     * to the class {@link BenchFinder}.
     * 
     * @param elements
     *            The object array of elements.
     * @return an IType array.
     * @throws InvocationTargetException
     *             The Exception which occurred.
     * @throws InterruptedException
     *             The Exception which occurred.
     */
    public static IType[] findBenchs(final Object[] elements)
            throws InvocationTargetException, InterruptedException {
        final Set<IType> result = new HashSet<IType>();

        if (elements.length > 0) {
            BenchFinder.findBenchsInContainer(elements, result, null);
        }
        return result.toArray(new IType[result.size()]);
    }

    /**
     * This method gets a java project and evaluates if it has a bench type or
     * not.
     * 
     * @param javaProject
     *            The java project for checking.
     * @return A boolean value if the java project contains benches.
     */
    public static boolean hasBenchType(final IJavaProject javaProject) {
        return benchType(javaProject) != null;
    }

    /**
     * This method return the first type found with the given fully qualified
     * name. In our case a type with a bench.
     * 
     * @param javaProject
     *            The java project for testing for benchs.
     * @return The type.
     */
    private static IType benchType(final IJavaProject javaProject) {
        IType returnBenchType = null;
        try {
            returnBenchType = javaProject.findType("org.perfidix.Benchmark");
        } catch (JavaModelException e) {
            PerclipseActivator.log(e);
        }
        return returnBenchType;
    }

}
