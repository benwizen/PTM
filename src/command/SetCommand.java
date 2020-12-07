package command;

import java.io.IOException;
import clientConnect.ClientSocket;

public class SetCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		
		ClientSocket.getClientSocket().send("set ")
	}

}
