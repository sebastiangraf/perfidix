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
package org.perfidix.socketadapter;

import java.util.Map;

import org.perfidix.exceptions.SocketViewException;

/**
 * This interface specifies the methods which have to update the eclipse view
 * when a given event occurs.
 * 
 * @author Lewandowski Lukas, University of Konstanz
 */
public interface IBenchRunSessionListener {
    /**
     * The initTotalBenchProgress initializes the progress bar in the eclipse
     * view. This param represent the sum value of all runs to be executed.
     * 
     * @param elems
     *            This param is an HashMap which consists of each java element
     *            name with its total bench runs value.
     * @throws SocketViewException
     *             if init fails
     */
    void initTotalBenchProgress(final Map<String, Integer> elems)
            throws SocketViewException;

    /**
     * The updateCurrentRun method notifies the view which element is currently
     * running.
     * 
     * @param currentElement
     *            This {@link String} param represents the current running java
     *            element.
     * @throws SocketViewException
     *             if update fails
     */
    void updateCurrentRun(final String currentElement)
            throws SocketViewException;

    /**
     * The updateError method updates the view that an error occurred while
     * benching the given java element.
     * 
     * @param element
     *            This {@link String} param represents the element name of the
     *            benched object where the error occurred.
     * @param exception
     *            The exception caused by the element.
     * @throws SocketViewException
     *             if update fails
     */
    void updateError(final String element, final String exception)
            throws SocketViewException;

    /**
     * This method notifies the view that all bench runs completed.
     * 
     * @throws SocketViewException
     *             if finish-update fails
     */
    void finishedBenchRuns() throws SocketViewException;

}
