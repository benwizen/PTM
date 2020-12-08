package command;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import interperter.Interperter;

public class WhileCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		List<String> argsList = Arrays.asList(args);
		List<String> predicate = argsList.subList(1, args.length - 1);
		return Interperter.Commands.get("condition").doCommand(predicate.toArray(String[]::new));
	}

}
