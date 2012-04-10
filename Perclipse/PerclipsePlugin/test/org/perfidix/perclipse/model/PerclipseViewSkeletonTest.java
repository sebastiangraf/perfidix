/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
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

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.launching.SocketUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.perclipse.model.PerclipseViewSkeleton}.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseViewSkeletonTest {

    private transient PerclipseViewSkeleton skeleton;
    private transient Map<String, Integer> elementsMap;
    private transient int port;
    private static final transient String NOT_NULL =
            "Tests if the object is not null.";

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

    // /**
    // * Simple tearDown - method.
    // *
    // * @throws java.lang.Exception
    // * The Exception occurred.
    // */
    // @After
    // public void tearDown() throws Exception {
    // skeleton = null;
    // elementsMap = null;
    // }

    /**
     * Tests the method
     * {@link org.perfidix.perclipse.model.PerclipseViewSkeleton#PerclipseViewSkeleton(int)}
     * .
     */
    @Test
    public void testPerclipseViewSkeleton() {
        assertNotNull(NOT_NULL, skeleton);

    }

    /**
     * Tests stub skeleton connection for initialization of the view.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test
    public void testInitView() throws IOException { // NOPMD by lewandow on
                                                    // 8/31/09 4:33 PM
        final WorkerStubForSkeletonTest stub =
                new WorkerStubForSkeletonTest(null, port);
        stub.initTotalBenchProgress(null);
        stub.initTotalBenchProgress(elementsMap);
        stub.finishedBenchRuns();
    }

    /**
     * Tests stub skeleton connection for updating the progress of the view.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test
    public void testUpdateCurrentRun() throws IOException { // NOPMD by lewandow
                                                            // on 8/31/09 4:33
                                                            // PM
        final WorkerStubForSkeletonTest stub =
                new WorkerStubForSkeletonTest(null, port);
        stub.initTotalBenchProgress(elementsMap);
        stub.updateCurrentRun("No");
        stub.updateCurrentRun("package.Class.method1");
        stub.finishedBenchRuns();
    }

    /**
     * Tests stub skeleton connection for updating the progress of the view.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test
    public void testUpdateError() throws IOException { // NOPMD by lewandow on
                                                       // 8/31/09 4:33 PM
        final WorkerStubForSkeletonTest stub =
                new WorkerStubForSkeletonTest(null, port);
        stub.initTotalBenchProgress(null);
        stub.initTotalBenchProgress(elementsMap);
        stub.updateError("Not");
        stub.updateError("package.Class.method1");
        stub.finishedBenchRuns();
    }

    // /**
    // * Test the stub skeleton for exception.
    // * @throws IOException Exception occurred.
    // */
    // @Test(expected = IOException.class)
    // public void testNoIncoming() throws IOException {
    // final WorkerStubForSkeletonTest stub =
    // new WorkerStubForSkeletonTest(null, port);
    // stub.finishedBenchRuns();
    // skeleton.start();
    // }

    /**
     * Test the stub skeleton for exception.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test(expected = IOException.class)
    public void testForFalsePortException() throws IOException {
        new WorkerStubForSkeletonTest(null, 9999);
    }

    /**
     * Test the stub skeleton for exception.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test(expected = IOException.class)
    public void testForFalseHostException() throws IOException {
        new WorkerStubForSkeletonTest("notLocalHost", port);
    }

    /**
     * Test the stub skeleton for exception.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test(expected = IOException.class)
    public void testForInitException() throws IOException {
        final WorkerStubForSkeletonTest stub =
                new WorkerStubForSkeletonTest(null, port);
        stub.finishedBenchRuns();
        stub.initTotalBenchProgress(elementsMap);
    }

    /**
     * Test the stub skeleton for exception.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test(expected = IOException.class)
    public void testForUpdateRunException() throws IOException {
        final WorkerStubForSkeletonTest stub =
                new WorkerStubForSkeletonTest(null, port);
        stub.finishedBenchRuns();
        stub.updateCurrentRun("element");
    }

    /**
     * Test the stub skeleton for exception.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test(expected = IOException.class)
    public void testForUpdateErrorException() throws IOException {
        final WorkerStubForSkeletonTest stub =
                new WorkerStubForSkeletonTest(null, port);
        stub.finishedBenchRuns();
        stub.updateError("element");
    }

    /**
     * Test the stub skeleton for exception.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test(expected = IOException.class)
    public void testForFinishedBenchsException() throws IOException {
        final WorkerStubForSkeletonTest stub =
                new WorkerStubForSkeletonTest(null, port);
        stub.finishedBenchRuns();
        stub.finishedBenchRuns();
    }

    /**
     * Test the stub skeleton for false command.
     * 
     * @throws IOException
     *             Exception occurred.
     */
    @Test
    public void testForFalseCommand() throws IOException { // NOPMD by lewandow
                                                           // on 8/31/09 4:33 PM
        final WorkerStubForSkeletonTest stub =
                new WorkerStubForSkeletonTest(null, port);
        stub.sendFalseCommand();
    }

    /**
     * WorkerStub simulates the stub of perfidix.
     * 
     * @author Lewandowski Lukas, DiSy, Univesity of Konstanz
     */
    private class WorkerStubForSkeletonTest {

        private transient String command;
        private final transient Socket socket;
        private final transient ObjectOutputStream outputStream;

        /**
         * The constructor initializes the given host name and the port, which
         * are needed for creating a client socket. Afterwards it creates the
         * client socket and the object output stream.
         * 
         * @param host
         *            Host represents the {@link String} host name
         * @param viewListenerPort
         *            This param represents the port of the view.
         * @throws IOException
         *             Exception occurred.
         */
        public WorkerStubForSkeletonTest(
                final String host, final int viewListenerPort)
                throws IOException {
            String hosti = host;
            final int listenerPort = viewListenerPort;
            if (hosti == null) {
                hosti = "localhost";
            }
            socket = new Socket(hosti, listenerPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        }

        /**
         * Test method for stub.
         * 
         * @param benchElements
         *            The map
         * @throws IOException
         *             Exception occurred.
         */
        public void initTotalBenchProgress(
                final Map<String, Integer> benchElements) throws IOException {
            command = "init";
            outputStream.writeObject(command);
            outputStream.writeObject(benchElements);

        }

        /**
         * Test method update current run.
         * 
         * @param currentElement
         *            the current element.
         * @throws IOException
         *             Exception occurred.
         */
        public void updateCurrentRun(final String currentElement)
                throws IOException {
            command = "updateCurrentRun";
            outputStream.writeObject(command);
            outputStream.writeObject(currentElement);

        }

        /**
         * Test method update error.
         * 
         * @param element
         *            the element where the error occurred.
         * @throws IOException
         *             Exception occurred.
         */
        public void updateError(final String element) throws IOException {
            command = "updateError";
            outputStream.writeObject(command);
            outputStream.writeObject(element);

        }

        /**
         * Test method for finished runs.
         * 
         * @throws IOException
         *             Exception occurred.
         */
        public void finishedBenchRuns() throws IOException {
            command = "finished";
            outputStream.writeObject(command);
            outputStream.close();
            socket.close();

        }

        /**
         * Dummy method to test an incoming false command at the skeleton.
         * 
         * @throws IOException
         *             Exception occurred.
         */
        public void sendFalseCommand() throws IOException {
            command = "falseCommand";
            outputStream.writeObject(command);

        }

    }

}
