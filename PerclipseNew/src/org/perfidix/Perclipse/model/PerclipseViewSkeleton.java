package org.perfidix.Perclipse.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class PerclipseViewSkeleton extends Thread {
	IBenchRunSessionListener sessionListener;
	ServerSocket serverSocket;
	int serverPort;
	//private ObjectOutputStream out;
	private ObjectInputStream in;

	public PerclipseViewSkeleton(int port) {
		sessionListener=new BenchRunSessionListener();
		serverPort = port;
		try {
			System.out.println("Creating server socket: " + serverPort);
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {

		Socket socket;
		try {
			socket = serverSocket.accept();
			System.out.println("Got connection!");
			//out= new ObjectOutputStream(socket.getOutputStream());
			in= new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean finished=false;
		while(!finished){
			try {
				String command;
				command=(String)in.readObject();
				if("init".equals(command)){
					System.out.println("init");
					int total=(Integer) in.readObject();
					Object[] elements= (Object[]) in.readObject();
					sessionListener.initTotalBenchProgress(total, elements);
				}
				else if("updateCurrentRun".equals(command)){
					System.out.println("updateCurrentRun");
					String currentElement=(String)in.readObject();
					sessionListener.updateCurrentRun(currentElement);
					
				}
				else if("updateError".equals(command)){
					System.out.println("updateError");
					String errorElement=(String)in.readObject();
					sessionListener.updateError(errorElement);
				}
				else if("finished".equals(command)){
					System.out.println("Bench runs finished");
					finished=true;
				}
				else {
					throw new RuntimeException("unknown command:" +command);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}

}
