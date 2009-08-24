package org.perfidix.Perclipse.buildpath;

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
import org.perfidix.Perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for adding the proposal for adding our perfidix jar
 * (perfidix library) to the classpath.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerfidixAddLibraryProposal implements IJavaCompletionProposal {

    private final IInvocationContext context;
    private final boolean isPerfidix;
    private final int relevance;

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
            boolean isPerfidix, IInvocationContext context, int relevance) {
        this.isPerfidix = isPerfidix;
        this.context = context;
        this.relevance = relevance;
    }

    /** {@inheritDoc} */
    public int getRelevance() {
        // TODO Auto-generated method stub
        return relevance;
    }

    /**
     * This method inserts our proposal - jar library - into classpath.
     * 
     * @param document
     *            see super method.
     * @see org.eclipse.jface.text.contentassist.ICompletionProposal#apply(org.eclipse.jface.text.IDocument)
     */
    public void apply(IDocument document) {
        IJavaProject project = context.getCompilationUnit().getJavaProject();
        Shell shell =
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
            int offset = context.getSelectionOffset();
            int length = context.getSelectionLength();
            String str;
            str = document.get(offset, length);

            document.replace(offset, length, str);

        } catch (BadLocationException e) {
            PerclipseActivator.log(e);
            e.printStackTrace();
        } catch (JavaModelException e) {
            PerclipseActivator.log(e);
            e.printStackTrace();
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
            Shell shell, final IJavaProject project, IClasspathEntry entry,
            IRunnableContext context) throws JavaModelException {

        IClasspathEntry[] oldEntries = project.getRawClasspath();
        ArrayList<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>(oldEntries.length + 1);
        boolean added = false;
        for (int i = 0; i < oldEntries.length; i++) {
            IClasspathEntry current = oldEntries[i];
            if (current.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
                IPath path = current.getPath();

                if (path.equals(entry.getPath())) {
                    System.out.println("true");
                    return true;
                } else if (path.matchingFirstSegments(entry.getPath()) > 0) {
                    if (!added) {
                        current = entry;
                    } else {
                        current = null;
                    }
                }

            } else if (current.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
                IPath path = current.getPath();
                if (path.segmentCount() > 0
                        && PerclipseActivator.PERFIDIX_HOME.equals(path
                                .segment(0))) {
                    if (!added) {
                        current = entry;
                    } else {
                        current = null;
                    }
                }
            }
            if (current != null) {
                newEntries.add(current);
            }

        }
        if (!added) {
            newEntries.add(entry);
        }
        final IClasspathEntry[] newCPEntries =
                (IClasspathEntry[]) newEntries
                        .toArray(new IClasspathEntry[newEntries.size()]);

        try {
            context.run(true, false, new IRunnableWithProgress() {
                public void run(IProgressMonitor monitor) {
                    try {
                        project.setRawClasspath(newCPEntries, monitor);
                    } catch (JavaModelException e) {
                        PerclipseActivator.log(e);
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } catch (InvocationTargetException e) {
            PerclipseActivator.log(e);
            Throwable t = e.getTargetException();
            if (t instanceof CoreException) {
                ErrorDialog.openError(
                        shell, "Add Perfidix library to the build path",
                        "Cannot Add", ((CoreException) t).getStatus());
            }
            return false;
        } catch (InterruptedException e) {
            PerclipseActivator.log(e);
            return false;
        }

    }

    /**
     * This method provides additional information for the proposal in a small
     * window.
     * 
     * @return The additional information for the proposal.
     * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getAdditionalProposalInfo()
     */
    public String getAdditionalProposalInfo() {

        if (isPerfidix) {
            return "Adds the Perfidix jar library to the build path. The jar was delivered with the Perclipse Plugin: "
                    .concat(PerclipseActivator.PLUGIN_ID);
        }

        return null;
    }

    /** {@inheritDoc} */
    public IContextInformation getContextInformation() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * This method provides the label within the proposal.
     * 
     * @return The display String for the "add perfidix"-proposal.
     * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getDisplayString()
     */
    public String getDisplayString() {
        if (isPerfidix) {
            return "Add Perfidix library to the build path";
        }
        return null;
    }

    /** {@inheritDoc} */
    public Image getImage() {
        // TODO Auto-generated method stub
        return JavaUI
                .getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
    }

    /** {@inheritDoc} */
    public Point getSelection(IDocument arg0) {
        // TODO Auto-generated method stub
        return new Point(context.getSelectionOffset(), context
                .getSelectionLength());
    }

}
