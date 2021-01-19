package server_side;
import java.io.IOException;
import java.net.*;

import clientConnect.ClientSocket;
import command.OpenDataServerCommand;

import java.io.*;

public class MySerialServer implements Server {

	private int port;
	private FlightSimulatorHandler client_handler;
	private volatile boolean stop;
	private static MySerialServer currentServer;
	ServerSocket server;
	OpenDataServerCommand dataServer;
	private Thread serverThread;
	
	public MySerialServer(int port, int timeout, OpenDataServerCommand dataServer) {
		this.dataServer = dataServer;
		this.port = port;
		this.stop = false;
		client_handler = new FlightSimulatorHandler(timeout);
		this.currentServer = this;
	}
	
    public synchronized static MySerialServer getServer() throws IOException {
        if (currentServer == null)
        {
        	currentServer = null;
        }

        return currentServer;
    }
    public Thread getServerThread()
    {
    	return serverThread;
    }
	
	public void open() {
		try
		{
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			//server.setSoTimeout(10000);
			
			Socket serverSocket = null;
			InputStream fromClient = null;
			OutputStream toClient = null;
			
			while(!this.stop)
			{
				try {
					serverSocket = server.accept();
					System.out.println("connected");
					dataServer.connected = true;
					fromClient = serverSocket.getInputStream();
					toClient = serverSocket.getOutputStream();
					client_handler.handleClient(fromClient, toClient);
					
				}
				catch(SocketTimeoutException e)
				{
					System.out.println("timeout");
					this.stop = true;
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
		serverThread = new Thread(()->open());
		serverThread.start();
	}

	@Override
	public void stop() {
		this.client_handler.stop = true;
		this.stop = true;
	}

}