/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.model.BenchRunSession;
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * This class BenchView is our eclipse view that opens when a bench run is
 * started. It shows the progress of the perfidix bench process with a counter
 * panel,a progress bar and a bench viewer.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchView extends ViewPart {

    /**
     * The view id, responsible for the view extensions point.
     */
    public static final String MY_VIEW_ID =
            "org.perfidix.perclipse.views.BenchView";
    private transient PerfidixProgressBar progressBar;
    private transient BenchViewCounterPanel benchCounterPanel;
    private transient BenchViewer benchViewer;
    private transient BenchRunSession benchRunSession;
    private transient Composite counterComposite;
    final private static transient boolean DISP = false;

    /**
     * A must constructor that do nothing special.
     */
    public BenchView() { // NOPMD by lewandow on 8/31/09 2:26 PM
        super();
    }

    /**
     * This method creates every intern part of the view, like progress bar or
     * the bench viewer.
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     * @param parent
     *            The composite of the parent.
     */
    @Override
    public void createPartControl(final Composite parent) {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        parent.setLayout(gridLayout);

        counterComposite = createProgressCountPanel(parent);
        counterComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.FILL_BOTH));

        benchViewer = new BenchViewer(counterComposite, this);

    }

    /**
     * @return The BenchViewer-Instance
     */
    public BenchViewer getBenchViewer() {
        return benchViewer;
    }

    /** {@inheritDoc} */
    @Override
    public void setFocus() {
        // Set focus to my widget. For a label no sense but for more
        // complextypes
    }

    /**
     * This method creates a counter panel within the bench view.
     * 
     * @param parent
     *            This param represents the composite of the view, in which the
     *            counter panel should be created.
     * @return It returns a modified composite.
     */
    public Composite createProgressCountPanel(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        composite.setLayout(layout);

        benchCounterPanel = new BenchViewCounterPanel(composite);
        benchCounterPanel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));
        progressBar = new PerfidixProgressBar(composite);
        progressBar.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));

        return composite;
    }

    /**
     * @return The created Bench Counter Panel object
     *         {@link org.perfidix.perclipse.views.PerfidixProgressBar}
     */
    public PerfidixProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * @return The created Bench Counter Panel object
     *         {@link org.perfidix.perclipse.views.BenchViewCounterPanel}
     */
    public BenchViewCounterPanel getBenchCounterPanel() {
        return benchCounterPanel;
    }

//    /**
//     * This method is responsible for creation an image within the view.
//     * 
//     * @param string
//     *            The String name/path of the image.
//     * @return It retruns the created image.
//     */
//    public static Image createImage(final String string) {
//        Image retImage = null;
//        if (string != null) {
//            final ImageDescriptor imageDescriptor =
//                    PerclipseActivator.getImageDescriptor(string);
//            if (imageDescriptor != null) {
//                retImage = imageDescriptor.createImage();
//            }
//        }
//        return retImage;
//    }

    /**
     * This method starts to update the view with the data of a given bench run
     * session.
     * 
     * @param benchRunSession
     *            The given bench run session.
     */
    public void startUpdateJobs(final BenchRunSession benchRunSession) {

        this.benchRunSession = benchRunSession;
        postSyncProcessChanges();

    }

    /** {@inheritDoc} */
    @Override
    public void init(final IViewSite site, final IMemento memento)
            throws PartInitException {
        final IMemento mMemento = memento;
        super.init(site, mMemento);
        final IWorkbenchSiteProgressService progressService =
                getProgressService();
        if (progressService != null) {
            progressService.showBusyForFamily(new Object());
        }
    }

    /**
     * This method returns the ProgressService or null.
     * 
     * @return This method returns the ProgressService or null.
     */
    private org.eclipse.ui.progress.IWorkbenchSiteProgressService getProgressService() {
        IWorkbenchSiteProgressService retService = null;
        final Object siteService =
                getSite()
                        .getAdapter(
                                org.eclipse.ui.progress.IWorkbenchSiteProgressService.class);
        if (siteService != null) {
            retService =
                    (org.eclipse.ui.progress.IWorkbenchSiteProgressService) siteService;
        }
        return retService;
    }

    /**
     * This method updates the process data with an ui thread.
     */
    private void postSyncProcessChanges() {
        postSyncRunnable(new Runnable() {
            public void run() {
                processChangesInUI();
            }
        });
    }

    /**
     * This method updates the process data with an ui thread.
     * 
     * @param runnable
     *            This param is a custom runnable.
     */
    private void postSyncRunnable(final Runnable runnable) {
        if (!isDisposed()) {
            getDisplay().syncExec(runnable);
        }
    }

    /**
     * @return This method returns a boolean value if an object is disposed or
     *         not.
     */
    private boolean isDisposed() {
        return DISP;
    }

    /**
     * @return This method returns the current display.
     */
    private org.eclipse.swt.widgets.Display getDisplay() {
        return getViewSite().getShell().getDisplay();
    }

    /**
     * This method changes the values of the view's data. It is used for
     * updating the view.
     */
    private void processChangesInUI() {

        if (!counterComposite.isDisposed()) {
            refreshCounters();
            if (benchRunSession != null) {
                benchViewer.processChangesInUI(benchRunSession);
            }
        }
    }

    /**
     * This method updates the progress in the view. It is responsible for
     * updating the counters.
     */
    private void refreshCounters() {
        int startedCount;
        int currentCount;
        int totalCount;
        int errorCount;
        boolean hasErrors;
        boolean stopped;

        if (benchRunSession == null) {
            startedCount = 0;
            currentCount = 0;
            totalCount = 0;
            errorCount = 0;
            hasErrors = false;
            stopped = false;
        } else {
            startedCount = benchRunSession.getStartedCount();
            currentCount = benchRunSession.getCurrentCount();
            totalCount = benchRunSession.getTotalCount();
            errorCount = benchRunSession.getErrorCount();
            hasErrors = errorCount > 0;
            stopped = benchRunSession.isStopped();
        }

        int ticksDone;
        if (startedCount == currentCount) {
            ticksDone = startedCount;
        } else {
            ticksDone = currentCount;
        }
        benchCounterPanel.setTotalRuns(totalCount);
        benchCounterPanel.setBenchRuns(ticksDone);
        benchCounterPanel.setBenchErrors(errorCount);
        progressBar.reset(hasErrors, stopped, ticksDone, totalCount);
    }

    /**
     * This method handles the action that will be executed when an element has
     * been selected in the tree viewer.
     * 
     * @param element
     *            The String value of the element in the tree view.
     */
    public void handleBenchSelection(final TreeDataProvider element) {
        showBench(element);
    }

    /**
     * This method show the selection from the tree view provider.
     * 
     * @param element
     *            The TreeDataProvider element that has been clicked.
     */
    private void showBench(final TreeDataProvider element) {
        if (element != null) {
            postSyncRunnable(new Runnable() {
                public void run() {
                    if (!isDisposed() && isCreated()) {
                        PerclipseActivator.logInfo(element
                                .getParentElementName()
                                + " has been selected");
                    }
                }
            });
        }

    }

    /**
     * @return Returns true if the composite is created. Otherwise false.
     */
    public boolean isCreated() {
        return counterComposite != null;
    }

}
