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
    public Object[] getChildren(Object parentElement) {
        if (parentElement != null) {
            TreeDataProvider treeData = (TreeDataProvider) parentElement;
            return treeData.getChildElements();
        }

        return new Object[0];
    }

    /** {@inheritDoc} */
    public Object getParent(Object element) {
        if (element != null) {
            TreeDataProvider treeData = (TreeDataProvider) element;
            return treeData.getParent();
        }
        return null;
    }

    /** {@inheritDoc} */
    public boolean hasChildren(Object element) {
        if (element != null) {
            TreeDataProvider treeData = (TreeDataProvider) element;
            if (treeData.getChildElements().length > 0) {
                return true;
            }
        }
        return false;

    }
}