package org.perfidix.Perclipse.tempPerfidixClientClasses;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class PerclipseViewStub implements IBenchRunSessionListener {
    private String host;
    private int viewListenerPort;
    private String command;
    private Socket socket;
    private ObjectOutputStream outputStream;

    public PerclipseViewStub(String host, int viewListenerPort) {
        this.host = host;
        this.viewListenerPort = viewListenerPort;
        try {
            socket = new Socket(host, viewListenerPort);
            outputStream =
                    new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void initTotalBenchProgress(
            int totalRun, Object[] benchElementsWithTotalBench) {
        command = "init";
        try {
            outputStream.writeObject(command);
            outputStream.writeObject(totalRun);
            outputStream.writeObject(benchElementsWithTotalBench);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void updateCurrentRun(String currentElement) {
        command = "updateCurrentRun";
        try {
            outputStream.writeObject(command);
            outputStream.writeObject(currentElement);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void updateError(String element) {
        command = "updateError";
        try {
            outputStream.writeObject(command);
            outputStream.writeObject(element);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void finishedBenchRuns() {
        command = "finished";
        try {
            outputStream.writeObject(command);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
