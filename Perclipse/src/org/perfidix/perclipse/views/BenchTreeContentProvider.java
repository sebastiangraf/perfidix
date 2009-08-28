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