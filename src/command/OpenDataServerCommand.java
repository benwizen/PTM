package command;

import server_side.MySerialServer;
import server_side.Server;
import java.io.IOException;

public class OpenDataServerCommand implements Command {
	public boolean connected = false;
	@Override
	public int doCommand(String[] args) throws IOException {
		if(MySerialServer.getServer() == null)
		{
			int port = Integer.parseInt(args[1]);
			Server server = new MySerialServer(port, Integer.parseInt(args[2]), this);
			server.start();
		}
		return 0;
	}
	
	public static void stop() throws IOException {
		
	}
}
