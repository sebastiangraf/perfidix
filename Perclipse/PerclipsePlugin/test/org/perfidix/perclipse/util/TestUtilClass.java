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
