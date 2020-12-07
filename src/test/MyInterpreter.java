package test;

import interperter.Interperter;

public class MyInterpreter {

	public static  int interpret(String[] lines){
		Interperter.parser(lines);
		return 0;
	}
}
