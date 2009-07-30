package org.perfidix.Perclipse.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

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
    private int serverPort;
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
            // TODO Auto-generated catch block
            e.printStackTrace();
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

        Socket socket;
        try {
            socket = serverSocket.accept();
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        boolean finished = false;
        String command;
        while (finished == false) {
            try {
                command = (String) in.readObject();
                if ("init".equals(command)) {
                    System.out.println("init");
                    HashMap<String, Integer> elements =
                            (HashMap<String, Integer>) in.readObject();
                    sessionListener.initTotalBenchProgress(elements);
                } else if ("updateCurrentRun".equals(command)) {
                    System.out.println("updateCurrentRun");
                    String currentElement = (String) in.readObject();
                    sessionListener.updateCurrentRun(currentElement);

                } else if ("updateError".equals(command)) {
                    System.out.println("updateError");
                    String errorElement = (String) in.readObject();
                    sessionListener.updateError(errorElement);
                } else if ("finished".equals(command)) {
                    System.out.println("Bench runs finished");
                    finished = true;
                } else {
                    throw new RuntimeException("unknown command:" + command);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                finished = true;
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                finished = true;
            }

        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
