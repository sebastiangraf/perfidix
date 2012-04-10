/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    public Image getImage(final Object element) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getText(final Object element) {
        String stringReturn = null;
        if (element != null) {
            final TreeDataProvider treeData = (TreeDataProvider) element;
            if (treeData.getCurrentBenchError() > 0) {
                stringReturn =
                        treeData.getParentElementName()
                                + "  ("
                                + treeData.getCurrentBench()
                                + "/"
                                + treeData.getNumberOfBenchsForElement()
                                + ") "
                                + "Errors: "
                                + treeData.getCurrentBenchError();
            } else {

                stringReturn =
                        treeData.getParentElementName()
                                + "  ("
                                + treeData.getCurrentBench()
                                + "/"
                                + treeData.getNumberOfBenchsForElement()
                                + ")";
            }
        }
        return stringReturn;
    }
}
