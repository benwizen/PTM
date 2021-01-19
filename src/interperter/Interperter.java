package interperter;

import command.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;

import variable.*;

public class Interperter {

	public static HashMap<String, ClientVariable> ClientVariables = new HashMap<String, ClientVariable>();
	public static final String lineSeperator = "\r\n";
	public static final HashMap<String, SimulatorVariable> SimVariables;
	static {
		SimVariables = new LinkedHashMap<String, SimulatorVariable>();
		SimVariables.put("/instrumentation/airspeed-indicator/indicated-speed-kt", new SimulatorVariable("/instrumentation/airspeed-indicator/indicated-speed-kt",""));
		SimVariables.put("/instrumentation/altimeter/indicated-altitude-ft", new SimulatorVariable("/instrumentation/altimeter/indicated-altitude-ft",""));
		SimVariables.put("/instrumentation/altimeter/pressure-alt-ft", new SimulatorVariable("/instrumentation/altimeter/pressure-alt-ft",""));
		SimVariables.put("/instrumentation/attitude-indicator/indicated-pitch-deg", new SimulatorVariable("/instrumentation/attitude-indicator/indicated-pitch-deg",""));
		SimVariables.put("/instrumentation/attitude-indicator/indicated-roll-deg", new SimulatorVariable("/instrumentation/attitude-indicator/indicated-roll-deg",""));
		SimVariables.put("/instrumentation/attitude-indicator/internal-pitch-deg", new SimulatorVariable("/instrumentation/attitude-indicator/internal-pitch-deg",""));
		SimVariables.put("/instrumentation/attitude-indicator/internal-roll-deg", new SimulatorVariable("/instrumentation/attitude-indicator/internal-roll-deg",""));
		SimVariables.put("/instrumentation/encoder/indicated-altitude-ft", new SimulatorVariable("/instrumentation/encoder/indicated-altitude-ft",""));
		SimVariables.put("/instrumentation/encoder/pressure-alt-ft", new SimulatorVariable("/instrumentation/encoder/pressure-alt-ft",""));
		SimVariables.put("/instrumentation/gps/indicated-altitude-ft", new SimulatorVariable("/instrumentation/gps/indicated-altitude-ft",""));
		SimVariables.put("/instrumentation/gps/indicated-ground-speed-kt", new SimulatorVariable("/instrumentation/gps/indicated-ground-speed-kt",""));
		SimVariables.put("/instrumentation/gps/indicated-vertical-speed", new SimulatorVariable("/instrumentation/gps/indicated-vertical-speed",""));
		SimVariables.put("/instrumentation/heading-indicator/indicated-heading-deg", new SimulatorVariable("/instrumentation/heading-indicator/indicated-heading-deg",""));
		SimVariables.put("/instrumentation/magnetic-compass/indicated-heading-deg", new SimulatorVariable("/instrumentation/magnetic-compass/indicated-heading-deg",""));
		SimVariables.put("/instrumentation/slip-skid-ball/indicated-slip-skid", new SimulatorVariable("/instrumentation/slip-skid-ball/indicated-slip-skid",""));
		SimVariables.put("/instrumentation/turn-indicator/indicated-turn-rate", new SimulatorVariable("/instrumentation/turn-indicator/indicated-turn-rate",""));
		SimVariables.put("/instrumentation/vertical-speed-indicator/indicated-speed-fpm", new SimulatorVariable("/instrumentation/vertical-speed-indicator/indicated-speed-fpm",""));
		SimVariables.put("/controls/flight/aileron", new SimulatorVariable("/controls/flight/aileron",""));
		SimVariables.put("/controls/flight/elevator", new SimulatorVariable("/controls/flight/elevator",""));
		SimVariables.put("/controls/flight/rudder", new SimulatorVariable("/controls/flight/rudder",""));
		SimVariables.put("/controls/flight/flaps", new SimulatorVariable("/controls/flight/flaps",""));
		SimVariables.put("/controls/engines/current-engine/throttle", new SimulatorVariable("/controls/engines/current-engine/throttle",""));
		SimVariables.put("/engines/engine/rpm", new SimulatorVariable("/engines/engine/rpm",""));
		SimVariables.put("/controls/flight/speedbrake", new SimulatorVariable("/controls/flight/speedbrake",""));
		SimVariables.put("/instrumentation/heading-indicator/offset-deg", new SimulatorVariable("/instrumentation/heading-indicator/offset-deg",""));
		SimVariables.put("/position/longitude-deg/", new SimulatorVariable("/position/longitude-deg/",""));
		SimVariables.put("/position/latitude-deg/", new SimulatorVariable("/position/latitude-deg/",""));
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
		Commands.put("sleep", new SleepCommand());
		Commands.put("print", new PrintCommand());
	};

	public static Command getCommand(String command_id) {
		return Commands.get(command_id);
	}

	public static int runLine(String line) throws IOException {
		List<String> words = lexer(line);

		while (words.contains("")) {
			words.remove("");
		}
		String command_id = words.get(0);
		String[] wordsArray = words.stream().toArray(String[]::new);
		return getCommand(command_id).doCommand(wordsArray);
	}

	public static int runLineWhile(String line) throws IOException {
		List<String> words = lexer(line);

		while (words.contains("")) {
			words.remove("");
		}
		String command_id = words.get(0);
		String[] wordsArray = words.stream().toArray(String[]::new);
		if (isCommand(command_id)) {
			if (command_id.equals("return")) {
				return getCommand(command_id).doCommand(wordsArray);
			} else {
				getCommand(command_id).doCommand(wordsArray);
			}
		} else {
			if (words.contains("bind")) {
				getCommand("bind").doCommand(wordsArray);
			}
			else if(words.contains("print")) {
				getCommand("print").doCommand(wordsArray);
			}
			else if(words.contains("sleep")) {
				getCommand("sleep").doCommand(wordsArray);
			}
			else {
				getCommand("assignment").doCommand(wordsArray);
			}

		}
		return 0;
	}

	public static boolean isCommand(String word) {
		return Commands.containsKey(word);
	}

	public static void putClientVariables(String name, double value) throws IOException {
		String valueStr = String.valueOf(value);
		ClientVariable clientVariable;

		if (ClientVariables.containsKey(name)) {
			clientVariable = ClientVariables.get(name);
			clientVariable.setValue(valueStr, false);
		} else {
			clientVariable = new ClientVariable(name, valueStr);
		}
		ClientVariables.put(name, clientVariable);
	}

	public static String[] ListToArray(List<String> list) {
		String[] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	public static List<String> lexer(String str) {
		List<String> final_words = new ArrayList<String>();
		String[] words = str.split("\\s+");
		for (String word : words) {
			final_words.addAll(Arrays.asList(word.split("(?<=[-+*=()/])|(?=[-+*=()/])")));
		}
		return final_words;
	}

	public static void clearVars() {
		for(SimulatorVariable s : SimVariables.values()) {
			s.deleteObservers();
		}
		ClientVariables.clear();
	}

	public static int parser(String[] lines) throws IOException {
		int executeFlag = 1;
		List<String> linesList = Arrays.asList(lines);
		for (String line : lines) {
			List<String> words = lexer(line);
			if (executeFlag == 0) {
				if (words.get(0).equals("}")) {
					executeFlag = 1;
				}
				continue;
			}
			String[] wordsArray = words.stream().toArray(String[]::new);
			String command_id = words.get(0);
			if (isCommand(command_id)) {
				if (command_id.equals("while")) {
					Command whileCommand = getCommand(command_id);
					while (whileCommand.doCommand(wordsArray) == 1) {
						int whileLineIndex = linesList.indexOf(line);
						while (whileLineIndex < linesList.indexOf("}") - 1) {
							whileLineIndex++;
							String currentLine = lines[whileLineIndex];
							runLineWhile(currentLine);
						}
					}
					executeFlag = 0;
				} else if (command_id.equals("return")) {
					return runLine(line);
				} else {
					runLine(line);
				}
			} else {
				if (words.contains("bind")) {
					getCommand("bind").doCommand(wordsArray);
				} else {
					getCommand("assignment").doCommand(wordsArray);
				}

			}
		}
		clearVars();
		return 0;
	}
}
