package org.perfidix.Perclipse.buildpath;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.perfidix.Perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for adding proposals to the QuickFix view within
 * eclipse.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class Quickfixprocessor implements IQuickFixProcessor {

    /**
     * This method provides the proposals for a given context and location.
     * 
     * @param context The invocation context.
     * @param locations The code location for the problem.
     * @throws CoreException The Exception occurred.
     * @see org.eclipse.jdt.ui.text.java.IQuickFixProcessor#getCorrections(org.eclipse.jdt.ui.text.java.IInvocationContext,
     *      org.eclipse.jdt.ui.text.java.IProblemLocation[])
     */
    public IJavaCompletionProposal[] getCorrections(
            IInvocationContext context, IProblemLocation[] locations)
            throws CoreException {

        ArrayList<PerfidixAddLibraryProposal> arraylist = null;
        for (IProblemLocation problemLocation : locations) {
            IProblemLocation problem = problemLocation;
            int id = problem.getProblemId();
            if (IProblem.UndefinedType == id || IProblem.ImportNotFound == id) {
                arraylist =
                        getPerfidixAddLibraryProposals(
                                context, problem, arraylist);
            }
        }
        if (arraylist == null || arraylist.isEmpty())
            return null;
        return (IJavaCompletionProposal[]) arraylist
                .toArray(new IJavaCompletionProposal[arraylist.size()]);
    }

    /**
     * Defines which problemId refers to our proposal.
     * 
     * @param unit The compilation unit.
     * @param problemId The problem id which occurred for quick fix.
     * @see org.eclipse.jdt.ui.text.java.IQuickFixProcessor#hasCorrections(org.eclipse.jdt.core.ICompilationUnit,
     *      int)
     */
    public boolean hasCorrections(ICompilationUnit unit, int problemId) {
        return problemId == IProblem.UndefinedType
                || problemId == IProblem.ImportNotFound;
    }

    /**
     * This method provides our proposals for the adding of our jar library.
     * 
     * @param context
     *            The context where the quick fix occurred.
     * @param problem
     *            The location of the coming problem.
     * @param arraylist
     *            The ArrayList of our proposals.
     * @return The ArrayList of our modified proposals.
     */
    private ArrayList<PerfidixAddLibraryProposal> getPerfidixAddLibraryProposals(
            IInvocationContext context, IProblemLocation problem,
            ArrayList<PerfidixAddLibraryProposal> arraylist) {

        ArrayList<String> allowedAnnotations = getInitAllowedAnnotation();

        ICompilationUnit unit = context.getCompilationUnit();
        int res = 0;
        String s;
        try {
            s =
                    unit.getBuffer().getText(
                            problem.getOffset(), problem.getLength());
            if (allowedAnnotations.contains(s)) {
                ASTNode node = problem.getCoveredNode(context.getASTRoot());
                if (node != null
                        && node.getLocationInParent() == MarkerAnnotation.TYPE_NAME_PROPERTY) {

                    IJavaProject javaProject = unit.getJavaProject();
                    if (arraylist == null) {
                        arraylist = new ArrayList<PerfidixAddLibraryProposal>();

                    }
                    arraylist.add(new PerfidixAddLibraryProposal(
                            true, context, 10));

                }

            }
        } catch (JavaModelException e) {
            PerclipseActivator.log(e);
            e.printStackTrace();
        }

        return arraylist;
    }

    /**
     * This method provides the allowed annotations to invoke our proposals for
     * the QuickFix processor.
     * 
     * @return Returns an ArrayList containing the allowed annotations for our
     *         proposal support.
     */
    private ArrayList<String> getInitAllowedAnnotation() {
        ArrayList<String> allowed = new ArrayList<String>();
        allowed.add("Bench");
        allowed.add("BenchClass");
        allowed.add("AfterBenchClass");
        allowed.add("AfterEachRun");
        allowed.add("BeforeBenchClass");
        allowed.add("BeforeEachRun");
        allowed.add("BeforeFirstRun");
        allowed.add("SkipBench");
        return allowed;
    }
}
