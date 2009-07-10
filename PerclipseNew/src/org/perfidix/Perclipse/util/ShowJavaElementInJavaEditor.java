package org.perfidix.Perclipse.util;

import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.perfidix.Perclipse.launcher.PerclipseActivator;



public class ShowJavaElementInJavaEditor {

	public ShowJavaElementInJavaEditor(Object object) {
	
		//ToDo: when the object was double clicked, opens the java element in the java editor
		
		System.out.println(object);
		
		openJavaEditor((String) object);

		
	}
	
	private void openJavaEditor(String editname){
		IWorkbenchPart activePart = null;
		IWorkbenchPage page = null;
		page = PerclipseActivator.getActivePage();
		activePart=page.getActivePart();
//		
//		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(editname);
//		page.openEditor(new FileEditorInput(file), desc.getId());
		
	}

}
