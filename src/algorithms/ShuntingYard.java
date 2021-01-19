package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
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
import interperter.Interperter;


public class ShuntingYard {
	public static HashMap<Character, Integer> priorityMap;
	static { 
		priorityMap = new HashMap<Character, Integer>();
		priorityMap.put('(', 0);
		priorityMap.put(')', 0);
		priorityMap.put('+', 1);
		priorityMap.put('-', 1);
		priorityMap.put('*', 2);
		priorityMap.put('/', 2);
	};
	
    public static double evaluate(String expression) {
        return calcPostFix(convertToPostFix(expression.split(" ")));
    }
    public static double calcPostFix(List<String> postfix) {

        Stack<Double> operands = new Stack<>();
        Double left, right;
        for (String s : postfix) {
        	if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^")) {
                right = operands.pop();
                if(operands.isEmpty()) {
                	return calculate(0.0, right, s.charAt(0));
                }
                left = operands.pop();
                operands.push(calculate(left, right, s.charAt(0)));
        	}
        	else {
                operands.add(Converter.varToNum(s));
        	}
        }
        return operands.pop();
       
    }
    
	public static List<String> convertToPostFix(String[] exp) {
        Stack<String> s = new Stack<String>();
        String postfix = ""; 
        for (int i=0;i<exp.length;i++){                 
        	if (!isOperandOrClientVar(exp[i]) && !(exp[i].compareTo(")") == 0)){                 
                while (!s.empty() && !s.peek().equals("(") && compareToPirority(s.peek(), exp[i])){
		                    postfix += " " + s.peek(); 
		                    s.pop();
                }
                s.push(exp[i]);
            }
            else if (isOperandOrClientVar(exp[i])) {
                postfix += " " + exp[i];
            }
            else if (exp[i].compareTo(")") == 0){
                while (!s.empty() && !s.peek().equals("(")){
                    postfix += " " + s.peek(); s.pop();
                }
                s.pop();
            }
        }
        while (!s.empty()){
            postfix += " " + s.peek(); s.pop();
        }
        return Arrays.asList(postfix.toString().replaceFirst(" ", "").split(" "));                                             
    }
   
 
    public static boolean isOperandOrClientVar(String operand){
    	try
    	{
    	  Float.parseFloat(operand);
    	  return true;
    	}
    	catch(NumberFormatException e)
    	{
    		if(Interperter.ClientVariables.get(operand)!=null) {
    			return true;
    		}
    		return false;
    	}
    }

    public static boolean compareToPirority(String operand1, String operand2){
        int operand1Priority = getOperatorPriority(operand1);
        int operand2Priority = getOperatorPriority(operand2);
        if ((operand1Priority == operand2Priority) && (operand1Priority != 3)){
            return true;
        }else if ((operand1Priority == 3) && (operand2Priority == 3)) {
        	return false;
        }
        return operand1Priority > operand2Priority ? true : false;
    }

    public static int getOperatorPriority(String operand){
        int weight = -1;
        if(priorityMap.get(operand.charAt(0)) != null)
        	weight = priorityMap.get(operand.charAt(0));
        return weight;
    }
    
    protected static double calculate(Double left, Double right, char operator) {
    	if(operator == '+')
                return (new Addition(left,right)).evaluate();
    	if(operator == '-')
                return (new Reduction(left,right)).evaluate();
    	if(operator == '*')
                return (new Multplication(left,right)).evaluate();
    	if(operator == '/')
                return (new Division(left,right)).evaluate();
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