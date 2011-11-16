/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.perclipse.views;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;

/**
 * This class represent the dialog which opens when the User clicks on the
 * "search"-Button in the @{link PerclipseMainTab} in the launch configuration
 * within eclipse. The user can choose classes from a specified project and add
 * them to the launch configuration.
 * 
 * @author Graf S., Lewandowski L.
 */
public class BenchSelectionDialog extends TwoPaneElementSelector {

    private transient final IType[] fTypes;

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

        /** {@inheritDoc} */
        @Override
        public Image getImage(final Object element) {
            return super.getImage(((IType) element).getPackageFragment());
        }

        /** {@inheritDoc} */
        @Override
        public String getText(final Object element) {
            return super.getText(((IType) element).getPackageFragment());
        }
    }

    /**
     * The constructor initializes the types which contains the necessary
     * benches and calls the super constructor.
     * 
     * @param shell
     *            The current shell.
     * @param types
     *            The types array of to be benched classes.
     */
    public BenchSelectionDialog(final Shell shell, final IType[] types) {
        super(
                shell, new JavaElementLabelProvider(
                        JavaElementLabelProvider.SHOW_BASICS
                                | JavaElementLabelProvider.SHOW_OVERLAY_ICONS),
                new PackageRenderer());
        fTypes = types.clone();
    }

    /** {@inheritDoc} */
    @Override
    public int open() {
        setElements(fTypes);
        return super.open();
    }

}
