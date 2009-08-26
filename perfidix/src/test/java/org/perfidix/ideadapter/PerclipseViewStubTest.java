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
package org.perfidix.ideadapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.socketadapter.SocketViewStub;

/**
 * This class tests the java class
 * {@link org.perfidix.socketadapter.SocketViewStub}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseViewStubTest {
    private SocketViewStub viewStub;
    private PerclipseViewSkeletonSimulator skeletonSimulator;
    private HashMap<String, Integer> dataForView;
    private boolean finished = false;

    /**
     * Simple setUp
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        skeletonSimulator = new PerclipseViewSkeletonSimulator(6363);
        skeletonSimulator.start();
        Thread.sleep(100);
        viewStub = new SocketViewStub(null, 6363);
    }

    /**
     * Simple tearDown
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        if (!finished) {
            viewStub.finishedBenchRuns();
            Thread.sleep(10);
        }
        skeletonSimulator = null;
        viewStub = null;
        finished = false;
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#PerclipseViewStub(java.lang.String, int)}
     * .
     */
    @Test
    public void testPerclipseViewStub() {
        assertNotNull(viewStub);
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#initTotalBenchProgress(java.util.Map)}
     * .
     * 
     * @throws InterruptedException
     */
    @Test
    public void testInitTotalBenchProgress() throws InterruptedException {
        dataForView = new HashMap<String, Integer>();
        dataForView.put("org.some.method", 55);
        dataForView.put("blue.some.other", 88);
        viewStub.initTotalBenchProgress(dataForView);
        Thread.sleep(10);
        assertEquals(dataForView, skeletonSimulator.getTheMap());
        viewStub.initTotalBenchProgress(null);
        Thread.sleep(10);
        assertEquals(null, skeletonSimulator.getTheMap());
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#updateCurrentRun(java.lang.String)}
     * .
     * 
     * @throws InterruptedException
     */
    @Test
    public void testUpdateCurrentRun() throws InterruptedException {
        viewStub.updateCurrentRun("some.Element");
        Thread.sleep(10);
        assertEquals("some.Element", skeletonSimulator
                .getReceivedStringObject());
        viewStub.updateCurrentRun(null);
        Thread.sleep(10);
        assertEquals(null, skeletonSimulator.getReceivedStringObject());
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#updateError(java.lang.String)}
     * .
     * 
     * @throws InterruptedException
     */
    @Test
    public void testUpdateError() throws InterruptedException {
        viewStub.updateError("some.Element");
        Thread.sleep(10);
        assertEquals("some.Element", skeletonSimulator
                .getReceivedStringObject());
        viewStub.updateError(null);
        Thread.sleep(10);
        assertEquals(null, skeletonSimulator.getReceivedStringObject());
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#finishedBenchRuns()}
     * .
     * 
     * @throws InterruptedException
     */
    @Test
    public void testFinishedBenchRuns() throws InterruptedException {
        viewStub.finishedBenchRuns();
        Thread.sleep(10);
        finished = true;
    }

    /**
     * The simulator of our skeleton within the eclipse plug-in.
     * 
     * @author Lewandowski Lukas, University of Konstanz
     */
    private class PerclipseViewSkeletonSimulator extends Thread {
        private HashMap<String, Integer> receivedMap;
        private String receivedStringObject;
        private ServerSocket serverSocket;
        private Socket socket = null;
        private int serverPort;
        private boolean finished = false;
        private ObjectInputStream in;

        /**
         * The constructor gets a given free port and initializes the bench run
         * session listener. Afterwards it creates a servers socket with the
         * given port number.
         * 
         * @param port
         *            The given free port number.
         */
        public PerclipseViewSkeletonSimulator(int port) {

            serverPort = port;
            try {
                serverSocket = new ServerSocket(serverPort);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }

        /**
         * This method is our Thread method. It is responsible for receiving
         * data from our perfidix bench process. When a message is received, it
         * delegates the data to the bench run session listener.
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {

            try {
                socket = serverSocket.accept();

                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }

            String command;
            while (!finished) {
                try {
                    command = (String) in.readObject();
                    if ("init".equals(command)) {
                        receivedMap =
                                (HashMap<String, Integer>) in.readObject();

                        // init happened
                    } else if ("updateCurrentRun".equals(command)) {
                        receivedStringObject = (String) in.readObject();
                        // update happened

                    } else if ("updateError".equals(command)) {
                        receivedStringObject = (String) in.readObject();
                        // error happened
                    } else if ("finished".equals(command)) {
                        // finished happened
                        finished = true;

                    } else {
                        // Unknown command. In the real skeleton an eclipse log
                        // gets this notification.
                    }
                } catch (IOException e) {
                    finished = true;
                    if (e instanceof EOFException) {
                        throw new RuntimeException(e);
                        // Running Bench process has been stopped or restarted

                    } else {
                        throw new RuntimeException(e);
                        // Another error occurred.
                    }
                } catch (ClassNotFoundException e) {
                    finished = true;
                    throw new RuntimeException(e);
                }

            }
            try {
                if (socket.isConnected()) {
                    socket.close();
                }
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /**
         * Received data just for testing purposes.
         * 
         * @return Received data just for testing purposes.
         */
        public HashMap<String, Integer> getTheMap() {
            return receivedMap;
        }

        /**
         * Received data just for testing purposes.
         * 
         * @return Received data just for testing purposes.
         */
        public String getReceivedStringObject() {
            return receivedStringObject;
        }

    }

}
