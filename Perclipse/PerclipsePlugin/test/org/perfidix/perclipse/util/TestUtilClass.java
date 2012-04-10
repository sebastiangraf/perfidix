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
package org.perfidix.perclipse.util;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.views.BenchView;

/**
 * Just a Util-Class for testing.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class TestUtilClass {

    /**
     * Set View just for testing.
     */
    public void setViewForTesting() {
        final BenchView benchView =
                showBenchViewInActivePage(findBenchViewInActivePage());
        PerclipseActivator.getDefault().setBenchView(benchView);
    }

    /**
     * Set View NULL just for testing.
     */
    public void setViewNull() {
        PerclipseActivator.getDefault().setBenchView(null);
    }

    /**
     * Utility copy. This method gets a defined view and opens it in the current
     * eclipse view window.
     * 
     * @param benchView
     *            This param is the custom defined view. In our case our
     *            BenchView.
     * @return It returns the displayed view.
     */
    private BenchView showBenchViewInActivePage(final BenchView benchView) {
        BenchView retView = null;
        IWorkbenchPage page = null;
        page = PerclipseActivator.getActivePage();

        if (benchView != null && benchView.isCreated()) {
            page.activate(benchView);
            retView = benchView;

        } else if (page != null) {
            try {
                final IViewPart viewPart = page.showView(BenchView.MY_VIEW_ID);
                final BenchView view = (BenchView) viewPart;
                page.activate(viewPart);
                retView = view;
            } catch (final PartInitException e) {
                PerclipseActivator.log(e);
            }

        }

        return retView;

    }

    /**
     * Utility copy. This method checks if the BenchView is already in the
     * current eclipse window views loaded.
     * 
     * @return Returns the loaded view.
     */
    private BenchView findBenchViewInActivePage() {
        BenchView returnView = null;
        final IWorkbenchPage page = PerclipseActivator.getActivePage();
        if (page != null) {
            returnView = (BenchView) page.findView(BenchView.MY_VIEW_ID);
        }
        return returnView;
    }
}
