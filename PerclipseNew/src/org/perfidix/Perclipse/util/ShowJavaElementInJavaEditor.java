package org.perfidix.Perclipse.util;

import java.lang.reflect.Constructor;

import javax.sql.rowset.BaseRowSet;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.internal.core.search.matching.IntersectingPattern;
import org.eclipse.jdt.internal.ui.packageview.ClassPathContainer;
import org.eclipse.jdt.launching.sourcelookup.containers.ClasspathContainerSourceContainer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.perfidix.Perclipse.launcher.PerclipseActivator;

public class ShowJavaElementInJavaEditor {

    public ShowJavaElementInJavaEditor(Object object) {

        // ToDo: when the object was double clicked, opens the java element in
        // the java editor

        System.out.println(object);

        openJavaEditor(object.toString());

    }

    private void openJavaEditor(String editname) {

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
