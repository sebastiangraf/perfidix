package org.perfidix.perclipse;

import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

public class PerfidixLaunchShortcut implements ILaunchShortcut{

	public void launch(ISelection arg0, String arg1) {
		System.out.println("TestSelection " + arg0.toString() + " and arg1 " +arg1);
	}

	public void launch(IEditorPart arg0, String arg1) {
		System.out.println("TestEditorPart " + arg0.toString() + " and arg1 " +arg1);
	}

}
