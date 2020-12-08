package test;

import java.io.IOException;

import interperter.Interperter;

public class MyInterpreter {

	public static  int interpret(String[] lines) throws IOException{
		Interperter.parser(lines);
		return 0;
	}
}
