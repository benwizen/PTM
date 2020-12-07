package command;

import java.io.IOException;

import interperter.Interperter;

public class AssignmentCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		if(args[3] == "bind")
		{
			Interperter.commandTable.get("bind").doCommand(args);
			return 0;
		}
		if(args[0] == "var")
		{
			Interperter.commandTable.get("var").doCommand(args);
			return 0;
		}
		else
		{
			Interperter.commandTable.get("set").doCommand(args);
			return 0;
		}
	}

}
