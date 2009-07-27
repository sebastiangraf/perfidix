package org.perfidix.Perclipse.views;

import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.perfidix.Perclipse.model.BenchRunSession;

public class BenchView extends ViewPart {

    public final static String MY_VIEW_ID =
            "org.perfidix.Perclipse.views.BenchView";
    public final static int LAYOUT_HIERARCHICAL = 1;
    private static final int REFRESH_INTERVAL = 20000;
    private PerfidixProgressBar progressBar;
    private BenchViewCounterPanel benchCounterPanel;
    private BenchViewer benchViewer;
    private BenchRunSession benchRunSession;
    private UpdateUIJob updateJob;
    private BenchIsRunningJob benchIsRunningJob;
    private SashForm sashForm;
    private Label label;
    private Composite counterComposite;
    private Composite parentComosite;
    private Clipboard clipboard;
    private IMemento memento;
    private org.eclipse.core.runtime.jobs.ILock benchIsRunningLock;
    private boolean isDisposed = false;

    protected boolean partIsVisible = false;

    public BenchView() {

    }

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

        benchViewer = new BenchViewer(counterComposite, clipboard, this);

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
        // Set focus to my widget. For a label no sense but for more
        // complextypes

    }

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

    public static Image createImage(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    private class UpdateUIJob extends org.eclipse.ui.progress.UIJob {
        private boolean running = true;

        public UpdateUIJob(String name) {
            super(name);
            setSystem(true);
        }

        public void stop() {
            running = false;
        }

        public boolean shouldSchedule() {
            return running;
        }

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

    private class BenchIsRunningJob extends org.eclipse.core.runtime.jobs.Job {
        public BenchIsRunningJob(String name) {
            super(name);
            setSystem(true);
        }

        public boolean belongsTo(Object family) {
            return family == new Object();
        }

        @Override
        protected org.eclipse.core.runtime.IStatus run(
                org.eclipse.core.runtime.IProgressMonitor arg0) {
            benchIsRunningLock.acquire();
            return Status.OK_STATUS;
        }
    }

    public void startUpdateJobs(BenchRunSession benchRunSession) {
        this.benchRunSession = benchRunSession;
        postSyncProcessChanges();

        if (updateJob != null) {
            return;
        }

        // benchIsRunningJob = new BenchIsRunningJob("wrapperJobName");
        // benchIsRunningLock =
        // org.eclipse.core.runtime.jobs.Job.getJobManager()
        // .newLock();
        // // acquire lock while a test run is running
        // // the lock is released when the test run terminates
        // // the wrapper job will wait on this lock.
        // benchIsRunningLock.acquire();
        // getProgressService().schedule(benchIsRunningJob);
        //
        updateJob = new UpdateUIJob("jobName");
        updateJob.schedule(REFRESH_INTERVAL);
    }

    // public BenchRunSession setActiveBenchRunSession(BenchRunSession
    // runSession) {
    // // TODO Auto-generated method stub
    // return null;
    // }

    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        this.memento = memento;
        IWorkbenchSiteProgressService progressService = getProgressService();
        if (progressService != null)
            progressService.showBusyForFamily(new Object());
    }

    private org.eclipse.ui.progress.IWorkbenchSiteProgressService getProgressService() {
        Object siteService =
                getSite()
                        .getAdapter(
                                org.eclipse.ui.progress.IWorkbenchSiteProgressService.class);
        if (siteService != null)
            return (org.eclipse.ui.progress.IWorkbenchSiteProgressService) siteService;
        return null;
    }

    private void postSyncProcessChanges() {
        postSyncRunnable(new Runnable() {
            public void run() {
                processChangesInUI();
            }
        });
    }

    private void postSyncRunnable(Runnable r) {
        if (!isDisposed())
            getDisplay().syncExec(r);
    }

    private boolean isDisposed() {
        return isDisposed;
    }

    private org.eclipse.swt.widgets.Display getDisplay() {
        return getViewSite().getShell().getDisplay();
    }

    private void stopUpdateJobs() {
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

    private void processChangesInUI() {
        if (counterComposite.isDisposed())
            return;

        refreshCounters();

        // if (!fPartIsVisible)
        // updateViewTitleProgress();
        // else {
        // updateViewIcon();
        // }
        // boolean hasErrorsOrFailures = hasErrorsOrFailures();
        // fNextAction.setEnabled(hasErrorsOrFailures);
        // fPreviousAction.setEnabled(hasErrorsOrFailures);

        benchViewer.processChangesInUI(benchRunSession);
    }

    private boolean hasErrors() {
        return getErrors() > 0;
    }

    private int getErrors() {
        if (benchRunSession == null) {
            return 0;
        } else {
            return benchRunSession.getErrorCount();
        }
    }

    private void refreshCounters() {
        // TODO: Inefficient. Either
        // - keep a boolean fHasTestRun and update only on changes, or
        // - improve components to only redraw on changes (once!).

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

            startedCount = 2;
            currentCount = 3;
            totalCount = 10;
            errorCount = 0;
            hasErrors = false;
            stopped = false;

            // startedCount = 0;
            // totalCount = 0;
            // errorCount = 0;
            // hasErrorsOrFailures = false;
            // stopped = false;
        }

        // int ticksDone;
        // if (startedCount == 0)
        // ticksDone = 0;
        // // else if (startedCount == totalCount && !
        // benchRunSession.isRunning())
        // else if (startedCount == totalCount)
        // ticksDone = totalCount;
        // else
        // ticksDone = startedCount - 1;
        int ticksDone;
        if (startedCount == currentCount)
            ticksDone = startedCount;
        else
            ticksDone = currentCount;

        benchCounterPanel.setTotalRuns(totalCount);
        benchCounterPanel.setBenchRuns(ticksDone);
        benchCounterPanel.setBenchErrors(errorCount);
        progressBar.reset(hasErrors, stopped, ticksDone, totalCount);
    }

    // public void setBenchRunSession(BenchRunSession benchRunSession){
    // this.benchRunSession=benchRunSession;
    // }

    public boolean isCreated() {
        return counterComposite != null;
    }

}
