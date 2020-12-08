package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import command.AssignmentCommand;
import command.BindCommand;
import command.Command;
import command.ConditionCommand;
import command.ConnectCommand;
import command.DisconnectCommand;
import command.OpenDataServerCommand;
import command.ReturnCommand;
import command.VarCommand;
import command.WhileCommand;


public class ShuntingYard {
	public static HashMap<Character, Integer> priorityMap;
	static { 
		priorityMap = new HashMap<Character, Integer>();
		priorityMap.put('+', 1);
		priorityMap.put('-', 1);
		priorityMap.put('*', 2);
		priorityMap.put('/', 2);
		priorityMap.put('^', 3);
	};
	
    public static double evaluate(String expression) {
        return calcPostFix(convertToPostFix(expression));
    }
    public static double calcPostFix(List<String> postfix) {

        Stack<Double> operands = new Stack<>();
        Double left, right;
        for (String s : postfix) {
            if (isDouble(s)) {
                operands.add(Double.parseDouble(s));
            } else {
                right = operands.pop();
                left = operands.pop();
                operands.push(calculate(left, right, s.charAt(0)));
            }
        }

        return operands.pop();
    }


    public static List<String> convertToPostFix(String infix) {
    	char last;
        List<String> postFix = new ArrayList<>();
        Stack<Character> opr = new Stack<>();
        StringBuilder currNum = new StringBuilder();

        for (int i = 0; i < infix.length(); i++) {
            char currChar = infix.charAt(i);
            if (currChar >= '0' && currChar <= '9') {
                last = currChar;
                currNum.append(last);
            } else {
                if (currNum.length() > 0) {
                    postFix.add(currNum.toString());
                    currNum = new StringBuilder();
                }
                if (currChar == '+' || currChar == '-' || currChar == '*' || currChar == '/' || currChar == '^') {
                    while (!opr.empty() && operatorPriority(currChar) <= operatorPriority(opr.lastElement())) {
                        postFix.add(opr.pop().toString());
                    }
                    opr.add(currChar);

                } else if (currChar == '(') {
                    opr.add(currChar);

                } else if (currChar == ')') {
                    while (!opr.empty() && opr.lastElement() != '(') {
                        postFix.add(opr.pop().toString());
                    }
                    opr.pop();
                }
            }

        }

        if (currNum.length() > 0) {
            postFix.add(currNum.toString());
        }

        while (!opr.empty()) {
            postFix.add(opr.pop().toString());
        }

        return postFix;
    }
    
    protected static double calculate(double left, double right, char operator) {
        switch (operator) {
            case '+':
                return (new Addition(left,right)).evaluate();
            case '-':
                return (new Reduction(left,right)).evaluate();
            case '*':
                return (new Multplication(left,right)).evaluate();
            case '/':
                return (new Division(left,right)).evaluate();
            case '^':
                return Math.pow(left, right);
        }
        return 0;
    }
    
    public static int operatorPriority(char operator) {
        if(priorityMap.containsKey(operator))
        	return priorityMap.get(operator);
        else
        	return 0;
    }

    public static boolean isDouble(String number)
    {	
	    try{
	        Double.parseDouble(number);
	    }
	    catch(Exception e){
	        return false;
	    }
	    return true;
	}
}