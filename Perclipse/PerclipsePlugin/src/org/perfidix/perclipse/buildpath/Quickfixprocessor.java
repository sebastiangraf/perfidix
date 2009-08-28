package org.perfidix.perclipse.buildpath;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.perfidix.perclipse.launcher.PerclipseActivator;

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
     * @param context
     *            The invocation context.
     * @param locations
     *            The code location for the problem.
     * @throws CoreException
     *             The Exception occurred.
     * @return A array of java completion proposals.
     * @see org.eclipse.jdt.ui.text.java.IQuickFixProcessor#getCorrections(org.eclipse.jdt.ui.text.java.IInvocationContext,
     *      org.eclipse.jdt.ui.text.java.IProblemLocation[])
     */
    public IJavaCompletionProposal[] getCorrections(
            final IInvocationContext context, final IProblemLocation[] locations)
            throws CoreException {
        IJavaCompletionProposal[] proposalsReturn = null;
        ArrayList<PerfidixAddLibraryProposal> arraylist = null;
        for (IProblemLocation problemLocation : locations) {
            final IProblemLocation problem = problemLocation;
            final int idNo = problem.getProblemId();
            if (IProblem.UndefinedType == idNo
                    || IProblem.ImportNotFound == idNo) {
                arraylist =
                        (ArrayList<PerfidixAddLibraryProposal>) getPerfidixAddLibraryProposals(
                                context, problem, arraylist);
            }
        }
        if (arraylist != null && !arraylist.isEmpty()) {
            proposalsReturn =
                    arraylist.toArray(new IJavaCompletionProposal[arraylist
                            .size()]);
        }
        return proposalsReturn;
    }

    /**
     * Defines which problemId refers to our proposal.
     * 
     * @param unit
     *            The compilation unit.
     * @param problemId
     *            The problem id which occurred for quick fix.
     * @return True or false depends on the parameters.
     * @see org.eclipse.jdt.ui.text.java.IQuickFixProcessor#hasCorrections(org.eclipse.jdt.core.ICompilationUnit,
     *      int)
     */
    public boolean hasCorrections(
            final ICompilationUnit unit, final int problemId) {
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
    private List<PerfidixAddLibraryProposal> getPerfidixAddLibraryProposals(
            final IInvocationContext context, final IProblemLocation problem,
            final List<PerfidixAddLibraryProposal> arraylist) {
        List<PerfidixAddLibraryProposal> returnList = arraylist;

        final List<String> okAnnotations = getInitAllowedAnnotation();

        final ICompilationUnit unit = context.getCompilationUnit();
        String string;
        try {
            string =
                    unit.getBuffer().getText(
                            problem.getOffset(), problem.getLength());
            if (okAnnotations.contains(string)) {
                final ASTNode node =
                        problem.getCoveredNode(context.getASTRoot());
                if (node != null
                        && node.getLocationInParent() == MarkerAnnotation.TYPE_NAME_PROPERTY && returnList==null) {
                    returnList =
                      new ArrayList<PerfidixAddLibraryProposal>();
                    returnList.add(new PerfidixAddLibraryProposal(
                            true, context, 10));
                    }
                else if(node != null
                            && node.getLocationInParent() == MarkerAnnotation.TYPE_NAME_PROPERTY && returnList!=null){
                    returnList.add(new PerfidixAddLibraryProposal(
                            true, context, 10));
                    }
                }

        } catch (JavaModelException e) {
            PerclipseActivator.log(e);
        }

        return returnList;
    }

    /**
     * This method provides the allowed annotations to invoke our proposals for
     * the QuickFix processor.
     * 
     * @return Returns an ArrayList containing the allowed annotations for our
     *         proposal support.
     */
    private List<String> getInitAllowedAnnotation() {
        final List<String> allowed = new ArrayList<String>();
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
