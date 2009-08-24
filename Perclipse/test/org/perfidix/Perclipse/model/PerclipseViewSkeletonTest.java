package org.perfidix.Perclipse.model;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.eclipse.jdt.launching.SocketUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the java class
 * {@link org.perfidix.Perclipse.model.PerclipseViewSkeleton}.
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
     * {@link org.perfidix.Perclipse.model.PerclipseViewSkeleton#PerclipseViewSkeleton(int)}
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
    }
}
