/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.perclipse.launcher;

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
    public static final String[] ATTR_MUST_MATCH =
            new String[] {
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR,
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    PerfidixLaunchConfiguration.BENCH_NAME_ATTR };

    /**
     * A empty String.
     */
    public static final String EMPTY = ""; //$NON-NLS-1$

    private static final String DEFAULT_VALUE = ""; //$NON-NLS-1$

    final private transient Map<String, String> fAttributes =
            new HashMap<String, String>();

    private final transient IJavaElement fElement;

    private final transient String fName;

    /**
     * The constructor gets an existing java element of type IJavaElement and a
     * name as a String value and set them. Afterwards it sets the attributes.
     * 
     * @param element
     *            The java element for a launch description.
     * @param name
     *            The name of the launch description.
     */
    public PerfidixLaunchDescription(
            final IJavaElement element, final String name) {
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
     * @param workCop
     *            The launch configuration working copy.
     */
    public void copyAttributesInto(final ILaunchConfigurationWorkingCopy workCop) {
        workCop.setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                getProjectName());

        final Set<?> definedAttributes = getDefinedAttributes();
        for (final Iterator<?> iter = definedAttributes.iterator(); iter
                .hasNext();) {
            final Entry<?, ?> attribute = (Entry<?, ?>) iter.next();
            workCop.setAttribute(
                    (String) attribute.getKey(), (String) attribute.getValue());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object arg0) {
        final PerfidixLaunchDescription desc = (PerfidixLaunchDescription) arg0;
        return areEqual(desc.fElement, fElement)
                && areEqual(desc.fName, fName)
                && areEqual(desc.fAttributes, fAttributes);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return super.hashCode() + 1;
    }

    /**
     * The getAttribute method returns the String value of the queried
     * attribute, otherwise it returns the DEFAULT_VALUE.
     * 
     * @param attr
     *            The queried attribute.
     * @return The String value of the searched attribute.
     */
    public String getAttribute(final String attr) {
        String returnString = DEFAULT_VALUE;
        if (fAttributes.containsKey(attr)) {
            returnString = fAttributes.get(attr);
        }
        return returnString;
    }

    /**
     * The getContainer method returns an existing attribute representing launch
     * container attribute.
     * 
     * @return The String container value.
     */
    public String getContainer() {
        return getAttribute(PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR);
    }

    /**
     * The getDefinedAttributes returns the entrySet of the Set fAttributes.
     * 
     * @return The Set of defined attributes.
     */
    public Set<?> getDefinedAttributes() {
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
    public PerfidixLaunchDescription setContainer(final String handleIdentifier) {
        return setAttribute(
                PerfidixLaunchConfiguration.LAUNCH_CONT_ATTR, handleIdentifier);
    }

    /**
     * The setMainType method set an attribute item of fAttributes with the main
     * type name and the mainType String value.
     * 
     * @param mainType
     *            The main type for the launch description.
     * @return The perfidix launch description.
     */
    public PerfidixLaunchDescription setMainType(final String mainType) {
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
    public PerfidixLaunchDescription setBenchName(final String benchName) {
        return setAttribute(
                PerfidixLaunchConfiguration.BENCH_NAME_ATTR, benchName);
    }

    /** {@inheritDoc} */
    @Override
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
        final IJavaProject project = getProject();
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
    public boolean attributesMatch(final ILaunchConfiguration config)
            throws CoreException {
        boolean returnValue = true;
        for (int i = 0; i < ATTR_MUST_MATCH.length; i++) {
            if (!configurationMatches(ATTR_MUST_MATCH[i], config)) {
                returnValue = false;
            }
        }
        return returnValue;
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
    public boolean configurationMatches(
            final String attributeName, final ILaunchConfiguration config)
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
    public void setMainType(final IType type) {
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
    private boolean areEqual(final Object thing, final Object otherThing) {
        boolean retValue = false;
        if (thing == null && otherThing == null) {
            retValue = true;
        }
        retValue = thing.equals(otherThing);
        return retValue;
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
    private PerfidixLaunchDescription setAttribute(
            final String attr, final String value) {
        fAttributes.put(attr, value);
        return this;
    }
}
