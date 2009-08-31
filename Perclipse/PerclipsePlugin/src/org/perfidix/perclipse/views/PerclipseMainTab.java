/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.perclipse.views; // NOPMD by lewandow on 8/31/09 2:27 PM

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
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.launcher.PerfidixLaunchConfiguration;
import org.perfidix.perclipse.util.BenchSearchEngine;

/**
 * This class represents the main tab within the launch configuration group of
 * the Perclipse plugin. It specifies the possible launch configurations for
 * this plugin. The User can choose either to bench his whole project or just
 * one special class of a project.
 * 
 * @author Lewandowski L.
 */
public class PerclipseMainTab extends AbstractLaunchConfigurationTab { // NOPMD
    // by
    // lewandow
    // on
    // 8/31/09
    // 2:27
    // PM

    // /**
    // * Empty constructor
    // */
    // public PerclipseMainTab(){
    //        
    // }

    // UI Elements for the single section
    private transient Button singleBenchRB;
    private transient Label fProjLabel;
    private transient Text fProjText;
    private transient Button fSearchButton;
    private transient Button fProjButton;
    private transient Label fBenchLabel;
    private transient Text fBenchText;
    private transient Label fBenchMethodLabel;

    // UI Elements for the all bench section
    private transient Button allBenchsRB;
    private transient Text textAllBenchs;
    private transient Button pButAllBenchs;
    private transient IJavaElement fContainerElement;

    private transient final ILabelProvider jElementLP =
            new JavaElementLabelProvider();
    private transient String origBenchMethName;
    private transient final IPath iconPath = new Path("icons/time.png");
    private transient final Image fTabIcon =
            createImageDescriptor(
                    PerclipseActivator.getDefault().getBundle(), iconPath, true)
                    .createImage();

    /** {@inheritDoc} */
    public void createControl(final Composite parent) {
        final Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);

        final GridLayout topLayout = new GridLayout();
        topLayout.numColumns = 3;
        comp.setLayout(topLayout);

        createSingleBenchSection(comp);

        createAllBenchsSection(comp);

        setEnableSingleBenchGroup(true);
        setEnableAllBenchGroup(false);

    }

    /** {@inheritDoc} */
    public void initializeFrom(final ILaunchConfiguration config) {
        updateProjectFromConfig(config);
        String containerHandle = ""; //$NON-NLS-1$
        try {
            containerHandle =
                    config.getAttribute(
                            PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, ""); //$NON-NLS-1$
        } catch (CoreException ce) {
            PerclipseActivator.log(ce);
        }

        if (containerHandle.length() > 0) {
            updateBenchAllFromConfig(config);

        } else {

            updateBenchTypeFromConfig(config);
        }
    }

    /** {@inheritDoc} */
    public void performApply(final ILaunchConfigurationWorkingCopy config) {

        if (allBenchsRB.getSelection() && fContainerElement != null) {

            performApplyContainer(config, fContainerElement);

        } else {

            config.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    fProjText.getText());
            config.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    fBenchText.getText());
            config.setAttribute(
                    PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, ""); //$NON-NLS-1$
            config.setAttribute(
                    PerfidixLaunchConfiguration.BENCH_NAME_ATTR,
                    origBenchMethName);
        }

    }

    /** {@inheritDoc} */
    public void setDefaults(final ILaunchConfigurationWorkingCopy config) {
        final IJavaElement javaElement = getContext();
        if (javaElement == null) {
            config.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
            config.setAttribute(
                    PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, ""); //$NON-NLS-1$
        } else {
            initializeJavaProject(javaElement, config);
        }
        initializeBenchAttributes(javaElement, config);

    }

    /** {@inheritDoc} */
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
    private void initializeBenchAttributes(
            final IJavaElement javaElement,
            final ILaunchConfigurationWorkingCopy config) {
        if (javaElement != null
                && javaElement.getElementType() < IJavaElement.COMPILATION_UNIT) {
            initializeBenchContainer(javaElement, config);
        } else {
            initializeBenchType(javaElement, config);
        }
    }

    /**
     * This method set attributes of a given
     * {@link ILaunchConfigurationWorkingCopy} and calls afterwards the method.
     * 
     * @param javaElement
     *            is the given not null {@link IJavaElement}
     * @param config
     *            is a given {@link ILaunchConfigurationWorkingCopy} type, which
     *            can change its attributes for a given LaunchConfiguration
     */
    private void initializeBenchContainer(
            final IJavaElement javaElement,
            final ILaunchConfigurationWorkingCopy config) {
        config.setAttribute(
                PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, javaElement
                        .getHandleIdentifier());
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
    private void initializeName(
            final ILaunchConfigurationWorkingCopy config, final String name) {
        String usedName = name;
        if (usedName == null) {
            usedName = ""; //$NON-NLS-1$
        }
        if (usedName.length() > 0) {
            final int index = usedName.lastIndexOf('.');
            if (index > 0) {
                usedName = usedName.substring(index + 1);
            }
            usedName = getLaunchConfigurationDialog().generateName(usedName);
            config.rename(usedName);
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
    private void initializeBenchType(
            final IJavaElement javaElement,
            final ILaunchConfigurationWorkingCopy config) {
        String name = ""; //$NON-NLS-1$
        try {
            // we only do a search for compilation units or class files or
            // or source references
            if ((javaElement instanceof ICompilationUnit)
                    || (javaElement instanceof ISourceReference)
                    || (javaElement instanceof IClassFile)) {

                final IType[] types =
                        BenchSearchEngine
                                .findBenchs(new Object[] { javaElement });
                if ((types == null) || (types.length < 1)) {
                    return;
                }
                // Simply grab the first main type found in the searched element
                name = types[0].getFullyQualifiedName();
            }
        } catch (InterruptedException ie) {
            PerclipseActivator.log(ie);
        } catch (InvocationTargetException ite) {
            PerclipseActivator.log(ite);
        }
        if (name == null) {
            name = ""; //$NON-NLS-1$
        }
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
    private void setBenchMethodLabel(final String benchMethodName) {
        if ("".equals(benchMethodName)) {
            fBenchMethodLabel.setText("");
        } else {
            fBenchMethodLabel.setText("BenchMethod:" + origBenchMethName);
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
    private void updateBenchTypeFromConfig(final ILaunchConfiguration config) {
        String benchTypeName = ""; //$NON-NLS-1$
        origBenchMethName = ""; //$NON-NLS-1$
        try {
            benchTypeName =
                    config
                            .getAttribute(
                                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                                    ""); //$NON-NLS-1$
            origBenchMethName =
                    config.getAttribute(
                            PerfidixLaunchConfiguration.BENCH_NAME_ATTR, ""); //$NON-NLS-1$
        } catch (CoreException ce) {
            PerclipseActivator.log(ce);
        }
        singleBenchRB.setSelection(true);
        setEnableSingleBenchGroup(true);
        fBenchText.setText(benchTypeName);
        //fContainerText.setText(""); //$NON-NLS-1$
        setBenchMethodLabel(origBenchMethName);
    }

    /**
     * This method updates the project name from the
     * {@link ILaunchConfiguration} instance.
     * 
     * @param config
     *            is the {@link ILaunchConfiguration} instance
     */
    private void updateProjectFromConfig(final ILaunchConfiguration config) {

        String projectName = ""; //$NON-NLS-1$
        try {

            projectName =
                    config
                            .getAttribute(
                                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                                    ""); //$NON-NLS-1$

        } catch (CoreException ce) {
            PerclipseActivator.log(ce);
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
    private void createSingleBenchSection(final Composite comp) {
        singleBenchRB = new Button(comp, SWT.RADIO);
        singleBenchRB.setText("Run a single bench");
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        singleBenchRB.setLayoutData(gridData);
        singleBenchRB.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
                if (singleBenchRB.getSelection()) {
                    benchModeChanged();
                }
            }
        });

        fProjLabel = new Label(comp, SWT.NONE);
        fProjLabel.setText("Project: ");
        gridData = new GridData();
        gridData.horizontalIndent = 25;
        fProjLabel.setLayoutData(gridData);

        fProjText = new Text(comp, SWT.SINGLE | SWT.BORDER);
        fProjText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fProjText.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent event) {
                validatePage();
                updateLaunchConfigurationDialog();
                fSearchButton.setEnabled(singleBenchRB.getSelection()
                        && fProjText.getText().length() > 0);
            }
        });

        fProjButton = new Button(comp, SWT.PUSH);
        fBenchLabel = new Label(comp, SWT.NONE);
        fBenchText = new Text(comp, SWT.SINGLE | SWT.BORDER);

        fProjButton.setText("Browse...");
        fProjButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
                handleProjectButtonSelected(fProjButton);
            }
        });
        setButtonGridData(fProjButton);

        gridData = new GridData();
        gridData.horizontalIndent = 25;
        fBenchLabel.setLayoutData(gridData);
        fBenchLabel.setText("Bench class: ");

        fBenchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fBenchText.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent event) {
                validatePage();
                updateLaunchConfigurationDialog();
            }
        });

        fSearchButton = new Button(comp, SWT.PUSH);
        fSearchButton.setEnabled(fProjText.getText().length() > 0);
        fSearchButton.setText("Search...");
        fSearchButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
                handleSearchButtonSelected();
            }
        });
        setButtonGridData(fSearchButton);

        fBenchMethodLabel = new Label(comp, SWT.NONE);
        fBenchMethodLabel.setText(""); //$NON-NLS-1$
        gridData = new GridData();
        gridData.horizontalSpan = 2;
        fBenchMethodLabel.setLayoutData(gridData);

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
    private void createAllBenchsSection(final Composite comp) {
        allBenchsRB = new Button(comp, SWT.RADIO);
        allBenchsRB.setText("Run all benchs in the selected project:");
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        allBenchsRB.setLayoutData(gridData);
        allBenchsRB.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
                if (allBenchsRB.getSelection()) {
                    benchModeChanged();
                }
            }
        });
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalIndent = 25;
        gridData.horizontalSpan = 2;

        textAllBenchs = new Text(comp, SWT.SINGLE | SWT.BORDER);
        textAllBenchs.setLayoutData(gridData);
        textAllBenchs.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent event) {
                validatePage();
                updateLaunchConfigurationDialog();
            }
        });
        pButAllBenchs = new Button(comp, SWT.PUSH);
        pButAllBenchs.setText("Search...");
        pButAllBenchs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
                handleProjectButtonSelected(pButAllBenchs);
            }
        });
        setButtonGridData(pButAllBenchs);

    }

    /**
     * This method creates a new {@link GridData} for a given Button
     * 
     * @param button
     *            is a given {@link Button}
     */
    private void setButtonGridData(final Button button) {
        final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        button.setLayoutData(gridData);
        // TODO look after this one
        // LayoutUtil.setButtonDimensionHint(button);
    }

    /**
     * This method is responsible for actions which has to be done when the
     * SearchButton was selected. It searches in the current project for class
     * objects which contain at least one bench.
     */
    private void handleSearchButtonSelected() {
        final Shell shell = getShell();

        IJavaProject javaProject = getJavaProject();

        IType[] types = new IType[0];
        boolean[] radioSetting = new boolean[2];
        try {
            radioSetting[0] = singleBenchRB.getSelection();
            // radioSetting[1]= fTestContainerRadioButton.getSelection();

            types =
                    BenchSearchEngine.findBenchs(
                            getLaunchConfigurationDialog(),
                            new Object[] { javaProject });
        } catch (InterruptedException exce) {
            PerclipseActivator.log(exce);
            setErrorMessage(exce.getMessage());
        } catch (InvocationTargetException exce) {
            PerclipseActivator.log(exce);
        } finally {
            singleBenchRB.setSelection(radioSetting[0]);

        }

        final SelectionDialog dialog = new BenchSelectionDialog(shell, types);
        dialog.setTitle("Search Bench types");
        dialog.setMessage("Search Bench types");
        if (dialog.open() != Window.CANCEL) {
            final Object[] results = dialog.getResult();
            if ((results != null) && (results.length >= 1)) {

                final IType type = (IType) results[0];

                if (type != null) {
                    fBenchText.setText(type.getFullyQualifiedName('.'));
                    javaProject = type.getJavaProject();
                    fProjText.setText(javaProject.getElementName());
                }
            }

        }

    }

    /**
     * This method lets the user to choose a selected project with a dialog. The
     * selected project will be returned as {@link IJavaProject}.
     * 
     * @return the project as an instance of {@link IJavaProject}
     */
    private IJavaProject chooseJavaProject() {
        IJavaProject retProject = null;
        IJavaProject[] projects;
        try {
            projects = JavaCore.create(getWorkspaceRoot()).getJavaProjects();
        } catch (JavaModelException e) {
            PerclipseActivator.log(e);
            projects = new IJavaProject[0];
        }

        final ILabelProvider labelProvider =
                new JavaElementLabelProvider(
                        JavaElementLabelProvider.SHOW_DEFAULT);
        final ElementListSelectionDialog dialog =
                new ElementListSelectionDialog(getShell(), labelProvider);
        dialog.setTitle("Select Project");
        dialog.setMessage("Select Project");
        dialog.setElements(projects);

        final IJavaProject javaProject = getJavaProject();
        if (javaProject != null) {
            dialog.setInitialSelections(new Object[] { javaProject });
        }
        if (dialog.open() == Window.OK) {
            retProject = (IJavaProject) dialog.getFirstResult();
        }
        return retProject;
    }

    /**
     * This method reacts on the user interaction when the project button was
     * selected and lets the user choose a given project for a Perfidix
     * benchmark. It calls the chooseJavaProject method and sets the text label
     * with the selected project.
     * 
     * @param button
     *            is a given button of type {@link Button} to react on an user
     *            action for this special button.
     */
    private void handleProjectButtonSelected(final Button button) {

        final IJavaProject project = chooseJavaProject();
        fContainerElement = project;
        if (project != null) {
            final String projectName = project.getElementName();

            if (button.equals(fProjButton)) {
                fProjText.setText(projectName);
                fBenchText.setText("");
            }
            if (button.equals(pButAllBenchs)) {
                textAllBenchs.setText(projectName);
            }
            validatePage();
            updateLaunchConfigurationDialog();
        }

    }

    /**
     * This method is called when an user select one of the two radio buttons
     * fSingleBenchRadioButton or fAllBenchsRadioButton and changes the values
     * of the LaunchConfigurations.
     */
    private void benchModeChanged() {
        final boolean isSingleBenchMode = singleBenchRB.getSelection();
        setEnableSingleBenchGroup(isSingleBenchMode);
        setEnableAllBenchGroup(!isSingleBenchMode);

        if (!isSingleBenchMode && textAllBenchs.getText().length() == 0) {
            final IJavaProject javaProject =
                    getJavaModel().getJavaProject(fProjText.getText());
            if (javaProject != null && javaProject.exists()) {
                fContainerElement = javaProject;
            }
            textAllBenchs.setText(getPresentationName(fContainerElement));
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
    private boolean validateJavaProject(final IJavaProject javaProject) {
        boolean retValue = true;
        if (!BenchSearchEngine.hasBenchType(javaProject)) {
            setErrorMessage("No Benchs on path");
            retValue = false;
        }
        return retValue;
    }

    /**
     * This method validates the MainTab text widgets depending on the selection
     * of the radio buttons. First it checks which radio button is selected.
     * Then second it validates the values of the text widgets. It checks for
     * existing benchs.
     */
    private void validatePage() {
        setErrorMessage(null);
        setMessage(null);

        if (allBenchsRB.getSelection()) {
            if (fContainerElement == null) {
                setErrorMessage("No java project selected");
            } else {
                validateJavaProject(fContainerElement.getJavaProject());
            }
        } else {
            final String projectName = fProjText.getText().trim();
            if (projectName.length() == 0) {
                setErrorMessage("Project not defined");
            } else {
                final IStatus status =
                        ResourcesPlugin.getWorkspace().validatePath(
                                IPath.SEPARATOR + projectName,
                                IResource.PROJECT);
                if (status.isOK()) {
                    final IProject project =
                            getWorkspaceRoot().getProject(projectName);
                    if (project.exists()) {
                        try {
                            if (project.hasNature(JavaCore.NATURE_ID)) {

                                final String className =
                                        fBenchText.getText().trim();
                                if (className.length() == 0) {
                                    setErrorMessage("No Benchs defined!");
                                }
                            } else {
                                setErrorMessage("Project is not a Java Project");

                            }
                        } catch (Exception e) {
                            PerclipseActivator.log(e);
                        }
                        final IJavaProject javaProject =
                                JavaCore.create(project);
                        validateJavaProject(javaProject);
                    } else {
                        setErrorMessage("Project does not exists!");

                    }
                } else {
                    setErrorMessage(MessageFormat
                            .format(
                                    "Invalid Projectname",
                                    new Object[] { projectName }));

                }

            }

        }
    }

    /**
     * This method gives the java project of an entered String into the
     * fProjText widget. If the entered text is an existing project it returns
     * the java project object, otherwise null.
     * 
     * @return the existing java project of type {@link IJavaProject} or null
     */
    private IJavaProject getJavaProject() {
        IJavaProject retProject = null;
        final String projectName = fProjText.getText().trim();
        if (projectName.length() >= 1) {
            retProject = getJavaModel().getJavaProject(projectName);
        }
        return retProject;
    }

    /**
     * This method returns the PresentationName of a given {@link IJavaElement}
     * 
     * @param element
     *            is a given {@link IJavaElement}
     * @return a String value of the fJavaElementLabelProvider as the
     *         PresentationName
     */
    private String getPresentationName(final IJavaElement element) {
        return jElementLP.getText(element);
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
    private void setEnableSingleBenchGroup(final boolean enabled) {
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
    private void setEnableAllBenchGroup(final boolean enabled) {
        pButAllBenchs.setEnabled(enabled);
        textAllBenchs.setEnabled(enabled);
    }

    /**
     * This method returns the context of type {@link IJavaElement}
     * 
     * @return the context of type {@link IJavaElement}
     */
    private IJavaElement getContext() {
        IJavaElement retElement = null;
        final IWorkbenchWindow actWorkWin =
                PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (actWorkWin != null) {
            final IWorkbenchPage page = actWorkWin.getActivePage();
            if (page != null) {
                final ISelection selection = page.getSelection();
                if (selection instanceof IStructuredSelection) {
                    final IStructuredSelection structSel =
                            (IStructuredSelection) selection;
                    if (!structSel.isEmpty()) {
                        final Object obj = structSel.getFirstElement();
                        if (obj instanceof IJavaElement) {
                            retElement = (IJavaElement) obj;
                        }
                        if (obj instanceof IResource) {
                            IJavaElement jElement =
                                    JavaCore.create((IResource) obj);
                            if (jElement == null) {
                                final IProject pro =
                                        ((IResource) obj).getProject();
                                jElement = JavaCore.create(pro);
                            }
                            if (jElement != null) {
                                retElement = jElement;
                            }
                        }
                    }
                }
                final IEditorPart part = page.getActiveEditor();
                if (part != null) {
                    final IEditorInput input = part.getEditorInput();
                    retElement =
                            (IJavaElement) input.getAdapter(IJavaElement.class);
                }
            }
        }
        return retElement;
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
    private void initializeJavaProject(
            final IJavaElement javaElement,
            final ILaunchConfigurationWorkingCopy config) {
        final IJavaProject javaProject = javaElement.getJavaProject();
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
     * @param missIDescr
     *            is the boolean value that specifies if an ImageDescriptor can
     *            consist of a shared descriptor for a missing image
     * @return an ImageDescriptor created of an URL or of a shared image
     *         descriptor for a missing image
     */
    private static ImageDescriptor createImageDescriptor(
            final Bundle bundle, final IPath path, final boolean missIDescr) {
        ImageDescriptor retDescriptor = null;
        final URL url = FileLocator.find(bundle, path, null);
        if (url != null) {
            retDescriptor = ImageDescriptor.createFromURL(url);
        }
        if (missIDescr) {
            retDescriptor = ImageDescriptor.getMissingImageDescriptor();
        }
        return retDescriptor;
    }

    /**
     * This method sets the values of the widgets in the MainTab
     * AllBenchSection. The values are loaded from the
     * {@link ILaunchConfiguration} instance.
     * 
     * @param config
     *            is type of {@link ILaunchConfiguration}
     */
    private void updateBenchAllFromConfig(final ILaunchConfiguration config) {
        String containerHandle = ""; //$NON-NLS-1$
        IJavaElement containerElement = null;
        try {
            containerHandle =
                    config.getAttribute(
                            PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, ""); //$NON-NLS-1$
            if (containerHandle.length() > 0) {
                containerElement = JavaCore.create(containerHandle);
            }
        } catch (CoreException ce) {
            PerclipseActivator.log(ce);
        }
        if (containerElement != null) {
            fContainerElement = containerElement;
        }
        allBenchsRB.setSelection(true);
        setEnableSingleBenchGroup(false);
        setEnableAllBenchGroup(true);
        singleBenchRB.setSelection(false);
        if (fContainerElement != null) {
            textAllBenchs.setText(getPresentationName(fContainerElement));
        }
        //$NON-NLS-1$
    }

    /** {@inheritDoc} */
    @Override
    public Image getImage() {

        return fTabIcon;

    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        super.dispose();
        jElementLP.dispose();
        fTabIcon.dispose();
    }

    private void performApplyContainer(
            final ILaunchConfigurationWorkingCopy config,
            final IJavaElement element) {

        if (validateJavaProject(element.getJavaProject())) {
            config.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    element.getJavaProject().getElementName());
            config.setAttribute(
                    PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, element
                            .getHandleIdentifier());
            config.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$
            // workaround for bug 65399
            config
                    .setAttribute(
                            PerfidixLaunchConfiguration.BENCH_NAME_ATTR, ""); //$NON-NLS-1$
        } else {
            config.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
            config.setAttribute(
                    PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, "");
            config.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""); //$NON-NLS-1$
            // workaround for bug 65399
        }
    }

}
