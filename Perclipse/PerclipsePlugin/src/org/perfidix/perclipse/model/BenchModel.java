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
package org.perfidix.perclipse.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.views.BenchView;

/**
 * This class is our model connection. It contains the class BenchLaunchListener
 * which listens to started launch process.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public final class BenchModel {

    final private transient ILaunchListener launchListener =
            new BenchLaunchListener();

    /**
     * This method gets the current LaunchManager and adds our
     * BenchLaunchListener to it. So we know then when an application has been
     * launched.
     */
    public void start() {
        final ILaunchManager launchManager =
                DebugPlugin.getDefault().getLaunchManager();
        launchManager.addLaunchListener(launchListener);

    }

    /**
     * When running application (perfidix) has stopped/ended, the method removes
     * the BenchLaunchListener from the LaunchManager.
     */
    public void stop() {
        final ILaunchManager launchManager =
                DebugPlugin.getDefault().getLaunchManager();
        launchManager.removeLaunchListener(launchListener);
    }

    /**
     * The BenchLaunchListener listens to activated launch process, like our
     * perfidix bench process.
     * 
     * @author Lewandowski Lukas, DiSy, University of Konstanz
     */
    private final class BenchLaunchListener implements ILaunchListener {

        private final transient Set<ILaunch> trackedLaunches =
                new HashSet<ILaunch>(20);

        /**
         * This method adds a new launch to a HashSet of tracked Launches.
         * 
         * @see org.eclipse.debug.core.ILaunchListener#launchAdded(org.eclipse.debug.core.ILaunch)
         */
        public void launchAdded(final ILaunch launch) {

            // We don't allow several benches process at the same time. So if a
            // new run event occurs, the current benching process will be
            // killed.
            if (!trackedLaunches.isEmpty()) {
                final Iterator<ILaunch> iterator = trackedLaunches.iterator();
                while (iterator.hasNext()) {
                    try {
                        final ILaunch oldLaunch = iterator.next();
                        oldLaunch.terminate();
                        trackedLaunches.remove(oldLaunch);
                    } catch (DebugException e) {
                        PerclipseActivator.log(e);
                    }
                }
            }
            trackedLaunches.add(launch);

        }

        /**
         * This method will be called when a launch changed in its status. In
         * our case it manages the upcoming of the Perfidix view within the
         * eclipse plug-in.
         * 
         * @see org.eclipse.debug.core.ILaunchListener#launchChanged(org.eclipse.debug.core.ILaunch)
         */
        public void launchChanged(final ILaunch launch) {
            if (trackedLaunches.contains(launch)
                    && launch.getLaunchConfiguration() != null) {
                final IJavaProject javaProject =
                        getJavaProject(launch.getLaunchConfiguration());
                if (javaProject != null) {
                    // ToDo test wether the launch defines the attributes
                    getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            connectBenchRunner(launch, javaProject);
                        }
                    });
                }
            }
        }

        /**
         * This method will be called, when a launch ended and it has to be
         * removed from the tracked Launch HashSet.
         * 
         * @see org.eclipse.debug.core.ILaunchListener#launchRemoved(org.eclipse.debug.core.ILaunch)
         */
        public void launchRemoved(final ILaunch launch) {
            trackedLaunches.remove(launch);

        }

        /**
         * This method gets a defined view and opens it in the current eclipse
         * view window.
         * 
         * @param benchView
         *            This param is the custom defined view. In our case our
         *            BenchView.
         * @return It returns the displayed view.
         */
        private BenchView showBenchViewInActivePage(final BenchView benchView) {
            BenchView returnView = null;
            IWorkbenchPage page = null;
            page = PerclipseActivator.getActivePage();
            if (benchView != null && benchView.isCreated()) {
                page.activate(benchView);
                returnView = benchView;
            } else if (page != null) {
                try {
                    final IViewPart viewPart =
                            page.showView(BenchView.MY_VIEW_ID);
                    final BenchView view = (BenchView) viewPart;
                    page.activate(viewPart);
                    returnView = view;
                } catch (final PartInitException e) {
                    PerclipseActivator.log(e);
                }
            }
            return returnView;

        }

        /**
         * This method checks if the BenchView is already in the current eclipse
         * window views loaded.
         * 
         * @return Returns the loaded view.
         */
        private BenchView findBenchViewInActivePage() {
            BenchView view = null;
            final IWorkbenchPage page = PerclipseActivator.getActivePage();
            if (page != null) {
                view = (BenchView) page.findView(BenchView.MY_VIEW_ID);
            }
            return view;
        }

        private IJavaProject getJavaProject(
                final ILaunchConfiguration configuration) {
            IJavaProject returnProject = null;
            try {
                final String projectName =
                        configuration
                                .getAttribute(
                                        IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                                        (String) null);
                if (projectName != null && projectName.length() > 0) {
                    returnProject =
                            JavaCore.create(ResourcesPlugin
                                    .getWorkspace().getRoot().getProject(
                                            projectName));
                }
            } catch (final CoreException e) {
                PerclipseActivator.log(e);
            }
            return returnProject;
        }

        /**
         * This method returns the current Display.
         * 
         * @return This method returns the current Display.
         */
        private Display getDisplay() {
            Display display = Display.getCurrent();
            if (display == null) {
                display = Display.getDefault();
            }
            return display;

        }

        /**
         * This method loads our view for the current launch to display the
         * progress process.
         * 
         * @param launch
         *            This param is the current loaded launch.
         * @param javaProject
         *            The project the launch started.
         */
        public void connectBenchRunner(
                final ILaunch launch, final IJavaProject javaProject) {
            final BenchView benchView =
                    showBenchViewInActivePage(findBenchViewInActivePage());
            PerclipseActivator.getDefault().setBenchView(benchView);

        }

    }
}
