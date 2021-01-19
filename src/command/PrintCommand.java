package command;

import java.io.IOException;

import interperter.Interperter;

public class PrintCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		if(args[1].contains("\"")) {
			System.out.println(args[1].replaceAll("\"", ""));
		}
		else {
			if(Interperter.ClientVariables.get(args[1]) == null)
				System.out.println("ERR : Variable not found");
			else
				System.out.println(Interperter.ClientVariables.get(args[1]).getValue());
		}
		return 0;
	}

}
