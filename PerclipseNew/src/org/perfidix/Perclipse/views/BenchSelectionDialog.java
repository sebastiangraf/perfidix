/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.perfidix.Perclipse.views;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.ui.dialogs.TwoPaneElementSelector;

import org.eclipse.jdt.core.IType;

import org.eclipse.jdt.ui.JavaElementLabelProvider;

/**
 * This class represent the dialog which opens when the User clicks on the
 * "search"-Button in the @{link PerclipseMainTab} in the launch configuration
 * within eclipse. The user can choose classes from a specified project and add
 * them to the launch configuration.
 * 
 * @author Graf S., Lewandowski L.
 */
public class BenchSelectionDialog extends TwoPaneElementSelector {

    private final IType[] fTypes;

    /**
     * This inner class is responsible for rendering of java packages.
     * 
     * @author Graf S.
     */
    private static class PackageRenderer extends JavaElementLabelProvider {
        /**
         * This constructor calls the super constructor with the below
         * {@link JavaElementLabelProvider} parameters.
         */
        public PackageRenderer() {
            super(JavaElementLabelProvider.SHOW_PARAMETERS
                    | JavaElementLabelProvider.SHOW_POST_QUALIFIED
                    | JavaElementLabelProvider.SHOW_ROOT);
        }

        /*
         * (non-Javadoc)
         * @see
         * org.eclipse.jdt.ui.JavaElementLabelProvider#getImage(java.lang.Object
         * )
         */
        public Image getImage(Object element) {
            return super.getImage(((IType) element).getPackageFragment());
        }

        /*
         * (non-Javadoc)
         * @see
         * org.eclipse.jdt.ui.JavaElementLabelProvider#getText(java.lang.Object)
         */
        public String getText(Object element) {
            return super.getText(((IType) element).getPackageFragment());
        }
    }

    /**
     * The constructor initializes the types which contains the necessary
     * benches and calls the super constructor.
     * 
     * @param shell The current shell.
     * @param types The types array of to be benched classes.
     */
    public BenchSelectionDialog(Shell shell, IType[] types) {
        super(
                shell, new JavaElementLabelProvider(
                        JavaElementLabelProvider.SHOW_BASICS
                                | JavaElementLabelProvider.SHOW_OVERLAY_ICONS),
                new PackageRenderer());
        fTypes = types;
    }

    /**
     * This method is responsible for configuring the shell. It delegates the
     * topics to the super class {@link TwoPaneElementSelector}.
     * 
     * @see org.eclipse.jface.window.Window#configureShell(Shell)
     * @param newShell The new shell value.
     */
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        // PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell, new
        // Object[] { IJavaHelpContextIds.MAINTYPE_SELECTION_DIALOG });
    }

    /*
     * @see Window#open()
     */
    public int open() {
        setElements(fTypes);
        return super.open();
    }

}
