package command;

import java.io.IOException;

import interperter.Interperter;
import variable.*;

public class BindCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		String clientVarName;
		String simVarName;
		int flag = 0;
		
		if(args[0].equals("var")){
			clientVarName = args[1];
			simVarName = args[4];
		}
		else{
			flag = 1;
			clientVarName = args[0];
			simVarName = args[3];
		}

		ClientVariable clientVar = Interperter.ClientVariables.get(clientVarName);
		if(simVarName.contains("\"")) {
			simVarName = "";
			if(flag == 0) {
				for(int i = 4; i<args.length; i++) {
					simVarName = String.join("", simVarName, args[i]);
				}
			}
			else {
				for(int i = 3; i<args.length; i++) {
					simVarName = String.join("", simVarName, args[i]);
				}
			}

		}
		simVarName = simVarName.replaceAll("\"", "");
		SimulatorVariable simVar = Interperter.SimVariables.get(simVarName);

		clientVar.setValue(simVar.getValue(), true);
		simVar.addObserver(clientVar);
		clientVar.addObserver(simVar);
		
		
		return 0;
	}

}
