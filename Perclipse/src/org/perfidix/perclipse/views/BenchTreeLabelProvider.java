package org.perfidix.perclipse.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * This class is responsible for translating of data of the TreeDataProvider
 * class into the eclipse viewer. It extends {@link LabelProvider}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchTreeLabelProvider extends LabelProvider {

    /** {@inheritDoc} */
    @Override
    public Image getImage(Object element) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getText(Object element) {
        if (element != null) {
            TreeDataProvider treeData = (TreeDataProvider) element;
            if (treeData.getCurrentBenchError() > 0) {
                return treeData.getParentElementName()
                        + "  ("
                        + treeData.getCurrentBench()
                        + "/"
                        + treeData.getNumberOfBenchsForElement()
                        + ") "
                        + "Errors: "
                        + treeData.getCurrentBenchError();
            } else {

                return treeData.getParentElementName()
                        + "  ("
                        + treeData.getCurrentBench()
                        + "/"
                        + treeData.getNumberOfBenchsForElement()
                        + ")";
            }
        }
        return null;
    }
}
