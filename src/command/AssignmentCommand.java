package command;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import interperter.Interperter;
import variable.ClientVariable;

public class AssignmentCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		String clientVarName = args[0];

		List<String> argsList = Arrays.asList(args);
		List<String> expression = argsList.subList(2, args.length);
		double value = algorithms.ShuntingYard.evaluate(String.join(" ", Interperter.ListToArray(expression)));
		Interperter.putClientVariables(clientVarName, value);
		return 0;
	}

}
