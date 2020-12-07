package command;

import java.io.IOException;
import variable.*;

public class BindCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		ClientVariable clientVar = "";
		SimulatorVariable simVar = "";
		
		simVar.addObserver(clientVar);
		clientVar.addObserver(simVar);
		simVar.setValue(clientVar.getValue());
		
		return 0;
	}

}
