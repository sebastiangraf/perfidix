package org.perfidix.Perclipse.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.perfidix.Perclipse.viewTreeTestdaten.TreeDataProvider;

public class BenchTreeContentProvider extends ArrayContentProvider implements
		ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		TreeDataProvider treeData = (TreeDataProvider) parentElement;
		return treeData.getChildElements();
	}

	public Object getParent(Object element) {
		TreeDataProvider treeData = (TreeDataProvider) element;
		return treeData.getParent();
	}

	public boolean hasChildren(Object element) {
		TreeDataProvider treeData = (TreeDataProvider) element;
		return treeData.getChildElements().length> 0;

	}
}