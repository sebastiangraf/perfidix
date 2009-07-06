package org.perfidix.Perclipse.views;



import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.perfidix.Perclipse.viewTreeTestdaten.TreeDataProvider;



public class BenchTreeLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		TreeDataProvider treeData = (TreeDataProvider) element;
		return treeData.toString();
	}
}
