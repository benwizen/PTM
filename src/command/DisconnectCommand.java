package command;

import java.io.IOException;

import clientConnect.ClientSocket;
import server_side.MySerialServer;

public class DisconnectCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		ClientSocket.getClientSocket().stop();
		MySerialServer.getServer().stop();
		while(MySerialServer.getServer().getServerThread().isAlive())
		{
			
		}
		return 0;
	}

}
