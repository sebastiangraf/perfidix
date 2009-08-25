package org.perfidix.Perclipse.model;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.perfidix.Perclipse.launcher.PerclipseActivator;

/**
 * The PerclipseViewSkeleton class is responsible for creating our ServerSocket.
 * At this point we are waiting for current progress information from our
 * Perfidix bench process.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class PerclipseViewSkeleton extends Thread {
    private IBenchRunSessionListener sessionListener;
    private ServerSocket serverSocket;
    private Socket socket = null;
    private int serverPort;
    private boolean finished = false;
    private ObjectInputStream in;

    /**
     * The constructor gets a given free port and initializes the bench run
     * session listener. Afterwards it creates a servers socket with the given
     * port number.
     * 
     * @param port
     *            The given free port number.
     */
    public PerclipseViewSkeleton(int port) {
        sessionListener = new BenchRunSessionListener();
        serverPort = port;
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
    public void run() {

        try {
            socket = serverSocket.accept();

            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e1) {
            PerclipseActivator.log(e1);
            e1.printStackTrace();
        }

        String command;
        while (!finished) {
            try {
                command = (String) in.readObject();
                if ("init".equals(command)) {
                    HashMap<String, Integer> elements =
                            (HashMap<String, Integer>) in.readObject();
                    sessionListener.initTotalBenchProgress(elements);
                } else if ("updateCurrentRun".equals(command)) {
                    String currentElement = (String) in.readObject();
                    sessionListener.updateCurrentRun(currentElement);

                } else if ("updateError".equals(command)) {
                    String errorElement = (String) in.readObject();
                    sessionListener.updateError(errorElement);
                } else if ("finished".equals(command)) {
                    finished = true;
                    PerclipseActivator.logInfo("Bench process finished");
                } else {
                    PerclipseActivator.logInfo("Unknown command:"
                            + command
                            + "received.");
                }
            } catch (IOException e) {
                if (e instanceof EOFException) {
                    PerclipseActivator
                            .log(
                                    e,
                                    "Running Bench process has been stopped or restarted");


                } else {
                    e.printStackTrace();
                    PerclipseActivator.log(e,"Running Bench process has been stopped or restarted");
                }
                finished = true;
            } catch (ClassNotFoundException e) {
                finished = true;
                PerclipseActivator.log(e);
                e.printStackTrace();
            }

        }
        try {
            if (socket.isConnected()) {
                socket.close();
            }
            serverSocket.close();
        } catch (IOException e) {
            PerclipseActivator.log(e);
            e.printStackTrace();
        }

    }

}
