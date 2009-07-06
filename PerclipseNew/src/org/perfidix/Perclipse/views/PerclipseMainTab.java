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

/**
 * This class represents the main tab within the launch configuration group of
 * the Perclipse plugin. It specifies the possible launch configurations for
 * this plugin. The User can choose either to bench his whole project or just
 * one special class of a project.
 * 
 * @author Lewandowski L.
 * 
 */
public class PerclipseMainTab extends AbstractLaunchConfigurationTab {

	// UI Elements for the single section
	private Button fSingleBenchRadioButton;
	private Label fProjLabel;
	private Text fProjText;
	private Button fSearchButton;
	private Button fProjButton;
	private Label fBenchLabel;
	private Text fBenchText;
	private Label fBenchMethodLabel;

	// UI Elements for the all bench section
	private Button fAllBenchsRadioButton;
	private Text fProjTextforAllBenchs;
	private Button fProjButtonforAllBenchs;
	private IJavaElement fContainerElement;

	private final ILabelProvider fJavaElementLabelProvider = new JavaElementLabelProvider();
	private String fOriginalBenchMethodName;
	private final IPath ICON_PATH = new Path("icons/time.png");
	private  Image fTabIcon = createImageDescriptor(
			PerclipseActivator.getDefault().getBundle(), ICON_PATH, true)
			.createImage();

	/* * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse
	 * .swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);

		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 3;
		comp.setLayout(topLayout);

		createSingleBenchSection(comp);

		createAllBenchsSection(comp);

		setEnableSingleBenchGroup(true);
		setEnableAllBenchGroup(false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse
	 * .debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration config) {
		updateProjectFromConfig(config);
		String containerHandle = ""; //$NON-NLS-1$
		try {
			containerHandle = config.getAttribute(
					PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR, ""); //$NON-NLS-1$
		} catch (CoreException ce) {
		}

		if (containerHandle.length() > 0) {
			updateBenchAllFromConfig(config);

		} else {

			updateBenchTypeFromConfig(config);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse
	 * .debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy config) {

		if (fAllBenchsRadioButton.getSelection() && fContainerElement != null) {
			

			
			performApplyContainer(config, fContainerElement);
			

		} else {

			config.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					fProjText.getText());
			config.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
					fBenchText.getText());
			config.setAttribute(
					PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR, ""); //$NON-NLS-1$
			config.setAttribute(PerfidixLaunchConfiguration.BENCH_NAME_ATTR,
					fOriginalBenchMethodName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.
	 * debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		IJavaElement javaElement = getContext();
		if (javaElement != null) {
			initializeJavaProject(javaElement, config);
		} else {
			config.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
			config.setAttribute(
					PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR, ""); //$NON-NLS-1$
		}
		initializeBenchAttributes(javaElement, config);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "Benchs";
	}

	/**
	 * This method gets an {@link IJavaElement} and an
	 * {@link ILaunchConfigurationWorkingCopy} and checks afterwards if its null
	 * or not and decides whether it calls the initializeBenchContainer method
	 * or the initializeBenchType method.
	 * 
	 * @param javaElement
	 *            is a given {@link IJavaElement}
	 * @param config
	 *            is a {@link ILaunchConfigurationWorkingCopy} and contains
	 *            elements specified by the user
	 */
	private void initializeBenchAttributes(IJavaElement javaElement,
			ILaunchConfigurationWorkingCopy config) {
		if (javaElement != null
				&& javaElement.getElementType() < IJavaElement.COMPILATION_UNIT)
			initializeBenchContainer(javaElement, config);
		else
			initializeBenchType(javaElement, config);
	}

	/**
	 * This method set attributes of a given
	 * {@link ILaunchConfigurationWorkingCopy} and calls afterwards the
	 *  method.
	 * 
	 * @param javaElement
	 *            is the given not null {@link IJavaElement}
	 * @param config
	 *            is a given {@link ILaunchConfigurationWorkingCopy} type, which
	 *            can change its attributes for a given LaunchConfiguration
	 */
	private void initializeBenchContainer(IJavaElement javaElement,
			ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR,
				javaElement.getHandleIdentifier());
		initializeName(config, javaElement.getElementName());
	}

	/**
	 * This method receives an {@link ILaunchConfigurationWorkingCopy} and a
	 * name representing a String to rename the current
	 * LaunchConfigurationWorkingCopy.
	 * 
	 * @param config
	 *            represents the current {@link ILaunchConfigurationWorkingCopy}
	 *            to be renamed
	 * @param name
	 *            is a String which represent the value of the new name of the
	 *            current ILaunchConfigurationWorkingCopy
	 */
	private void initializeName(ILaunchConfigurationWorkingCopy config,
			String name) {
		if (name == null) {
			name = ""; //$NON-NLS-1$
		}
		if (name.length() > 0) {
			int index = name.lastIndexOf('.');
			if (index > 0) {
				name = name.substring(index + 1);
			}
			name = getLaunchConfigurationDialog().generateName(name);
			config.rename(name);
		}
	}

	/**
	 * This method checks the given {@link IJavaElement} for a specific type and
	 * searches afterwards for existing Benchs
	 * 
	 * @param javaElement
	 *            represents a given {@link IJavaElement}
	 * @param config
	 *            represents the current {@link ILaunchConfigurationWorkingCopy}
	 */
	private void initializeBenchType(IJavaElement javaElement,
			ILaunchConfigurationWorkingCopy config) {
		String name = ""; //$NON-NLS-1$
		try {
			// we only do a search for compilation units or class files or
			// or source references
			if ((javaElement instanceof ICompilationUnit)
					|| (javaElement instanceof ISourceReference)
					|| (javaElement instanceof IClassFile)) {

				IType[] types = BenchSearchEngine
						.findBenchs(new Object[] { javaElement });
				if ((types == null) || (types.length < 1)) {
					return;
				}
				// Simply grab the first main type found in the searched element
				name = types[0].getFullyQualifiedName();
			}
		} catch (InterruptedException ie) {
		} catch (InvocationTargetException ite) {
		}
		if (name == null)
			name = ""; //$NON-NLS-1$
		config.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, name);
		initializeName(config, name);
	}

	/**
	 * This method sets the SWT Label with the original bench method name.
	 * 
	 * @param benchMethodName
	 *            is the given String value of name
	 */
	private void setBenchMethodLabel(String benchMethodName) {
		if (!"".equals(benchMethodName)) { //$NON-NLS-1$
			fBenchMethodLabel
					.setText("BenchMethod:" + fOriginalBenchMethodName);
		} else {
			fBenchMethodLabel.setText(""); //$NON-NLS-1$
		}
	}

	/**
	 * This method sets the bench type and the bench text from the
	 * {@link ILaunchConfiguration} instance and selects and enables the
	 * SingleBenchGroup.
	 * 
	 * @param config
	 *            is an instance of the {@link ILaunchConfiguration}
	 */
	private void updateBenchTypeFromConfig(ILaunchConfiguration config) {
		String benchTypeName = ""; //$NON-NLS-1$
		fOriginalBenchMethodName = ""; //$NON-NLS-1$
		try {
			benchTypeName = config.getAttribute(
					IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$
			fOriginalBenchMethodName = config.getAttribute(
					PerfidixLaunchConfiguration.BENCH_NAME_ATTR, ""); //$NON-NLS-1$
		} catch (CoreException ce) {
		}
		fSingleBenchRadioButton.setSelection(true);
		setEnableSingleBenchGroup(true);
		fBenchText.setText(benchTypeName);
		//fContainerText.setText(""); //$NON-NLS-1$
		setBenchMethodLabel(fOriginalBenchMethodName);
	}

	/**
	 * This method updates the project name from the
	 * {@link ILaunchConfiguration} instance.
	 * 
	 * @param config
	 *            is the {@link ILaunchConfiguration} instance
	 */
	private void updateProjectFromConfig(ILaunchConfiguration config) {

		String projectName = ""; //$NON-NLS-1$
		try {

			projectName = config.getAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$

		} catch (CoreException ce) {
			ce.printStackTrace();
		}
		fProjText.setText(projectName);
	}

	/**
	 * This method creates the SWT widgets for the SingleBenchSection of the
	 * LaunchConfiguration MainTab. It contains several label, text fields and
	 * some button widgets with their corresponding listener objects. The
	 * buttons react on the user interaction with their SelctionListeners.
	 * 
	 * @param comp
	 *            is of type {@link Composite} and will contain the afterwards
	 *            specified widgets
	 */
	private void createSingleBenchSection(Composite comp) {
		fSingleBenchRadioButton = new Button(comp, SWT.RADIO);
		fSingleBenchRadioButton.setText("Run a single bench");
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		fSingleBenchRadioButton.setLayoutData(gd);
		fSingleBenchRadioButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (fSingleBenchRadioButton.getSelection())
					benchModeChanged();
			}
		});

		fProjLabel = new Label(comp, SWT.NONE);
		fProjLabel.setText("Project: ");
		gd = new GridData();
		gd.horizontalIndent = 25;
		fProjLabel.setLayoutData(gd);

		fProjText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		fProjText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fProjText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				validatePage();
				updateLaunchConfigurationDialog();
				fSearchButton.setEnabled(fSingleBenchRadioButton.getSelection()
						&& fProjText.getText().length() > 0);
			}
		});

		fProjButton = new Button(comp, SWT.PUSH);
		fBenchLabel = new Label(comp, SWT.NONE);
		fBenchText = new Text(comp, SWT.SINGLE | SWT.BORDER);

		fProjButton.setText("Browse...");
		fProjButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleProjectButtonSelected(fProjButton);
			}
		});
		setButtonGridData(fProjButton);

		gd = new GridData();
		gd.horizontalIndent = 25;
		fBenchLabel.setLayoutData(gd);
		fBenchLabel.setText("Bench class: ");

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

		fBenchMethodLabel = new Label(comp, SWT.NONE);
		fBenchMethodLabel.setText(""); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalSpan = 2;
		fBenchMethodLabel.setLayoutData(gd);

	}

	/**
	 * This method creates the SWT widgets for the AllBenchsSection. The
	 * AllBenchsSection lets the user choose a project which has at least one
	 * bench. Afterwards the user can run the launch configuration and every
	 * bench in the chosen project will be evaluated.
	 * 
	 * @param comp
	 *            is of type {@link Composite} and will contain the widgets
	 *            which will be specified in this method
	 */
	private void createAllBenchsSection(Composite comp) {
		fAllBenchsRadioButton = new Button(comp, SWT.RADIO);
		fAllBenchsRadioButton
				.setText("Run all benchs in the selected project:");
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		fAllBenchsRadioButton.setLayoutData(gd);
		fAllBenchsRadioButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (fAllBenchsRadioButton.getSelection()) {
					benchModeChanged();
				}
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = 25;
		gd.horizontalSpan = 2;

		fProjTextforAllBenchs = new Text(comp, SWT.SINGLE | SWT.BORDER);
		fProjTextforAllBenchs.setLayoutData(gd);
		fProjTextforAllBenchs.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				validatePage();
				updateLaunchConfigurationDialog();
			}
		});
		fProjButtonforAllBenchs = new Button(comp, SWT.PUSH);
		fProjButtonforAllBenchs.setText("Search...");
		fProjButtonforAllBenchs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleProjectButtonSelected(fProjButtonforAllBenchs);
			}
		});
		setButtonGridData(fProjButtonforAllBenchs);

	}

	/**
	 * This method creates a new {@link GridData} for a given Button
	 * 
	 * @param button
	 *            is a given {@link Button}
	 */
	private void setButtonGridData(Button button) {
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		button.setLayoutData(gridData);
		// TODO look after this one
		// LayoutUtil.setButtonDimensionHint(button);
	}

	/**
	 * This method is responsible for actions which has to be done when the
	 * SearchButton was selected. It searches in the current project for class
	 * objects which contain at least one bench.
	 * 
	 */
	private void handleSearchButtonSelected() {
		Shell shell = getShell();

		IJavaProject javaProject = getJavaProject();

		IType[] types = new IType[0];
		boolean[] radioSetting = new boolean[2];
		try {
			radioSetting[0] = fSingleBenchRadioButton.getSelection();
			// radioSetting[1]= fTestContainerRadioButton.getSelection();

			types = BenchSearchEngine.findBenchs(
					getLaunchConfigurationDialog(),
					new Object[] { javaProject });
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
		IType type = (IType) results[0];

		if (type != null) {
			fBenchText.setText(type.getFullyQualifiedName('.'));
			javaProject = type.getJavaProject();
			fProjText.setText(javaProject.getElementName());
		}
	}

	/**
	 * This method lets the user to choose a selected project with a dialog. The
	 * selected project will be returned as {@link IJavaProject}.
	 * 
	 * @return the project as an instance of {@link IJavaProject}
	 */
	private IJavaProject chooseJavaProject() {
		IJavaProject[] projects;
		try {
			projects = JavaCore.create(getWorkspaceRoot()).getJavaProjects();
		} catch (JavaModelException e) {
			projects = new IJavaProject[0];
		}

		ILabelProvider labelProvider = new JavaElementLabelProvider(
				JavaElementLabelProvider.SHOW_DEFAULT);
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				getShell(), labelProvider);
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

	/**
	 * This method reacts on the user interaction when the project button was
	 * selected and lets the user choose a given project for a Perfidix
	 * benchmark. It calls the chooseJavaProject method and sets the text label
	 * with the selected project.
	 * 
	 * @param bt
	 *            is a given button of type {@link Button} to react on an user
	 *            action for this special button.
	 */
	private void handleProjectButtonSelected(Button bt) {

		IJavaProject project = chooseJavaProject();
		fContainerElement = project;
		if (project == null) {
			return;
		}

		String projectName = project.getElementName();

		if (bt == fProjButton) {
			fProjText.setText(projectName);
			fBenchText.setText("");
		}
		if (bt == fProjButtonforAllBenchs) {
			fProjTextforAllBenchs.setText(projectName);
		}
		validatePage();
		updateLaunchConfigurationDialog();
	}

	/**
	 * This method is called when an user select one of the two radio buttons
	 * fSingleBenchRadioButton or fAllBenchsRadioButton and changes the values
	 * of the LaunchConfigurations.
	 * 
	 */
	private void benchModeChanged() {
		boolean isSingleBenchMode = fSingleBenchRadioButton.getSelection();
		setEnableSingleBenchGroup(isSingleBenchMode);
		setEnableAllBenchGroup(!isSingleBenchMode);

		if (!isSingleBenchMode && fProjTextforAllBenchs.getText().length() == 0) {
			IJavaProject javaProject = getJavaModel().getJavaProject(
					fProjText.getText());
			if (javaProject != null && javaProject.exists())
				fContainerElement = javaProject;
			fProjTextforAllBenchs
					.setText(getPresentationName(fContainerElement));
		}
		validatePage();
		updateLaunchConfigurationDialog();
	}

	/**
	 * This method validates the entered java project for contained benchs. It
	 * searches the project for benchs. If it has no ones it writes an error
	 * message.
	 * 
	 * @param javaProject
	 */
	private boolean validateJavaProject(IJavaProject javaProject) {
		if (!BenchSearchEngine.hasBenchType(javaProject)) {
			setErrorMessage("No Benchs on path");
			
			return false;
		}
		return true;
	}

	/**
	 * This method validates the MainTab text widgets depending on the selection
	 * of the radio buttons. First it checks which radio button is selected.
	 * Then second it validates the values of the text widgets. It checks for
	 * existing benchs.
	 * 
	 */
	private void validatePage() {
		setErrorMessage(null);
		setMessage(null);

		if (fAllBenchsRadioButton.getSelection()) {
			if (fContainerElement == null) {
				setErrorMessage("No java project selected");
				return;
			}
			validateJavaProject(fContainerElement.getJavaProject());
			return;
		}

		String projectName = fProjText.getText().trim();
		if (projectName.length() == 0) {
			setErrorMessage("Project not defined");
			return;
		}

		IStatus status = ResourcesPlugin.getWorkspace().validatePath(
				IPath.SEPARATOR + projectName, IResource.PROJECT);
		if (!status.isOK()) {
			setErrorMessage(MessageFormat.format("Invalid Projectname",
					new Object[] { projectName }));
			return;
		}

		IProject project = getWorkspaceRoot().getProject(projectName);
		if (!project.exists()) {
			setErrorMessage("Project does not exists!");
			return;
		}

		try {
			if (!project.hasNature(JavaCore.NATURE_ID)) {
				setErrorMessage("Project is not a Java Project");
				return;
			}
			String className = fBenchText.getText().trim();
			if (className.length() == 0) {
				setErrorMessage("No Benchs defined!");
				return;
			}
		} catch (Exception e) {
		}
		IJavaProject javaProject = JavaCore.create(project);
		validateJavaProject(javaProject);
	}

	/**
	 * This method gives the java project of an entered String into the
	 * fProjText widget. If the entered text is an existing project it returns
	 * the java project object, otherwise null.
	 * 
	 * @return the existing java project of type {@link IJavaProject} or null
	 */
	private IJavaProject getJavaProject() {
		String projectName = fProjText.getText().trim();
		if (projectName.length() < 1) {
			return null;
		}
		return getJavaModel().getJavaProject(projectName);
	}

	/**
	 * This method returns the PresentationName of a given {@link IJavaElement}
	 * 
	 * @param element
	 *            is a given {@link IJavaElement}
	 * @return a String value of the fJavaElementLabelProvider as the
	 *         PresentationName
	 */
	private String getPresentationName(IJavaElement element) {
		return fJavaElementLabelProvider.getText(element);
	}

	/**
	 * This method returns the java model from the workspace root.
	 * 
	 * @return the java model of type IJavaModel
	 */
	private IJavaModel getJavaModel() {
		return JavaCore.create(getWorkspaceRoot());
	}

	/**
	 * This method returns the workspace root from the resource plugin.
	 * 
	 * @return the workspace root of type IWorkspaceRoot
	 */
	private IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * This method enables each widget in the SingleBenchGroup of the MainTab
	 * 
	 * @param enabled
	 *            is a boolean value which sets the enabled widgets within the
	 *            SingleBenchGroup either true or false
	 */
	private void setEnableSingleBenchGroup(boolean enabled) {
		fProjLabel.setEnabled(enabled);
		fProjText.setEnabled(enabled);
		fProjButton.setEnabled(enabled);
		fBenchLabel.setEnabled(enabled);
		fBenchText.setEnabled(enabled);
		fSearchButton.setEnabled(enabled && fProjText.getText().length() > 0);
		fBenchMethodLabel.setEnabled(enabled);
	}

	/**
	 * This method enables each widget in the AllBenchGroup of the
	 * PerclipseMainTab
	 * 
	 * @param enabled
	 *            is a boolean value which sets the enabled widgets within the
	 *            AllBenchGroup either true or false
	 */
	private void setEnableAllBenchGroup(boolean enabled) {
		fProjButtonforAllBenchs.setEnabled(enabled);
		fProjTextforAllBenchs.setEnabled(enabled);
	}

	/**
	 * This method returns the context of type {@link IJavaElement}
	 * 
	 * @return the context of type {@link IJavaElement}
	 */
	private IJavaElement getContext() {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return null;
		}
		IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
		if (page != null) {
			ISelection selection = page.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) selection;
				if (!ss.isEmpty()) {
					Object obj = ss.getFirstElement();
					if (obj instanceof IJavaElement) {
						return (IJavaElement) obj;
					}
					if (obj instanceof IResource) {
						IJavaElement je = JavaCore.create((IResource) obj);
						if (je == null) {
							IProject pro = ((IResource) obj).getProject();
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

	/**
	 * This method validates a given {@link IJavaElement} as a java project and
	 * set this attribute in the {@link ILaunchConfigurationWorkingCopy}
	 * 
	 * @param javaElement
	 *            is a given java Element of type {@link IJavaElement}
	 * @param config
	 *            is an instance of {@link ILaunchConfigurationWorkingCopy}
	 *            which will be modified by the given javaElement
	 */
	private void initializeJavaProject(IJavaElement javaElement,
			ILaunchConfigurationWorkingCopy config) {
		IJavaProject javaProject = javaElement.getJavaProject();
		String name = null;
		if (javaProject != null && javaProject.exists()) {
			name = javaProject.getElementName();
		}
		config.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, name);
	}

	/**
	 * The createImageDescriptor method creates a {@link ImageDescriptor} from a
	 * bundle and a relative path. Within the method ImageDescriptor is made by
	 * a specified {@link URL}. The URL consists of the params bundle and path.
	 * 
	 * @param bundle
	 *            is the param which specify the destination bundle of the
	 *            current project files
	 * @param path
	 *            is the relative path of a given image
	 * @param useMissingImageDescriptor
	 *            is the boolean value that specifies if an ImageDescriptor can
	 *            consist of a shared descriptor for a missing image
	 * @return an ImageDescriptor created of an URL or of a shared image
	 *         descriptor for a missing image
	 */
	private static ImageDescriptor createImageDescriptor(Bundle bundle,
			IPath path, boolean useMissingImageDescriptor) {
		URL url = FileLocator.find(bundle, path, null);
		if (url != null) {
			return ImageDescriptor.createFromURL(url);
		}
		if (useMissingImageDescriptor) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
		return null;
	}

	/**
	 * This method sets the values of the widgets in the MainTab
	 * AllBenchSection. The values are loaded from the
	 * {@link ILaunchConfiguration} instance.
	 * 
	 * @param config
	 *            is type of {@link ILaunchConfiguration}
	 */
	private void updateBenchAllFromConfig(ILaunchConfiguration config) {
		String containerHandle = ""; //$NON-NLS-1$
		IJavaElement containerElement = null;
		try {
			containerHandle = config.getAttribute(
					PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR, ""); //$NON-NLS-1$
			if (containerHandle.length() > 0) {
				containerElement = JavaCore.create(containerHandle);
			}
		} catch (CoreException ce) {
		}
		if (containerElement != null)
			fContainerElement = containerElement;

		fAllBenchsRadioButton.setSelection(true);
		setEnableSingleBenchGroup(false);
		setEnableAllBenchGroup(true);
		fSingleBenchRadioButton.setSelection(false);
		if (fContainerElement != null)
			fProjTextforAllBenchs
					.setText(getPresentationName(fContainerElement));
		//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	public Image getImage() {

		return fTabIcon;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#dispose()
	 */
	public void dispose() {
		super.dispose();
		fJavaElementLabelProvider.dispose();
		fTabIcon.dispose();
	}
	
	private void performApplyContainer(ILaunchConfigurationWorkingCopy config, IJavaElement element){
		
		if(!validateJavaProject(element.getJavaProject())){
			config.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					"");
			config.setAttribute(
					PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR,
					"");
			config.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$
			// workaround for bug 65399
		}
		else {
		config.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
				 element.getJavaProject().getElementName());
		config.setAttribute(
				PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR,
				 element.getHandleIdentifier());
		config.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$
		// workaround for bug 65399
		config
				.setAttribute(PerfidixLaunchConfiguration.BENCH_NAME_ATTR,
						""); //$NON-NLS-1$
		}
		}

}
