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
