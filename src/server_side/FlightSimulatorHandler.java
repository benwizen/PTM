package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import interperter.Interperter;
import variable.SimulatorVariable;

public class FlightSimulatorHandler {
	public static volatile boolean stop;
	private int wait;
	
	public FlightSimulatorHandler(int wait) {
		this.wait = wait;
	}


	public void handleClient(InputStream in, OutputStream out) throws IOException {
		BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(in));
        String client_input;
        try {
            while (!stop && !(client_input = buffered_reader.readLine()).equals("bye") && client_input != null) {
                try {
                	String[] client_lines = client_input.split(",");
                	int i = 0;
                	for(SimulatorVariable variable : Interperter.SimVariables.values()){
                		variable.setValue(client_lines[i], true);
                		i++;
                	}
                    Thread.sleep(1000/wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (NullPointerException e) {}
	}
}
