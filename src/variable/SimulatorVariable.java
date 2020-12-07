package variable;

import java.io.IOException;

import clientConnect.ClientSocket;

public class SimulatorVariable extends Variable {

	public SimulatorVariable(String name, String value) {
		super(name, value);
	}
	
	public String getSetCommand() {
		return "set " + this.getName() + " " + this.getValue();
	}
	
	@Override
	public void setValue(String value) throws IOException {
		super.setValue(value);
		ClientSocket.getClientSocket().send(this.getSetCommand());
	}
	

}
