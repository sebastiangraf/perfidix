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

public class Quickfixprocessor implements IQuickFixProcessor {

	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		
		ArrayList arraylist=null;
		for ( IProblemLocation problemLocation : locations) {
			IProblemLocation problem = problemLocation;
			int id=problem.getProblemId();
			if(IProblem.UndefinedType==id || IProblem.ImportNotFound==id){
				arraylist=getPerfidixAddLibraryProposals(context, problem, arraylist);
			}
		}
		if(arraylist==null || arraylist.isEmpty())
		return null;
		return (IJavaCompletionProposal[]) arraylist.toArray(new IJavaCompletionProposal[arraylist.size()]);
	}


	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		return problemId==IProblem.UndefinedType || problemId == IProblem.ImportNotFound;
	}

	private ArrayList getPerfidixAddLibraryProposals(
			IInvocationContext context, IProblemLocation problem,
			ArrayList arraylist) {
		
		ArrayList<String> allowedAnnotations=getInitAllowedAnnotation();
		
		ICompilationUnit unit=context.getCompilationUnit();
		int res=0;
		String s;
		try {
			s = unit.getBuffer().getText(problem.getOffset(), problem.getLength());
			if(allowedAnnotations.contains(s)){
				ASTNode node = problem.getCoveredNode(context.getASTRoot());
				if(node!=null && node.getLocationInParent()==MarkerAnnotation.TYPE_NAME_PROPERTY){
					
					IJavaProject javaProject=unit.getJavaProject();
					if(arraylist==null){
						arraylist=new ArrayList();
						
					}
					arraylist.add(new PerfidixAddLibraryProposal(true, context, 10));
					
				}
				
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arraylist;
	}


	private ArrayList<String> getInitAllowedAnnotation() {
		ArrayList<String> allowed= new ArrayList<String>();
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
