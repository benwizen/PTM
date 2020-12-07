package interperter;

import command.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import variable.*;

public class Interperter {
	
	public static HashMap<String, ClientVariable> ClientVariables = new HashMap<String, ClientVariable>();
	public static final HashMap<String, SimulatorVariable> SimVariables;
	static {
		SimVariables = new HashMap<String, SimulatorVariable>();
		SimVariables.put("simX", new SimulatorVariable("simX", ""));
		SimVariables.put("simY", new SimulatorVariable("simY", ""));
		SimVariables.put("simZ", new SimulatorVariable("simZ", ""));
	};
	
	public static HashMap<String, Command> Commands;
	static { 
		Commands = new HashMap<String, Command>();
        Commands.put("openDataServer", new OpenDataServerCommand());
        Commands.put("connect", new ConnectCommand());
        Commands.put("var", new VarCommand());
        Commands.put("while", new WhileCommand());
        Commands.put("bind", new BindCommand());
        Commands.put("assignment", new AssignmentCommand());
        Commands.put("return", new ReturnCommand());
        Commands.put("disconnect", new DisconnectCommand());
        Commands.put("condition", new ConditionCommand());
	};
	
	public static Command getCommand(String command_id) {
		return Commands.get(command_id);
	}
	
	public static int runLine(String line) throws IOException {
		List<String> words = lexer(line);
		String command_id = words.get(0);
		return getCommand(command_id).doCommand(words.stream().toArray(String[]::new));
	}
	
	public static boolean is_command(String word) {
		return Commands.containsKey(word);
	}
	
	
	public static List<String> lexer(String str) {
		List<String> final_words = new ArrayList<String>();
		String[] words = str.split("\\s+");
		for(String word: words) {
			final_words.addAll(Arrays.asList(word.split("(?<=[-+*()/])|(?=[-+*()/])")));
		}
		return final_words;
	}
	
	public static int parser(String[] lines) throws IOException {
		int executeFlag = 1;
		List<String> linesList = Arrays.asList(lines);
		for(String line: lines) {
			List<String> words = lexer(line);
			if(executeFlag == 0) {
				if(words.get(0) == "}") {
					executeFlag = 1;
				}
				continue;
			}
			String[] wordsArray = words.stream().toArray(String[]::new);
			String command_id = words.get(0);
			if(is_command(command_id)) {
				if(command_id == "while") {
					Command whileCommand = getCommand(command_id); 
					while(whileCommand.doCommand(wordsArray) == 1) {
						int whileLineIndex = linesList.indexOf(line);
						while(whileLineIndex < linesList.indexOf("}")) {
							whileLineIndex++;
							String currentLine = lines[whileLineIndex];
							runLine(currentLine);
						}
					}
					executeFlag = 0;
				}
				else if (command_id == "return") {
					return runLine(line);
				}
				else {
					runLine(line);
				}
			}
			else {
				if(words.contains("bind")) {
					getCommand("bind").doCommand(wordsArray);
				}
				else {
					getCommand("assignment").doCommand(wordsArray);
				}
				
			}
			
		}
		return 0;
	}
}
