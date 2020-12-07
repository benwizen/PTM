package server_side;
import java.io.IOException;
import java.net.*;

import clientConnect.ClientSocket;

import java.io.*;

public class MySerialServer implements Server {

	private int port;
	private FlightSimulatorHandler client_handler;
	private volatile boolean stop;
	private static Server currentServer;
	
	public MySerialServer(int port) {
		this.port = port;
		this.stop = false;
		this.currentServer = this;
	}
	
    public synchronized static Server getServer() throws IOException {
        if (currentServer == null)
        {
        	currentServer = null;
        }

        return currentServer;
    }
	
	public void open() {
		try
		{
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(10000);
			
			Socket serverSocket = null;
			InputStream fromClient = null;
			OutputStream toClient = null;
			
			while(!this.stop)
			{
				try {
					serverSocket = server.accept();
					System.out.println("connected");
					fromClient = serverSocket.getInputStream();
					toClient = serverSocket.getOutputStream();
					client_handler.handleClient(fromClient, toClient);
					
				}
				catch(SocketTimeoutException e)
				{
					System.out.println("timeout");
				}
				finally {
					fromClient.close();
					toClient.close();
					serverSocket.close();
				}
			}
			server.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	
	@Override
	public void start()
	{
		new Thread(()->open()).start();
	}

	@Override
	public void stop() {
		this.stop = true;
	}

}