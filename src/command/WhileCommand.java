package command;

import java.io.IOException;

public class WhileCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		Expression exp = args[0];
		Commands[] commands = args[1:]
		return 0;
	}

}
