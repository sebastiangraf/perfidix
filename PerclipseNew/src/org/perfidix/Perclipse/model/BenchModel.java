package org.perfidix.Perclipse.model;

import java.util.HashSet;
import java.util.LinkedList;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.views.BenchView;

/**
 * This class is our model connection. It contains the class BenchLaunchListener
 * which listens to started launch process.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public final class BenchModel {

    private final LinkedList<?> benchRunSessionList = new LinkedList();
    private final ILaunchListener launchListener = new BenchLaunchListener();

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

        private final HashSet trackedLaunches = new HashSet(20);

        /**
         * This method adds a new launch to a HashSet of tracked Launches.
         * 
         * @see org.eclipse.debug.core.ILaunchListener#launchAdded(org.eclipse.debug.core.ILaunch)
         */
        public void launchAdded(final ILaunch launch) {
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
            if (!trackedLaunches.contains(launch)) {
                return;
            }
            final ILaunchConfiguration config = launch.getLaunchConfiguration();
            if (config == null) {
                return;
            }
            final IJavaProject javaProject = getJavaProject(config);
            if (javaProject == null) {
                return;
            }

            // ToDo test wether the launch defines the attributes
            System.out.println();

            getDisplay().asyncExec(new Runnable() {
                public void run() {
                    connectBenchRunner(launch, javaProject);
                }
            });

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
            IWorkbenchPart activePart = null;
            IWorkbenchPage page = null;
            page = PerclipseActivator.getActivePage();

            if (benchView != null && benchView.isCreated()) {
                page.activate(benchView);
                return benchView;

            }
            if (page == null) {
                return null;
            }
            activePart = page.getActivePart();
            try {
                final IViewPart viewPart = page.showView(BenchView.MY_VIEW_ID);
                final BenchView view = (BenchView) viewPart;
                page.activate(viewPart);
                return view;
            } catch (final PartInitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

        }

        /**
         * This method checks if the BenchView is already in the current eclipse
         * window views loaded.
         * 
         * @return Returns the loaded view.
         */
        private BenchView findBenchViewInActivePage() {
            final IWorkbenchPage page = PerclipseActivator.getActivePage();
            if (page == null) {
                return null;
            }
            return (BenchView) page.findView(BenchView.MY_VIEW_ID);
        }

        private IJavaProject getJavaProject(
                final ILaunchConfiguration configuration) {
            try {
                final String projectName =
                        configuration
                                .getAttribute(
                                        IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                                        (String) null);
                if (projectName != null && projectName.length() > 0) {
                    return JavaCore.create(ResourcesPlugin
                            .getWorkspace().getRoot().getProject(projectName));
                }
            } catch (final CoreException e) {
            }
            return null;
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
