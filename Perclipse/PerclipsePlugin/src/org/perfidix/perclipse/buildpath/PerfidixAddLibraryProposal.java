/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
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
package org.perfidix.perclipse.buildpath;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for adding the proposal for adding our perfidix jar
 * (perfidix library) to the classpath.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
@SuppressWarnings("restriction")
public class PerfidixAddLibraryProposal implements IJavaCompletionProposal {

    private final transient IInvocationContext context;
    private final transient boolean isPerfidix;
    private final transient int relevance;

    /**
     * The constructor sets the proposal arguments isPerfidix, the adding
     * context and the relevance (ranking within proposal).
     * 
     * @param isPerfidix
     *            The param that says if it is our Perfidix proposal.
     * @param context
     *            The context for our proposal.
     * @param relevance
     *            The {@link Integer} relevance value for the ranking of our
     *            proposal (on top).
     */
    public PerfidixAddLibraryProposal(
            final boolean isPerfidix, final IInvocationContext context,
            final int relevance) {
        this.isPerfidix = isPerfidix;
        this.context = context;
        this.relevance = relevance;
    }

    /** {@inheritDoc} */
    public int getRelevance() {
        return relevance;
    }

    /**
     * This method inserts our proposal - jar library - into classpath.
     * 
     * @param document
     *            see super method.
     * @see org.eclipse.jface.text.contentassist.ICompletionProposal#apply(org.eclipse.jface.text.IDocument)
     */
    public void apply(final IDocument document) {
        final IJavaProject project =
                context.getCompilationUnit().getJavaProject();
        final Shell shell =
                PerclipseActivator
                        .getActivePage().getWorkbenchWindow().getShell();
        try {
            IClasspathEntry entry = null;
            if (isPerfidix) {
                entry = BuildPathSupport.getPerfidixClasspathEntry();
            }
            if (entry != null) {
                addToClasspath(
                        shell, project, entry,
                        new BusyIndicatorRunnableContext());
            }
            final int offset = context.getSelectionOffset();
            final int length = context.getSelectionLength();
            String str;
            str = document.get(offset, length);

            document.replace(offset, length, str);

        } catch (BadLocationException e) {
            PerclipseActivator.log(e);
        } catch (JavaModelException e) {
            PerclipseActivator.log(e);
        }

    }

    /**
     * This method inserts our jar library into the corresponding classpath.
     * 
     * @param shell
     *            The corresponding shell of the plug-in.
     * @param project
     *            The project where the proposal should work.
     * @param entry
     *            The classpath entry of our library.
     * @param context
     *            The context of the selection.
     * @return The result of this process, if the library has been successful
     *         added.
     * @throws JavaModelException
     */
    private static boolean addToClasspath(
            final Shell shell, final IJavaProject project,
            final IClasspathEntry entry, final IRunnableContext context)
            throws JavaModelException {
        boolean returnValue = false;
        boolean goAhead = true;
        final IClasspathEntry[] oldEntries = project.getRawClasspath();
        final ArrayList<IClasspathEntry> newEntries =
                new ArrayList<IClasspathEntry>(oldEntries.length + 1);
        final boolean added = false;
        for (int i = 0; i < oldEntries.length; i++) {
            IClasspathEntry current = oldEntries[i];
            if (current.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
                final IPath path = current.getPath();

                if (path.equals(entry.getPath())) {
                    returnValue = true;
                    goAhead = false;
                } else if (path.matchingFirstSegments(entry.getPath()) > 0) {
                    if (added) {
                        final IClasspathEntry newEntry = null;
                        current = newEntry;
                    } else {
                        current = entry;
                    }
                }

            } else if (current.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
                final IPath path = current.getPath();
                if (path.segmentCount() > 0
                        && PerclipseActivator.PERFIDIX_HOME.equals(path
                                .segment(0))) {
                    if (added) {
                        final IClasspathEntry newEntry = null;
                        current = newEntry;
                    } else {
                        current = entry;
                    }
                }
            }
            if (current != null && goAhead) {
                newEntries.add(current);
            }

        }
        if (goAhead) {
            if (!added) {
                newEntries.add(entry);
            }
            final IClasspathEntry[] newCPEntries =
                    newEntries.toArray(new IClasspathEntry[newEntries.size()]);

            try {
                context.run(true, false, new IRunnableWithProgress() {
                    public void run(final IProgressMonitor monitor) {
                        try {
                            project.setRawClasspath(newCPEntries, monitor);
                        } catch (JavaModelException e) {
                            PerclipseActivator.log(e);
                        }
                    }
                });
                returnValue = true;
            } catch (InvocationTargetException e) {
                PerclipseActivator.log(e);
                final Throwable thowi = e.getTargetException();
                if (thowi instanceof CoreException) {
                    ErrorDialog.openError(
                            shell, "Add Perfidix library to the build path",
                            "Cannot Add", ((CoreException) thowi).getStatus());
                }
            } catch (InterruptedException e) {
                PerclipseActivator.log(e);
            }
        }

        return returnValue;

    }

    /**
     * This method provides additional information for the proposal in a small
     * window.
     * 
     * @return The additional information for the proposal.
     * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getAdditionalProposalInfo()
     */
    public String getAdditionalProposalInfo() {
        String returnAddInfo = null;
        if (isPerfidix) {
            returnAddInfo =
                    "Adds the Perfidix jar library to the build path. The jar was delivered with the Perclipse Plugin: "
                            .concat(PerclipseActivator.PLUGIN_ID);
        }

        return returnAddInfo;
    }

    /** {@inheritDoc} */
    public IContextInformation getContextInformation() {
        return null;
    }

    /**
     * This method provides the label within the proposal.
     * 
     * @return The display String for the "add perfidix"-proposal.
     * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getDisplayString()
     */
    public String getDisplayString() {
        String returnDisplayText = null;
        if (isPerfidix) {
            returnDisplayText = "Add Perfidix library to the build path";
        }
        return returnDisplayText;
    }

    /** {@inheritDoc} */
    public Image getImage() {
        return JavaUI
                .getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
    }

    /** {@inheritDoc} */
    public Point getSelection(final IDocument arg0) {
        return new Point(context.getSelectionOffset(), context
                .getSelectionLength());
    }

}
