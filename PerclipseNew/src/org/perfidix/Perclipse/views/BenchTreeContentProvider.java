package org.perfidix.Perclipse.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.perfidix.Perclipse.viewTreeData.TreeDataProvider;

/**
 * This class is responsible for the content translation of the TreeDataProvider
 * for the eclipse viewer. It extends the class {@link ArrayContentProvider}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchTreeContentProvider extends ArrayContentProvider
        implements ITreeContentProvider {

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
     * Object)
     */
    public Object[] getChildren(Object parentElement) {
        if(parentElement!=null){
        TreeDataProvider treeData = (TreeDataProvider) parentElement;
        return treeData.getChildElements();
        } 
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
     * )
     */
    public Object getParent(Object element) {
        if(element!=null){
        TreeDataProvider treeData = (TreeDataProvider) element;
        return treeData.getParent();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
     * Object)
     */
    public boolean hasChildren(Object element) {
        if(element!=null){
        TreeDataProvider treeData = (TreeDataProvider) element;
        return treeData.getChildElements().length > 0;
        }
        return false;

    }
}