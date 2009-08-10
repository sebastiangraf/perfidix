package org.perfidix.Perclipse.views;

import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.perfidix.Perclipse.model.BenchRunSession;
import org.perfidix.Perclipse.viewTreeData.TreeDataProvider;

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
            "org.perfidix.Perclipse.views.BenchView";
    private static final int REFRESH_INTERVAL = 2000;
    private PerfidixProgressBar progressBar;
    private BenchViewCounterPanel benchCounterPanel;
    private BenchViewer benchViewer;
    private BenchRunSession benchRunSession;
    private UpdateUIJob updateJob;
    private BenchIsRunningJob benchIsRunningJob;
    private Composite counterComposite;
    private Composite parentComosite;
    private Clipboard clipboard;
    private IMemento memento;
    private org.eclipse.core.runtime.jobs.ILock benchIsRunningLock;
    private boolean isDisposed = false;

    /**
     * A must constructor that do nothing.
     */
    public BenchView() {

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
    public void createPartControl(Composite parent) {
        parentComosite = parent;

        clipboard = new Clipboard(parent.getDisplay());

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        parent.setLayout(gridLayout);

        counterComposite = createProgressCountPanel(parent);
        counterComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.FILL_BOTH));

        benchViewer = new BenchViewer(counterComposite, this);

    }

    /** {@inheritDoc} */
    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
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
    protected Composite createProgressCountPanel(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
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
     * This method is responsible for creation an image within the view.
     * 
     * @param string
     *            The String name/path of the image.
     * @return It retruns the created image.
     */
    public static Image createImage(String string) {
        // Currently not used
        return null;
    }

    /**
     * This class is responsible for updating the created view within an ui job.
     * It extends {@link UIJob}
     * 
     * @author Lewandowski Lukas, DiSy, University of Konstanz
     */
    private class UpdateUIJob extends org.eclipse.ui.progress.UIJob {
        private boolean running = true;

        /**
         * The constructor sets an update ui job for a given name.
         * 
         * @param name
         *            The given ui job name.
         */
        public UpdateUIJob(String name) {
            super(name);
            setSystem(true);
        }

        /**
         * This method is called when an ui job stop
         */
        public void stop() {
            running = false;
        }

        /*
         * (non-Javadoc)
         * @see org.eclipse.core.runtime.jobs.Job#shouldSchedule()
         */
        public boolean shouldSchedule() {
            return running;
        }

        /*
         * (non-Javadoc)
         * @see
         * org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime
         * .IProgressMonitor)
         */
        @Override
        public org.eclipse.core.runtime.IStatus runInUIThread(
                org.eclipse.core.runtime.IProgressMonitor monitor) {

            if (!isDisposed()) {
                processChangesInUI();
            }
            schedule(REFRESH_INTERVAL);
            return Status.OK_STATUS;

        }
    }

    /**
     * This class is responsible for updating of data in the displayed view. It
     * extends {@link org.eclipse.core.runtime.jobs.Job}.
     * 
     * @author Lewandowski Lukas, DiSy, University of Konstanz
     */
    private class BenchIsRunningJob extends org.eclipse.core.runtime.jobs.Job {
        /**
         * The constructor sets a job with a given name.
         * 
         * @param name
         *            The given name for a job registration.
         */
        public BenchIsRunningJob(String name) {
            super(name);
            setSystem(true);
        }

        /*
         * (non-Javadoc)
         * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
         */
        public boolean belongsTo(Object family) {
            return family == new Object();
        }

        /*
         * (non-Javadoc)
         * @seeorg.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
         * IProgressMonitor)
         */
        @Override
        protected org.eclipse.core.runtime.IStatus run(
                org.eclipse.core.runtime.IProgressMonitor arg0) {
            benchIsRunningLock.acquire();
            return Status.OK_STATUS;
        }
    }

    /**
     * This method starts to update the view with the data of a given bench run
     * session.
     * 
     * @param benchRunSession
     *            The given bench run session.
     */
    public void startUpdateJobs(BenchRunSession benchRunSession) {
        this.benchRunSession = benchRunSession;
        postSyncProcessChanges();

        if (updateJob != null) {
            return;
        }

        // updateJob = new UpdateUIJob("jobName");
        // updateJob.schedule(REFRESH_INTERVAL);
    }

    /** {@inheritDoc} */
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        this.memento = memento;
        IWorkbenchSiteProgressService progressService = getProgressService();
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
        Object siteService =
                getSite()
                        .getAdapter(
                                org.eclipse.ui.progress.IWorkbenchSiteProgressService.class);
        if (siteService != null) {
            return (org.eclipse.ui.progress.IWorkbenchSiteProgressService) siteService;
        }
        return null;
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
     * @param r
     *            This param is a custom runnable.
     */
    private void postSyncRunnable(Runnable r) {
        if (!isDisposed()) {
            getDisplay().syncExec(r);
        }
    }

    /**
     * @return This method returns a boolean value if an object is disposed or
     *         not.
     */
    private boolean isDisposed() {
        return isDisposed;
    }

    /**
     * @return This method returns the current display.
     */
    private org.eclipse.swt.widgets.Display getDisplay() {
        return getViewSite().getShell().getDisplay();
    }

    /**
     * This method is called when a update job has to stop.
     */
    public void stopUpdateJobs() {
        if (updateJob != null) {
            updateJob.stop();
            updateJob = null;
        }
        if (benchIsRunningJob != null && benchIsRunningLock != null) {
            benchIsRunningLock.release();
            benchIsRunningJob = null;
        }
        postSyncProcessChanges();
    }

    /**
     * This method changes the values of the view's data. It is used for
     * updating the view.
     */
    private void processChangesInUI() {

        if (counterComposite.isDisposed()) {
            return;
        }
        refreshCounters();
        benchViewer.processChangesInUI(benchRunSession);
    }

    /**
     * @return This method returns the amount of occurred errors during the
     *         benching process of a java element.
     */
    private int getErrors() {
        if (benchRunSession == null) {
            return 0;
        } else {
            return benchRunSession.getErrorCount();
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

        if (benchRunSession != null) {
            startedCount = benchRunSession.getStartedCount();
            currentCount = benchRunSession.getCurrentCount();
            totalCount = benchRunSession.getTotalCount();
            errorCount = benchRunSession.getErrorCount();
            hasErrors = errorCount > 0;
            stopped = benchRunSession.isStopped();
        } else {

            startedCount = 0;
            currentCount = 0;
            totalCount = 0;
            errorCount = 0;
            hasErrors = false;
            stopped = false;
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
    public void handleBenchSelection(TreeDataProvider element) {

        showBench(element);

    }

    /**
     * This method show the selection from the tree view provider.
     * 
     * @param element
     *            The TreeDataProvider element that has been clicked.
     */
    private void showBench(TreeDataProvider element) {
        postSyncRunnable(new Runnable() {
            public void run() {
                if (!isDisposed()) {
                    System.out.println("Selection");
                }
            }
        });

    }

    /**
     * @return Returns true if the composite is created. Otherwise false.
     */
    public boolean isCreated() {
        return counterComposite != null;
    }

}
