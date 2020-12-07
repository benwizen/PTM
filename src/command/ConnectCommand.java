package command;

import java.io.IOException;
import java.net.Socket;

import clientConnect.ClientSocket;

public class ConnectCommand implements Command {
	
	@Override
	public int doCommand(String[] args) throws IOException {
		ClientSocket client = new ClientSocket(args[1], Integer.parseInt(args[2]));
		return 0;
	}

}
