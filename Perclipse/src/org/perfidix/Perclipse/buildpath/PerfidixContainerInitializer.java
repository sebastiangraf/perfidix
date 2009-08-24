package org.perfidix.Perclipse.buildpath;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.perfidix.Perclipse.launcher.PerclipseActivator;

/**
 * This class is responsible for initializing the custom perfidix container.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerfidixContainerInitializer extends ClasspathContainerInitializer {
    /**
     * This field specifies the container id, needed by the corresponding
     * extension point.
     */
    public static final String BENCH_CONTAINER_ID =
            "org.perfidix.Perclipse.PERFIDIX_CONTAINER";
    /**
     * This field provides the custom status "not supported".
     */
    public static final IStatus NOT_SUPPORTED =
            new Status(
                    IStatus.ERROR, PerclipseActivator.PLUGIN_ID,
                    ClasspathContainerInitializer.ATTRIBUTE_NOT_SUPPORTED,
                    new String(), null);
    /**
     * This field provides the custom status READ_ONLY with corresponding
     * attributes with read only functions of the classpath container.
     */
    public static final IStatus READ_ONLY =
            new Status(
                    IStatus.ERROR, PerclipseActivator.PLUGIN_ID,
                    ClasspathContainerInitializer.ATTRIBUTE_READ_ONLY,
                    new String(), null);

    /**
     * This field sets the Perfidix path with the bench container id.
     */
    public static final IPath PERFIDIX_PATH = new Path(BENCH_CONTAINER_ID);

    /**
     * This inner class collects information about the classpath container
     * entry.
     * 
     * @author Lewandowski Lukas, DiSy, University of Konstanz
     */
    private static class PerfidixContainer implements IClasspathContainer {

        private final IClasspathEntry[] entries;
        private final IPath path;

        /**
         * The constructor sets the path and the classpath entries.
         * 
         * @param path
         * @param entries
         */
        public PerfidixContainer(IPath path, IClasspathEntry[] entries) {
            this.path = path;
            this.entries = entries;
        }

        /** {@inheritDoc} */
        public IClasspathEntry[] getClasspathEntries() {

            return entries;
        }

        /**
         * This method supports the description of the custom classpath entry.
         * 
         * @see org.eclipse.jdt.core.IClasspathContainer#getDescription()
         */
        public String getDescription() {

            return "Perfidix Library";
        }

        /** {@inheritDoc} */
        public int getKind() {

            return IClasspathContainer.K_APPLICATION;
        }

        /** {@inheritDoc} */
        public IPath getPath() {

            return path;
        }

    }

    /**
     * The empty constructor, needed by the corresponding extension point.
     */
    public PerfidixContainerInitializer() {

    }

    /**
     * This method initializes the custom classpath container with the
     * PerfidixContainer
     * 
     * @param cpath
     *            see the super method.
     * @param project
     *            see the super method.
     * @throws CoreException
     *             see the super method.
     * @see org.eclipse.jdt.core.ClasspathContainerInitializer#initialize(org.eclipse.core.runtime.IPath,
     *      org.eclipse.jdt.core.IJavaProject)
     */
    @Override
    public void initialize(IPath cpath, IJavaProject project)
            throws CoreException {
        if (isValidPerfidixContainerPath(cpath)) {
            PerfidixContainer perfidixContainer = getNewContainer(cpath);
            JavaCore.setClasspathContainer(
                    cpath, new IJavaProject[] { project },
                    new IClasspathContainer[] { perfidixContainer }, null);
        }

    }

    /**
     * This method generates an new custom container with our perfidix library
     * entry.
     * 
     * @param cpath
     *            The classpath of the launched project.
     * @return Returns the custom Perfidix container.
     */
    private PerfidixContainer getNewContainer(IPath cpath) {

        IClasspathEntry entry = null;
        entry = BuildPathSupport.getPerfidixLibraryEntry();

        IClasspathEntry[] entries;
        if (entry != null) {
            entries = new IClasspathEntry[] { entry };
        } else {
            entries = new IClasspathEntry[] {};
        }

        return new PerfidixContainer(cpath, entries);
    }

    /**
     * This method checks if the entered class path is valid.
     * 
     * @param cpath
     *            The app path.
     * @return Return true if valid otherwise false.
     */
    private boolean isValidPerfidixContainerPath(IPath cpath) {

        return cpath != null && BENCH_CONTAINER_ID.equals(cpath.segment(0));
    }

    /** {@inheritDoc} */
    public boolean canUpdateClasspathContainer(
            IPath containerPath, IJavaProject project) {
        return true;
    }

    /** {@inheritDoc} */
    public IStatus getAccessRulesStatus(
            IPath containerPath, IJavaProject project) {
        return NOT_SUPPORTED;
    }

    /**
     * SourceAttachmentStatus returns only READ_ONLY status.
     * 
     * @param containerPath
     *            see super method.
     * @param project
     *            see super method.
     * @return The status.
     * @see org.eclipse.jdt.core.ClasspathContainerInitializer#getSourceAttachmentStatus(org.eclipse.core.runtime.IPath,
     *      org.eclipse.jdt.core.IJavaProject)
     */
    public IStatus getSourceAttachmentStatus(
            IPath containerPath, IJavaProject project) {
        return READ_ONLY;
    }

    /**
     * This method returns the status of the attributes.
     * 
     * @param containerPath
     *            The path of the container.
     * @param project
     *            The java project where the problem occurs.
     * @param attributeKey
     *            The attribute for the status.
     * @return The status.
     * @see org.eclipse.jdt.core.ClasspathContainerInitializer#getAttributeStatus(org.eclipse.core.runtime.IPath,
     *      org.eclipse.jdt.core.IJavaProject, java.lang.String)
     */
    public IStatus getAttributeStatus(
            IPath containerPath, IJavaProject project, String attributeKey) {
        if (attributeKey
                .equals(IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME)) {
            return Status.OK_STATUS;
        }
        return NOT_SUPPORTED;
    }

    /** {@inheritDoc} */
    public void requestClasspathContainerUpdate(
            IPath containerPath, IJavaProject project,
            IClasspathContainer containerSuggestion) {
        IClasspathEntry[] entries = containerSuggestion.getClasspathEntries();
        if (entries.length == 1 && isValidPerfidixContainerPath(containerPath)) {

            IClasspathAttribute[] extraAttributes =
                    entries[0].getExtraAttributes();
            for (int i = 0; i < extraAttributes.length; i++) {
                IClasspathAttribute attrib = extraAttributes[i];
                if (attrib.getName().equals(
                        IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME)) {
                    IPreferenceStore preferenceStore =
                            PerclipseActivator
                                    .getDefault().getPreferenceStore();
                    preferenceStore.setValue(
                            "This is the Perfidix JavaDoc", attrib.getValue());
                }
            }
            try {
                rebindClasspathEntries(project.getJavaModel(), containerPath);
            } catch (JavaModelException e) {
                PerclipseActivator.log(e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Rebinds the classpath entries to the effected projects.
     * 
     * @param javaModel
     * @param containerPath
     * @throws JavaModelException
     */
    private void rebindClasspathEntries(
            IJavaModel javaModel, IPath containerPath)
            throws JavaModelException {
        ArrayList<IJavaProject> affectedProject = new ArrayList<IJavaProject>();
        IJavaProject[] projects = javaModel.getJavaProjects();
        for (int i = 0; i < projects.length; i++) {
            IJavaProject javaProject = projects[i];
            IClasspathEntry[] entries = javaProject.getRawClasspath();
            for (int j = 0; j < entries.length; j++) {
                IClasspathEntry curr = entries[j];
                if (curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER
                        && containerPath.equals(curr.getPath())) {
                    affectedProject.add(javaProject);
                }
            }
        }

        if (!affectedProject.isEmpty()) {
            IJavaProject[] affected =
                    (IJavaProject[]) affectedProject
                            .toArray(new IJavaProject[affectedProject.size()]);
            IClasspathContainer[] containers =
                    new IClasspathContainer[affected.length];
            for (int i = 0; i < containers.length; i++) {
                containers[i] = getNewContainer(containerPath);

            }
            JavaCore.setClasspathContainer(
                    containerPath, affected, containers, null);
        }

    }

    /**
     * This method shows the description for the valid container path.
     * 
     * @param containerPath
     *            see super method.
     * @param project
     *            see super method.
     * @return The String description.
     * @see org.eclipse.jdt.core.ClasspathContainerInitializer#getDescription(org.eclipse.core.runtime.IPath,
     *      org.eclipse.jdt.core.IJavaProject)
     */
    public String getDescription(IPath containerPath, IJavaProject project) {
        if (isValidPerfidixContainerPath(containerPath)) {
            return "Bench Container Init Description";
        }
        return "Unresolved";
    }

    /** {@inheritDoc} */
    public Object getComparisonID(IPath containerPath, IJavaProject project) {
        return containerPath;
    }

}
