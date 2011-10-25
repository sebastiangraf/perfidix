/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
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
package org.perfidix.perclipse.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * The PerclipseViewSkeleton class is responsible for creating our ServerSocket.
 * At this point we are waiting for current progress information from our
 * Perfidix bench process.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseViewSkeleton extends Thread {
    final private transient IBenchRunSessionListener sessionListener;
    private transient ServerSocket serverSocket;
    private transient boolean finished = false;

    /**
     * The constructor gets a given free port and initializes the bench run
     * session listener. Afterwards it creates a servers socket with the given
     * port number.
     * 
     * @param port
     *            The given free port number.
     */
    public PerclipseViewSkeleton(final int port) {
        super();
        sessionListener = new BenchRunSessionListener();
        final int serverPort = port;
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            PerclipseActivator.log(e);
        }
    }

    /**
     * This method is our Thread method. It is responsible for receiving data
     * from our perfidix bench process. When a message is received, it delegates
     * the data to the bench run session listener.
     * 
     * @see java.lang.Thread#run()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            final Socket socket = serverSocket.accept();
            final ObjectInputStream inStream =
                    new ObjectInputStream(socket.getInputStream());

            String command;
            while (!finished) {
                try {
                    command = (String) inStream.readObject();
                    if ("init".equals(command)) {
                        final Map<String, Integer> elements =
                                (HashMap<String, Integer>) inStream
                                        .readObject();
                        sessionListener
                                .initTotalBenchProgress((HashMap<String, Integer>) elements);
                    } else if ("updateCurrentRun".equals(command)) {
                        final String currentElement =
                                (String) inStream.readObject();
                        sessionListener.updateCurrentRun(currentElement);

                    } else if ("updateError".equals(command)) {
                        final String errorElement =
                                (String) inStream.readObject();

                        final String exceptionOccurred =
                                (String) inStream.readObject();
                        sessionListener.updateError(
                                errorElement, exceptionOccurred);
                    } else if ("finished".equals(command)) {
                        finished = true;
                        PerclipseActivator.logInfo("Bench process finished");
                    } else {
                        PerclipseActivator.logInfo("Unknown command:"
                                + command
                                + "received.");
                    }
                } catch (IOException e) {

                    PerclipseActivator
                            .log(
                                    e,
                                    "Running Bench process has been stopped or restarted");

                    finished = true;
                } catch (ClassNotFoundException e) {
                    finished = true;
                    PerclipseActivator.log(e);
                }

            }
            try {
                if (socket.isConnected()) {
                    inStream.close();
                    socket.close();
                }
                serverSocket.close();
            } catch (IOException e) {
                PerclipseActivator.log(e);
            }

        } catch (IOException e1) {
            PerclipseActivator.log(e1);
        }
    }

}
