/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     David Saff (saff@mit.edu) - initial API and implementation
 *             (bug 102632: [JUnit] Support for JUnit 4.)
 *******************************************************************************/

package org.perfidix.Perclipse.launcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

/**
 * The PerfidixLaunchDescription class is responsible for the setting of various
 * launch description items you need to run a launch configuration
 * 
 * @author Graf Sebastian, Lewandowski Lukas, Disy Group, University of Konstanz
 */
public class PerfidixLaunchDescription {

    /** Attributes to match in order to find an existing configuration. */
    public static final String[] ATTRIBUTES_THAT_MUST_MATCH =
            new String[] {
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR,
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    PerfidixLaunchConfiguration.BENCH_NAME_ATTR };

    /**
     * A empty String.
     */
    public static final String EMPTY = ""; //$NON-NLS-1$

    private static final String DEFAULT_VALUE = ""; //$NON-NLS-1$

    private Map<String, String> fAttributes = new HashMap<String, String>();

    private final IJavaElement fElement;

    private final String fName;

    /**
     * The constructor gets an existing java element of type IJavaElement and a
     * name as a String value and set them. Afterwards it sets the attributes.
     * 
     * @param element
     *            The java element for a launch description.
     * @param name
     *            The name of the launch description.
     */
    public PerfidixLaunchDescription(IJavaElement element, String name) {
        fElement = element;
        fName = name;
        setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                getProjectName());
    }

    /**
     * The copyAttributeInto sets the attributes in the working copy of the
     * launch configuration (ILaunchConfigurationWorkingCopy).
     * 
     * @param wc
     *            The launch configuration working copy.
     */
    public void copyAttributesInto(ILaunchConfigurationWorkingCopy wc) {
        wc.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                getProjectName());

        Set<?> definedAttributes = getDefinedAttributes();
        for (Iterator<?> iter = definedAttributes.iterator(); iter.hasNext();) {
            Entry<?, ?> attribute = (Entry<?, ?>) iter.next();
            wc.setAttribute((String) attribute.getKey(), (String) attribute
                    .getValue());
        }
    }

    /** {@inheritDoc} */
    public boolean equals(Object arg0) {
        PerfidixLaunchDescription desc = (PerfidixLaunchDescription) arg0;
        return areEqual(desc.fElement, fElement)
                && areEqual(desc.fName, fName)
                && areEqual(desc.fAttributes, fAttributes);
    }

    /**
     * The getAttribute method returns the String value of the queried
     * attribute, otherwise it returns the DEFAULT_VALUE.
     * 
     * @param attr
     *            The queried attribute.
     * @return The String value of the searched attribute.
     */
    public String getAttribute(String attr) {
        if (fAttributes.containsKey(attr)) {
            return (String) fAttributes.get(attr);
        }
        return DEFAULT_VALUE;
    }

    /**
     * The getContainer method returns an existing attribute representing launch
     * container attribute.
     * 
     * @return The String container value.
     */
    public String getContainer() {
        return getAttribute(PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR);
    }

    /**
     * The getDefinedAttributes returns the entrySet of the Set fAttributes.
     * 
     * @return The Set of defined attributes.
     */
    public Set getDefinedAttributes() {
        return fAttributes.entrySet();
    }

    /**
     * The getElement method returns the existing fElement of type
     * {@link IJavaElement}.
     * 
     * @return The java element.
     */
    public IJavaElement getElement() {
        return fElement;
    }

    /**
     * The getName method returns the unexpected value of fName.
     * 
     * @return The name.
     */
    public String getName() {
        return fName;
    }

    /**
     * The setContainer method set an attribute item of fAttributes with launch
     * container parameter and a handleIdentifier.
     * 
     * @param handleIdentifier
     *            The String identifier.
     * @return The perfidix launch description.
     */
    public PerfidixLaunchDescription setContainer(String handleIdentifier) {
        return setAttribute(
                PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR,
                handleIdentifier);
    }

    /**
     * The setMainType method set an attribute item of fAttributes with the main
     * type name and the mainType String value.
     * 
     * @param mainType
     *            The main type for the launch description.
     * @return The perfidix launch description.
     */
    public PerfidixLaunchDescription setMainType(String mainType) {
        return setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, mainType);
    }

    /**
     * The setBenchName method sets an attribute item of fAttributes with the
     * bench name attribute, consisting of plugin id and bench name, and a given
     * benchName of type String.
     * 
     * @param benchName
     *            The bench name.
     * @return The launch description.
     */
    public PerfidixLaunchDescription setBenchName(String benchName) {
        return setAttribute(
                PerfidixLaunchConfiguration.BENCH_NAME_ATTR, benchName);
    }

    /** {@inheritDoc} */
    public String toString() {
        return "PerfidixLaunchDescription(" + fName + ")"; //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * The getProjectName method returns either the existing project name of the
     * project element or the value null.
     * 
     * @return The project name.
     */
    private String getProjectName() {
        IJavaProject project = getProject();
        return project == null ? null : project.getElementName();
    }

    /**
     * The following method validates the attributes with the
     * ILaunchConfiguration config.
     * 
     * @param config
     *            The launch configuration.
     * @return The boolean value if the attributes for a given configuration
     *         match.
     * @throws CoreException
     *             The exception.
     */
    boolean attributesMatch(ILaunchConfiguration config) throws CoreException {
        for (int i = 0; i < ATTRIBUTES_THAT_MUST_MATCH.length; i++) {
            if (!configurationMatches(ATTRIBUTES_THAT_MUST_MATCH[i], config)) {
                return false;
            }
        }
        return true;
    }

    /**
     * The following method checks if configs attribute is equal to the
     * fAttributes attribute.
     * 
     * @param attributeName
     *            The attribute name.
     * @param config
     *            The launch configuration.
     * @return The boolean value for matching or not.
     * @throws CoreException
     *             A core exception.
     */
    boolean configurationMatches(
            final String attributeName, ILaunchConfiguration config)
            throws CoreException {
        return config.getAttribute(attributeName, EMPTY).equals(
                getAttribute(attributeName));
    }

    /**
     * The following method sets a new attribute to fAttributes with the a given
     * type of IType.
     * 
     * @param type
     *            The type parameter.
     */
    void setMainType(IType type) {
        setMainType(type.getFullyQualifiedName());
    }

    /**
     * The following method checks two objects if their are equal. If the thing
     * object is null so otherThing has to be null too.
     * 
     * @param thing
     * @param otherThing
     * @return
     */
    private boolean areEqual(Object thing, Object otherThing) {
        if (thing == null) {
            return otherThing == null;
        }
        return thing.equals(otherThing);
    }

    /**
     * The getProject method returns the getJavaProject item of fElement
     * otherwise null.
     * 
     * @return The java project.
     */
    public final IJavaProject getProject() {
        return fElement == null ? null : fElement.getJavaProject();
    }

    /**
     * The setAttribute method expects two String, an attribute value and a
     * String value representing the name and adds a new attribute to the
     * fAttributes of type Map.
     * 
     * @param attr
     * @param value
     * @return The perfidix launch description.
     */
    private PerfidixLaunchDescription setAttribute(String attr, String value) {
        fAttributes.put(attr, value);
        return this;
    }
}
