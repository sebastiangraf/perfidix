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
 * @author lewandow
 */
public class PerfidixLaunchDescription {

    /** Attributes to match in order to find an existing configuration. */
    public static final String[] ATTRIBUTES_THAT_MUST_MATCH =
            new String[] {
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR,
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    PerfidixLaunchConfiguration.BENCH_NAME_ATTR };

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
     * @param name
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
     */
    public void copyAttributesInto(ILaunchConfigurationWorkingCopy wc) {
        wc.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                getProjectName());

        Set definedAttributes = getDefinedAttributes();
        for (Iterator iter = definedAttributes.iterator(); iter.hasNext();) {
            Entry attribute = (Entry) iter.next();
            wc.setAttribute((String) attribute.getKey(), (String) attribute
                    .getValue());
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
     * @return
     */
    public String getAttribute(String attr) {
        if (fAttributes.containsKey(attr))
            return (String) fAttributes.get(attr);
        return DEFAULT_VALUE;
    }

    /**
     * The getContainer method returns an existing attribute representing launch
     * container attribute.
     * 
     * @return
     */
    public String getContainer() {
        return getAttribute(PerfidixLaunchConfiguration.LAUNCH_CONTAINER_ATTR);
    }

    /**
     * The getDefinedAttributes returns the entrySet of the Set fAttributes.
     * 
     * @return
     */
    public Set getDefinedAttributes() {
        return fAttributes.entrySet();
    }

    /**
     * The getElement method returns the existing fElement of type
     * {@link IJavaElement}.
     * 
     * @return
     */
    public IJavaElement getElement() {
        return fElement;
    }

    /**
     * The getName method returns the unexpected value of fName.
     * 
     * @return
     */
    public String getName() {
        return fName;
    }

    /**
     * The setContainer method set an attribute item of fAttributes with launch
     * container parameter and a handleIdentifier.
     * 
     * @param handleIdentifier
     * @return
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
     * @return
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
     * @return
     */
    public PerfidixLaunchDescription setBenchName(String benchName) {
        return setAttribute(
                PerfidixLaunchConfiguration.BENCH_NAME_ATTR, benchName);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "PerfidixLaunchDescription(" + fName + ")"; //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * The getProjectName method returns either the existing project name of the
     * project element or the value null.
     * 
     * @return
     */
    protected String getProjectName() {
        IJavaProject project = getProject();
        return project == null ? null : project.getElementName();
    }

    /**
     * The following method validates the attributes with the
     * ILaunchConfiguration config.
     * 
     * @param config
     * @return
     * @throws CoreException
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
     * @param config
     * @return
     * @throws CoreException
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
        if (thing == null)
            return otherThing == null;
        return thing.equals(otherThing);
    }

    /**
     * The getProject method returns the getJavaProject item of fElement
     * otherwise null.
     * 
     * @return
     */
    public IJavaProject getProject() {
        return fElement == null ? null : fElement.getJavaProject();
    }

    /**
     * The setAttribute method expects two String, an attribute value and a
     * String value representing the name and adds a new attribute to the
     * fAttributes of type Map.
     * 
     * @param attr
     * @param value
     * @return
     */
    private PerfidixLaunchDescription setAttribute(String attr, String value) {
        fAttributes.put(attr, value);
        return this;
    }
}
