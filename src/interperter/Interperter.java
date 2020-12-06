package interperter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import command.Command;

public class Interperter {
	
	private HashMap<String, Command> bindsTable;
	private HashMap<String, Command> varsTable;
	private static HashMap<String, Command> commandTable = new HashMap<String, Command>(){{
        commandTable.put("openDataServer", new Command());
        commandTable.put("connect", new Command());
        commandTable.put("var", new Command());
        commandTable.put("while", new Command());
        commandTable.put("set", new Command());
        commandTable.put("bind", new Command());
        commandTable.put("return", new Command());
        commandTable.put("disconnect", new Command());
        commandTable.put("if", new Command());
        commandTable.put("condition", new Command());
	}};
	
	public static String[] lexer(String str) {
		List<String> final_words = new ArrayList<String>();
		String[] words = str.split("\\s+");
		
		for(String word: words) {
			final_words.addAll(Arrays.asList(word.split("(?<=[-+*/])|(?=[-+*/])")));
		}
		return (String[]) final_words.toArray();
	}
	public static int parser(String[] words) {
		
		
		return 0;
	}
}
