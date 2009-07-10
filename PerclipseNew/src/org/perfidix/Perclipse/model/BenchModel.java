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

public final class BenchModel {
	
	private final LinkedList benchRunSessionList = new LinkedList();
	private final ILaunchListener launchListener = new BenchLaunchListener();
	
	public void start(){
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		launchManager.addLaunchListener(launchListener);
		
	}
	
	public void stop(){
		ILaunchManager launchManager=DebugPlugin.getDefault().getLaunchManager();
		launchManager.removeLaunchListener(launchListener);
	}
	
	
	private final class BenchLaunchListener implements ILaunchListener {

		private HashSet trackedLaunches = new HashSet(20);

		public void launchAdded(ILaunch launch) {
			trackedLaunches.add(launch);

		}

		public void launchChanged(final ILaunch launch) {
			if(!trackedLaunches.contains(launch)){
				return;
			}
			ILaunchConfiguration config=launch.getLaunchConfiguration();
			if(config==null){
				return;
			}
			final IJavaProject javaProject= getJavaProject(config);
			if(javaProject==null){
				return;
			}

			//ToDo test wether the launch defines the attributes
			
			getDisplay().asyncExec(new Runnable(){
				public void run(){
					connectTestRunner(launch, javaProject);
				}
			});
			
		}


		public void launchRemoved(ILaunch launch) {
			trackedLaunches.remove(launch);

		}

		private BenchView showBenchViewInActivePage(BenchView benchView) {
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
				IViewPart viewPart =page.showView(BenchView.MY_VIEW_ID);
				BenchView view =(BenchView) viewPart;
				page.activate(viewPart);
				return view;
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} 

		}
		

		private BenchView findBenchViewInActivePage() {
			IWorkbenchPage page = PerclipseActivator.getActivePage();
			if (page == null) {
				return null;
			}
			return (BenchView) page.findView(BenchView.MY_VIEW_ID);
		}

		private IJavaProject getJavaProject(
				ILaunchConfiguration configuration) {
			try {
				String projectName = configuration.getAttribute(
						IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
						(String) null);
				if (projectName != null && projectName.length() > 0) {
					return JavaCore.create(ResourcesPlugin.getWorkspace()
							.getRoot().getProject(projectName));
				}
			} catch (CoreException e) {
			}
			return null;
		}
		
		private Display getDisplay(){
			Display display = Display.getCurrent();
			if(display==null){
				display=Display.getDefault();
			}
			return display;
			
		}

		public void connectTestRunner(ILaunch launch,
				IJavaProject javaProject) {
			BenchView benchView=showBenchViewInActivePage(findBenchViewInActivePage());
			PerclipseActivator.getDefault().setBenchView(benchView);
			
			
		}
	}

}
