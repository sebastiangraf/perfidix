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


import org.perfidix.exceptions.SocketViewException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;


/**
 * This class creates the connection to the eclipse view, which depict the current progress of the operation. It
 * contains also the methods to upudate the view.
 *
 * @author Lewandowski Lukas, University of Konstanz
 */
public final class SocketViewStub implements IBenchRunSessionListener {

    /**
     * The host name for the connection to the socket skeleton.
     */
    private transient final String host;
    /**
     * The command that will be send to the socket skeleton. So the skeleton knows which data will be dispatched
     * afterwards.
     */
    private transient String command;
    /**
     * The client socket that will connect to the server socket of the ide plug-in.
     */
    private transient Socket socket;
    /**
     * The output stream for dispatching data to the ide skeleton to visualize the bench process / progress.
     */
    private transient ObjectOutputStream outputStream;

    /**
     * The constructor initializes the given host name and the port, which are needed for creating a client socket.
     * Afterwards it creates the client socket and the object output stream.
     *
     * @param host             Host represents the {@link String} host name
     * @param viewListenerPort This param represents the port of the view.
     * @throws SocketViewException if communitcation fails
     */
    public SocketViewStub(final String host, final int viewListenerPort) throws SocketViewException {
        if (host == null) {
            this.host = "localhost";
        } else {
            this.host = host;
        }
        try {
            socket = new Socket(this.host, viewListenerPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (final UnknownHostException e) {
            throw new SocketViewException(e);
        } catch (final IOException e) {
            throw new SocketViewException(e);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void initTotalBenchProgress(final Map<String, Integer> elems) throws SocketViewException {
        command = "init";
        try {
            outputStream.writeObject(command);
            outputStream.writeObject(elems);
        } catch (final IOException e) {
            throw new SocketViewException(e);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void updateCurrentRun(final String currentElement) throws SocketViewException {
        command = "updateCurrentRun";
        try {
            outputStream.writeObject(command);
            outputStream.writeObject(currentElement);
        } catch (final IOException e) {
            throw new SocketViewException(e);
        }

    }

    /**
     * {@inheritDoc}
     */

    public void updateError(final String element, final String exception) throws SocketViewException {

        command = "updateError";
        try {
            outputStream.writeObject(command);
            outputStream.writeObject(element);
            outputStream.writeObject(exception);
        } catch (final IOException e) {
            throw new SocketViewException(e);
        }

    }

    /**
     * {@inheritDoc}
     */
    public boolean finishedBenchRuns() throws SocketViewException {
        command = "finished";
        try {
            outputStream.writeObject(command);
            outputStream.close();
            socket.close();
            return true;
        } catch (final IOException e) {
            throw new SocketViewException(e);
        }

    }

}
