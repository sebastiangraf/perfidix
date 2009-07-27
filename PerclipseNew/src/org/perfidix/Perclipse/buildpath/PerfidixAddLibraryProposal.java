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

public class PerfidixAddLibraryProposal implements IJavaCompletionProposal {

    private final IInvocationContext context;
    private final boolean isPerfidix;
    private final int relevance;

    public PerfidixAddLibraryProposal(
            boolean isPerfidix, IInvocationContext context, int relevance) {
        this.isPerfidix = isPerfidix;
        this.context = context;
        this.relevance = relevance;
    }

    public int getRelevance() {
        // TODO Auto-generated method stub
        return relevance;
    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static boolean addToClasspath(
            Shell shell, final IJavaProject project, IClasspathEntry entry,
            IRunnableContext context) throws JavaModelException {

        IClasspathEntry[] oldEntries = project.getRawClasspath();
        ArrayList newEntries = new ArrayList(oldEntries.length + 1);
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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof CoreException) {
                ErrorDialog.openError(
                        shell, "Add Perfidix library to the build path",
                        "Cannot Add", ((CoreException) t).getStatus());
            }
            return false;
        } catch (InterruptedException e) {
            return false;
        }

    }

    public String getAdditionalProposalInfo() {

        if (isPerfidix) {
            return "Adds the Perfidix jar library to the build path";
        }

        return null;
    }

    public IContextInformation getContextInformation() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDisplayString() {
        if (isPerfidix) {
            return "Add Perfidix library to the build path";
        }
        return null;
    }

    public Image getImage() {
        // TODO Auto-generated method stub
        return JavaUI
                .getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
    }

    public Point getSelection(IDocument arg0) {
        // TODO Auto-generated method stub
        return new Point(context.getSelectionOffset(), context
                .getSelectionLength());
    }

}
