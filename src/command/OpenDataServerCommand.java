package command;

import server_side.MySerialServer;
import server_side.Server;
import java.io.IOException;

public class OpenDataServerCommand implements Command {
	Server server;
	
	@Override
	public int doCommand(String[] args) throws IOException {
		int port = Integer.parseInt(args[1]);
		server = new MySerialServer(port);
		server.start();
		return 0;
	}
	
	public static void stop() throws IOException {
		
	}
}
