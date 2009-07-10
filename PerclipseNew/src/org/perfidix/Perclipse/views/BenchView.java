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
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.model.SimulatedWorkClass;
import org.perfidix.Perclipse.util.BenchRunSession;

public class BenchView extends ViewPart {

	public final static String MY_VIEW_ID = "org.perfidix.Perclipse.views.BenchView";
	public final static int LAYOUT_HIERARCHICAL = 1;
	private static final int VIEW_ORIENTATION_VERTICAL = 0;
	private static final int VIEW_ORIENTATION_HORIZONTAL = 1;
	private static final int VIEW_ORIENTATION_AUTOMATIC = 2;
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
	private int viewOrientation = VIEW_ORIENTATION_AUTOMATIC;
	private int currentOrientation;
	private boolean isDisposed = false;

	private IPartListener2 partListener = new IPartListener2() {
		public void partActivated(IWorkbenchPartReference ref) {
		}

		public void partBroughtToTop(IWorkbenchPartReference ref) {
		}

		public void partInputChanged(IWorkbenchPartReference ref) {
		}

		public void partClosed(IWorkbenchPartReference ref) {
		}

		public void partDeactivated(IWorkbenchPartReference ref) {
		}

		public void partOpened(IWorkbenchPartReference ref) {
		}

		public void partVisible(IWorkbenchPartReference ref) {
			if (getSite().getId().equals(ref.getId())) {
				partIsVisible = true;
			}
		}

		public void partHidden(IWorkbenchPartReference ref) {
			if (getSite().getId().equals(ref.getId())) {
				partIsVisible = false;
			}
		}
	};

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
		setCounterColumns(layout);

		benchCounterPanel = new BenchViewCounterPanel(composite);
		benchCounterPanel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		progressBar = new PerfidixProgressBar(composite);
		progressBar.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));

		return composite;
	}

	private void setCounterColumns(GridLayout layout) {

		// here you can modify the visualization if the orientation changes
		// (horizontal vs vertical)

	}

	public static Image createImage(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	// private void computeOrientation(){
	// if(viewOrientation != VIEW_ORIENTATION_AUTOMATIC){
	// currentOrientation=viewOrientation;
	// setOrientation(currentOrientation);
	// }
	// else{
	// Point size=this.parentComosite.getSize();
	// if(size.x!=0 && size.y!=0){
	// if(size.x>size.y){
	// setOrientation(VIEW_ORIENTATION_HORIZONTAL);
	// }
	// else{
	// setOrientation(VIEW_ORIENTATION_VERTICAL);
	// }
	// }
	// }
	// }

	// private void setOrientation(int orientation) {
	// if ((sashForm == null) || sashForm.isDisposed())
	// return;
	// boolean horizontal = orientation == VIEW_ORIENTATION_HORIZONTAL;
	// sashForm.setOrientation(horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
	// for (int i = 0; i < toggleOrientationActions.length; ++i)
	// toggleOrientationActions[i].setChecked(viewOrientation ==
	// toggleOrientationActions[i].getOrientation());
	// currentOrientation = orientation;
	// GridLayout layout= (GridLayout) this.counterComposite.getLayout();
	// setCounterColumns(layout);
	// this.parentComosite.layout();
	// }

//	private class BenchRunSessionListener implements IBenchRunSessionListener {
//		public void sessionAdded(BenchRunSession runSession) {
//			if (getSite().getWorkbenchWindow() == PerclipseActivator
//					.getActiveWorkbenchWindow()) {
//				BenchRunSession deactivatedSession = setActiveBenchRunSession(runSession);
//				if (deactivatedSession != null)
//					deactivatedSession.swapOut();
//				// String testRunLabel=
//				// BasicElementLabels.getJavaElementName(benchRunSession.getTestRunName());
//				// String msg;
//				// if (runSession.getLaunch() != null) {
//				// msg=
//				// Messages.format(JUnitMessages.TestRunnerViewPart_Launching,
//				// new Object[]{ testRunLabel });
//				// } else {
//				// msg= testRunLabel;
//				// }
//				// setContentDescription(msg);
//			}
//		}

//		public void sessionRemoved(BenchRunSession runSession) {
//			if (runSession.equals(benchRunSession)) {
//				List benchRunSessions = PerclipseActivator.getModel()
//						.getTestRunSessions();
//				BenchRunSession deactivatedSession;
//				if (!benchRunSessions.isEmpty()) {
//					deactivatedSession = setActiveBenchRunSession((BenchRunSession) benchRunSessions
//							.get(0));
//				} else {
//					deactivatedSession = setActiveBenchRunSession(null);
//				}
//				if (deactivatedSession != null)
//					deactivatedSession.swapOut();
//			}
//		}
//	}

//	private class BenchSessionListener implements IBenchSessionListener {
//		public void sessionStarted() {
//			fTestViewer.registerViewersRefresh();
//			fShowOnErrorOnly = getShowOnErrorOnly();
//
//			startUpdateJobs();
//
//			fStopAction.setEnabled(true);
//			fRerunLastTestAction.setEnabled(true);
//		}
//
//		public void sessionEnded(long elapsedTime) {
//			fTestViewer.registerAutoScrollTarget(null);
//
//			String[] keys = { elapsedTimeAsString(elapsedTime) };
//			String msg = Messages.format(
//					JUnitMessages.TestRunnerViewPart_message_finish, keys);
//			registerInfoMessage(msg);
//
//			postSyncRunnable(new Runnable() {
//				public void run() {
//					if (isDisposed())
//						return;
//					fStopAction.setEnabled(lastLaunchIsKeptAlive());
//					updateRerunFailedFirstAction();
//					processChangesInUI();
//					if (hasErrorsOrFailures()) {
//						selectFirstFailure();
//					}
//					if (fDirtyListener == null) {
//						fDirtyListener = new DirtyListener();
//						JavaCore.addElementChangedListener(fDirtyListener);
//					}
//					warnOfContentChange();
//				}
//			});
//			stopUpdateJobs();
//		}
//
//		public void sessionStopped(final long elapsedTime) {
//			fTestViewer.registerAutoScrollTarget(null);
//
//			registerInfoMessage(JUnitMessages.TestRunnerViewPart_message_stopped);
//			handleStopped();
//		}
//
//		public void sessionTerminated() {
//			fTestViewer.registerAutoScrollTarget(null);
//
//			registerInfoMessage(JUnitMessages.TestRunnerViewPart_message_terminated);
//			handleStopped();
//		}
//
//		public void runningBegins() {
//			if (!fShowOnErrorOnly)
//				postShowTestResultsView();
//		}
//
//		public void testStarted(TestCaseElement testCaseElement) {
//			fTestViewer.registerAutoScrollTarget(testCaseElement);
//			fTestViewer.registerViewerUpdate(testCaseElement);
//
//			String className = BasicElementLabels
//					.getJavaElementName(testCaseElement.getClassName());
//			String method = BasicElementLabels
//					.getJavaElementName(testCaseElement.getTestMethodName());
//			String status = Messages.format(
//					JUnitMessages.TestRunnerViewPart_message_started,
//					new String[] { className, method });
//			registerInfoMessage(status);
//		}
//
//		public void testFailed(TestElement testElement,
//				TestElement.Status status, String trace, String expected,
//				String actual) {
//			if (isAutoScroll()) {
//				fTestViewer.registerFailedForAutoScroll(testElement);
//			}
//			fTestViewer.registerViewerUpdate(testElement);
//
//			// show the view on the first error only
//			if (fShowOnErrorOnly && (getErrorsPlusFailures() == 1))
//				postShowTestResultsView();
//
//			// TODO:
//			// [Bug 35590] JUnit window doesn't report errors from
//			// junit.extensions.TestSetup [JUnit]
//			// when a failure occurs in test setup then no test is running
//			// to update the views we artificially signal the end of a test run
//			// if (!fTestIsRunning) {
//			// fTestIsRunning= false;
//			// testEnded(testCaseElement);
//			// }
//		}
//
//		public void testEnded(TestCaseElement testCaseElement) {
//			fTestViewer.registerViewerUpdate(testCaseElement);
//		}
//
//		public void testReran(TestCaseElement testCaseElement,
//				TestElement.Status status, String trace, String expectedResult,
//				String actualResult) {
//			fTestViewer.registerViewerUpdate(testCaseElement); // TODO:
//																// autoExpand?
//			postSyncProcessChanges();
//			showFailure(testCaseElement);
//		}
//
//		public void testAdded(TestElement testElement) {
//			fTestViewer.registerTestAdded(testElement);
//		}
//
//		public boolean acceptsSwapToDisk() {
//			return false;
//		}
//	}

	private class UpdateUIJob extends org.eclipse.ui.progress.UIJob {
		private boolean fRunning = true;

		public UpdateUIJob(String name) {
			super(name);
			setSystem(true);
		}

		public void stop() {
			fRunning = false;
		}

		public boolean shouldSchedule() {
			return fRunning;
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

	public void startUpdateJobs() {
		postSyncProcessChanges();

		if (updateJob != null) {
			return;
		}

		
//		benchIsRunningJob = new BenchIsRunningJob("wrapperJobName");
//		benchIsRunningLock = org.eclipse.core.runtime.jobs.Job.getJobManager()
//				.newLock();
//		// acquire lock while a test run is running
//		// the lock is released when the test run terminates
//		// the wrapper job will wait on this lock.
//		benchIsRunningLock.acquire();
//		getProgressService().schedule(benchIsRunningJob);
//
		updateJob = new UpdateUIJob("jobName");
		updateJob.schedule(REFRESH_INTERVAL);
	}

	public BenchRunSession setActiveBenchRunSession(BenchRunSession runSession) {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		this.memento = memento;
		IWorkbenchSiteProgressService progressService = getProgressService();
		if (progressService != null)
			progressService.showBusyForFamily(new Object());
	}

	private org.eclipse.ui.progress.IWorkbenchSiteProgressService getProgressService() {
		Object siteService = getSite().getAdapter(
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
		//boolean hasErrorsOrFailures = hasErrorsOrFailures();
		// fNextAction.setEnabled(hasErrorsOrFailures);
		// fPreviousAction.setEnabled(hasErrorsOrFailures);

		benchViewer.processChangesInUI();
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
		int ignoredCount;
		int totalCount;
		int errorCount;
		boolean hasErrors;
		boolean stopped;
		
		
		
		if (benchRunSession != null) {
			startedCount = benchRunSession.getStartedCount();
			ignoredCount = benchRunSession.getIgnoredCount();
			totalCount = benchRunSession.getTotalCount();
			errorCount = benchRunSession.getErrorCount();
			hasErrors = errorCount  > 0;
			stopped = benchRunSession.isStopped();
		} else {
			
			startedCount = 2;
			ignoredCount = 2;
			totalCount = 10;
			errorCount = 0;
			hasErrors = false;
			stopped = false;
			
//			startedCount = 0;
//			ignoredCount = 0;
//			totalCount = 0;
//			errorCount = 0;
//			failureCount = 0;
//			hasErrorsOrFailures = false;
//			stopped = false;
		}

		benchCounterPanel.setTotalRuns(totalCount);
		benchCounterPanel.setBenchRuns(startedCount, ignoredCount);
		benchCounterPanel.setBenchErrors(errorCount);


		int ticksDone;
		if (startedCount == 0)
			ticksDone = 0;
		// else if (startedCount == totalCount && ! benchRunSession.isRunning())
		else if (startedCount == totalCount)
			ticksDone = totalCount;
		else
			ticksDone = startedCount - 1;
		

		progressBar.reset(hasErrors, stopped, ticksDone, totalCount);
	}
	
	public void setBenchRunSession(BenchRunSession benchRunSession){
		this.benchRunSession=benchRunSession;
	}
	
	 public boolean isCreated() {
		         return counterComposite != null;
		     }

}
