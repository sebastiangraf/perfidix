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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * This class is responsible for the content translation of the TreeDataProvider
 * for the eclipse viewer. It extends the class {@link ArrayContentProvider}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchTreeContentProvider extends ArrayContentProvider
        implements ITreeContentProvider {

    /** {@inheritDoc} */
    public Object[] getChildren(final Object parentElement) {
        Object[] returnArray = new Object[0];
        if (parentElement != null) {
            final TreeDataProvider treeData = (TreeDataProvider) parentElement;
            returnArray = treeData.getChildElements();
        }

        return returnArray;
    }

    /** {@inheritDoc} */
    public Object getParent(final Object element) {
        Object retParent = null;
        if (element != null) {
            final TreeDataProvider treeData = (TreeDataProvider) element;
            retParent = treeData.getParent();
        }
        return retParent;
    }

    /** {@inheritDoc} */
    public boolean hasChildren(final Object element) {
        boolean hasChilds = false;
        if (element != null) {
            final TreeDataProvider treeData = (TreeDataProvider) element;
            if (treeData.getChildElements().length > 0) {
                hasChilds = true;
            }
        }
        return hasChilds;

    }
}