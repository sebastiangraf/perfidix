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
