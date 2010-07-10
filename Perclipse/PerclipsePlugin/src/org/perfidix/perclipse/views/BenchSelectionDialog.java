/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
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
