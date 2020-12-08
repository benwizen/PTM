package command;

import interperter.Interperter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class VarCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		String varName = args[1];
		List<String> argsList = Arrays.asList(args);

		if (args.length == 2)
			Interperter.putClientVariables(varName, -1.0);

		else if (argsList.contains("bind"))
		{
			Interperter.putClientVariables(varName, -1.0);
			Interperter.Commands.get("bind").doCommand(args);
		}
		else if (args.length > 2) {
			int index = argsList.indexOf("=");
			List<String> afterEqualArgs = argsList.subList(index + 1, args.length);
			double value = algorithms.ShuntingYard.evaluate(String.join("", Interperter.ListToArray(afterEqualArgs)));
			Interperter.putClientVariables(varName, value);
		}
		return 0;
	}

}
