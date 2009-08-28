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
