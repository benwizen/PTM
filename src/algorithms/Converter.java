package algorithms;
import java.util.HashMap;

import algorithms.ShuntingYard;

public class Converter {
	
	public static String[] expToNums(String[] expression)
	{
		String[] numArray = new String[expression.length];
		for(int i = 0 ; i < expression.length; i++)
		{
			if(!ShuntingYard.isDouble(expression[i]) && !expression[i].equals("+") && !expression[i].equals("-") && !expression[i].equals("*")&& !expression[i].equals("/")&& !expression[i].equals("^")&& !expression[i].equals("(")&& !expression[i].equals(")"))
			{
				numArray[i] = interperter.Interperter.ClientVariables.get(expression[i]).getValue();
			}
			else
			{
				numArray[i] = expression[i];
			}
		}
		return numArray;
	}
}
