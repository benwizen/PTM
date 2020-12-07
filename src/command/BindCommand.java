package command;

import java.io.IOException;
import interperter.Interperter;

public class BindCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		Interperter.bindsTable.put(args[1], args[3]);
		return 0;
	}

}
