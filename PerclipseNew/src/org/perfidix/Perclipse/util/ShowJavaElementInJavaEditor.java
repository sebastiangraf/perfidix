package org.perfidix.Perclipse.util;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

/**
 * This Class is responsible for displaying a clicked benched java element item
 * from the Perclipse view in a java editor.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class ShowJavaElementInJavaEditor {

    /**
     * This constructor opens a given object in an java editor.
     * 
     * @param object
     *            This param is a given object that should be opened in an
     *            editor.
     */
    public ShowJavaElementInJavaEditor(Object object) {

        // ToDo: when the object was double clicked, opens the java element in
        // the java editor
        // This class and methods are still in development!

        System.out.println(object);

        openJavaEditor(object.toString());

    }

    /**
     * This private method is responsible for opening an object in the java
     * editor. Its a worker method.
     * 
     * @param editname
     */
    private void openJavaEditor(String editname) {

        // This class and methods are still in development!

        System.out.println("in openJavaEditor");

        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        System.out.println(workspace);

        IWorkspaceRoot workspaceRoot = workspace.getRoot();
        IPath path = workspaceRoot.getLocation();

        // IWorkbenchPart activePart = null;
        // IWorkbenchPage page = null;
        // page = PerclipseActivator.getActivePage();
        // activePart=page.getActivePart();
        //		
        // IEditorDescriptor desc =
        // PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(editname);
        // page.openEditor(new FileEditorInput(file), desc.getId());

    }

}
