package variable;

import java.io.IOException;

import clientConnect.ClientSocket;
import interperter.Interperter;

public class SimulatorVariable extends Variable {

	public SimulatorVariable(String name, String value) {
		super(name, value);
	}
	
	public String getSetCommand() {
		return "set " + this.getName() + " " + this.getValue() + Interperter.lineSeperator;
	}
	
	//@Override
	public void setValue(String value, boolean comingFromServer) throws IOException {
		if(value == this.getValue())
			return;
		super.setValue(value, comingFromServer);
		if(!ClientSocket.getClientSocket().s.isClosed() && !comingFromServer)
			ClientSocket.getClientSocket().send(this.getSetCommand());
		
	}
	

}
