package command;

import server_side.MySerialServer;
import server_side.Server;
import java.io.IOException;

public class OpenDataServerCommand implements Command {
	
	@Override
	public int doCommand(String[] args) throws IOException {
		int port = Integer.parseInt(args[1]);
		Server server = new MySerialServer(port, Integer.parseInt(args[2]));
		server.start();
		return 0;
	}
	
	public static void stop() throws IOException {
		
	}
}
