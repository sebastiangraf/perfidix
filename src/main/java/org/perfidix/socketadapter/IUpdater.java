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

import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.meter.AbstractMeter;

import java.util.Map;

/**
 * This class creates the connection to the eclipse view via
 * {@link SocketViewStub}. It contains the methods which update the view to
 * inform about the bench process progress.
 *
 * @author Nuray Guerler, University of Konstanz
 */
public interface IUpdater {

	/**
	 * This method initializes the values of the eclipse view and resets the
	 * progress bar.
	 *
	 * @param mapping
	 *            a mapping with all methods to benchmark and the related runs
	 * @return return init success in progress view
	 * @throws SocketViewException
	 *             if progress fails in init
	 */
	public boolean initProgressView(final Map<BenchmarkMethod, Integer> mapping)
			throws SocketViewException;

	/**
	 * This method notifies the eclipse view which element is currently benched.
	 *
	 * @param meter
	 *            The current meter.
	 * @param name
	 *            This param represents the java element which is currently
	 *            benched and which is fully qualified.
	 * @return return update success in current element
	 * @throws SocketViewException
	 *             if update in current elements fails
	 */
	public boolean updateCurrentElement(final AbstractMeter meter,
			final String name) throws SocketViewException;

	/**
	 * This method informs the view that an error occurred while benching the
	 * current element.
	 *
	 * @param name
	 *            Element represents the java element which has not been
	 *            executed successfully.
	 * @param exception
	 *            The exception caused by the element.
	 * @return if update fails, return false
	 * @throws SocketViewException
	 *             if update in error fails
	 */
	public boolean updateErrorInElement(final String name,
			final Exception exception) throws SocketViewException;

	/**
	 * This method notifies the view that all benches have been executed and the
	 * bench progress is finished.
	 *
	 * @throws SocketViewException
	 *             if finished communication fails
	 */
	public void finished() throws SocketViewException;

}
