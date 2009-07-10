package org.perfidix.Perclipse.views;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.osgi.framework.Bundle;
import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.launcher.PerfidixLaunchConfiguration;
import org.perfidix.Perclipse.util.BenchSearchEngine;

public class PerclipseMainTabSingle extends AbstractLaunchConfigurationTab {

	//UI Elements for the single section
	private Button fSingleBenchRadioButton;
	private Label fProjLabel;
	private Text fProjText;
	private Button fSearchButton;
	private Button fProjButton;
	private Label fBenchLabel;
	private Text fBenchText;
	private Label fBenchMethodLabel;
	
	//UI Elements for the all bench section
	private Button fAllBenchsRadioButton;
	private Text fProjTextforAllBenchs;
	
	
	private final ILabelProvider fJavaElementLabelProvider= new JavaElementLabelProvider();
	private String fOriginalBenchMethodName;
	private IPath path = new Path("icons/test.gif");
	private final Image fTabIcon=createImageDescriptor(PerclipseActivator.getDefault().getBundle(), path, true).createImage();
	
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);

		GridLayout topLayout = new GridLayout();
		topLayout.numColumns= 3;
		comp.setLayout(topLayout);		
		
		createSingleBenchSection(comp);

	

	
	}


	public void initializeFrom(ILaunchConfiguration config) {
		updateProjectFromConfig(config);
//		String containerHandle= ""; //$NON-NLS-1$
//		try {
//			containerHandle = config.getAttribute(PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR, ""); //$NON-NLS-1$
//		} catch (CoreException ce) {			
//		}
		

			updateTestTypeFromConfig(config);
	}

	public void performApply(ILaunchConfigurationWorkingCopy config) {

			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, fProjText.getText());
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, fBenchText.getText());
			config.setAttribute(PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR, ""); //$NON-NLS-1$
			config.setAttribute(PerfidixLaunchConfiguration.BENCH_NAME_ATTR, fOriginalBenchMethodName);
		
		
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		IJavaElement javaElement = getContext();
		if (javaElement != null) {
			initializeJavaProject(javaElement, config);
		} else {
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
			config.setAttribute(PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR, ""); //$NON-NLS-1$
		}
		initializeTestAttributes(javaElement, config);
		
	}
	
	public String getName() {
		return "Benchs";
	}
	
	private void initializeTestAttributes(IJavaElement javaElement, ILaunchConfigurationWorkingCopy config) {
		if (javaElement != null && javaElement.getElementType() < IJavaElement.COMPILATION_UNIT) 
			initializeTestContainer(javaElement, config);
		else
			initializeTestType(javaElement, config);
	}
	
	private void initializeTestContainer(IJavaElement javaElement, ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR, javaElement.getHandleIdentifier());
		initializeName(config, javaElement.getElementName());
	}

	private void initializeName(ILaunchConfigurationWorkingCopy config, String name) {
		if (name == null) {
			name= ""; //$NON-NLS-1$
		}
		if (name.length() > 0) {
			int index = name.lastIndexOf('.');
			if (index > 0) {
				name = name.substring(index + 1);
			}
			name= getLaunchConfigurationDialog().generateName(name);
			config.rename(name);
		}
	}
	
	private void initializeTestType(IJavaElement javaElement, ILaunchConfigurationWorkingCopy config) {
		String name= ""; //$NON-NLS-1$
		try {
			// we only do a search for compilation units or class files or 
			// or source references
			if ((javaElement instanceof ICompilationUnit) || 
				(javaElement instanceof ISourceReference) ||
				(javaElement instanceof IClassFile)) {
		
				IType[] types = BenchSearchEngine.findBenchs(new Object[] {javaElement});
				if ((types == null) || (types.length < 1)) {
					return;
				}
				// Simply grab the first main type found in the searched element
				name= types[0].getFullyQualifiedName();
			}	
		} catch (InterruptedException ie) {
		} catch (InvocationTargetException ite) {
		}
		if (name == null)
			name= ""; //$NON-NLS-1$
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, name);
		initializeName(config, name);
	}
	

	
	private void setTestMethodLabel(String testMethodName) {
		if (!"".equals(testMethodName)) { //$NON-NLS-1$
			fBenchMethodLabel.setText("BenchMethod:"+fOriginalBenchMethodName); 
		} else {
			fBenchMethodLabel.setText(""); //$NON-NLS-1$
		}
	}
	
	private void updateTestTypeFromConfig(ILaunchConfiguration config) {
		String testTypeName= ""; //$NON-NLS-1$
		fOriginalBenchMethodName= ""; //$NON-NLS-1$
		try {
			testTypeName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$
			fOriginalBenchMethodName = config.getAttribute(PerfidixLaunchConfiguration.BENCH_NAME_ATTR, ""); //$NON-NLS-1$
		} catch (CoreException ce) {			
		}
		fSingleBenchRadioButton.setSelection(true);
		setEnableSingleTestGroup(true);
		fBenchText.setText(testTypeName);
		//fContainerText.setText(""); //$NON-NLS-1$
		setTestMethodLabel(fOriginalBenchMethodName);
	}
	
	private void updateProjectFromConfig(ILaunchConfiguration config) {
		
		String projectName= ""; //$NON-NLS-1$
		try {
		
			projectName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
			
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
		fProjText.setText(projectName);
	}
	
	private void createSingleBenchSection(Composite comp) {
		fSingleBenchRadioButton= new Button(comp, SWT.RADIO);
		fSingleBenchRadioButton.setText("Run a single bench"); 
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		fSingleBenchRadioButton.setLayoutData(gd); 
		fSingleBenchRadioButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (fSingleBenchRadioButton.getSelection())
					testModeChanged();
			}
		});
		
		fProjLabel = new Label(comp, SWT.NONE);
		fProjLabel.setText("Project"); 
		gd= new GridData();
		gd.horizontalIndent = 25;
		fProjLabel.setLayoutData(gd);
		
		fProjText= new Text(comp, SWT.SINGLE | SWT.BORDER);
		fProjText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fProjText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				validatePage();
				updateLaunchConfigurationDialog();				
				fSearchButton.setEnabled(fSingleBenchRadioButton.getSelection() && fProjText.getText().length() > 0);
			}
		});
			
		fProjButton = new Button(comp, SWT.PUSH);
		fBenchLabel = new Label(comp, SWT.NONE);
		fBenchText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		
		fProjButton.setText("Browse..."); 
		fProjButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleProjectButtonSelected();
			}
		});
		setButtonGridData(fProjButton);
		
		
		gd = new GridData();
		gd.horizontalIndent = 25;
		fBenchLabel.setLayoutData(gd);
		fBenchLabel.setText("Bench class"); 
		
	
		
		fBenchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fBenchText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				validatePage();
				updateLaunchConfigurationDialog();
			}
		});
		
		fSearchButton = new Button(comp, SWT.PUSH);
		fSearchButton.setEnabled(fProjText.getText().length() > 0);
		fSearchButton.setText("Search..."); 
		fSearchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleSearchButtonSelected();
			}
		});
		setButtonGridData(fSearchButton);
		
		new Label(comp, SWT.NONE);
		
		fBenchMethodLabel= new Label(comp, SWT.NONE);
		fBenchMethodLabel.setText("");  //$NON-NLS-1$
		gd= new GridData();
		gd.horizontalSpan = 2;
		fBenchMethodLabel.setLayoutData(gd);
		
	}
	

	private void setButtonGridData(Button button) {
		GridData gridData= new GridData();
		button.setLayoutData(gridData);
//		TODO look after this one
//		LayoutUtil.setButtonDimensionHint(button);
	}
	
	private void handleSearchButtonSelected() {
		Shell shell = getShell();
		
		IJavaProject javaProject = getJavaProject();
		
		IType[] types= new IType[0];
		boolean[] radioSetting= new boolean[2];
		try {
			radioSetting[0]= fSingleBenchRadioButton.getSelection();
			//radioSetting[1]= fTestContainerRadioButton.getSelection();
			
			types= BenchSearchEngine.findBenchs(getLaunchConfigurationDialog(), new Object[] {javaProject}); 
		} catch (InterruptedException e) {
			setErrorMessage(e.getMessage());
			return;
		} catch (InvocationTargetException e) {
			return;
		} finally {
			fSingleBenchRadioButton.setSelection(radioSetting[0]);

		}

		SelectionDialog dialog = new BenchSelectionDialog(shell, types);
		dialog.setTitle("Search Bench types"); 
		dialog.setMessage("Search Bench types"); 
		if (dialog.open() == Window.CANCEL) {
			return;
		}
		
		Object[] results = dialog.getResult();
		if ((results == null) || (results.length < 1)) {
			return;
		}		
		IType type = (IType)results[0];
		
		if (type != null) {
			fBenchText.setText(type.getFullyQualifiedName('.'));
			javaProject = type.getJavaProject();
			fProjText.setText(javaProject.getElementName());
		}
	}
	
	
	private IJavaProject chooseJavaProject() {
		IJavaProject[] projects;
		try {
			projects= JavaCore.create(getWorkspaceRoot()).getJavaProjects();
		} catch (JavaModelException e) {
			projects= new IJavaProject[0];
		}
		
		ILabelProvider labelProvider= new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setTitle("Select Project"); 
		dialog.setMessage("Select Project"); 
		dialog.setElements(projects);
		
		IJavaProject javaProject = getJavaProject();
		if (javaProject != null) {
			dialog.setInitialSelections(new Object[] { javaProject });
		}
		if (dialog.open() == Window.OK) {			
			return (IJavaProject) dialog.getFirstResult();
		}			
		return null;		
	}
	
	private void handleProjectButtonSelected() {
	
		IJavaProject project = chooseJavaProject();
		if (project == null) {
			return;
		}
		
		String projectName = project.getElementName();
		fProjText.setText(projectName);	
		fBenchText.setText("");
	}
	
	private void testModeChanged() {
		boolean isSingleTestMode= fSingleBenchRadioButton.getSelection();
		setEnableSingleTestGroup(isSingleTestMode);
//
//		if (!isSingleTestMode && fContainerText.getText().length() == 0) {
//			IJavaProject javaProject= getJavaModel().getJavaProject(fProjText.getText());
//			if (javaProject != null && javaProject.exists())
//				setContainerElement(javaProject);
//		}
		validatePage();
		updateLaunchConfigurationDialog();
	}
	

	
	private void validateJavaProject(IJavaProject javaProject) {
		if (! BenchSearchEngine.hasBenchType(javaProject)) {
			setErrorMessage("No Tests on path"); 
			return;				
		}
	}
	
	private void validatePage() {
		setErrorMessage(null);
		setMessage(null);


		String projectName= fProjText.getText().trim();
		if (projectName.length() == 0) {
			setErrorMessage("Project not defined");
			return;
		}

		IStatus status= ResourcesPlugin.getWorkspace().validatePath(IPath.SEPARATOR + projectName, IResource.PROJECT);
		if (!status.isOK()) {
			setErrorMessage(MessageFormat.format("Invalid Projectname", new Object[] { projectName}));
			return;
		}

		IProject project= getWorkspaceRoot().getProject(projectName);
		if (!project.exists()) {
			setErrorMessage("Project does not exists!");
			return;
		}

		try {
			if (!project.hasNature(JavaCore.NATURE_ID)) {
				setErrorMessage("Project is not a Java Project");
				return;
			}
			String className= fBenchText.getText().trim();
			if (className.length() == 0) {
				setErrorMessage("No Tests defined!");
				return;
			}
		} catch (Exception e) {
		}
		IJavaProject javaProject= JavaCore.create(project);
		validateJavaProject(javaProject);
	}
	
	private IJavaProject getJavaProject() {
		String projectName = fProjText.getText().trim();
		if (projectName.length() < 1) {
			return null;
		}
		return getJavaModel().getJavaProject(projectName);		
	}
	
	private String getPresentationName(IJavaElement element) {
		return fJavaElementLabelProvider.getText(element);
	}
	
	private IJavaModel getJavaModel() {
		return JavaCore.create(getWorkspaceRoot());
	}
	
	private IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	

	
	private void setEnableSingleTestGroup(boolean enabled) {
		fProjLabel.setEnabled(enabled);
		fProjText.setEnabled(enabled);
		fProjButton.setEnabled(enabled);
		fBenchLabel.setEnabled(enabled);
		fBenchText.setEnabled(enabled);
		fSearchButton.setEnabled(enabled && fProjText.getText().length() > 0);
		fBenchMethodLabel.setEnabled(enabled);
	}
	
	private IJavaElement getContext() {
		IWorkbenchWindow activeWorkbenchWindow= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return null;
		}
		IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
		if (page != null) {
			ISelection selection = page.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection)selection;
				if (!ss.isEmpty()) {
					Object obj = ss.getFirstElement();
					if (obj instanceof IJavaElement) {
						return (IJavaElement)obj;
					}
					if (obj instanceof IResource) {
						IJavaElement je = JavaCore.create((IResource)obj);
						if (je == null) {
							IProject pro = ((IResource)obj).getProject();
							je = JavaCore.create(pro);
						}
						if (je != null) {
							return je;
						}
					}
				}
			}
			IEditorPart part = page.getActiveEditor();
			if (part != null) {
				IEditorInput input = part.getEditorInput();
				return (IJavaElement) input.getAdapter(IJavaElement.class);
			}
		}
		return null;
	}
	
	private void initializeJavaProject(IJavaElement javaElement, ILaunchConfigurationWorkingCopy config) {
		IJavaProject javaProject = javaElement.getJavaProject();
		String name = null;
		if (javaProject != null && javaProject.exists()) {
			name = javaProject.getElementName();
		}
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, name);
	}
	
	private static ImageDescriptor createImageDescriptor(Bundle bundle, IPath path, boolean useMissingImageDescriptor) {
		         URL url= FileLocator.find(bundle, path, null);
		         if (url != null) {
		             return ImageDescriptor.createFromURL(url);
		         }
		         if (useMissingImageDescriptor) {
		             return ImageDescriptor.getMissingImageDescriptor();
		         }
		         return null;
		     }

	
	public Image getImage() {

		         return fTabIcon;

		     }
	
	public void dispose(){
		super.dispose();
		fJavaElementLabelProvider.dispose();
		fTabIcon.dispose();
	}


}
