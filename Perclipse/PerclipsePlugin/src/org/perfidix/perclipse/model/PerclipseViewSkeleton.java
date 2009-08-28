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
