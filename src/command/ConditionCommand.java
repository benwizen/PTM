package command;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import interperter.Interperter;


public class ConditionCommand implements Command {

    @Override
    public int doCommand(String[] args) throws IOException {
    	List<String> leftArgs;
    	List<String> rightArgs;
    	List<String> argsList = Arrays.asList(args);
    	
        String[] equalityPredicators = new String[] {"==", "!=", "<", ">", "<=", ">="};
        int indexOfEqualityPredicator = -1;
        for(String ep: equalityPredicators) {
        	indexOfEqualityPredicator = argsList.indexOf(ep);
        	if(indexOfEqualityPredicator != -1)
        		break;
        }
        leftArgs = argsList.subList(0, indexOfEqualityPredicator);
        rightArgs = argsList.subList(indexOfEqualityPredicator + 1, argsList.size());

        double leftValue = algorithms.ShuntingYard.evaluate(String.join("", Interperter.ListToArray(leftArgs)));
        double rightValue = algorithms.ShuntingYard.evaluate(String.join("", Interperter.ListToArray(rightArgs)));
        
        String equalityPredicator = args[indexOfEqualityPredicator];
        switch (equalityPredicator) {
			case ">":
				return leftValue > rightValue ? 1 : 0;
			case "<":
				return leftValue < rightValue ? 1 : 0;
			case "==":
				return leftValue == rightValue ? 1 : 0;
			case "!=":
				return leftValue != rightValue ? 1 : 0;
			case "<=":
				return leftValue <= rightValue ? 1 : 0;
			case ">=":
				return leftValue >= rightValue ? 1 : 0;
			}
        return 0;
    }

}
