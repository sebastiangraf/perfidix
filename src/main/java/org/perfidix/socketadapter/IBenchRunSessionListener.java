/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.socketadapter;


import java.util.Map;

import org.perfidix.exceptions.SocketViewException;


/**
 * This interface specifies the methods which have to update the eclipse view when a given event occurs.
 * 
 * @author Lewandowski Lukas, University of Konstanz
 */
public interface IBenchRunSessionListener {
    /**
     * The initTotalBenchProgress initializes the progress bar in the eclipse view. This param represent the sum value
     * of all runs to be executed.
     * 
     * @param elems This param is an HashMap which consists of each java element name with its total bench runs value.
     * @throws SocketViewException if init fails
     */
    void initTotalBenchProgress (final Map<String , Integer> elems) throws SocketViewException;

    /**
     * The updateCurrentRun method notifies the view which element is currently running.
     * 
     * @param currentElement This {@link String} param represents the current running java element.
     * @throws SocketViewException if update fails
     */
    void updateCurrentRun (final String currentElement) throws SocketViewException;

    /**
     * The updateError method updates the view that an error occurred while benching the given java element.
     * 
     * @param element This {@link String} param represents the element name of the benched object where the error
     *            occurred.
     * @param exception The exception caused by the element.
     * @throws SocketViewException if update fails
     */
    void updateError (final String element, final String exception) throws SocketViewException;

    /**
     * This method notifies the view that all bench runs completed.
     * 
     * @throws SocketViewException if finish-update fails
     */
    boolean finishedBenchRuns () throws SocketViewException;

}
