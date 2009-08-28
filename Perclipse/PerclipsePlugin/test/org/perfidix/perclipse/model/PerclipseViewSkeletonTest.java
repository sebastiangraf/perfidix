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
package org.perfidix.perclipse.model;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.eclipse.jdt.launching.SocketUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.PerclipseViewSkeleton}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseViewSkeletonTest {

    private PerclipseViewSkeleton skeleton;
    private HashMap<String, Integer> elementsMap;
    private int port;

    /**
     * Simple setUp - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @Before
    public void setUp() throws Exception {
        port = SocketUtil.findFreePort();
        skeleton = new PerclipseViewSkeleton(port);
        skeleton.start();
        elementsMap = new HashMap<String, Integer>();
        elementsMap.put("package.Class.method1", 50);
        elementsMap.put("package1.Class1.method1", 25);
    }

    /**
     * Simple tearDown - method.
     * 
     * @throws java.lang.Exception
     *             The Exception occurred.
     */
    @After
    public void tearDown() throws Exception {
        skeleton = null;
        elementsMap = null;
    }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.PerclipseViewSkeleton#PerclipseViewSkeleton(int)}
     * .
     */
    @Test
    public void testPerclipseViewSkeleton() {
        assertNotNull(skeleton);

    }

    /**
     * Tests stub skeleton connection for initialization of the view.
     */
    @Test
    public void testInitView() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.initTotalBenchProgress(null);
        stubForSkeletonTest.initTotalBenchProgress(elementsMap);
        stubForSkeletonTest.finishedBenchRuns();
    }

    /**
     * Tests stub skeleton connection for updating the progress of the view.
     */
    @Test
    public void testUpdateCurrentRun() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.initTotalBenchProgress(elementsMap);
        stubForSkeletonTest.updateCurrentRun("No");
        stubForSkeletonTest.updateCurrentRun("package.Class.method1");
        stubForSkeletonTest.finishedBenchRuns();
    }

    /**
     * Tests stub skeleton connection for updating the progress of the view.
     */
    @Test
    public void testUpdateError() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.initTotalBenchProgress(null);
        stubForSkeletonTest.initTotalBenchProgress(elementsMap);
        stubForSkeletonTest.updateError("Not");
        stubForSkeletonTest.updateError("package.Class.method1");
        stubForSkeletonTest.finishedBenchRuns();
    }

    /**
     * Test the stub skeleton for exception.
     */
    @Test(expected = RuntimeException.class)
    public void testNoIncoming() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.finishedBenchRuns();
        skeleton.start();
    }

    /**
     * Test the stub skeleton for exception.
     */
    @Test(expected = RuntimeException.class)
    public void testForFalsePortException() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, 9999);
    }

    /**
     * Test the stub skeleton for exception.
     */
    @Test(expected = RuntimeException.class)
    public void testForFalseHostException() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest("notLocalHost", port);
    }

    /**
     * Test the stub skeleton for exception.
     */
    @Test(expected = RuntimeException.class)
    public void testForInitException() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.finishedBenchRuns();
        stubForSkeletonTest.initTotalBenchProgress(elementsMap);
    }

    /**
     * Test the stub skeleton for exception.
     */
    @Test(expected = RuntimeException.class)
    public void testForUpdateRunException() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.finishedBenchRuns();
        stubForSkeletonTest.updateCurrentRun("element");
    }

    /**
     * Test the stub skeleton for exception.
     */
    @Test(expected = RuntimeException.class)
    public void testForUpdateErrorException() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.finishedBenchRuns();
        stubForSkeletonTest.updateError("element");
    }

    /**
     * Test the stub skeleton for exception.
     */
    @Test(expected = RuntimeException.class)
    public void testForFinishedBenchsException() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.finishedBenchRuns();
        stubForSkeletonTest.finishedBenchRuns();
    }

    /**
     * Test the stub skeleton for false command.
     */
    @Test
    public void testForFalseCommand() {
        WorkerStubForSkeletonTest stubForSkeletonTest =
                new WorkerStubForSkeletonTest(null, port);
        stubForSkeletonTest.sendFalseCommand();
    }

    /**
     * WorkerStub simulates the stub of perfidix.
     * 
     * @author Lewandowski Lukas, DiSy, Univesity of Konstanz
     */
    private class WorkerStubForSkeletonTest {
        private String host;
        private int viewListenerPort;
        private String command;
        private Socket socket;
        private ObjectOutputStream outputStream;

        /**
         * The constructor initializes the given host name and the port, which
         * are needed for creating a client socket. Afterwards it creates the
         * client socket and the object output stream.
         * 
         * @param host
         *            Host represents the {@link String} host name
         * @param viewListenerPort
         *            This param represents the port of the view.
         */
        public WorkerStubForSkeletonTest(String host, int viewListenerPort) {
            if (host == null) {
                this.host = "localhost";
            } else {
                this.host = host;
            }
            this.viewListenerPort = viewListenerPort;
            try {
                socket = new Socket(this.host, this.viewListenerPort);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /** {@inheritDoc} */
        public void initTotalBenchProgress(
                HashMap<String, Integer> benchElementsWithTotalBench) {
            command = "init";
            try {
                outputStream.writeObject(command);
                outputStream.writeObject(benchElementsWithTotalBench);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /** {@inheritDoc} */
        public void updateCurrentRun(String currentElement) {
            command = "updateCurrentRun";
            try {
                outputStream.writeObject(command);
                outputStream.writeObject(currentElement);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /** {@inheritDoc} */
        public void updateError(String element) {
            command = "updateError";
            try {
                outputStream.writeObject(command);
                outputStream.writeObject(element);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /** {@inheritDoc} */
        public void finishedBenchRuns() {
            command = "finished";
            try {
                outputStream.writeObject(command);
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /**
         * Dummy method to test an incoming false command at the skeleton.
         */
        public void sendFalseCommand() {
            command = "falseCommand";
            try {
                outputStream.writeObject(command);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
