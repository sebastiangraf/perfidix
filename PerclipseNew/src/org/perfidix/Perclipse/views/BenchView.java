package org.perfidix.Perclipse.views;


import Display;
import ILock;
import IProgressMonitor;
import IStatus;
import IWorkbenchSiteProgressService;
import Job;
import TestRunnerViewPart;

import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import TestRunnerViewPart.JUnitIsRunningJob;
import TestRunnerViewPart.UpdateUIJob;

public class BenchView extends ViewPart {

	public final static String MY_VIEW_ID = "org.perfidix.Perclipse.views.BenchView";
	public final static int LAYOUT_HIERARCHICAL=1;
	private static final int VIEW_ORIENTATION_VERTICAL= 0;
	private static final int VIEW_ORIENTATION_HORIZONTAL= 1;
	private static final int VIEW_ORIENTATION_AUTOMATIC= 2;
	private static final int REFRESH_INTERVAL=200;
	private PerfidixProgressBar progressBar;
	private BenchViewCounterPanel benchCounterPanel;
	private BenchViewer benchViewer;
	private UpdateUIJob updateJob;
	private BenchIsRunningJob benchIsRunningJob;
	private SashForm sashForm;
	private Label label;
	private Composite counterComposite;
	private Composite parentComosite;
	private Clipboard clipboard;
	private org.eclipse.core.runtime.jobs.ILock benchIsRunningLock;
	private int viewOrientation = VIEW_ORIENTATION_AUTOMATIC;
	private int currentOrientation;
	private boolean isDisposed=false;
	

	

	public BenchView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		parentComosite = parent;

		clipboard= new Clipboard(parent.getDisplay());
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);

		counterComposite = createProgressCountPanel(parent);
		counterComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_BOTH));
		
		progressBar.reset(false, false, 20, 50);
		
		benchViewer= new BenchViewer(counterComposite, clipboard, this);
		

		
		


	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		// Set focus to my widget. For a label no sense but for more
		// complextypes

	}

	public void showProgress(Composite parent) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			Thread.sleep(500);
			label = new Label(parent, SWT.WRAP);
			label.setText("still working... ");
		}
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
	
//	private void computeOrientation(){
//		if(viewOrientation != VIEW_ORIENTATION_AUTOMATIC){
//			currentOrientation=viewOrientation;
//			setOrientation(currentOrientation);
//		}
//		else{
//			Point size=this.parentComosite.getSize();
//			if(size.x!=0 && size.y!=0){
//				if(size.x>size.y){
//					setOrientation(VIEW_ORIENTATION_HORIZONTAL);
//				}
//				else{
//					setOrientation(VIEW_ORIENTATION_VERTICAL);
//				}
//			}
//		}
//	}
	
//	private void setOrientation(int orientation) {
//		        if ((sashForm == null) || sashForm.isDisposed())
//		            return;
//		        boolean horizontal = orientation == VIEW_ORIENTATION_HORIZONTAL;
//		        sashForm.setOrientation(horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
//		        for (int i = 0; i < toggleOrientationActions.length; ++i)
//		            toggleOrientationActions[i].setChecked(viewOrientation == toggleOrientationActions[i].getOrientation());
//		        currentOrientation = orientation;
//		        GridLayout layout= (GridLayout) this.counterComposite.getLayout();
//		        setCounterColumns(layout);
//		        this.parentComosite.layout();
//		    }
	
	private class UpdateUIJob extends org.eclipse.ui.progress.UIJob {
		private boolean fRunning= true;

		public UpdateUIJob(String name) {
			super(name);
			setSystem(true);
		}

		public void stop() {
			fRunning= false;
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
			return null;
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
	
	private void startUpdateJobs() {
		postSyncProcessChanges();

		if (updateJob != null) {
			return;
		}
		benchIsRunningJob= new BenchIsRunningJob("wrapperJobName");
		benchIsRunningLock= org.eclipse.core.runtime.jobs.Job.getJobManager().newLock();
		// acquire lock while a test run is running
		// the lock is released when the test run terminates
		// the wrapper job will wait on this lock.
		benchIsRunningLock.acquire();
		getProgressService().schedule(benchIsRunningJob);

		updateJob= new UpdateUIJob("jobName");
		updateJob.schedule(REFRESH_INTERVAL);
	}
	
	private org.eclipse.ui.progress.IWorkbenchSiteProgressService getProgressService() {
		Object siteService= getSite().getAdapter(org.eclipse.ui.progress.IWorkbenchSiteProgressService.class);
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
			updateJob= null;
		}
		if (benchIsRunningJob != null && benchIsRunningLock != null) {
			benchIsRunningLock.release();
			benchIsRunningJob= null;
		}
		postSyncProcessChanges();
	}

	private void processChangesInUI() {
		if (sashForm.isDisposed())
			return;

		doShowInfoMessage();
		refreshCounters();

		if (! fPartIsVisible)
			updateViewTitleProgress();
		else {
			updateViewIcon();
		}
		boolean hasErrorsOrFailures= hasErrorsOrFailures();
		fNextAction.setEnabled(hasErrorsOrFailures);
		fPreviousAction.setEnabled(hasErrorsOrFailures);

		fTestViewer.processChangesInUI();
	}


}
