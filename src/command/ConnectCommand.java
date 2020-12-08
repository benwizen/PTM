package command;

import java.io.IOException;
import java.net.Socket;

import clientConnect.ClientSocket;

public class ConnectCommand implements Command {
	
	@Override
	public int doCommand(String[] args) throws IOException {
		String ip = args[1];
		int port = Integer.parseInt(args[2]);
		ClientSocket client = new ClientSocket(ip, port);
		return 0;
	}

}
