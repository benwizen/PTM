package command;

import java.io.IOException;

import interperter.Interperter;
import variable.*;

public class BindCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		String clientVarName;
		String simVarName;
		
		if(args[0].equals("var")){
			clientVarName = args[1];
			simVarName = args[4];
		}
		else{
			clientVarName = args[0];
			simVarName = args[3];
		}

		ClientVariable clientVar = Interperter.ClientVariables.get(clientVarName);
		SimulatorVariable simVar = Interperter.SimVariables.get(simVarName);

		simVar.addObserver(clientVar);
		clientVar.addObserver(simVar);
		simVar.setValue(clientVar.getValue());
		
		return 0;
	}

}
