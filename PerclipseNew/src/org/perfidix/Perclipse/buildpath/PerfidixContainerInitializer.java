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

public class PerfidixContainerInitializer extends ClasspathContainerInitializer {
    public static final String BENCH_CONTAINER_ID =
            "org.perfidix.Perclipse.PERFIDIX_CONTAINER";
    public static final IStatus NOT_SUPPORTED =
            new Status(
                    IStatus.ERROR, PerclipseActivator.PLUGIN_ID,
                    ClasspathContainerInitializer.ATTRIBUTE_NOT_SUPPORTED,
                    new String(), null);
    public static final IStatus READ_ONLY =
            new Status(
                    IStatus.ERROR, PerclipseActivator.PLUGIN_ID,
                    ClasspathContainerInitializer.ATTRIBUTE_READ_ONLY,
                    new String(), null);

    public static final IPath PERFIDIX_PATH = new Path(BENCH_CONTAINER_ID);

    private static class PerfidixContainer implements IClasspathContainer {

        private final IClasspathEntry[] entries;
        private final IPath path;

        public PerfidixContainer(IPath path, IClasspathEntry[] entries) {
            this.path = path;
            this.entries = entries;
        }

        public IClasspathEntry[] getClasspathEntries() {

            return entries;
        }

        public String getDescription() {

            return "Perfidix Library";
        }

        public int getKind() {

            return IClasspathContainer.K_APPLICATION;
        }

        public IPath getPath() {

            return path;
        }

    }

    public PerfidixContainerInitializer() {

    }

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

    private boolean isValidPerfidixContainerPath(IPath cpath) {

        return cpath != null && BENCH_CONTAINER_ID.equals(cpath.segment(0));
    }

    public boolean canUpdateClasspathContainer(
            IPath containerPath, IJavaProject project) {
        return true;
    }

    public IStatus getAccessRulesStatus(
            IPath containerPath, IJavaProject project) {
        return NOT_SUPPORTED;
    }

    public IStatus getSourceAttachmentStatus(
            IPath containerPath, IJavaProject project) {
        return READ_ONLY;
    }

    public IStatus getAttributeStatus(
            IPath containerPath, IJavaProject project, String attributeKey) {
        if (attributeKey
                .equals(IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME)) {
            return Status.OK_STATUS;
        }
        return NOT_SUPPORTED;
    }

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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void rebindClasspathEntries(
            IJavaModel javaModel, IPath containerPath)
            throws JavaModelException {
        ArrayList affectedProject = new ArrayList();
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

    public String getDescription(IPath containerPath, IJavaProject project) {
        if (isValidPerfidixContainerPath(containerPath)) {
            return "Bench Container Init Description";
        }
        return "Unresolved";
    }

    public Object getComparisonID(IPath containerPath, IJavaProject project) {
        return containerPath;
    }

}
