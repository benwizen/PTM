package interperter;

import command.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class Interperter {
	
	public static HashMap<String, String> bindsTable = new HashMap<String, String>();
	public static HashMap<String, String> varsTable = new HashMap<String, String>();
	public static HashMap<String, Command> commandTable = new HashMap<String, Command>(){{
        commandTable.put("openDataServer", new OpenDataServerCommand());
        commandTable.put("connect", new ConnectCommand());
        commandTable.put("var", new VarCommand());
        commandTable.put("while", new WhileCommand());
        commandTable.put("set", new SetCommand());
        commandTable.put("bind", new BindCommand());
        commandTable.put("return", new ReturnCommand());
        commandTable.put("disconnect", new DisconnectCommand());
        commandTable.put("condition", new ConditionCommand());
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
