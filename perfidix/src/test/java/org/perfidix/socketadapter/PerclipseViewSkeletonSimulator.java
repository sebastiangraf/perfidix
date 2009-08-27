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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * The simulator of our skeleton within the eclipse plug-in.
 * 
 * @author Lewandowski Lukas, University of Konstanz
 */
public final class PerclipseViewSkeletonSimulator extends Thread {
    private transient Map<String, Integer> receivedMap;
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

                    // init happened
                } else if ("updateCurrentRun".equals(command)) {
                    receivedString = (String) inStream.readObject();
                    // update happened

                } else if ("updateError".equals(command)) {
                    receivedString = (String) inStream.readObject();
                    errorOccurred = (String) inStream.readObject();
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

}
