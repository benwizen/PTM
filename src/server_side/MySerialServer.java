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
	ServerSocket server;
	
	public MySerialServer(int port, int timeout) {
		this.port = port;
		this.stop = false;
		client_handler = new FlightSimulatorHandler(timeout);
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
			server = new ServerSocket(port);
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
					if(fromClient != null)
					{
						fromClient.close();
					}
					if(toClient != null)
						toClient.close();
					if(serverSocket!=null)
					{
						serverSocket.close();
					}
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
		this.client_handler.stop = true;
		this.stop = true;
	}

}