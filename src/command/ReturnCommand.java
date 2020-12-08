package command;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import interperter.Interperter;

public class ReturnCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		List<String> argsList = Arrays.asList(args);
		List<String> expression = argsList.subList(1, args.length);
		String[] expressionNums = algorithms.Converter.expToNums(expression.toArray(String[]::new));
		double value = algorithms.ShuntingYard.evaluate(String.join("", expressionNums));
		return (int)(value);
	}

}
