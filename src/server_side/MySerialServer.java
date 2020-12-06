package server_side;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class MySerialServer implements Server {

	private int port;
	private FlightSimulatorHandler client_handler;
	private volatile boolean stop;
	
	public MySerialServer(int port) {
		this.port = port;
		this.stop = false;
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