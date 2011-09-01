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
package org.perfidix.socketadapter;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The simulator of our skeleton within the eclipse plug-in.
 * 
 * @author Lewandowski Lukas, University of Konstanz
 */
public final class PerclipseViewSkeletonSimulator extends Thread {
    private transient Map<String, Integer> receivedMap;
    private transient Map<String, MethodWhichIsBenchin> benchElements =
            new HashMap<String, MethodWhichIsBenchin>();
    private transient String receivedString;
    private transient ServerSocket serverSocket;
    private transient boolean finished = false;
    private transient String errorOccurred;

    /**
     * The constructor gets a given free port and initializes the bench run
     * session listener. Afterwards it creates a servers socket with the given
     * port number.
     * 
     * @param port
     *            The given free port number.
     */
    public PerclipseViewSkeletonSimulator(final int port) {
        super();
        final int serverPort = port;
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (final IOException e) {
            fail(e.toString());

        }
    }

    /**
     * This method is our Thread method. It is responsible for receiving data
     * from our perfidix bench process. When a message is received, it delegates
     * the data to the bench run session listener.
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run() {

        Socket socket = null;
        ObjectInputStream inStream = null;
        try {
            socket = serverSocket.accept();

            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e1) {
            fail(e1.toString());
        }

        String command;
        try {
            while (!finished) {

                command = (String) inStream.readObject();
                if ("init".equals(command)) {
                    receivedMap = (Map<String, Integer>) inStream.readObject();
                    benchElements = new HashMap<String, MethodWhichIsBenchin>();
                    if (receivedMap != null) {
                        final Set<String> stringSet = receivedMap.keySet();
                        for (String name : stringSet) {
                            benchElements.put(name, new MethodWhichIsBenchin(
                                    name, receivedMap.get(name)));
                        }
                    }
                    // init happened
                } else if ("updateCurrentRun".equals(command)) {
                    receivedString = (String) inStream.readObject();
                    if (getElements().containsKey(receivedString)) {
                        final MethodWhichIsBenchin method =
                                getElements().get(receivedString);
                        method.updateRun();
                    }
                    // update happened

                } else if ("updateError".equals(command)) {
                    receivedString = (String) inStream.readObject();
                    errorOccurred = (String) inStream.readObject();
                    if (getElements().containsKey(receivedString)) {
                        final MethodWhichIsBenchin method =
                                getElements().get(receivedString);
                        method.updateError();
                    }
                    // error happened
                } else if ("finished".equals(command)) {
                    // finished happened
                    finished = true;
                } else {
                    fail();
                }

            }
        } catch (final IOException e) {
            finished = true;
            // fail(e.toString());
        } catch (ClassNotFoundException e) {
            finished = true;
            fail(e.toString());
        } finally {
            try {
                if (socket.isConnected()) {
                    socket.close();
                }
                serverSocket.close();
            } catch (IOException e) {
                fail(e.toString());
            }

        }

    }

    /**
     * Received data just for testing purposes.
     * 
     * @return Received data just for testing purposes.
     */
    public Map<String, Integer> getTheMap() {
        return receivedMap;
    }

    /**
     * Received data just for testing purposes.
     * 
     * @return Received data just for testing purposes.
     */
    public String getReceivedStringObject() {
        return receivedString;
    }

    /**
     * Received data just for testing purposes.
     * 
     * @return Received data just for testing purposes.
     */
    public String getErrorStringObject() {
        return errorOccurred;
    }

    /**
     * Received data just for testing purposes.
     * 
     * @return Received data just for testing purposes.
     */
    public boolean isFinsihed() {
        return finished;
    }

    /**
     * Received data just for testing purposes.
     * 
     * @return Received data just for testing purposes.
     */
    public Map<String, MethodWhichIsBenchin> getElements() {
        return benchElements;
    }

    class MethodWhichIsBenchin {
        private final transient String name;
        private final transient int initValue;
        private transient int run;
        private transient int error;

        MethodWhichIsBenchin(final String name, final int initValue) {
            this.name = name;
            this.initValue = initValue;
            run = 0;
            error = 0;
        }

        public void updateRun() {
            run = run + 1;
        }

        public void updateError() {
            error = error + 1;
        }

        public int getCurrentRun() {
            return run;
        }

        public int getErrorRuns() {
            return error;
        }

        public int getSumToBeBenched() {
            return initValue;
        }

        public String getName() {
            return name;
        }
    }

}
