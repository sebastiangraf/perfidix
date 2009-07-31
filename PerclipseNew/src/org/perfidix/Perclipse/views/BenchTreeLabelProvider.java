package org.perfidix.Perclipse.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.perfidix.Perclipse.viewTreeData.TreeDataProvider;

/**
 * This class is responsible for translating of data of the TreeDataProvider
 * class into the eclipse viewer. It extends {@link LabelProvider}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchTreeLabelProvider extends LabelProvider {

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
     */
    public Image getImage(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
     */
    public String getText(Object element) {
        if (element != null) {
            TreeDataProvider treeData = (TreeDataProvider) element;
            return treeData.getParentElementName()
                    + "  ("
                    + treeData.getCurrentBench()
                    + "/"
                    + treeData.getNumberOfBenchsForElement()
                    + ")";
        }
        return null;
    }
}
