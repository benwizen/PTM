package test;

import java.io.IOException;

import interperter.Interperter;

public class MyInterpreter {

	public static  int interpret(String[] lines){
		try {
			return Interperter.parser(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
