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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.socketadapter.SocketViewException;
import org.perfidix.socketadapter.SocketViewStub;

/**
 * This class tests the java class
 * {@link org.perfidix.socketadapter.SocketViewStub}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class SocketViewStubTest {
    // TODO javadoc
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
        finished = false;
    }

    /**
     * Test method for
     * {@link org.perfidix.socketadapter.SocketViewStub#SocketViewStub(java.lang.String, int)}
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
     * @throws SocketViewException
     */
    @Test
    public void testInitTotalBenchProgress()
            throws InterruptedException, SocketViewException {
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
     * @throws SocketViewException
     */
    @Test
    public void testUpdateCurrentRun()
            throws InterruptedException, SocketViewException {
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
     * @throws SocketViewException
     */
    @Test
    public void testUpdateError()
            throws InterruptedException, SocketViewException {
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
     * {@link org.perfidix.socketadapter.SocketViewStub#finishedBenchRuns()} .
     * 
     * @throws InterruptedException
     * @throws SocketViewException
     */
    @Test
    public void testFinishedBenchRuns()
            throws InterruptedException, SocketViewException {
        viewStub.finishedBenchRuns();
        Thread.sleep(10);
        finished = true;
    }

    /**
     * The simulator of our skeleton within the eclipse plug-in.
     * 
     * @author Lewandowski Lukas, University of Konstanz
     */
    private final class PerclipseViewSkeletonSimulator extends Thread {
        private Map<String, Integer> receivedMap;
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
            } catch (final IOException e) {
                fail(e.toString());

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
                fail(e1.toString());
            }

            String command;
            try {
                while (!finished) {

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
                        fail();
                    }

                }
            } catch (final IOException e) {
                finished = true;
                fail(e.toString());
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
            return receivedStringObject;
        }

    }

}
